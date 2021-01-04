package com.ruqaiah.android.firebase_challenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    lateinit var user_name: EditText
    lateinit var user_email: EditText
    lateinit var user_pass: EditText
    lateinit var confrim_pass: EditText
    lateinit var sign: Button
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        user_name = findViewById(R.id.username)
        user_email = findViewById(R.id.useremail)
        user_pass = findViewById(R.id.userpassward)
        confrim_pass = findViewById(R.id.confrimpassward)
        sign = findViewById(R.id.signup)
        sign.setOnClickListener {
            register()
        }
    }

    fun register() {
        auth = FirebaseAuth.getInstance()
        if( user_pass.text.toString().length < 6 || user_name.text.toString().length < 6){
            Toast.makeText(this, "passward must to be more than 6 ", Toast.LENGTH_LONG).show()
        }else if( user_pass.text.toString() != confrim_pass.text.toString()){
            Toast.makeText(this, "passward is not match ,try again ", Toast.LENGTH_LONG).show()
        }
        auth.createUserWithEmailAndPassword(user_email.text.toString(), user_pass.text.toString())
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    sendEmailVerfication()
                } else {
                    Toast.makeText(this, "user not created"+it.exception.toString(), Toast.LENGTH_LONG).show()
                    Log.d("fail", it.exception.toString())
                }
            }

    }

    fun sendEmailVerfication() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val loginintent = Intent(this, FirstActivity::class.java)
                startActivity(loginintent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}