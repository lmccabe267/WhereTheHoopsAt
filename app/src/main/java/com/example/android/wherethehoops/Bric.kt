package com.example.android.wherethehoops

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Bric : AppCompatActivity() {
    private lateinit var checkInButton: Button
    private lateinit var checkOutButton: Button
    private lateinit var playerCount: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private val bricViewModel: BricViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        checkInButton = findViewById(R.id.checkInButton)
        checkOutButton = findViewById(R.id.checkOutButton)
        playerCount = findViewById(R.id.counter_text)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val checkedIn = sharedPreferences.getBoolean(KEY_CHECKED_IN, false)
        bricViewModel.count = sharedPreferences.getInt(KEY_COUNT, 0)
        playerCount.setText(bricViewModel.getCount())
        updateButtonVisibility(checkedIn)
        checkInButton.setOnClickListener {
            performCheckIn()
        }
        checkOutButton.setOnClickListener {
            performCheckOut()
        }
    }

    private fun performCheckIn() {
        bricViewModel.checkIn()
        playerCount.setText(bricViewModel.getCount())
        with(sharedPreferences.edit()) {
            putInt(KEY_COUNT, bricViewModel.count)
            putBoolean(KEY_CHECKED_IN, bricViewModel.isCheckedIn)
            apply()
        }
        updateButtonVisibility(true)
        Toast.makeText(this, "Checked in", Toast.LENGTH_SHORT).show()
    }

    private fun performCheckOut() {
        bricViewModel.checkOut()
        playerCount.setText(bricViewModel.getCount())
        with(sharedPreferences.edit()) {
            putInt(KEY_COUNT, bricViewModel.count)
            putBoolean(KEY_CHECKED_IN, bricViewModel.isCheckedIn)
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
        private const val KEY_COUNT = "key_count"
    }
}
