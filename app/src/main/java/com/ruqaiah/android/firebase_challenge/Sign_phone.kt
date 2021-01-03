package com.ruqaiah.android.firebase_challenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_sign_phone.*
import java.util.concurrent.TimeUnit

class Sign_phone : AppCompatActivity() {
    lateinit var phone_num: EditText
    lateinit var otp: EditText
    lateinit var sign_num: Button
    lateinit var otpbtn: Button
    lateinit var auth: FirebaseAuth
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var storedVerificationId:String
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_phone)
        auth = FirebaseAuth.getInstance()
        phone_num = findViewById(R.id.phone_num)
        otp = findViewById(R.id.otp)
        sign_num = findViewById(R.id.sign_num)
        otpbtn = findViewById(R.id.otpbtn)
        sign_num.setOnClickListener {
            var phone = phone_num.text.toString().trim()
            if (phone.isEmpty()|| phone.length< 9 ) {
                Toast.makeText(this, "please enter phone_number", Toast.LENGTH_LONG).show()
            } else {
                sendverficationcode( "+967"+phone)
            }
        }

        otpbtn .setOnClickListener {
            var otp1 = otp.text.toString().trim()
            if (otp1.isEmpty()) {
                Toast.makeText(this, "please enter otp", Toast.LENGTH_LONG).show()
            } else {
                verficationCode(otp1)
            }
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code:String? = phoneAuthCredential.smsCode
                if (code != null) {
                    otp.setText(code)

                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("fail", ""+p0.message.toString())
                Toast.makeText(applicationContext,p0.message.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                storedVerificationId = verificationId
                resendToken = token
                phone_layout.visibility= View.GONE
                otp_layout.visibility=View.VISIBLE
            }


        }

    }

    fun sendverficationcode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    fun verficationCode(code:String){
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")

                    val user = task.result?.user
                    Toast.makeText(applicationContext, "signup successfuly", Toast.LENGTH_LONG).show()
                    val loginintent = Intent(this, FirstActivity::class.java)
                    startActivity(loginintent)
                    finish()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(applicationContext, "code Enterd is incorrect", Toast.LENGTH_LONG).show()
                        otp.setText("")
                    }
                }
            }
    }
}