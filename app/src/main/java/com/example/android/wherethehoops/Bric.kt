package com.example.android.wherethehoops

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
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
    private lateinit var editText: EditText
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
        editText = findViewById(R.id.editText)
        sharedPreferences = getPreferences(MODE_PRIVATE)
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
        val name = editText.text.toString()
        writeNewUser(name)
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
        val name = editText.text.toString()
        deleteUser(name)
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

    private fun writeNewUser(name: String){
        if (name.isNotEmpty() && isValidName(name)) {
            myReference.child("Users").child(name).setValue("user")
        } else {
            // Handle invalid name (optional)
            println("Invalid user name")
        }


    }
    private fun deleteUser(name: String){

        myReference.child("Users").child(name).removeValue()
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
    private fun isValidName(name: String): Boolean{
        val usersReference = myReference.child("Users")

        // Check if the user already exists
        usersReference.child(name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User already exists
                    println("User with name $name already exists")
                } else {
                    // User doesn't exist, proceed to add the new user
                    usersReference.child(name).setValue("user")
                    println("User $name added successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

    })
        return true
    }


    companion object {
        private const val KEY_CHECKED_IN = "checked_in"
        private const val KEY_COUNT = "key_count"
    }
}
