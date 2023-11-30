package com.example.android.wherethehoops

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Home : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        button = findViewById(R.id.bricButton)
        button.setOnClickListener {
            openDashboard()
        }
    }

    private fun openDashboard() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
    }
}