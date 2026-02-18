package com.example.securemedia

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

class EncryptActivity : FragmentActivity() {

    private var selectedUri: Uri? = null

    private val pickFile =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            selectedUri = uri
            Toast.makeText(this, "File Selected", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encrypt)

        findViewById<Button>(R.id.selectBtn).setOnClickListener {
            pickFile.launch(arrayOf("*/*"))
        }

        findViewById<Button>(R.id.encryptBtn).setOnClickListener {

            BiometricHelper.authenticate(this) {

                val key = findViewById<EditText>(R.id.keyInput).text.toString()
                if (selectedUri == null || key.isEmpty()) {
                    Toast.makeText(this, "Select file & enter key", Toast.LENGTH_SHORT).show()
                    return@authenticate
                }

                val inputBytes =
                    contentResolver.openInputStream(selectedUri!!)!!.readBytes()

                val mimeType = contentResolver.getType(selectedUri!!)
                val extension = android.webkit.MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(mimeType) ?: "bin"


                val encrypted = ChaChaCrypto.encrypt(inputBytes, key, extension)


                saveToDownloads(encrypted, "encrypted_${System.currentTimeMillis()}.enc")
            }
        }
    }

    private fun saveToDownloads(data: ByteArray, fileName: String) {

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
        }

        val uri = contentResolver.insert(
            MediaStore.Files.getContentUri("external"),
            contentValues
        )

        uri?.let {
            contentResolver.openOutputStream(it)?.use { output ->
                output.write(data)
            }
            Toast.makeText(this, "Encrypted file saved in Downloads", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
