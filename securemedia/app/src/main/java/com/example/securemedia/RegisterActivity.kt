package com.example.securemedia

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val username = findViewById<EditText>(R.id.usernameInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val confirm = findViewById<EditText>(R.id.confirmInput)

        findViewById<Button>(R.id.registerBtn).setOnClickListener {

            if (password.text.toString() != confirm.text.toString()) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("users", MODE_PRIVATE)
            prefs.edit()
                .putString(username.text.toString(), password.text.toString())
                .apply()

            Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
