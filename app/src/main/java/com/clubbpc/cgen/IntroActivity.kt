package com.clubbpc.cgen

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    // TODO: CHANGE ALL .PUTEXTRA TO A PULL FROM THE DATABASE UNLESS VITALLY NECESSARY

    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val buttonSignIn: Button = findViewById(R.id.sign_in_button)
        buttonSignIn.setOnClickListener {
            val email = findViewById<EditText>(R.id.email_editTextText).text.toString()
            val password = findViewById<EditText>(R.id.password_editTextText).text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "Authentication successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                        reload()
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        val buttonSignUp: Button = findViewById(R.id.sign_up_button)
        buttonSignUp.setOnClickListener {
            val email = findViewById<EditText>(R.id.email_editTextText).text.toString()
            val password = findViewById<EditText>(R.id.password_editTextText).text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "Account creation successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                        reload()
                    } else {
                        Toast.makeText(
                            baseContext, "Account creation failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()

        if (auth.currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        val myIntent = Intent(this, PlayerInfoActivity::class.java)
        startActivity(myIntent)
        finish()
    }
}