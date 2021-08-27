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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.view_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var toReturn = false

        when (item.itemId) {
            R.id.action_goBack_item -> {
                Utility.MENU.GoBack(this)
                toReturn = true
            }
            R.id.action_edit_item -> {
                Utility.MENU.Edit(this, gameMode, charType, currentPlayer)
                toReturn = true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return toReturn
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
        val name_TVA = Array<TextView>(3, {m -> TextView(this)})
        name_TVA[0] = findViewById<TextView>(R.id.attack1_name_textView)
        name_TVA[1] = findViewById<TextView>(R.id.attack2_name_textView)
        name_TVA[2] = findViewById<TextView>(R.id.attack3_name_textView)

        val bonus_TVA = Array<TextView>(3, {m -> TextView(this)})
        bonus_TVA[0] = findViewById<TextView>(R.id.attack1_bonus_textView)
        bonus_TVA[1] = findViewById<TextView>(R.id.attack2_bonus_textView)
        bonus_TVA[2] = findViewById<TextView>(R.id.attack3_bonus_textView)

        val type_TVA = Array<TextView>(3, {m -> TextView(this)})
        type_TVA[0] = findViewById<TextView>(R.id.attack1_damageType_textView)
        type_TVA[1] = findViewById<TextView>(R.id.attack2_damageType_textView)
        type_TVA[2] = findViewById<TextView>(R.id.attack3_damageType_textView)

        var i: Int = 0
        for (h in 0..2 ) {
            while (i < _player._attacks.size) {
                if (_player._attacks[i] != null) {
                    if (_player._attacks[i] is Physical) {
                        val tempPhysical: Physical = _player._attacks[i] as Physical

                        name_TVA[h].text = tempPhysical._name

                        temp = "+${tempPhysical._bonus}"
                        bonus_TVA[h].text = temp

                        temp = "${tempPhysical._num_dice}${tempPhysical._die}  /  "
                        temp += _player._attacks[i]._special   // TODO: CHANGE TO DAMAGE TYPE
                        type_TVA[h].text = temp

                        i++
                        break
                    } else {
                        i++
                    }
                } else {
                    break
                }
            }
        }

        // WEAPONS INFORMATION
        val weapons_textView = findViewById<TextView>(R.id.otherAttacks_textView)
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

            val spellSectionsArray = Array<ConstraintLayout>(10, { m -> ConstraintLayout(this) })
            spellSectionsArray[0] = findViewById<ConstraintLayout>(R.id.Row16)
            spellSectionsArray[1] = findViewById<ConstraintLayout>(R.id.Row17)
            spellSectionsArray[2] = findViewById<ConstraintLayout>(R.id.Row18)
            spellSectionsArray[3] = findViewById<ConstraintLayout>(R.id.Row19)
            spellSectionsArray[4] = findViewById<ConstraintLayout>(R.id.Row20)
            spellSectionsArray[5] = findViewById<ConstraintLayout>(R.id.Row21)
            spellSectionsArray[6] = findViewById<ConstraintLayout>(R.id.Row22)
            spellSectionsArray[7] = findViewById<ConstraintLayout>(R.id.Row23)
            spellSectionsArray[8] = findViewById<ConstraintLayout>(R.id.Row24)
            spellSectionsArray[9] = findViewById<ConstraintLayout>(R.id.Row25)

            val radioGroupsArray = Array<RadioGroup>(10, { m -> RadioGroup(this) })
            radioGroupsArray[0] = findViewById<RadioGroup>(R.id.cantrips_radioGroup)
            radioGroupsArray[1] = findViewById<RadioGroup>(R.id.spell1_radioGroup)
            radioGroupsArray[2] = findViewById<RadioGroup>(R.id.spell2_radioGroup)
            radioGroupsArray[3] = findViewById<RadioGroup>(R.id.spell3_radioGroup)
            radioGroupsArray[4] = findViewById<RadioGroup>(R.id.spell4_radioGroup)
            radioGroupsArray[5] = findViewById<RadioGroup>(R.id.spell5_radioGroup)
            radioGroupsArray[6] = findViewById<RadioGroup>(R.id.spell6_radioGroup)
            radioGroupsArray[7] = findViewById<RadioGroup>(R.id.spell7_radioGroup)
            radioGroupsArray[8] = findViewById<RadioGroup>(R.id.spell8_radioGroup)
            radioGroupsArray[9] = findViewById<RadioGroup>(R.id.spell9_radioGroup)

            val slotsTextViewsArray = Array<TextView>(10, { m -> TextView(this) })
            slotsTextViewsArray[1] = findViewById<TextView>(R.id.spell1_total_textView)
            slotsTextViewsArray[2] = findViewById<TextView>(R.id.spell2_total_textView)
            slotsTextViewsArray[3] = findViewById<TextView>(R.id.spell3_total_textView)
            slotsTextViewsArray[4] = findViewById<TextView>(R.id.spell4_total_textView)
            slotsTextViewsArray[5] = findViewById<TextView>(R.id.spell5_total_textView)
            slotsTextViewsArray[6] = findViewById<TextView>(R.id.spell6_total_textView)
            slotsTextViewsArray[7] = findViewById<TextView>(R.id.spell7_total_textView)
            slotsTextViewsArray[8] = findViewById<TextView>(R.id.spell8_total_textView)
            slotsTextViewsArray[9] = findViewById<TextView>(R.id.spell9_total_textView)

            val usedslotsTextViewsArray = Array<TextView>(10, { m -> TextView(this) })
            usedslotsTextViewsArray[1] = findViewById<TextView>(R.id.spell1_expended_textView)
            usedslotsTextViewsArray[2] = findViewById<TextView>(R.id.spell2_expended_textView)
            usedslotsTextViewsArray[3] = findViewById<TextView>(R.id.spell3_expended_textView)
            usedslotsTextViewsArray[4] = findViewById<TextView>(R.id.spell4_expended_textView)
            usedslotsTextViewsArray[5] = findViewById<TextView>(R.id.spell5_expended_textView)
            usedslotsTextViewsArray[6] = findViewById<TextView>(R.id.spell6_expended_textView)
            usedslotsTextViewsArray[7] = findViewById<TextView>(R.id.spell7_expended_textView)
            usedslotsTextViewsArray[8] = findViewById<TextView>(R.id.spell8_expended_textView)
            usedslotsTextViewsArray[9] = findViewById<TextView>(R.id.spell9_expended_textView)

            for (h in radioGroupsArray.indices) {
                if (totalSpells[h] > 0) {
                    for (k in currentPlayer._attacks.indices) {
                        if (currentPlayer._attacks[k] != null) {
                            if (currentPlayer._attacks[k] is Magical) {
                                val spell: Magical = currentPlayer._attacks[k] as Magical

                                if (spell._level == h) {
                                    val radio = RadioButton(this)
                                    radio.id = View.generateViewId()
                                    radio.text = spell._name
                                    radioGroupsArray[h].addView(radio)
                                }
                            }
                        }
                    }

                    slotsTextViewsArray[h].text = currentPlayer._spell_slots[h].toString()
                    usedslotsTextViewsArray[h].text = currentPlayer._used_spell_slots[h].toString()
                }
                else {
                    spellSectionsArray[h].visibility = View.INVISIBLE
                    spellSectionsArray[h].maxHeight = 0
                }
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