package com.gaby.aboutme

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import com.gaby.aboutme.databinding.ActivityMainBinding


class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private val myName: MyName =
        MyName("Gabriela Sánchez")  //create instance for name and add the name


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //     setContentView(R.layout.activity_main)  //replace with Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.myName = myName

        //  findViewById<Button>(R.id.done_button).setOnClickListener {
        //  addNickname(it)
        binding.doneButton.setOnClickListener {
            addNickname(it)
        }
    }

    private fun addNickname(view: View) {
        binding.apply {
           // nicknameText.text = binding.nicknameEdit.text
            myName?.nickname = nicknameEdit.text.toString()
            invalidateAll()         //to refresh the Ui with the new data...
            nicknameEdit.visibility = View.GONE
            doneButton.visibility = View.GONE
            binding.nicknameText.visibility = View.VISIBLE

        }
        //            val editText = findViewById<EditText>(R.id.nickname_edit)
        //            val nicknameTextView = findViewById<TextView>(R.id.nickname_text)
        //
        //            nicknameTextView.text = editText.text
        //            editText.visibility = View.GONE
        //            view.visibility = View.GONE
        //            nicknameTextView.visibility = View.VISIBLE

        //
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

}
