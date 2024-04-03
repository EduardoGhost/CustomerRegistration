package com.eduardo.customerregistration.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.eduardo.customerregistration.R

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUserName: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeComponents()


    }

    private fun initializeComponents() {
        editTextUserName = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
    }
}