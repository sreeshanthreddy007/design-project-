package com.example.securemedia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        findViewById<Button>(R.id.encryptBtn).setOnClickListener {
            startActivity(Intent(this, EncryptActivity::class.java))
        }

        findViewById<Button>(R.id.decryptBtn).setOnClickListener {
            startActivity(Intent(this, DecryptActivity::class.java))
        }
    }
}
