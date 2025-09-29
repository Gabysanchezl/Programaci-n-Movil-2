package com.gaby.colormyviews

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setListeners()

        }

    private fun setListeners() {
        val boxOne = findViewById<TextView>(R.id.box_one_text) // Assuming it's a TextView
        val boxTwo = findViewById<TextView>(R.id.box_two_text)
        val boxThree = findViewById<TextView>(R.id.box_three_text)
        val boxFour = findViewById<TextView>(R.id.box_four_text)
        val boxFive = findViewById<TextView>(R.id.box_five_text)
        val constraintLayout = findViewById<View>(R.id.constraint_layout)
        val redButton = findViewById<TextView>(R.id.red_button)
        val greenButton = findViewById<TextView>(R.id.green_button)
        val yellowButton = findViewById<TextView>(R.id.yellow_button)

      val clickableViews: List<View> =
        listOf(boxOne, boxTwo, boxThree, boxFour, boxFive, constraintLayout,
            redButton, greenButton, yellowButton
        )

        for (item in clickableViews) {
            item.setOnClickListener { makeColored(it) }
        }
    }
    fun makeColored(view: View) {
        when (view.id) {

            // Boxes using Color class colors for background
            R.id.box_one_text -> view.setBackgroundColor(Color.DKGRAY)
            R.id.box_two_text -> view.setBackgroundColor(Color.GRAY)

            // Boxes using Android color resources for background
            R.id.box_three_text -> view.setBackgroundResource(android.R.color.holo_green_light)
            R.id.box_four_text -> view.setBackgroundResource(android.R.color.holo_green_dark)
            R.id.box_five_text -> view.setBackgroundResource(android.R.color.holo_green_light)
            R.id.red_button -> {
                val boxThree = findViewById<TextView>(R.id.box_three_text)
                boxThree.setBackgroundColor(ContextCompat.getColor(this, R.color.my_red))
            }
            R.id.yellow_button -> {
                val boxFour = findViewById<TextView>(R.id.box_four_text) // Find it here
                boxFour.setBackgroundColor(ContextCompat.getColor(this, R.color.my_yellow))
            }
            R.id.green_button -> {
                val boxFive = findViewById<TextView>(R.id.box_five_text) // Find it here
                boxFive.setBackgroundColor(ContextCompat.getColor(this, R.color.my_green))
            }
            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }

}

