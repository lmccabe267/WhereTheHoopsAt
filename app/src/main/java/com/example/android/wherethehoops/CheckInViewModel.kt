package com.example.android.wherethehoops

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class CheckInViewModel(private val savedStateHandle: SavedStateHandle): ViewModel(){
    private var count: Int = 0
    private var checkedIn: Boolean = false

    fun getQuestionCount(): Int {
        return questionBank.size
    }

}