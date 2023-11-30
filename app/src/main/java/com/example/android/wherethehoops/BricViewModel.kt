package com.example.android.wherethehoops

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
const val COUNT_KEY = "COUNT_KEY"
const val IS_CHECKEDIN_KEY = "IS_CHECKEDIN_KEY"
class BricViewModel(private val savedStateHandle: SavedStateHandle): ViewModel(){
    var count: Int
        get() = savedStateHandle.get(COUNT_KEY) ?: 0
        set(value) = savedStateHandle.set(COUNT_KEY, value)

    var isCheckedIn: Boolean
        get() = savedStateHandle.get(IS_CHECKEDIN_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHECKEDIN_KEY, value)

    fun checkIn(){
        count += 1
        isCheckedIn = true
    }
    fun getCount(): String{
        return "" + count
    }

    fun checkOut(){
        count -= 1
        isCheckedIn = false
    }
}