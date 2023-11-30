package com.example.android.wherethehoops

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TestCourt : AppCompatActivity() {
    private lateinit var checkInButton: Button
    private lateinit var checkOutButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_court)

        checkInButton = findViewById(R.id.checkInButton)
        checkOutButton = findViewById(R.id.checkOutButton)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val checkedIn = sharedPreferences.getBoolean(KEY_CHECKED_IN, false)
        updateButtonVisibility(checkedIn)
        checkInButton.setOnClickListener {
            performCheckIn()
        }
        checkOutButton.setOnClickListener {
            performCheckOut()
        }
    }

    private fun performCheckIn() {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_CHECKED_IN, true)
            apply()
        }
        updateButtonVisibility(true)
        Toast.makeText(this, "Checked in", Toast.LENGTH_SHORT).show()
    }

    private fun performCheckOut() {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_CHECKED_IN, false)
            apply()
        }
        updateButtonVisibility(false)
        Toast.makeText(this, "Checked out", Toast.LENGTH_SHORT).show()
    }

    private fun updateButtonVisibility(checkedIn: Boolean) {
        checkInButton.isEnabled = !checkedIn
        checkOutButton.isEnabled = checkedIn
    }

    companion object {
        private const val KEY_CHECKED_IN = "checked_in"
    }
}
