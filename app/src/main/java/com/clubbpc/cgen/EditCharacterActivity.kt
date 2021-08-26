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

    // TODO: DELETE THIS; MOVE TO A SAVE BUTTON THEN OPEN VIEW CHARACTER
    override fun onDestroy() {
        super.onDestroy()

        if (gameMode == "DND") {
            if (charType == "PLAYER") {
                saveInformation(currentPlayer)
                tempDNDplayers.document(currentPlayer._name).set(currentPlayer._hashMap)
            }
            else {
                saveInformation(currentMonster)
                tempDNDmonsters.document(currentMonster._name).set(currentMonster._hashMap)
            }
        }
        else if (gameMode == "PATHFINDER") {
            if (charType == "PLAYER") {
                saveInformation(currentPlayer)
                tempPFplayers.document(currentPlayer._name).set(currentPlayer._hashMap)
            }
            else {
                saveInformation(currentMonster)
                tempPFmonsters.document(currentMonster._name).set(currentMonster._hashMap)
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun saveInformation(_player: CharacterPackage.Player) {
        var temp = ""
        val tempInt = 0

        // CHARACTER INFORMATION
        val class_spinner = findViewById<Spinner>(R.id.class_spinner_actual)
        _player._classe = Player.Classes.valueOf(class_spinner.selectedItem.toString())

        val background_editText = findViewById<EditText>(R.id.background_editText_actual)
        _player._background = background_editText.text.toString()

        val race_spinner = findViewById<Spinner>(R.id.race_spinner_actual)
        _player._race = Player.Race.valueOf(race_spinner.selectedItem.toString())

        val align_spinner = findViewById<Spinner>(R.id.alignment_spinner_actual)
        _player._alignment = Character.Alignment.valueOf(align_spinner.selectedItem.toString())

        // GENERIC STAT INFORMATION
        val initiative_editText = findViewById<EditText>(R.id.initiative_editText_actual)
        _player._initiative = initiative_editText.text.toString().toInt()

        val inspiration = findViewById<EditText>(R.id.insp_editText_actual)
        _player._inspiration = inspiration.text.toString().toInt()

        val profBonus = findViewById<EditText>(R.id.profBonus_editText_actual)
        _player._proficiency_bonus = profBonus.text.toString().toInt()

        val xp = findViewById<EditText>(R.id.xp_editText_actual)
        _player._experience_points = xp.text.toString().toInt()

        val level = findViewById<EditText>(R.id.level_editText_actual)
        _player._level = level.text.toString().toInt()

        val ac = findViewById<EditText>(R.id.ac_editText_actual)
        _player._armor_rating = ac.text.toString().toInt()

        val speed = findViewById<EditText>(R.id.speed_editText_actual)
        _player._speed = speed.text.toString().toInt()

        val maxHP = findViewById<EditText>(R.id.maxHealthPoints_editText_actual)
        _player._health_points = maxHP.text.toString().toInt()

        // TODO: NEED TO IMPLEMENT ONE MORE THING FOR THIS SECTION: SPINNER OF DIE
        val hDice = findViewById<EditText>(R.id.hd_editText_actual)
        _player._total_hp_dice = hDice.text.toString().toInt()

        val dsS = findViewById<EditText>(R.id.ds_successes_editText_actual)
        _player._death_saves.successes = dsS.text.toString().toInt()

        val dsF = findViewById<EditText>(R.id.ds_failures_editText_actual)
        _player._death_saves.failures = dsF.text.toString().toInt()

        // BASE STAT INFORMATION
        val str = findViewById<EditText>(R.id.strength_editText_actual)
        _player._statistics.Strength = str.text.toString().toInt()

        val dex = findViewById<EditText>(R.id.dexterity_editText_actual)
        _player._statistics.Dexterity = dex.text.toString().toInt()

        val cons = findViewById<EditText>(R.id.constitution_editText_actual)
        _player._statistics.Constitution = cons.text.toString().toInt()

        val int = findViewById<EditText>(R.id.intelligence_editText_actual)
        _player._statistics.Intelligence = int.text.toString().toInt()

        val wis = findViewById<EditText>(R.id.wisdom_editText_actual)
        _player._statistics.Wisdom = wis.text.toString().toInt()

        val char = findViewById<EditText>(R.id.charisma_editText_actual)
        _player._statistics.Charisma = char.text.toString().toInt()

        val perc = findViewById<EditText>(R.id.perception_editText_actual)
        _player._statistics.Perception = perc.text.toString().toInt()

        // SAVING THROWS INFORMATION
        val strST = findViewById<RadioButton>(R.id.save_throw_str)
        if (strST.isChecked) {
            _player.add_saving_throw(
                Character.Saving_Throw(
                    Character.Base_Stats_Enum.STRENGTH,
                    Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Strength)
                )
            )
        }

        val dexST = findViewById<RadioButton>(R.id.save_throw_dex)
        if (dexST.isChecked) {
            _player.add_saving_throw(
                Character.Saving_Throw(
                    Character.Base_Stats_Enum.DEXTERITY,
                    Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Dexterity)
                )
            )
        }

        val conST = findViewById<RadioButton>(R.id.save_throw_const)
        if (conST.isChecked) {
            _player.add_saving_throw(
                Character.Saving_Throw(
                    Character.Base_Stats_Enum.CONSTITUTION,
                    Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Constitution)
                )
            )
        }

        val intST = findViewById<RadioButton>(R.id.save_throw_int)
        if (intST.isChecked) {
            _player.add_saving_throw(
                Character.Saving_Throw(
                    Character.Base_Stats_Enum.INTELLIGENCE,
                    Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                )
            )
        }

        val wisST = findViewById<RadioButton>(R.id.save_throw_wis)
        if (wisST.isChecked) {
            _player.add_saving_throw(
                Character.Saving_Throw(
                    Character.Base_Stats_Enum.WISDOM,
                    Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom)
                )
            )
        }

        val charST = findViewById<RadioButton>(R.id.save_throw_char)
        if (charST.isChecked) {
            _player.add_saving_throw(
                Character.Saving_Throw(
                    Character.Base_Stats_Enum.CHARISMA,
                    Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                )
            )
        }
        
        // PROFICIENCIES INFORMATION
        val acro = findViewById<RadioButton>(R.id.prof_acrobatics)
        if (acro.isChecked) {
            val prof = Character.Proficient_In (
                    Character.Skill.ACROBATICS,
                    _player._level,
                    "",
                    Character.Base_Stats_Enum.DEXTERITY
                )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Dexterity)
            
            _player.add_proficiency(prof)
        }

        val AH = findViewById<RadioButton>(R.id.prof_animal_handling)
        if (AH.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.ANIMAL_HANDLING,
                _player._level,
                "",
                Character.Base_Stats_Enum.WISDOM
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                    _player._statistics.Wisdom
                )

            _player.add_proficiency(prof)
        }

        val arc = findViewById<RadioButton>(R.id.prof_arcana)
        if (arc.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.ARCANA,
                _player._level,
                "",
                Character.Base_Stats_Enum.INTELLIGENCE
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Intelligence
            )

            _player.add_proficiency(prof)
        }

        val ath = findViewById<RadioButton>(R.id.prof_athletics)
        if (ath.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.ATHLETICS,
                _player._level,
                "",
                Character.Base_Stats_Enum.STRENGTH
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Strength
            )

            _player.add_proficiency(prof)
        }

        val dec = findViewById<RadioButton>(R.id.prof_deception)
        if (dec.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.DECEPTION,
                _player._level,
                "",
                Character.Base_Stats_Enum.CHARISMA
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Charisma
            )

            _player.add_proficiency(prof)
        }

        val his = findViewById<RadioButton>(R.id.prof_history)
        if (his.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.HISTORY,
                _player._level,
                "",
                Character.Base_Stats_Enum.INTELLIGENCE
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Intelligence
            )

            _player.add_proficiency(prof)
        }

        val ins = findViewById<RadioButton>(R.id.prof_insight)
        if (ins.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.INSIGHT,
                _player._level,
                "",
                Character.Base_Stats_Enum.WISDOM
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Wisdom
            )

            _player.add_proficiency(prof)
        }

        val intP = findViewById<RadioButton>(R.id.prof_intimidation)
        if (intP.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.INTIMIDATION,
                _player._level,
                "",
                Character.Base_Stats_Enum.CHARISMA
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Charisma
            )

            _player.add_proficiency(prof)
        }

        val inv = findViewById<RadioButton>(R.id.prof_investigation)
        if (inv.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.INVESTIGATION,
                _player._level,
                "",
                Character.Base_Stats_Enum.INTELLIGENCE
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Intelligence
            )

            _player.add_proficiency(prof)
        }

        val med = findViewById<RadioButton>(R.id.prof_medicine)
        if (med.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.MEDICINE,
                _player._level,
                "",
                Character.Base_Stats_Enum.WISDOM
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Wisdom
            )

            _player.add_proficiency(prof)
        }

        val nat = findViewById<RadioButton>(R.id.prof_nature)
        if (nat.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.NATURE,
                _player._level,
                "",
                Character.Base_Stats_Enum.INTELLIGENCE
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Intelligence
            )

            _player.add_proficiency(prof)
        }

        val percP = findViewById<RadioButton>(R.id.prof_perception)
        if (percP.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.PERCEPTION,
                _player._level,
                "",
                Character.Base_Stats_Enum.WISDOM
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Wisdom
            )

            _player.add_proficiency(prof)
        }

        val perf = findViewById<RadioButton>(R.id.prof_performance)
        if (perf.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.PERFORMANCE,
                _player._level,
                "",
                Character.Base_Stats_Enum.CHARISMA
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Charisma
            )

            _player.add_proficiency(prof)
        }

        val pers = findViewById<RadioButton>(R.id.prof_persuasion)
        if (pers.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.PERSUASION,
                _player._level,
                "",
                Character.Base_Stats_Enum.CHARISMA
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Charisma
            )

            _player.add_proficiency(prof)
        }

        val rel = findViewById<RadioButton>(R.id.prof_religion)
        if (rel.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.RELIGION,
                _player._level,
                "",
                Character.Base_Stats_Enum.INTELLIGENCE
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Intelligence
            )

            _player.add_proficiency(prof)
        }

        val soh = findViewById<RadioButton>(R.id.prof_sleight_of_hand)
        if (soh.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.SLEIGHT_OF_HAND,
                _player._level,
                "",
                Character.Base_Stats_Enum.DEXTERITY
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Dexterity
            )

            _player.add_proficiency(prof)
        }

        val ste = findViewById<RadioButton>(R.id.prof_stealth)
        if (ste.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.STEALTH,
                _player._level,
                "",
                Character.Base_Stats_Enum.DEXTERITY
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Dexterity
            )

            _player.add_proficiency(prof)
        }

        val sur = findViewById<RadioButton>(R.id.prof_survival)
        if (sur.isChecked) {
            val prof = Character.Proficient_In (
                Character.Skill.SURVIVAL,
                _player._level,
                "",
                Character.Base_Stats_Enum.WISDOM
            )
            prof.bonus += Character.Base_Stats_Struct.calculate_modifier(
                _player._statistics.Wisdom
            )

            _player.add_proficiency(prof)
        }
        
        // OTHER PROFICIENCIES, ATTACKS, WEAPONS, EQUIPMENT, AND SPELLS
        // INFORMATION IS UNDER THE BUTTONS ABOVE IN ONSTART(), EXCEPT
        // FOR THE FOLLOWING:
        
        val copper = findViewById<EditText>(R.id.copper_editText)
        _player._money.copper = copper.text.toString().toInt()
        
        val silver = findViewById<EditText>(R.id.silver_editText)
        _player._money.silver = silver.text.toString().toInt()
        
        val electrum = findViewById<EditText>(R.id.electrum_editText)
        _player._money.electrum  = electrum.text.toString().toInt()
        
        val gold = findViewById<EditText>(R.id.gold_editText)
        _player._money.gold = gold.text.toString().toInt()
        
        val platinum = findViewById<EditText>(R.id.platinum_editText)
        _player._money.platinum = platinum.text.toString().toInt()
        
        //

        val ability = findViewById<TextView>(R.id.spell_ability_editText_actual)
        val dc = findViewById<TextView>(R.id.spell_dc_editText_actual)
        val bonus = findViewById<TextView>(R.id.spell_bonus_editText_actual)

        var tempdc = 8 + _player._proficiency_bonus
        val tempBonus = tempdc + _player._proficiency_bonus
        temp = "+${tempBonus}"
        bonus.text = temp

        when (_player._classe) {
            Player.Classes.ARTIFICER -> {
                ability.text = Character.Base_Stats_Enum.INTELLIGENCE.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                temp = "+${tempdc}"
                dc.text = temp
            }
            Player.Classes.BARD -> {
                ability.text = Character.Base_Stats_Enum.CHARISMA.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.CLERIC -> {
                ability.text = Character.Base_Stats_Enum.WISDOM.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.DRUID -> {
                ability.text = Character.Base_Stats_Enum.WISDOM.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.FIGHTER -> {
                ability.text = Character.Base_Stats_Enum.INTELLIGENCE.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.PALADIN -> {
                ability.text = Character.Base_Stats_Enum.CHARISMA.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.RANGER -> {
                ability.text = Character.Base_Stats_Enum.WISDOM.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.ROGUE -> {
                ability.text = Character.Base_Stats_Enum.INTELLIGENCE.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.SORCERER -> {
                ability.text = Character.Base_Stats_Enum.CHARISMA.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.WARLOCK -> {
                ability.text = Character.Base_Stats_Enum.CHARISMA.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma)
                temp = "+" + tempdc
                dc.text = temp
            }
            Player.Classes.WIZARD -> {
                ability.text = Character.Base_Stats_Enum.INTELLIGENCE.toString()
                tempdc += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence)
                temp = "+" + tempdc
                dc.text = temp
            }
            else -> {
                ability.text = "None"
                dc.text = "N/A"
                bonus.text = "N/A"
            }
        }
        
        //
        
        val spell1 = findViewById<EditText>(R.id.spell1_total_editText)
        _player._spell_slots[1] = spell1.text.toString().toInt()

        val usedSpell1 = findViewById<EditText>(R.id.spell1_expended_editText)
        _player._used_spell_slots[1] = usedSpell1.text.toString().toInt()

        val spell2 = findViewById<EditText>(R.id.spell2_total_editText)
        _player._spell_slots[2] = spell2.text.toString().toInt()

        val usedSpell2 = findViewById<EditText>(R.id.spell2_expended_editText)
        _player._used_spell_slots[2] = usedSpell2.text.toString().toInt()

        val spell3 = findViewById<EditText>(R.id.spell3_total_editText)
        _player._spell_slots[3] = spell3.text.toString().toInt()

        val usedSpell3 = findViewById<EditText>(R.id.spell3_expended_editText)
        _player._used_spell_slots[3] = usedSpell3.text.toString().toInt()

        val spell4 = findViewById<EditText>(R.id.spell4_total_editText)
        _player._spell_slots[4] = spell4.text.toString().toInt()

        val usedSpell4 = findViewById<EditText>(R.id.spell4_expended_editText)
        _player._used_spell_slots[4] = usedSpell4.text.toString().toInt()

        val spell5 = findViewById<EditText>(R.id.spell5_total_editText)
        _player._spell_slots[5] = spell5.text.toString().toInt()

        val usedSpell5 = findViewById<EditText>(R.id.spell5_expended_editText)
        _player._used_spell_slots[5] = usedSpell5.text.toString().toInt()

        val spell6 = findViewById<EditText>(R.id.spell6_total_editText)
        _player._spell_slots[6] = spell6.text.toString().toInt()

        val usedSpell6 = findViewById<EditText>(R.id.spell6_expended_editText)
        _player._used_spell_slots[6] = usedSpell6.text.toString().toInt()

        val spell7 = findViewById<EditText>(R.id.spell7_total_editText)
        _player._spell_slots[7] = spell7.text.toString().toInt()

        val usedSpell7 = findViewById<EditText>(R.id.spell7_expended_editText)
        _player._used_spell_slots[7] = usedSpell7.text.toString().toInt()

        val spell8 = findViewById<EditText>(R.id.spell8_total_editText)
        _player._spell_slots[8] = spell8.text.toString().toInt()

        val usedSpell8 = findViewById<EditText>(R.id.spell8_expended_editText)
        _player._used_spell_slots[8] = usedSpell8.text.toString().toInt()

        val spell9 = findViewById<EditText>(R.id.spell9_total_editText)
        _player._spell_slots[9] = spell9.text.toString().toInt()

        val usedSpell9 = findViewById<EditText>(R.id.spell9_expended_editText)
        _player._used_spell_slots[9] = usedSpell9.text.toString().toInt()
    }
    private fun saveInformation(_monster: CharacterPackage.Monster) {

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