package com.clubbpc.cgen

import CharacterPackage.Character
import CharacterPackage.Monster
import CharacterPackage.Player

import Utility.Utility.TAG

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EditCharacterActivity : AppCompatActivity() {

    val auth: FirebaseAuth = Firebase.auth
    val tempUsers = Firebase.firestore.collection(TAG.USERS_COLLECTION)
    val tempUserDoc = tempUsers.document(auth.currentUser?.email.toString())

    var tempDNDplayers = tempUserDoc.collection(TAG.DND_PLAYERSHEETS_DOCUMENT)
    var tempPFplayers = tempUserDoc.collection(TAG.PATHFINDER_PLAYERSHEETS_DOCUMENT)
    var tempDNDmonsters = tempUserDoc.collection(TAG.DND_MONSTERSHEETS_DOCUMENT)
    var tempPFmonsters = tempUserDoc.collection(TAG.PATHFINDER_MONSTERSHEETS_DOCUMENT)

    private var currentPlayer: Player = Player("")
    private var currentMonster: Monster = Monster("")

    private var gameMode: String = ""
    private var charType: String = ""
    private var charName: String = ""
    private var previousActivity: String = ""

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_character)

        charName = intent.getStringExtra("CHARACTER NAME").toString()
        charType = intent.getStringExtra("CHARACTER TYPE").toString()
        gameMode = intent.getStringExtra("CHARACTER GAME").toString()
        previousActivity = intent.getStringExtra("INFO FROM").toString()

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////         Retrieve from enums          //////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        val class_spinner = findViewById<Spinner>(R.id.class_spinner_actual)
        class_spinner.setAdapter( ArrayAdapter(this, android.R.layout.simple_spinner_item, Player.Classes.values()) )

        val race_spinner = findViewById<Spinner>(R.id.race_spinner_actual)
        race_spinner.setAdapter( ArrayAdapter(this, android.R.layout.simple_spinner_item, Player.Race.values()) )

        val alignment_spinner = findViewById<Spinner>(R.id.alignment_spinner_actual)
        alignment_spinner.setAdapter( ArrayAdapter(this, android.R.layout.simple_spinner_item, Character.Alignment.values()) )

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////         Retrieve from database (FROM NEW CHARACTER DIALOG)         ////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (gameMode == "DND") {
            if (charType == "PLAYER") {
                tempDNDplayers.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentPlayer = Player(document.result?.data as java.util.HashMap<String, String>)

                        if (previousActivity == "NEW CHARACTER") {
                            setNewInformation(currentPlayer)
                        }

                        //setInformation()
                    }
            }
            else {
                tempDNDmonsters.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentMonster = Monster(document.result?.data as java.util.HashMap<String, String>)

                        if (previousActivity == "NEW CHARACTER") {
                            setNewInformation(currentMonster)
                        }

                        //setInformation()
                    }
            }
        }
        else if (gameMode == "PATHFINDER") {
            if (charType == "PLAYER") {
                tempPFplayers.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentPlayer = Player(document.result?.data as java.util.HashMap<String, String>)

                        if (previousActivity == "NEW CHARACTER") {
                            setNewInformation(currentPlayer)
                        }

                        //setInformation()
                    }
            }
            else {
                tempPFmonsters.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentMonster = Monster(document.result?.data as java.util.HashMap<String, String>)

                        if (previousActivity == "NEW CHARACTER") {
                            setNewInformation(currentMonster)
                        }

                        //setInformation()
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val backButton: ImageButton = findViewById<ImageButton>(R.id.back_imageButton)
        backButton.setOnClickListener() {
            val myIntent = Intent(this, CharacterGridActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun setMadeInformation(_player: CharacterPackage.Player) {

    }

    private fun setNewInformation(_player : CharacterPackage.Player) {
        var temp = ""
        var tempEditable: Editable
        var tempInt: Int

        // INTRO TOOLBAR
        val name_toolbar = findViewById<Toolbar>(R.id.characterName_toolbar)
        name_toolbar.title = _player._name
        temp = "${_player._game_mode} ${_player._char_type}"
        name_toolbar.subtitle = temp

        // CHARACTER INFORMATION FROM DIALOG
        val level_editText = findViewById<EditText>(R.id.level_editText_actual)
        tempEditable = Editable.Factory.getInstance().newEditable(_player._level.toString())
        level_editText.text = tempEditable

        val race_spinner = findViewById<Spinner>(R.id.race_spinner_actual)
        tempInt = 0
        enumValues<Player.Race>().forEach { race ->
            if (race == _player._race) {
                tempInt = race.ordinal
            }
        }
        race_spinner.setSelection(tempInt, false)
    }
    private fun setNewInformation(_monster : CharacterPackage.Monster) {
        // TODO: MOVE TO OWN XML FILE
        //          COPY AND PASTE
        //          THEN CHANGE

        var temp = ""

        val name_toolbar = findViewById<Toolbar>(R.id.characterName_toolbar)
        name_toolbar.title = _monster._name
        temp = "${_monster._game_mode} ${_monster._char_type}"
        name_toolbar.subtitle = temp

        findViewById<TextView>(R.id.class_textView).text = "Size:"
        val class_textView = findViewById<TextView>(R.id.class_textView_actual)
        class_textView.text = _monster._size.toString()

        val alignment_textView = findViewById<TextView>(R.id.alignment_textView_actual)
        alignment_textView.text = _monster._alignment.toString()
    }

    companion object {
        private const val TAG1_s = "SUCCESS_LOADDATA:NCA"
        private const val TAG1_f = "FAILURE_LOADDATA:NCA"
    }
}