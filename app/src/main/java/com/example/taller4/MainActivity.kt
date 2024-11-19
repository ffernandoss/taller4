package com.example.taller4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import java.util.*

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val greetingTextView: TextView = findViewById(R.id.greetingTextView)
        val navigateButton: Button = findViewById(R.id.navigateButton)

        // Set personalized greeting
        val greeting = getGreetingMessage()
        greetingTextView.text = greeting

        // Set button click listener to navigate to SegundaVentana
        navigateButton.setOnClickListener {
            val intent = Intent(this, SegundaVentana::class.java)
            startActivity(intent)
        }
    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Buenos días"
            in 12..17 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }
}