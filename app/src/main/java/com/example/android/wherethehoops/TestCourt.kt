package com.example.android.wherethehoops

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TestCourt : AppCompatActivity() {
    private lateinit var checkInButton: Button
    private lateinit var checkOutButton: Button
    private lateinit var playerCountTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var playerCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_court)

        checkInButton = findViewById(R.id.checkInButton)
        checkOutButton = findViewById(R.id.checkOutButton)
        playerCountTextView = findViewById(R.id.playerCountTextView)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        playerCount = sharedPreferences.getInt(KEY_PLAYER_COUNT, 0)

        updatePlayerCountText()
        updateButtonVisibility()

        checkInButton.setOnClickListener {
            performCheckIn()
        }

        checkOutButton.setOnClickListener {
            performCheckOut()
        }
    }

    private fun performCheckIn() {
        if (playerCount < COURT_CAPACITY) {
            with(sharedPreferences.edit()) {
                playerCount++
                putInt(KEY_PLAYER_COUNT, playerCount)
                apply()
            }
            updatePlayerCountText()
            updateButtonVisibility()

            if (playerCount >= COURT_CAPACITY) {
                sendCourtFullNotification()
            }

            Toast.makeText(this, "Checked in", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Court is full. Cannot check in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performCheckOut() {
        if (playerCount > 0) {
            with(sharedPreferences.edit()) {
                playerCount--
                putInt(KEY_PLAYER_COUNT, playerCount)
                apply()
            }
            updatePlayerCountText()
            updateButtonVisibility()
            Toast.makeText(this, "Checked out", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No users to check out.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendCourtFullNotification() {
        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.basketball)
            .setContentTitle("Court is full!")
            .setContentText("The court is full! No more check-ins allowed.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CourtFullChannel"
            val descriptionText = "Court Full Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateButtonVisibility() {
        checkInButton.isEnabled = playerCount < COURT_CAPACITY
        checkOutButton.isEnabled = playerCount > 0
    }

    private fun updatePlayerCountText() {
        playerCountTextView.text = "Players: $playerCount"
    }

    companion object {
        private const val KEY_PLAYER_COUNT = "player_count"
        private const val COURT_CAPACITY = 1
        private const val CHANNEL_ID = "court_full_channel"
        private const val NOTIFICATION_ID = 1
    }
}
