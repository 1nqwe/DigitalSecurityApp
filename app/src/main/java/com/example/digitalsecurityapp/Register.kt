package com.example.digitalsecurityapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPassword: EditText = findViewById(R.id.user_password)
        val userPassword2: EditText = findViewById(R.id.user_password2)
        val regButton: Button = findViewById(R.id.registerButton)
        val linkToAuth: TextView = findViewById(R.id.linkToAuth)


        linkToAuth.setOnClickListener {
            val intent = Intent(this, Authorization::class.java)
            startActivity(intent)
        }


        regButton.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val password = userPassword.text.toString().trim()
            val password2 = userPassword2.text.toString().trim()

            if(login.length < 2 || email.length < 2 || password.length < 2) {
                Toast.makeText(this, "Поля заполнены неверно", Toast.LENGTH_LONG).show()
            }
            else {
                val user = User(login, email, password)
                val db = DatabaseHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Пользователь успешно добавлен", Toast.LENGTH_LONG).show()
                userLogin.text.clear()
                userEmail.text.clear()
                userPassword.text.clear()
                userPassword2.text.clear()
                val intent = Intent(this, Authorization::class.java)
                startActivity(intent)
            }
        }
    }
}