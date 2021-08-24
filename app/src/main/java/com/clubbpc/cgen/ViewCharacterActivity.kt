package com.clubbpc.cgen

import CharacterPackage.Character
import CharacterPackage.Monster
import CharacterPackage.Player
import Utility.Utility
import Utility.Utility.TAG
import Utility.Utility.checkForEnumValueInArray
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

        // TODO: CHECK ATTACKS, WEAPONS, PROFICIENCIES, AND SAVE THROWS

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

                        // TODO: DELETE WHEN DONE DEBUGGING
                        //      comment out when not using
                        //currentPlayer = Utility.setInformationForDebugging(currentPlayer)

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

        // INTRO TOOLBAR
        val name_toolbar = findViewById<Toolbar>(R.id.characterName_toolbar)
        name_toolbar.title = _player._name
        temp = "${_player._game_mode} ${_player._char_type}"
        name_toolbar.subtitle = temp

        // CHARACTER INFORMATION
        val class_textView = findViewById<TextView>(R.id.class_textView_actual)
        class_textView.text = _player._classe.toString()

        val background_textView = findViewById<TextView>(R.id.background_textView_actual)
        background_textView.text = _player._background.toString()

        val race_textView = findViewById<TextView>(R.id.race_textView_actual)
        race_textView.text = _player._race.toString()

        val alignment_textView = findViewById<TextView>(R.id.alignment_textView_actual)
        alignment_textView.text = _player._alignment.toString()

        // GENERIC STAT INFORMATION
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

        val insp_textView = findViewById<TextView>(R.id.insp_textView_actual)
        insp_textView.text = currentPlayer._inspiration.toString()

        val profBonus_textView = findViewById<TextView>(R.id.profBonus_textView_actual)
        profBonus_textView.text = currentPlayer._proficiency_bonus.toString()

        // BASE STAT INFORMATION
        val str_textView = findViewById<TextView>(R.id.strength_textView_actual)
        str_textView.text = currentPlayer._statistics.Strength.toString()
        val str_mod_textView = findViewById<TextView>(R.id.strength_textView_mod_actual)
            if (currentPlayer._statistics.Strength - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(currentPlayer._statistics.Strength).toString()
        str_mod_textView.text = temp

        val dex_textView = findViewById<TextView>(R.id.dexterity_textView_actual)
        dex_textView.text = currentPlayer._statistics.Dexterity.toString()
        val dex_mod_textView = findViewById<TextView>(R.id.dexterity_textView_mod_actual)
            if (currentPlayer._statistics.Dexterity - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(currentPlayer._statistics.Dexterity).toString()
        dex_mod_textView.text = temp

        val const_textView = findViewById<TextView>(R.id.constitution_textView_actual)
        const_textView.text = currentPlayer._statistics.Constitution.toString()
        val const_mod_textView = findViewById<TextView>(R.id.constitution_textView_mod_actual)
            if (currentPlayer._statistics.Constitution - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(currentPlayer._statistics.Constitution).toString()
        const_mod_textView.text = temp

        val intel_textView = findViewById<TextView>(R.id.intelligence_textView_actual)
        intel_textView.text = currentPlayer._statistics.Intelligence.toString()
        val intel_mod_textView = findViewById<TextView>(R.id.intelligence_textView_mod_actual)
            if (currentPlayer._statistics.Intelligence - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(currentPlayer._statistics.Intelligence).toString()
        intel_mod_textView.text = temp

        val wis_textView = findViewById<TextView>(R.id.wisdom_textView_actual)
        wis_textView.text = currentPlayer._statistics.Wisdom.toString()
        val wis_mod_textView = findViewById<TextView>(R.id.wisdom_textView_mod_actual)
            if (currentPlayer._statistics.Wisdom - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(currentPlayer._statistics.Wisdom).toString()
        wis_mod_textView.text = temp

        val charisma_textView = findViewById<TextView>(R.id.charisma_textView_actual)
        charisma_textView.text = currentPlayer._statistics.Charisma.toString()
        val charisma_mod_textView = findViewById<TextView>(R.id.charisma_textView_mod_actual)
            if (currentPlayer._statistics.Charisma - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(currentPlayer._statistics.Charisma).toString()
        charisma_mod_textView.text = temp

        val perc_textView = findViewById<TextView>(R.id.perception_textView_actual)
        perc_textView.text = currentPlayer._statistics.Perception.toString()

        // SAVING THROWS INFORMATION
        val stStr_radio = findViewById<RadioButton>(R.id.save_throw_str)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.STRENGTH, currentPlayer._saving_throws)) {
            stStr_radio.isChecked = true;
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.STRENGTH, currentPlayer._saving_throws)}  Strength"
            stStr_radio.text = temp
        }

        val stDex_radio = findViewById<RadioButton>(R.id.save_throw_dex)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.DEXTERITY, currentPlayer._saving_throws)) {
            stDex_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.DEXTERITY, currentPlayer._saving_throws)}  Dexterity"
            stDex_radio.text = temp
        }

        val stConst_radio = findViewById<RadioButton>(R.id.save_throw_const)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.CONSTITUTION, currentPlayer._saving_throws)) {
            stConst_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.CONSTITUTION, currentPlayer._saving_throws)}  Constitution"
            stConst_radio.text = temp
        }

        val stInt_radio = findViewById<RadioButton>(R.id.save_throw_int)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.INTELLIGENCE, currentPlayer._saving_throws)) {
            stInt_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.INTELLIGENCE, currentPlayer._saving_throws)}  Intelligence"
            stInt_radio.text = temp
        }

        val stWis_radio = findViewById<RadioButton>(R.id.save_throw_wis)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.WISDOM, currentPlayer._saving_throws)) {
            stWis_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.WISDOM, currentPlayer._saving_throws)}  Wisdom"
            stWis_radio.text = temp
        }

        val stChar_radio = findViewById<RadioButton>(R.id.save_throw_char)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.CHARISMA, currentPlayer._saving_throws)) {
            stChar_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.CHARISMA, currentPlayer._saving_throws)}  Charisma"
            stChar_radio.text = temp
        }

        // SKILLS INFORMATION
        val acro_radio = findViewById<RadioButton>(R.id.prof_acrobatics)
        if (checkForEnumValueInArray(Character.Skill.ACROBATICS, currentPlayer._proficiencies)) {
            acro_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ACROBATICS, currentPlayer._proficiencies)}  Acrobatics"
            acro_radio.text = temp
        }

        val AH_radio = findViewById<RadioButton>(R.id.prof_animal_handling)
        if (checkForEnumValueInArray(Character.Skill.ANIMAL_HANDLING, currentPlayer._proficiencies)) {
            AH_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ANIMAL_HANDLING, currentPlayer._proficiencies)}  Animal Handling"
            AH_radio.text = temp
        }

        val arcana_radio = findViewById<RadioButton>(R.id.prof_arcana)
        if (checkForEnumValueInArray(Character.Skill.ARCANA, currentPlayer._proficiencies)) {
            arcana_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ARCANA, currentPlayer._proficiencies)}  Arcana"
            arcana_radio.text = temp
        }

        val athl_radio = findViewById<RadioButton>(R.id.prof_athletics)
        if (checkForEnumValueInArray(Character.Skill.ATHLETICS, currentPlayer._proficiencies)) {
            athl_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ATHLETICS, currentPlayer._proficiencies)}  Athletics"
            athl_radio.text = temp
        }

        val dec_radio = findViewById<RadioButton>(R.id.prof_deception)
        if (checkForEnumValueInArray(Character.Skill.DECEPTION, currentPlayer._proficiencies)) {
            dec_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.DECEPTION, currentPlayer._proficiencies)}  Deception"
            dec_radio.text = temp
        }

        val his_radio = findViewById<RadioButton>(R.id.prof_history)
        if (checkForEnumValueInArray(Character.Skill.HISTORY, currentPlayer._proficiencies)) {
            his_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.HISTORY, currentPlayer._proficiencies)}  History"
            his_radio.text = temp
        }

        val Insight_radio = findViewById<RadioButton>(R.id.prof_insight)
        if (checkForEnumValueInArray(Character.Skill.INSIGHT, currentPlayer._proficiencies)) {
            Insight_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INSIGHT, currentPlayer._proficiencies)}  Insight"
            Insight_radio.text = temp
        }

        val int_radio = findViewById<RadioButton>(R.id.prof_intimidation)
        if (checkForEnumValueInArray(Character.Skill.INTIMIDATION, currentPlayer._proficiencies)) {
            int_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INTIMIDATION, currentPlayer._proficiencies)}  Intimidation"
            int_radio.text = temp
        }

        val inv_radio = findViewById<RadioButton>(R.id.prof_investigation)
        if (checkForEnumValueInArray(Character.Skill.INVESTIGATION, currentPlayer._proficiencies)) {
            inv_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INVESTIGATION, currentPlayer._proficiencies)}  Investigation"
            inv_radio.text = temp
        }

        val med_radio = findViewById<RadioButton>(R.id.prof_medicine)
        if (checkForEnumValueInArray(Character.Skill.MEDICINE, currentPlayer._proficiencies)) {
            med_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.MEDICINE, currentPlayer._proficiencies)}  Medicine"
            med_radio.text = temp
        }

        val nat_radio = findViewById<RadioButton>(R.id.prof_nature)
        if (checkForEnumValueInArray(Character.Skill.NATURE, currentPlayer._proficiencies)) {
            nat_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.NATURE, currentPlayer._proficiencies)}  Nature"
            nat_radio.text = temp
        }

        val perc_radio = findViewById<RadioButton>(R.id.prof_perception)
        if (checkForEnumValueInArray(Character.Skill.PERCEPTION, currentPlayer._proficiencies)) {
            perc_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERCEPTION, currentPlayer._proficiencies)}  Perception"
            perc_radio.text = temp
        }

        val perf_radio = findViewById<RadioButton>(R.id.prof_performance)
        if (checkForEnumValueInArray(Character.Skill.PERFORMANCE, currentPlayer._proficiencies)) {
            perf_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERFORMANCE, currentPlayer._proficiencies)}  Performance"
            perf_radio.text = temp
        }

        val pers_radio = findViewById<RadioButton>(R.id.prof_persuasion)
        if (checkForEnumValueInArray(Character.Skill.PERSUASION, currentPlayer._proficiencies)) {
            pers_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERSUASION, currentPlayer._proficiencies)}  Persuasion"
            pers_radio.text = temp
        }

        val rel_radio = findViewById<RadioButton>(R.id.prof_religion)
        if (checkForEnumValueInArray(Character.Skill.RELIGION, currentPlayer._proficiencies)) {
            rel_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.RELIGION, currentPlayer._proficiencies)}  Religion"
            rel_radio.text = temp
        }

        val soh_radio = findViewById<RadioButton>(R.id.prof_sleight_of_hand)
        if (checkForEnumValueInArray(Character.Skill.SLEIGHT_OF_HAND, currentPlayer._proficiencies)) {
            soh_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.SLEIGHT_OF_HAND, currentPlayer._proficiencies)}  Sleight of Hand"
            soh_radio.text = temp
        }

        val st_radio = findViewById<RadioButton>(R.id.prof_stealth)
        if (checkForEnumValueInArray(Character.Skill.STEALTH, currentPlayer._proficiencies)) {
            st_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.STEALTH, currentPlayer._proficiencies)}  Stealth"
            st_radio.text = temp
        }

        val sur_radio = findViewById<RadioButton>(R.id.prof_survival)
        if (checkForEnumValueInArray(Character.Skill.SURVIVAL, currentPlayer._proficiencies)) {
            sur_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.SURVIVAL, currentPlayer._proficiencies)}  Survival"
            sur_radio.text = temp
        }

        val otherProf_textView = findViewById<TextView>(R.id.other_proficiencies_textView)
        temp = ""
        for (prof : Character.Proficient_In in currentPlayer._proficiencies) {
            if (   prof.proficiency != Character.Skill.ACROBATICS
                && prof.proficiency != Character.Skill.ANIMAL_HANDLING
                && prof.proficiency != Character.Skill.ARCANA
                && prof.proficiency != Character.Skill.ATHLETICS
                && prof.proficiency != Character.Skill.DECEPTION
                && prof.proficiency != Character.Skill.HISTORY
                && prof.proficiency != Character.Skill.INSIGHT
                && prof.proficiency != Character.Skill.INTIMIDATION
                && prof.proficiency != Character.Skill.INVESTIGATION
                && prof.proficiency != Character.Skill.MEDICINE
                && prof.proficiency != Character.Skill.NATURE
                && prof.proficiency != Character.Skill.PERCEPTION
                && prof.proficiency != Character.Skill.PERFORMANCE
                && prof.proficiency != Character.Skill.PERSUASION
                && prof.proficiency != Character.Skill.RELIGION
                && prof.proficiency != Character.Skill.SLEIGHT_OF_HAND
                && prof.proficiency != Character.Skill.STEALTH
                && prof.proficiency != Character.Skill.SURVIVAL
            ) {
                temp += "${prof.bonus}  ${prof.proficiency}  "
                when (prof.stat_used) {
                    Character.Base_Stats_Enum.STRENGTH -> temp += "(Str)"
                    Character.Base_Stats_Enum.DEXTERITY -> temp += "(Dex)"
                    Character.Base_Stats_Enum.CONSTITUTION -> temp += "(Con)"
                    Character.Base_Stats_Enum.INTELLIGENCE -> temp += "(Int)"
                    Character.Base_Stats_Enum.WISDOM -> temp += "(Wis)"
                    Character.Base_Stats_Enum.CHARISMA -> temp += "(Cha)"
                    Character.Base_Stats_Enum.PERCEPTION -> temp += "(Per)"
                    null -> temp += ""
                }
            }
        }
        otherProf_textView.text = temp
    }
    private fun setInformation(_monster : CharacterPackage.Monster) {
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