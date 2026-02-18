package com.example.securemedia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val encryptBtn = findViewById<Button>(R.id.encryptBtn)
        val decryptBtn = findViewById<Button>(R.id.decryptBtn)

        encryptBtn?.setOnClickListener {
            startActivity(Intent(this, EncryptActivity::class.java))
        }

        decryptBtn?.setOnClickListener {
            startActivity(Intent(this, DecryptActivity::class.java))
        }
    }
}
