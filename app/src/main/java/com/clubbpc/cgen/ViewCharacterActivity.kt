package com.clubbpc.cgen

import AttackPackage.Magical
import AttackPackage.Physical
import CharacterPackage.Character
import CharacterPackage.Monster
import CharacterPackage.Player
import Utility.Utility
import Utility.Utility.TAG
import Utility.Utility.checkForEnumValueInArray
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
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
        backButton.setOnClickListener {
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
            }
            else {
                tempDNDmonsters.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentMonster = Monster(document.result?.data as java.util.HashMap<String, String>)
                        setInformation()
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
            }
            else {
                tempPFmonsters.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentMonster = Monster(document.result?.data as java.util.HashMap<String, String>)
                        setInformation()
                    }
            }
        }


    }

    // TODO: DELETE AT END
    override fun onDestroy() {
        super.onDestroy()

        if (gameMode == "DND") {
            if (charType == "PLAYER") {
                tempDNDplayers.document(currentPlayer._name).set(currentPlayer._hashMap)
            }
            else {

                tempDNDmonsters.document(currentMonster._name).set(currentMonster._hashMap)
            }
        }
        else if (gameMode == "PATHFINDER") {
            if (charType == "PLAYER") {
                tempPFplayers.document(currentPlayer._name).set(currentPlayer._hashMap)
            }
            else {
                tempPFmonsters.document(currentMonster._name).set(currentMonster._hashMap)
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun setInformation() {
        if (charType == "PLAYER") {
            if (!currentPlayer._name.equals("")) {
                setInformation(currentPlayer)
            }
        }
        else {
            if(!currentMonster._name.equals("")) {
                setInformation(currentMonster)
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
        mhp_textView.text = _player._health_points.toString()

        val hd_textView = findViewById<TextView>(R.id.hd_textView_actual)
        temp = "${_player._total_hp_dice}${_player._hp_die}"
        hd_textView.text = temp

        val dsF_textView = findViewById<TextView>(R.id.ds_failures_textView_actual)
        dsF_textView.text = _player._death_saves.failures.toString()
        val dsS_textView = findViewById<TextView>(R.id.ds_successes_textView_actual)
        dsS_textView.text = _player._death_saves.successes.toString()

        val insp_textView = findViewById<TextView>(R.id.insp_textView_actual)
        insp_textView.text = _player._inspiration.toString()

        val profBonus_textView = findViewById<TextView>(R.id.profBonus_textView_actual)
        profBonus_textView.text = _player._proficiency_bonus.toString()

        // BASE STAT INFORMATION
        val str_textView = findViewById<TextView>(R.id.strength_textView_actual)
        str_textView.text = _player._statistics.Strength.toString()
        val str_mod_textView = findViewById<TextView>(R.id.strength_textView_mod_actual)
            if (_player._statistics.Strength - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Strength).toString()
        str_mod_textView.text = temp

        val dex_textView = findViewById<TextView>(R.id.dexterity_textView_actual)
        dex_textView.text = _player._statistics.Dexterity.toString()
        val dex_mod_textView = findViewById<TextView>(R.id.dexterity_textView_mod_actual)
            if (_player._statistics.Dexterity - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Dexterity).toString()
        dex_mod_textView.text = temp

        val const_textView = findViewById<TextView>(R.id.constitution_textView_actual)
        const_textView.text = _player._statistics.Constitution.toString()
        val const_mod_textView = findViewById<TextView>(R.id.constitution_textView_mod_actual)
            if (_player._statistics.Constitution - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Constitution).toString()
        const_mod_textView.text = temp

        val intel_textView = findViewById<TextView>(R.id.intelligence_textView_actual)
        intel_textView.text = _player._statistics.Intelligence.toString()
        val intel_mod_textView = findViewById<TextView>(R.id.intelligence_textView_mod_actual)
            if (_player._statistics.Intelligence - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence).toString()
        intel_mod_textView.text = temp

        val wis_textView = findViewById<TextView>(R.id.wisdom_textView_actual)
        wis_textView.text = _player._statistics.Wisdom.toString()
        val wis_mod_textView = findViewById<TextView>(R.id.wisdom_textView_mod_actual)
            if (_player._statistics.Wisdom - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom).toString()
        wis_mod_textView.text = temp

        val charisma_textView = findViewById<TextView>(R.id.charisma_textView_actual)
        charisma_textView.text = _player._statistics.Charisma.toString()
        val charisma_mod_textView = findViewById<TextView>(R.id.charisma_textView_mod_actual)
            if (_player._statistics.Charisma - 10 >= 0) { temp = "+" }
            else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma).toString()
        charisma_mod_textView.text = temp

        val perc_textView = findViewById<TextView>(R.id.perception_textView_actual)
        perc_textView.text = _player._statistics.Perception.toString()

        // SAVING THROWS INFORMATION
        val stStr_radio = findViewById<RadioButton>(R.id.save_throw_str)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.STRENGTH, _player._saving_throws)) {
            stStr_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.STRENGTH, _player._saving_throws)}  Strength"
            stStr_radio.text = temp
        }

        val stDex_radio = findViewById<RadioButton>(R.id.save_throw_dex)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.DEXTERITY, _player._saving_throws)) {
            stDex_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.DEXTERITY, _player._saving_throws)}  Dexterity"
            stDex_radio.text = temp
        }

        val stConst_radio = findViewById<RadioButton>(R.id.save_throw_const)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.CONSTITUTION, _player._saving_throws)) {
            stConst_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.CONSTITUTION, _player._saving_throws)}  Constitution"
            stConst_radio.text = temp
        }

        val stInt_radio = findViewById<RadioButton>(R.id.save_throw_int)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.INTELLIGENCE, _player._saving_throws)) {
            stInt_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.INTELLIGENCE, _player._saving_throws)}  Intelligence"
            stInt_radio.text = temp
        }

        val stWis_radio = findViewById<RadioButton>(R.id.save_throw_wis)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.WISDOM, _player._saving_throws)) {
            stWis_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.WISDOM, _player._saving_throws)}  Wisdom"
            stWis_radio.text = temp
        }

        val stChar_radio = findViewById<RadioButton>(R.id.save_throw_char)
        if (checkForEnumValueInArray(Character.Base_Stats_Enum.CHARISMA, _player._saving_throws)) {
            stChar_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.CHARISMA, _player._saving_throws)}  Charisma"
            stChar_radio.text = temp
        }

        // SKILLS INFORMATION
        val acro_radio = findViewById<RadioButton>(R.id.prof_acrobatics)
        if (checkForEnumValueInArray(Character.Skill.ACROBATICS, _player._proficiencies)) {
            acro_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ACROBATICS, _player._proficiencies)}  Acrobatics (Dex)"
            acro_radio.text = temp
        }

        val AH_radio = findViewById<RadioButton>(R.id.prof_animal_handling)
        if (checkForEnumValueInArray(Character.Skill.ANIMAL_HANDLING, _player._proficiencies)) {
            AH_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ANIMAL_HANDLING, _player._proficiencies)}  Animal Handling (Wis)"
            AH_radio.text = temp
        }

        val arcana_radio = findViewById<RadioButton>(R.id.prof_arcana)
        if (checkForEnumValueInArray(Character.Skill.ARCANA, _player._proficiencies)) {
            arcana_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ARCANA, _player._proficiencies)}  Arcana (Int)"
            arcana_radio.text = temp
        }

        val athl_radio = findViewById<RadioButton>(R.id.prof_athletics)
        if (checkForEnumValueInArray(Character.Skill.ATHLETICS, _player._proficiencies)) {
            athl_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ATHLETICS, _player._proficiencies)}  Athletics (Str)"
            athl_radio.text = temp
        }

        val dec_radio = findViewById<RadioButton>(R.id.prof_deception)
        if (checkForEnumValueInArray(Character.Skill.DECEPTION, _player._proficiencies)) {
            dec_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.DECEPTION, _player._proficiencies)}  Deception (Cha)"
            dec_radio.text = temp
        }

        val his_radio = findViewById<RadioButton>(R.id.prof_history)
        if (checkForEnumValueInArray(Character.Skill.HISTORY, _player._proficiencies)) {
            his_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.HISTORY, _player._proficiencies)}  History (Int)"
            his_radio.text = temp
        }

        val Insight_radio = findViewById<RadioButton>(R.id.prof_insight)
        if (checkForEnumValueInArray(Character.Skill.INSIGHT, _player._proficiencies)) {
            Insight_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INSIGHT, _player._proficiencies)}  Insight (Wis)"
            Insight_radio.text = temp
        }

        val int_radio = findViewById<RadioButton>(R.id.prof_intimidation)
        if (checkForEnumValueInArray(Character.Skill.INTIMIDATION, _player._proficiencies)) {
            int_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INTIMIDATION, _player._proficiencies)}  Intimidation (Cha)"
            int_radio.text = temp
        }

        val inv_radio = findViewById<RadioButton>(R.id.prof_investigation)
        if (checkForEnumValueInArray(Character.Skill.INVESTIGATION, _player._proficiencies)) {
            inv_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INVESTIGATION, _player._proficiencies)}  Investigation (Int)"
            inv_radio.text = temp
        }

        val med_radio = findViewById<RadioButton>(R.id.prof_medicine)
        if (checkForEnumValueInArray(Character.Skill.MEDICINE, _player._proficiencies)) {
            med_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.MEDICINE, _player._proficiencies)}  Medicine (Wis)"
            med_radio.text = temp
        }

        val nat_radio = findViewById<RadioButton>(R.id.prof_nature)
        if (checkForEnumValueInArray(Character.Skill.NATURE, _player._proficiencies)) {
            nat_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.NATURE, _player._proficiencies)}  Nature (Int)"
            nat_radio.text = temp
        }

        val perc_radio = findViewById<RadioButton>(R.id.prof_perception)
        if (checkForEnumValueInArray(Character.Skill.PERCEPTION, _player._proficiencies)) {
            perc_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERCEPTION, _player._proficiencies)}  Perception (Wis)"
            perc_radio.text = temp
        }

        val perf_radio = findViewById<RadioButton>(R.id.prof_performance)
        if (checkForEnumValueInArray(Character.Skill.PERFORMANCE, _player._proficiencies)) {
            perf_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERFORMANCE, _player._proficiencies)}  Performance (Cha)"
            perf_radio.text = temp
        }

        val pers_radio = findViewById<RadioButton>(R.id.prof_persuasion)
        if (checkForEnumValueInArray(Character.Skill.PERSUASION, _player._proficiencies)) {
            pers_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERSUASION, _player._proficiencies)}  Persuasion (Cha)"
            pers_radio.text = temp
        }

        val rel_radio = findViewById<RadioButton>(R.id.prof_religion)
        if (checkForEnumValueInArray(Character.Skill.RELIGION, _player._proficiencies)) {
            rel_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.RELIGION, _player._proficiencies)}  Religion (Int)"
            rel_radio.text = temp
        }

        val soh_radio = findViewById<RadioButton>(R.id.prof_sleight_of_hand)
        if (checkForEnumValueInArray(Character.Skill.SLEIGHT_OF_HAND, _player._proficiencies)) {
            soh_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.SLEIGHT_OF_HAND, _player._proficiencies)}  Sleight of Hand (Dex)"
            soh_radio.text = temp
        }

        val st_radio = findViewById<RadioButton>(R.id.prof_stealth)
        if (checkForEnumValueInArray(Character.Skill.STEALTH, _player._proficiencies)) {
            st_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.STEALTH, _player._proficiencies)}  Stealth (Dex)"
            st_radio.text = temp
        }

        val sur_radio = findViewById<RadioButton>(R.id.prof_survival)
        if (checkForEnumValueInArray(Character.Skill.SURVIVAL, _player._proficiencies)) {
            sur_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.SURVIVAL, _player._proficiencies)}  Survival (Wis)"
            sur_radio.text = temp
        }

        // OTHER PROFICIENCIES INFORMATION
        val otherProf_textView = findViewById<TextView>(R.id.other_proficiencies_textView)
        temp = ""
        for (i in _player._proficiencies.indices) {
            if (_player._proficiencies[i] != null) {
                val prof: Character.Proficient_In = _player._proficiencies[i]

                if (prof.proficiency != Character.Skill.ACROBATICS
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
                    temp += "+${prof.bonus}  ${prof.proficiency}\n\n"
                }
            }
        }
        otherProf_textView.text = temp

        // INITIATIVE INFORMATION
        val init_textView = findViewById<TextView>(R.id.initiative_textView_actual)
        init_textView.text = _player._initiative.toString()

        // ATTACK INFORMATION
        val a1n_textView = findViewById<TextView>(R.id.attack1_name_editText)
        val a1b_textView = findViewById<TextView>(R.id.attack1_bonus_editText)
        val a1dt_textView = findViewById<TextView>(R.id.attack1_damageType_editText)
        var i : Int = 0
        while (i < _player._attacks.size) {
            if (_player._attacks[i] != null) {
                if (_player._attacks[i] is Physical) {
                    val tempPhysical: Physical = _player._attacks[i] as Physical
                    a1n_textView.text = tempPhysical._name
                    temp = "+${tempPhysical._bonus}"
                    a1b_textView.text = temp
                    temp = "${tempPhysical._num_dice}${tempPhysical._die}  /  "
                    temp += _player._attacks[i]._special   // TODO: CHANGE TO DAMAGE TYPE
                    a1dt_textView.text = temp

                    i++
                    break
                }
                else {
                    i++
                }
            }
            else {
                break
            }
        }

        val a2n_textView = findViewById<TextView>(R.id.attack2_name_editText)
        val a2b_textView = findViewById<TextView>(R.id.attack2_bonus_editText)
        val a2dt_textView = findViewById<TextView>(R.id.attack2_damageType_editText)
        while (i < _player._attacks.size) {
            if (_player._attacks[i] != null) {
                if (_player._attacks[i] is Physical) {
                    val tempPhysical: Physical = _player._attacks[i] as Physical
                    a2n_textView.text = tempPhysical._name
                    temp = "+${tempPhysical._bonus}"
                    a2b_textView.text = temp
                    temp = "${tempPhysical._num_dice}${tempPhysical._die}  /  "
                    temp += _player._attacks[i]._special   // TODO: CHANGE TO DAMAGE TYPE
                    a2dt_textView.text = temp

                    i++
                    break
                }
                else {
                    i++
                }
            }
            else {
                break
            }
        }

        val a3n_textView = findViewById<TextView>(R.id.attack3_name_editText)
        val a3b_textView = findViewById<TextView>(R.id.attack3_bonus_editText)
        val a3dt_textView = findViewById<TextView>(R.id.attack3_damageType_editText)
        while (i < _player._attacks.size) {
            if (_player._attacks[i] != null) {
                if (_player._attacks[i] is Physical) {
                    val tempPhysical: Physical = _player._attacks[i] as Physical
                    a3n_textView.text = tempPhysical._name
                    temp = "+${tempPhysical._bonus}"
                    a3b_textView.text = temp
                    temp = "${tempPhysical._num_dice}${tempPhysical._die}  /  "
                    temp += _player._attacks[i]._special   // TODO: CHANGE TO DAMAGE TYPE
                    a3dt_textView.text = temp

                    i++
                    break
                }
                else {
                    i++
                }
            }
            else {
                break
            }
        }

        // WEAPONS INFORMATION
        val weapons_textView = findViewById<TextView>(R.id.otherAttacks_editText)
        temp = ""
        for (k in _player._weapons.indices) {
            if (_player._weapons[k] != null) {
                val weapon: Physical.Weapon_Struct = _player._weapons[k]

                temp += "${weapon.weapon}  :  ${weapon.num_die}${weapon.die}\n\n"
            }
        }
        weapons_textView.text = temp

        // EQUIPMENT INFORMATION
        val cp_textView = findViewById<TextView>(R.id.copper_textView)
        cp_textView.text = _player._money.copper.toString()

        val sp_textView = findViewById<TextView>(R.id.silver_textView)
        sp_textView.text = _player._money.silver.toString()

        val ep_textView = findViewById<TextView>(R.id.electrum_textView)
        ep_textView.text = _player._money.electrum.toString()

        val gp_textView = findViewById<TextView>(R.id.gold_textView)
        gp_textView.text = _player._money.gold.toString()

        val pp_textView = findViewById<TextView>(R.id.platinum_textView)
        pp_textView.text = _player._money.platinum.toString()

        val equip_textView = findViewById<TextView>(R.id.equipment_textView)
        temp = ""
        for (p in _player._items.indices) {
            if (_player._items[p] != null) {
                val item: Character.Item = _player._items[p]

                temp += item.item

                if (item.attributes != "") {
                    temp += "(${item.attributes})"
                }

                temp += "\n\n"
            }
        }
        equip_textView.text = temp

        // SPELLS INFORMATION
        val spells_section = findViewById<ConstraintLayout>(R.id.spells_section)
        val ability = findViewById<TextView>(R.id.spell_ability_textView_actual)
        val dc = findViewById<TextView>(R.id.spell_dc_textView_actual)
        val bonus = findViewById<TextView>(R.id.spell_bonus_textView_actual)

        var tempdc = 8 + _player._proficiency_bonus
        val tempBonus = tempdc + _player._proficiency_bonus
        temp = "+${tempBonus}"
        bonus.text = temp

        var spellcaster = true
        when (_player._classe) {
            Player.Classes.ARTIFICER -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.INTELLIGENCE.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                temp = "+${tempdc}"
                dc.text = temp
            }
            Player.Classes.BARD -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.CHARISMA.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.CLERIC -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.WISDOM.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.DRUID -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.WISDOM.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.FIGHTER -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.INTELLIGENCE.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.PALADIN -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.CHARISMA.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.RANGER -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.WISDOM.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.ROGUE -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.INTELLIGENCE.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.SORCERER -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.CHARISMA.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.WARLOCK -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.CHARISMA.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.WIZARD -> {
                spells_section.visibility = View.VISIBLE
                ability.text = Character.Base_Stats_Enum.INTELLIGENCE.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                temp = "+" + tempdc
                dc.text = temp
            }
            else -> {
                spells_section.visibility = View.INVISIBLE
                spells_section.maxHeight = 0
                spellcaster = false
            }
        }

        if (spellcaster) {
            val totalSpells = arrayOf(0,0,0,0,0,0,0,0,0,0)

            for (k in _player._attacks.indices) {
                if (_player._attacks[k] != null) {
                    if (_player._attacks[k] is Magical) {
                        val spell: Magical = _player._attacks[k] as Magical

                        when (spell._level) {
                            0 -> totalSpells[0] += 1
                            1 -> totalSpells[1] += 1
                            2 -> totalSpells[2] += 1
                            3 -> totalSpells[3] += 1
                            4 -> totalSpells[4] += 1
                            5 -> totalSpells[5] += 1
                            6 -> totalSpells[6] += 1
                            7 -> totalSpells[7] += 1
                            8 -> totalSpells[8] += 1
                            9 -> totalSpells[9] += 1
                        }
                    }
                }
            }

            // SET CANTRIPS
            val cantrips = findViewById<ConstraintLayout>(R.id.Row16)
            if (totalSpells[0] > 0) {
                val cantrips_rg = findViewById<RadioGroup>(R.id.cantrips_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 0) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                cantrips_rg.addView(radio)
                            }
                        }
                    }
                }
            }
            else {
                cantrips.visibility = View.INVISIBLE
                cantrips.maxHeight = 0
            }

            // SET LEVEL 1 SPELLS
            val spells1 = findViewById<ConstraintLayout>(R.id.Row17) // 1 = 17
            if (totalSpells[1] > 0) {
                val spells1_rg = findViewById<RadioGroup>(R.id.spell1_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 1) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                radio.isClickable = false
                                spells1_rg.addView(radio)
                            }
                        }
                    }
                }

                val l1_slots = findViewById<TextView>(R.id.spell1_total_textView)
                l1_slots.text = _player._spell_slots[1].toString()

                val l1_used_slots = findViewById<TextView>(R.id.spell1_expended_textView)
                l1_used_slots.text = _player._used_spell_slots[1].toString()
            }
            else {
                spells1.visibility = View.INVISIBLE
                spells1.maxHeight = 0
            }

            // SET LEVEL 2 SPELLS
            val spells2 = findViewById<ConstraintLayout>(R.id.Row18)
            if (totalSpells[2] > 0) {
                val spells2_rg = findViewById<RadioGroup>(R.id.spell2_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 2) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                spells2_rg.addView(radio)
                            }
                        }
                    }
                }

                val l2_slots = findViewById<TextView>(R.id.spell2_total_textView)
                l2_slots.text = _player._spell_slots[2].toString()

                val l2_used_slots = findViewById<TextView>(R.id.spell2_expended_textView)
                l2_used_slots.text = _player._used_spell_slots[2].toString()
            }
            else {
                spells2.visibility = View.INVISIBLE
                spells2.maxHeight = 0
            }

            // SET LEVEL 3 SPELLS
            val spells3 = findViewById<ConstraintLayout>(R.id.Row19)
            if (totalSpells[3] > 0) {
                val spells3_rg = findViewById<RadioGroup>(R.id.spell3_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 3) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                spells3_rg.addView(radio)
                            }
                        }
                    }
                }

                val l3_slots = findViewById<TextView>(R.id.spell3_total_textView)
                l3_slots.text = _player._spell_slots[3].toString()

                val l3_used_slots = findViewById<TextView>(R.id.spell3_expended_textView)
                l3_used_slots.text = _player._used_spell_slots[3].toString()
            }
            else {
                spells3.visibility = View.INVISIBLE
                spells3.maxHeight = 0
            }

            // SET LEVEL 4 SPELLS
            val spells4 = findViewById<ConstraintLayout>(R.id.Row20)
            if (totalSpells[4] > 0) {
                val spells4_rg = findViewById<RadioGroup>(R.id.spell4_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 4) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                spells4_rg.addView(radio)
                            }
                        }
                    }
                }

                val l4_slots = findViewById<TextView>(R.id.spell4_total_textView)
                l4_slots.text = _player._spell_slots[4].toString()

                val l4_used_slots = findViewById<TextView>(R.id.spell4_expended_textView)
                l4_used_slots.text = _player._used_spell_slots[4].toString()
            }
            else {
                spells4.visibility = View.INVISIBLE
                spells4.maxHeight = 0
            }

            // SET LEVEL 5 SPELLS
            val spells5 = findViewById<ConstraintLayout>(R.id.Row21)
            if (totalSpells[5] > 0) {
                val spells5_rg = findViewById<RadioGroup>(R.id.spell5_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 5) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                spells5_rg.addView(radio)
                            }
                        }
                    }
                }

                val l5_slots = findViewById<TextView>(R.id.spell5_total_textView)
                l5_slots.text = _player._spell_slots[5].toString()

                val l5_used_slots = findViewById<TextView>(R.id.spell5_expended_textView)
                l5_used_slots.text = _player._used_spell_slots[5].toString()
            }
            else {
                spells5.visibility = View.INVISIBLE
                spells5.maxHeight = 0
            }

            // SET LEVEL 6 SPELLS
            val spells6 = findViewById<ConstraintLayout>(R.id.Row22)
            if (totalSpells[6] > 0) {
                val spells6_rg = findViewById<RadioGroup>(R.id.spell6_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 6) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                spells6_rg.addView(radio)
                            }
                        }
                    }
                }

                val l6_slots = findViewById<TextView>(R.id.spell6_total_textView)
                l6_slots.text = _player._spell_slots[6].toString()

                val l6_used_slots = findViewById<TextView>(R.id.spell6_expended_textView)
                l6_used_slots.text = _player._used_spell_slots[6].toString()
            }
            else {
                spells6.visibility = View.INVISIBLE
                spells6.maxHeight = 0
            }

            // SET LEVEL 7 SPELLS
            val spells7 = findViewById<ConstraintLayout>(R.id.Row23)
            if (totalSpells[7] > 0) {
                val spells7_rg = findViewById<RadioGroup>(R.id.spell7_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 7) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                spells7_rg.addView(radio)
                            }
                        }
                    }
                }

                val l7_slots = findViewById<TextView>(R.id.spell7_total_textView)
                l7_slots.text = _player._spell_slots[7].toString()

                val l7_used_slots = findViewById<TextView>(R.id.spell7_expended_textView)
                l7_used_slots.text = _player._used_spell_slots[7].toString()
            }
            else {
                spells7.visibility = View.INVISIBLE
                spells7.maxHeight = 0
            }

            // SET LEVEL 8 SPELLS
            val spells8 = findViewById<ConstraintLayout>(R.id.Row24)
            if (totalSpells[8] > 0) {
                val spells8_rg = findViewById<RadioGroup>(R.id.spell8_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 8) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                spells8_rg.addView(radio)
                            }
                        }
                    }
                }

                val l8_slots = findViewById<TextView>(R.id.spell8_total_textView)
                l8_slots.text = _player._spell_slots[8].toString()

                val l8_used_slots = findViewById<TextView>(R.id.spell8_expended_textView)
                l8_used_slots.text = _player._used_spell_slots[8].toString()
            }
            else {
                spells8.visibility = View.INVISIBLE
                spells8.maxHeight = 0
            }

            // SET LEVEL 9 SPELLS
            val spells9 = findViewById<ConstraintLayout>(R.id.Row25)
            if (totalSpells[9] > 0) {
                val spells9_rg = findViewById<RadioGroup>(R.id.spell9_radioGroup)

                for (k in _player._attacks.indices) {
                    if (_player._attacks[k] != null) {
                        if (_player._attacks[k] is Magical) {
                            val spell: Magical = _player._attacks[k] as Magical

                            if (spell._level == 9) {
                                val radio = RadioButton(this)
                                radio.id = View.generateViewId()
                                radio.text = spell._name
                                spells9_rg.addView(radio)
                            }
                        }
                    }
                }

                val l9_slots = findViewById<TextView>(R.id.spell9_total_textView)
                l9_slots.text = _player._spell_slots[9].toString()

                val l9_used_slots = findViewById<TextView>(R.id.spell9_expended_textView)
                l9_used_slots.text = _player._used_spell_slots[9].toString()
            }
            else {
                spells9.visibility = View.INVISIBLE
                spells9.maxHeight = 0
            }
        }
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