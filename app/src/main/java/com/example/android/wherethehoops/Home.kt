package com.example.android.wherethehoops

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Home : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var button2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        button = findViewById(R.id.bricButton)
        button.setOnClickListener {
            openDashboard()
        }

        button2 = findViewById(R.id.testButton)
        button2.setOnClickListener {
            openTest()
        }
    }

    private fun openDashboard() {
        val intent = Intent(this, Bric::class.java)
        startActivity(intent)
    }

    private fun openTest() {
        val intent = Intent(this, TestCourt::class.java)
        startActivity(intent)
    }
}