package com.clubbpc.cgen

import AttackPackage.Physical
import AttackPackage.Physical.Weapon
import CharacterPackage.Character
import CharacterPackage.Monster
import CharacterPackage.Player
import Utility.User
import Utility.Utility.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class ViewCharacterActivity : AppCompatActivity() {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_character)

        // TODO: CREATE EDIT BUTTON TO TAKE TO NEW CHARACTER ACTIVITY WITH INFORMATION FILLED IN

        val backButton: ImageButton = findViewById<ImageButton>(R.id.back_imageButton)
        backButton.setOnClickListener() {
            val myIntent = Intent(this, CharacterGridActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////         Retrieve from database          /////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        charName = intent.getStringExtra("CHARACTER NAME").toString()
        charType = intent.getStringExtra("CHARACTER TYPE").toString()
        gameMode = intent.getStringExtra("CHARACTER GAME").toString()

        if (gameMode == "DND") {
            if (charType == "PLAYER") {
                tempDNDplayers.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentPlayer = Player(document.result?.data as java.util.HashMap<String, String>)
                        setInformation()
                    }
                    .addOnFailureListener {
                        Log.e("FAILURE", "Information not gathered from database - DND Player.")
                    }
            }
            else {
                tempDNDmonsters.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentMonster = Monster(document.result?.data as java.util.HashMap<String, String>)
                        setInformation()
                    }
                    .addOnFailureListener {
                        Log.e("FAILURE", "Information not gathered from database - DND Monster.")
                    }
            }
        }
        else if (gameMode == "PATHFINDER") {
            if (charType == "PLAYER") {
                tempPFplayers.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentPlayer = Player(document.result?.data as java.util.HashMap<String, String>)
                        setInformation()
                    }
                    .addOnFailureListener {
                        Log.e("FAILURE", "Information not gathered from database - PF Player.")
                    }
            }
            else {
                tempPFmonsters.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentMonster = Monster(document.result?.data as java.util.HashMap<String, String>)
                        setInformation()
                    }
                    .addOnFailureListener {
                        Log.e("FAILURE", "Information not gathered from database - PF Monster.")
                    }
            }
        }
        else {
            Log.e("INFORMATION LOST", "No information was retrieved for character of name ${charName} in ${gameMode} ${charType}.")
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        if (gameMode == "DND") {
            if (charType == "PLAYER") {
                tempDNDplayers.document(currentPlayer._name).set(currentPlayer._hashMap)
                Log.e("INFORMATION SAVED", currentPlayer._hashMap.toString())
            }
            else {

                tempDNDmonsters.document(currentMonster._name).set(currentMonster._hashMap)
                Log.e("INFORMATION SAVED", currentMonster._hashMap.toString())
            }
        }
        else if (gameMode == "PATHFINDER") {
            if (charType == "PLAYER") {
                tempPFplayers.document(currentPlayer._name).set(currentPlayer._hashMap)
                Log.e("INFORMATION SAVED", currentPlayer._hashMap.toString())
            }
            else {
                tempPFmonsters.document(currentMonster._name).set(currentMonster._hashMap)
                Log.e("INFORMATION SAVED", currentMonster._hashMap.toString())
            }
        }
        else {
            Log.e("INFORMATION LOST", "No information was saved.")
        }


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun setInformation() {
        if (charType == "PLAYER") {
            if (!currentPlayer._name.equals("")) {
                setInformation(currentPlayer)
                Log.e("CURRENT PLAYER", charName.toString() + " + " + currentPlayer._name.toString())
            }
            else {
                Log.e(TAG1_f, currentPlayer._hashMap.toString())
                Log.e(TAG1_f, "Data didn't transfer successfully")
            }
        }
        else {
            if(!currentMonster._name.equals("")) {
                setInformation(currentMonster)
                Log.e("CURRENT PLAYER", charName.toString() + " + " + currentMonster._name.toString())
            }
            else {
                Log.e(TAG1_f, currentMonster._hashMap.toString())
                Log.e(TAG1_f, "Data didn't transfer successfully")
            }
        }
    }
    private fun setInformation(_player : CharacterPackage.Player) {
        var temp = ""

        val name_toolbar = findViewById<Toolbar>(R.id.characterName_toolbar)
        name_toolbar.title = _player._name
        temp = "${_player._game_mode} ${_player._char_type}"
        name_toolbar.subtitle = temp

        val class_textView = findViewById<TextView>(R.id.class_textView_actual)
        class_textView.text = _player._classe.toString()

        val background_textView = findViewById<TextView>(R.id.background_textView_actual)
        background_textView.text = _player._background.toString()

        val race_textView = findViewById<TextView>(R.id.race_textView_actual)
        race_textView.text = _player._race.toString()

        val alignment_textView = findViewById<TextView>(R.id.alignment_textView_actual)
        alignment_textView.text = _player._alignment.toString()

        val xp_textView = findViewById<TextView>(R.id.xp_textView_actual)
        xp_textView.text = _player._experience_points.toString()

        val level_textView = findViewById<TextView>(R.id.level_textView_actual)
        level_textView.text = _player._level.toString()

        val ac_textView = findViewById<TextView>(R.id.ac_textView_actual)
        ac_textView.text = _player._armor_rating.toString()

        val speed_textView = findViewById<TextView>(R.id.speed_textView_actual)
        speed_textView.text = _player._speed.toString()

        val mhp_textView = findViewById<TextView>(R.id.maxHealthPoints_textView_actual)
        mhp_textView.text = currentPlayer._health_points.toString()

        val hd_textView = findViewById<TextView>(R.id.hd_textView_actual)
        temp = "${currentPlayer._total_hp_dice}${currentPlayer._hp_die}"
        hd_textView.text = temp

        val dsF_textView = findViewById<TextView>(R.id.ds_failures_textView_actual)
        dsF_textView.text = currentPlayer._death_saves.failures.toString()
        val dsS_textView = findViewById<TextView>(R.id.ds_successes_textView_actual)
        dsS_textView.text = currentPlayer._death_saves.successes.toString()

        val str_textView = findViewById<TextView>(R.id.strength_textView_actual)
        str_textView.text = currentPlayer._statistics.Strength.toString()
        val str_mod_textView = findViewById<TextView>(R.id.strength_textView_mod_actual)
        str_mod_textView.text = Character.Base_Stats.calculate_modifier(currentPlayer._statistics.Strength).toString()

        val dex_textView = findViewById<TextView>(R.id.dexterity_textView_actual)
        dex_textView.text = currentPlayer._statistics.Dexterity.toString()
        val dex_mod_textView = findViewById<TextView>(R.id.dexterity_textView_mod_actual)
        dex_mod_textView.text = Character.Base_Stats.calculate_modifier(currentPlayer._statistics.Dexterity).toString()

        val const_textView = findViewById<TextView>(R.id.constitution_textView_actual)
        const_textView.text = currentPlayer._statistics.Constitution.toString()
        val const_mod_textView = findViewById<TextView>(R.id.constitution_textView_mod_actual)
        const_mod_textView.text = Character.Base_Stats.calculate_modifier(currentPlayer._statistics.Constitution).toString()

        val intel_textView = findViewById<TextView>(R.id.intelligence_textView_actual)
        intel_textView.text = currentPlayer._statistics.Intelligence.toString()
        val intel_mod_textView = findViewById<TextView>(R.id.intelligence_textView_mod_actual)
        intel_mod_textView.text = Character.Base_Stats.calculate_modifier(currentPlayer._statistics.Intelligence).toString()

        val wis_textView = findViewById<TextView>(R.id.wisdom_textView_actual)
        wis_textView.text = currentPlayer._statistics.Wisdom.toString()
        val wis_mod_textView = findViewById<TextView>(R.id.wisdom_textView_mod_actual)
        wis_mod_textView.text = Character.Base_Stats.calculate_modifier(currentPlayer._statistics.Wisdom).toString()

        val charisma_textView = findViewById<TextView>(R.id.charisma_textView_actual)
        charisma_textView.text = currentPlayer._statistics.Charisma.toString()
        val charisma_mod_textView = findViewById<TextView>(R.id.charisma_textView_mod_actual)
        charisma_mod_textView.text = Character.Base_Stats.calculate_modifier(currentPlayer._statistics.Charisma).toString()

        val perc_textView = findViewById<TextView>(R.id.perception_textView_actual)
        perc_textView.text = currentPlayer._statistics.Perception.toString()
    }
    private fun setInformation(_monster : CharacterPackage.Monster) {
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

        val xp_textView = findViewById<TextView>(R.id.xp_textView_actual)
        xp_textView.text = _monster._XP.toString()

        val level_textView = findViewById<TextView>(R.id.level_textView_actual)
        level_textView.text = _monster._level.toString()

        val ac_textView = findViewById<TextView>(R.id.ac_textView_actual)
        ac_textView.text = _monster._armor_rating.toString()

        val speed_textView = findViewById<TextView>(R.id.speed_textView_actual)
        speed_textView.text = _monster._speed.toString()

        val mhp_textView = findViewById<TextView>(R.id.maxHealthPoints_textView_actual)
        mhp_textView.text = currentPlayer._health_points.toString()

        val hd_textView = findViewById<TextView>(R.id.hd_textView_actual)
        temp = "${currentPlayer._total_hit_dice}${currentPlayer._hit_die}"
        hd_textView.text = temp

        val dsF_textView = findViewById<TextView>(R.id.ds_failures_textView_actual)
        dsF_textView.text = currentPlayer._death_saves.failures.toString()

        val dsS_textView = findViewById<TextView>(R.id.ds_successes_textView_actual)
        dsS_textView.text = currentPlayer._death_saves.successes.toString()
    }


    companion object {
        private const val TAG1_s = "SUCCESS_LOADDATA:VCA"
        private const val TAG1_f = "FAILURE_LOADDATA:VCA"
    }
}