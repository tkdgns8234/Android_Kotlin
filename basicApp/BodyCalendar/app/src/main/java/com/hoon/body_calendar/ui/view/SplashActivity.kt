package com.hoon.body_calendar.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hoon.body_calendar.R
import com.hoon.body_calendar.util.Constants

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth
        auth.currentUser?.let {
            Toast.makeText(this, Constants.LOGIN_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show()
            showMainActivity()
        } ?: anonymousLogin()
    }

    private fun anonymousLogin() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Toast.makeText(this, Constants.LOGIN_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show()
                    showMainActivity()
                } else {
                    // Sign in fails
                    Toast.makeText(this, Constants.LOGIN_FAILED_MESSAGE, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showMainActivity() {
        Handler(mainLooper).postDelayed(Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000L)
    }
}