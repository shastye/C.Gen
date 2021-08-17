package com.clubbpc.cgen

import CharacterPackage.Character
import CharacterPackage.Monster
import CharacterPackage.Player
import Utility.Utility.TAG

import Utility.User
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.clubbpc.cgen.NewCharacterActivity as NewCharacterActivity1

class NewCharacterDialog : DialogFragment() {

    private var auth: FirebaseAuth = Firebase.auth
    private var thisUser: User = User()
    private var newChar: Player = Player("")
    private val tempUsers = Firebase.firestore.collection(TAG.USERS_COLLECTION)
    private val tempUserDoc = tempUsers.document(/*"Sierra"*/auth.currentUser?.email.toString())

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        return inflater.inflate(R.layout.dialog_new_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.isCancelable = false

        val created = view.findViewById<Button>(R.id.dialog_create_button)
        created.setOnClickListener {
            if (canContinue()) {
                getInfoFromDialog(view)
                reload()
            }
        }

        val cancelled = view.findViewById<Button>(R.id.dialog_cancel_button)
        cancelled.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onStart() {

        // TODO: MAKE USER.LEVEL TRANSFER TO THE DIALOG BEFORE IT OPENS SO THE DEFAULT SHOWS TO THE USER

        super.onStart()
        dialog!!.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        thisUser = User(activity?.intent?.getSerializableExtra("USER_INFO") as? HashMap<String, String>?)
        if (isInfoTransferred()) {
            Log.e(TAG1_s, thisUser._hashMap.toString())

            val charLevel = this.view?.findViewById<EditText>(R.id.dialog_characterLevel_editText)
            if (charLevel != null) {
                val temp: Editable = Editable.Factory.getInstance().newEditable(thisUser._default_character_level.toString())
                charLevel.text = temp
            }
        }
        else {
            Log.e(TAG1_f, "Info not transferred correctly.")
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun getUserInfo(tempHash: HashMap<String, String>?) {
        thisUser._email = tempHash?.get("email")
        thisUser._name = tempHash?.get(TAG.NAME)
        thisUser._default_character_level = tempHash?.get(TAG.DEFAULT_CHARACTER_LEVEL).toString().toInt()
    }

    private fun isInfoTransferred(): Boolean {
        var infoTransferred = true

        if (thisUser._name == "") {
            Log.e(TAG1_f, "Name: ${thisUser._name}")
            infoTransferred = false
        }

        if (thisUser._default_character_level == -1) {
            Log.e(TAG1_f, "Level: ${thisUser._default_character_level}")
            infoTransferred = false
        }

        if (thisUser._email == "") {
            Log.e(TAG1_f, "Email: ${thisUser._email}")
            infoTransferred = false
        }

        return infoTransferred
    }

    private fun getInfoFromDialog(view: View) {
        enumValues<Character.Game_Mode>().forEach {
            Log.e("GameMode it.name", it.name)

            if (it.name.lowercase() == view.findViewById<Spinner>(R.id.dialog_gameMode_spinner).selectedItem.toString().lowercase()) {
                newChar._game_mode = it
            }
        }

        enumValues<Character.Type>().forEach {
            Log.e("Type it.name", it.name)

            if (it.name.lowercase() == view.findViewById<Spinner>(R.id.dialog_characterType_spinner).selectedItem.toString().lowercase()) {
                newChar._char_type = it
            }
        }

        newChar._name = view.findViewById<EditText>(R.id.dialog_characterName_editText).text.toString()

        enumValues<Player.Race>().forEach {
            Log.e("Race it.name", it.name)

            if (it.name.lowercase() == view.findViewById<Spinner>(R.id.dialog_characterRace_spinner).selectedItem.toString().lowercase()) {
                newChar._race = it
            }
        }

        val temp: String = view.findViewById<EditText>(R.id.dialog_characterLevel_editText).text.toString()
        newChar._level = Integer.parseInt(temp)
    }

    private fun reload() {
        val myIntent = Intent(activity, NewCharacterActivity1::class.java)


        val newPlayer: Player
        val newMonster: Monster
        val cont: Boolean

        if (newChar._char_type.toString() == "PLAYER") {
            newPlayer = Player(newChar)

            cont = canContinue(newPlayer)
            if (cont) {
                myIntent.putExtra(TAG2, newPlayer._hashMap)

                if (newPlayer._game_mode.toString() == "DND") {
                    tempUserDoc.collection(TAG.DND_PLAYERSHEETS_DOCUMENT).document(newPlayer._name).set(newPlayer._hashMap)
                }
                else if (newPlayer._game_mode.toString() == "PATHFINDER") {
                    tempUserDoc.collection(TAG.PATHFINDER_PLAYERSHEETS_DOCUMENT).document(newPlayer._name).set(newPlayer._hashMap)
                }

                startActivity(myIntent)
                this.dismiss()
            }
        } else if (newChar._char_type.toString() == "MONSTER") {
            newMonster = Monster(newChar)

            cont = canContinue(newMonster)
            if (cont) {
                myIntent.putExtra(TAG2, newMonster._hashMap)

                if (newMonster._game_mode.toString() == "DND") {
                    tempUserDoc.collection(TAG.DND_MONSTERSHEETS_DOCUMENT).document(newMonster._name).set(newMonster._hashMap)
                }
                else if (newMonster._game_mode.toString() == "PATHFINDER") {
                    tempUserDoc.collection(TAG.PATHFINDER_MONSTERSHEETS_DOCUMENT).document(newMonster._name).set(newMonster._hashMap)
                }

                startActivity(myIntent)
                this.dismiss()
            }
        }
    }

    private fun canContinue(): Boolean {
        var doCont = true

        if (view?.findViewById<EditText>(R.id.dialog_characterName_editText)?.text.toString() == "") {
            doCont = false
        }
        if (view?.findViewById<EditText>(R.id.dialog_characterLevel_editText)?.text.toString() == "") {
            doCont = false
        }

        return doCont
    }
    private fun canContinue(char: Character): Boolean {
        var doCont = true

        if (char._name == "") {
            doCont = false
        }
        if (char._level == -1) {
            doCont = false
        }

        return doCont
    }

    companion object {
        private const val TAG1_s = "SUCCESS_LOADDATA:DIALOG"
        private const val TAG1_f = "FAILURE_LOADDATA:DIALOG"

        private const val TAG2 = "NEW CHARACTER"
    }
}