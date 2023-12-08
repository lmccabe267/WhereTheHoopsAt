package com.example.android.wherethehoops

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Bric : AppCompatActivity() {
    private lateinit var checkInButton: Button
    private lateinit var checkOutButton: Button
    private lateinit var playerCount: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private val bricViewModel: BricViewModel by viewModels()
    private lateinit var database : FirebaseDatabase
    private lateinit var myReference: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        checkInButton = findViewById(R.id.checkInButton)
        checkOutButton = findViewById(R.id.checkOutButton)
        playerCount = findViewById(R.id.counter_text)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val checkedIn = sharedPreferences.getBoolean(KEY_CHECKED_IN, false)
        database = FirebaseDatabase.getInstance()
        myReference = database.reference
        //bricViewModel.count = sharedPreferences.getInt(KEY_COUNT, dataBeginning())

        updateButtonVisibility(checkedIn)
        checkInButton.setOnClickListener {
            performCheckIn()
        }
        checkOutButton.setOnClickListener {
            performCheckOut()
        }
        dataChanged { value ->
            playerCount.text = value.toString()
            with(sharedPreferences.edit()) {
                putInt(KEY_COUNT, value)
                putBoolean(KEY_CHECKED_IN, bricViewModel.isCheckedIn)
                apply()
            }
        }
    }

    private fun performCheckIn() {
        bricViewModel.checkIn()
        writeNewUser()
        dataChanged { value ->
            playerCount.text = value.toString()
            with(sharedPreferences.edit()) {
                putInt(KEY_COUNT, value)
                putBoolean(KEY_CHECKED_IN, bricViewModel.isCheckedIn)
                apply()
            }
            updateButtonVisibility(true)
            Toast.makeText(this, "Checked in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performCheckOut() {
        bricViewModel.checkOut()
        deleteUser()
        dataChanged { value ->
            playerCount.text = value.toString()
            with(sharedPreferences.edit()) {
                putInt(KEY_COUNT, value)
                putBoolean(KEY_CHECKED_IN, bricViewModel.isCheckedIn)
                apply()
            }
            updateButtonVisibility(false)
            Toast.makeText(this, "Checked out", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateButtonVisibility(checkedIn: Boolean) {
        checkInButton.isEnabled = !checkedIn
        checkOutButton.isEnabled = checkedIn
    }

    private fun writeNewUser(){
        myReference.child("Users").child("Players2").setValue("user")


    }
    private fun deleteUser(){
        myReference.child("Users").child("Players2").removeValue()
    }
    private fun dataChanged(callback: (Int) -> Unit) {
        val databaseRef: DatabaseReference = myReference.child("Users")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val value = snapshot.childrenCount.toInt()
                    callback(value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors that occur during the read operation
            }
        })
    }

    private fun dataBeginning(callback: (Int) -> Unit) {
        val databaseRef: DatabaseReference = myReference.child("Users")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value
                val numberOfChildren = dataSnapshot.childrenCount.toInt()
                callback(numberOfChildren)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors that occur during the read operation
            }
        })
    }


    companion object {
        private const val KEY_CHECKED_IN = "checked_in"
        private const val KEY_COUNT = "key_count"
    }
}
