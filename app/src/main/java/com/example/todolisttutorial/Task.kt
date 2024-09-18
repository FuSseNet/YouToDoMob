package com.example.todolisttutorial

import android.content.Context
import androidx.core.content.ContextCompat
import java.time.LocalTime
import java.util.*

class Task(
    var name: String,
    var desc: String,
    var dueTime: LocalTime?,
    var completedDate: String,
    var id: String = UUID.randomUUID().toString()
)
{
    fun isCompleted() = completedDate != ""
    fun imageResource(): Int = if(isCompleted()) R.drawable.checked_24 else R.drawable.unchecked_24
    fun imageColor(context: Context): Int = if(isCompleted()) done(context) else black(context)
    fun background(context: Context): Int = if(isCompleted()) gray(context) else white(context)

    private fun white(context: Context) = ContextCompat.getColor(context, R.color.white)
    private fun gray(context: Context) = ContextCompat.getColor(context, R.color.gray)
    private fun done(context: Context) = ContextCompat.getColor(context, R.color.done)
    private fun black(context: Context) = ContextCompat.getColor(context, R.color.black)
}