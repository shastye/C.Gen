package com.clubbpc.cgen

import Utility.User
import Utility.Utility.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.clubbpc.cgen.CharacterGridActivity as CharacterGridActivity1

class PlayerInfoActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth
    private var thisUser: User = User()
    private val tempUserDoc = Firebase.firestore.collection(TAG.USERS_COLLECTION).document(auth.currentUser?.email.toString())

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_info)

        thisUser = setUser()

        // TODO: MAKE USER INFO DISPLAY ON SCREEN AS TEXT IF ONLY ONE IS SET IN DATABASE

        val button: Button = findViewById(R.id.finish_button)
        button.setOnClickListener {
            if (findViewById<EditText>(R.id.playerName_editText).text.toString() != "" && findViewById<EditText>(R.id.characterLevel_editTextNumber).text.toString() != "") {
                thisUser._name = findViewById<EditText>(R.id.playerName_editText).text.toString()
                thisUser._default_character_level = findViewById<EditText>(R.id.characterLevel_editTextNumber).text.toString().toInt()

                tempUserDoc.set(thisUser._hashMap)



                reload()
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    private fun reload() {
        val myIntent = Intent(this, CharacterGridActivity1::class.java)

        myIntent.putExtra("USER_INFO", thisUser._hashMap)

        startActivity(myIntent)
        finish()
    }

    private fun setUser() : User {
        var tempUser: User = User()

        tempUserDoc.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (document.data != null) {
                        val tempHash = document.data

                        tempUser._name = tempHash?.get(TAG.NAME).toString()
                        tempUser._default_character_level = tempHash?.get(TAG.DEFAULT_CHARACTER_LEVEL).toString().toInt()
                        tempUser._email = tempHash?.get(TAG.EMAIL).toString()

                        if (isInfoTransferred()) {
                            reload()
                        }
                    }

                }
            }

        return tempUser
    }

    private fun isInfoTransferred(): Boolean {
        var infoTransferred = true

        if (thisUser._name == "") {
            infoTransferred = false
        }

        if (thisUser._default_character_level == -1) {
            infoTransferred = false
        }

        if (thisUser._email == "") {
            infoTransferred = false
        }

        return infoTransferred
    }

    companion object {
        private const val TAG1_s = "SUCCESS_USER:PIA"
        private const val TAG1_f = "ERROR_USER:PIA"

        private const val TAG2_s = "SUCCESS_USER DOC:PIA"
        private const val TAG2_sbn = "SBN_USER DOC:PIA"
        private const val TAG2_f = "ERROR_USER DOC:PIA"

        private const val TAG3_s = "SUCCESS_SEND DATA:PIA"
        private const val TAG3_f = "FAILURE_SEND DATA:PIA"
    }
}