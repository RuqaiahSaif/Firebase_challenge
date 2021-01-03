package com.ruqaiah.android.firebase_challenge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*

class MainActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var pass: EditText
    lateinit var login: Button
    lateinit var sign_email: Button
    lateinit var sign_phone: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        email = findViewById(R.id.email)
        pass = findViewById(R.id.passward)
        login = findViewById(R.id.login)
        sign_email = findViewById(R.id.sign_email)
        sign_phone = findViewById(R.id.sign_phone)
        sign_phone.setOnClickListener {
            val intent = Intent(this, Sign_phone::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {
            logIn()
        }
        sign_email.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    private fun logIn() {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.text.toString(), pass.text.toString())
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    verifyEmail()
                    Toast.makeText(this, "user login", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, FirstActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "user not login${it.exception}", Toast.LENGTH_LONG).show()
                    Log.d("fail", it.exception.toString())
                }
            }
    }

    fun verifyEmail() {
        val user = auth.currentUser
        if (user!!.isEmailVerified) {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "please verify your account", Toast.LENGTH_LONG).show()
        }
    }
}