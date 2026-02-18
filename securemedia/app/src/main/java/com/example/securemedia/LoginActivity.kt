package com.example.securemedia

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.usernameInput)
        val password = findViewById<EditText>(R.id.passwordInput)

        findViewById<Button>(R.id.loginBtn).setOnClickListener {

            val prefs = getSharedPreferences("users", MODE_PRIVATE)
            val savedPassword = prefs.getString(username.text.toString(), null)

            if (savedPassword == password.text.toString()) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.goRegisterBtn).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
