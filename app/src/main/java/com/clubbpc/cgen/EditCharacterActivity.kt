package com.clubbpc.cgen

import AttackPackage.Magical
import AttackPackage.Physical
import CharacterPackage.Character
import CharacterPackage.Monster
import CharacterPackage.Player
import Utility.Die
import Utility.Utility
import Utility.Utility.TAG

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var toReturn = false

        when (item.itemId) {
            R.id.action_goBack_item -> {
                Utility.MENU.GoBack(this)
                toReturn = true
            }
            R.id.action_cancelView_item -> {
                Utility.MENU.CancelAndView(this, gameMode, charType, currentPlayer)
                toReturn = true
            }
            R.id.action_saveBack_item -> {
                Utility.MENU.SaveAndGoBack(this, gameMode, charType, currentPlayer)
                toReturn = true
            }
            R.id.action_saveView_item -> {
                Utility.MENU.SaveAndView(this, gameMode, charType, currentPlayer)
                toReturn = true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return toReturn
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_character)

        // HIDE NEW ITEM DIALOGS
        val newProfRow = findViewById<ConstraintLayout>(R.id.Row13_1)
        newProfRow.visibility = View.GONE
        newProfRow.layoutParams.height = 0

        val newItemRow = findViewById<ConstraintLayout>(R.id.Row13_6)
        newItemRow.visibility = View.GONE
        newItemRow.layoutParams.height = 0

        val newAttackRow = findViewById<ConstraintLayout>(R.id.Row14_5)
        newAttackRow.visibility = View.GONE
        newAttackRow.layoutParams.height = 0

        val newWeaponRow = findViewById<ConstraintLayout>(R.id.Row14_6)
        newWeaponRow.visibility = View.GONE
        newWeaponRow.layoutParams.height = 0

        val newSpellRow = findViewById<ConstraintLayout>(R.id.Row15_5)
        newSpellRow.visibility = View.GONE
        newSpellRow.layoutParams.height = 0

        // DEAL WITH BUTTONS
            // NEW PROFICIENCY
        val newProfButton = findViewById<ImageButton>(R.id.newProficiency_imageButton)
        newProfButton.setOnClickListener {
            newProfRow.visibility = View.VISIBLE
            newProfRow.layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280f, resources.displayMetrics).toInt()
        }

        val createProf = findViewById<Button>(R.id.dialog_create_button_prof)
        createProf.setOnClickListener {
            if (canContinue_prof()) {
                currentPlayer.add_proficiency(
                    Character.Proficient_In(
                        Character.Skill.valueOf(findViewById<Spinner>(R.id.dialog_skill_spinner_prof).selectedItem.toString()),
                        findViewById<EditText>(R.id.dialog_playerLevel_editText_prof).text.toString().toInt(),
                        findViewById<EditText>(R.id.dialog_special_editText_prof).text.toString(),
                        Character.Base_Stats_Enum.valueOf(findViewById<Spinner>(R.id.dialog_stat_spinner_prof).selectedItem.toString())
                    )
                )

                Toast.makeText(this, "Proficiency Added Successfully.",
                    Toast.LENGTH_SHORT).show()

                newProfRow.visibility = View.GONE
                newProfRow.layoutParams.height = 0


                val otherProf_textView = findViewById<TextView>(R.id.other_proficiencies_editText)
                var temp = ""
                for (i in currentPlayer._proficiencies.indices) {
                    if (currentPlayer._proficiencies[i] != null) {
                        val prof: Character.Proficient_In = currentPlayer._proficiencies[i]

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
            }
            else {
                Toast.makeText(this, "Proficiency Not Added: One or more values missing.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        val cancelProf = findViewById<Button>(R.id.dialog_cancel_button_prof)
        cancelProf.setOnClickListener {
            newProfRow.visibility = View.GONE
            newProfRow.layoutParams.height = 0
        }

            // NEW ITEM
        val newItemButton = findViewById<ImageButton>(R.id.newEquipment_imageButton)
        newItemButton.setOnClickListener {
            newItemRow.visibility = View.VISIBLE
            newItemRow.layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500f, resources.displayMetrics).toInt()
        }

        val createItem = findViewById<Button>(R.id.dialog_create_button_item)
        createItem.setOnClickListener {
            if (canContinue_item()) {
                val tempItem = Character.Item()
                tempItem.item = findViewById<EditText>(R.id.dialog_item_editText_item).text.toString()
                tempItem.cost = Character.Money (
                    findViewById<EditText>(R.id.dialog_copper_editText_item).text.toString().toInt(),
                    findViewById<EditText>(R.id.dialog_silver_editText_item).text.toString().toInt(),
                    findViewById<EditText>(R.id.dialog_electrum_editText_item).text.toString().toInt(),
                    findViewById<EditText>(R.id.dialog_gold_editText_item).text.toString().toInt(),
                    findViewById<EditText>(R.id.dialog_platinum_editText_item).text.toString().toInt(),
                )
                tempItem.weight = findViewById<EditText>(R.id.dialog_weight_editText_item).text.toString().toInt()
                tempItem.attributes =findViewById<EditText>(R.id.dialog_attributes_editText_item).text.toString()

                currentPlayer.add_item(tempItem)

                Toast.makeText(this, "Item Added Successfully.",
                    Toast.LENGTH_SHORT).show()

                newItemRow.visibility = View.GONE
                newItemRow.layoutParams.height = 0

                val equip_textView = findViewById<TextView>(R.id.equipment_editText)
                var temp = ""
                for (p in currentPlayer._items.indices) {
                    if (currentPlayer._items[p] != null) {
                        val item: Character.Item = currentPlayer._items[p]

                        temp += item.item

                        if (item.attributes != "") {
                            temp += " (${item.attributes})"
                        }

                        temp += "\n\n"
                    }
                }
                equip_textView.text = temp
            }
            else {
                Toast.makeText(this, "Item Not Added: One or more values missing.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        val cancelItem = findViewById<Button>(R.id.dialog_cancel_button_item)
        cancelItem.setOnClickListener {
            newItemRow.visibility = View.GONE
            newItemRow.layoutParams.height = 0
        }

            // NEW ATTACK
        val newAttackButton = findViewById<ImageButton>(R.id.newAttack_imageButton)
        newAttackButton.setOnClickListener {
            newAttackRow.visibility = View.VISIBLE
            newAttackRow.layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 375f, resources.displayMetrics).toInt()
        }

        val createAttack = findViewById<Button>(R.id.dialog_create_button_attack)
        createAttack.setOnClickListener {
            if (canContinue_attack()) {
                val tempAttack = Physical()
                tempAttack._name = findViewById<EditText>(R.id.dialog_name_editText_attack).text.toString()
                tempAttack._special = findViewById<EditText>(R.id.dialog_special_editText_attack).text.toString()
                tempAttack._bonus = findViewById<EditText>(R.id.dialog_bonus_editText_attack).text.toString().toInt()
                tempAttack._num_dice = findViewById<EditText>(R.id.dialog_numDie_editText_attack).text.toString().toInt()
                tempAttack._die = Die.valueOf(findViewById<Spinner>(R.id.dialog_die_spinner_attack).selectedItem.toString())
                tempAttack._weapon_info = Physical.Weapon_Struct(
                    Physical.Weapon_Enum.valueOf(findViewById<Spinner>(R.id.dialog_weapon_spinner_attack).selectedItem.toString()),
                    findViewById<EditText>(R.id.dialog_numHitDie_editText_attack).text.toString().toInt(),
                    Die.valueOf(findViewById<Spinner>(R.id.dialog_hitDie_spinner_attack).selectedItem.toString()),
                )

                currentPlayer.add_attack(tempAttack)

                Toast.makeText(this, "Attack Added Successfully.",
                    Toast.LENGTH_SHORT).show()

                newAttackRow.visibility = View.GONE
                newAttackRow.layoutParams.height = 0



                val name_TVA = Array<TextView>(3, {m -> TextView(this)})
                name_TVA[0] = findViewById<TextView>(R.id.attack1_name_editText)
                name_TVA[1] = findViewById<TextView>(R.id.attack2_name_editText)
                name_TVA[2] = findViewById<TextView>(R.id.attack3_name_editText)

                val bonus_TVA = Array<TextView>(3, {m -> TextView(this)})
                bonus_TVA[0] = findViewById<TextView>(R.id.attack1_bonus_editText)
                bonus_TVA[1] = findViewById<TextView>(R.id.attack2_bonus_editText)
                bonus_TVA[2] = findViewById<TextView>(R.id.attack3_bonus_editText)

                val type_TVA = Array<TextView>(3, {m -> TextView(this)})
                type_TVA[0] = findViewById<TextView>(R.id.attack1_damageType_editText)
                type_TVA[1] = findViewById<TextView>(R.id.attack2_damageType_editText)
                type_TVA[2] = findViewById<TextView>(R.id.attack3_damageType_editText)

                var i: Int = 0
                for (h in 0..2 ) {
                    while (i < currentPlayer._attacks.size) {
                        var temp = ""
                        if (currentPlayer._attacks[i] != null) {
                            if (currentPlayer._attacks[i] is Physical) {
                                val tempPhysical: Physical = currentPlayer._attacks[i] as Physical

                                name_TVA[h].text = tempPhysical._name

                                temp = "+${tempPhysical._bonus}"
                                bonus_TVA[h].text = temp

                                temp = "${tempPhysical._num_dice}${tempPhysical._die}  /  "
                                temp += currentPlayer._attacks[i]._special   // TODO: CHANGE TO DAMAGE TYPE
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
            }
            else {
                Toast.makeText(this, "Attack Not Added: One or more values missing.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        val cancelAttack = findViewById<Button>(R.id.dialog_cancel_button_attack)
        cancelAttack.setOnClickListener {
            newAttackRow.visibility = View.GONE
            newAttackRow.layoutParams.height = 0
        }

            // NEW WEAPON
        val newWeaponButton = findViewById<ImageButton>(R.id.newWeapon_imageButton)
        newWeaponButton.setOnClickListener {
            newWeaponRow.visibility = View.VISIBLE
            newWeaponRow.layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190f, resources.displayMetrics).toInt()
        }

        val createWeapon = findViewById<Button>(R.id.dialog_create_button_weapon)
        createWeapon.setOnClickListener {
            if (canContinue_weapon()) {
                val tempWeapon = Physical.Weapon_Struct(
                    Physical.Weapon_Enum.valueOf(findViewById<Spinner>(R.id.dialog_weapon_spinner_weapon).selectedItem.toString()),
                    findViewById<EditText>(R.id.dialog_numDie_editText_weapon).text.toString().toInt(),
                    Die.valueOf(findViewById<Spinner>(R.id.dialog_die_spinner_weapon).selectedItem.toString()),
                )

                currentPlayer.add_weapon(tempWeapon)

                Toast.makeText(this, "Weapon Added Successfully.",
                    Toast.LENGTH_SHORT).show()

                newWeaponRow.visibility = View.GONE
                newWeaponRow.layoutParams.height = 0

                val weapons_textView = findViewById<TextView>(R.id.otherAttacks_editText)
                var temp = ""
                for (k in currentPlayer._weapons.indices) {
                    if (currentPlayer._weapons[k] != null) {
                        val weapon: Physical.Weapon_Struct = currentPlayer._weapons[k]

                        temp += "${weapon.weapon}  :  ${weapon.num_die}${weapon.die}\n\n"
                    }
                }
                weapons_textView.text = temp
            }
            else {
                Toast.makeText(this, "Weapon Not Added: One or more values missing.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        val cancelWeapon = findViewById<Button>(R.id.dialog_cancel_button_weapon)
        cancelWeapon.setOnClickListener {
            newWeaponRow.visibility = View.GONE
            newWeaponRow.layoutParams.height = 0
        }

            // NEW SPELL
        val newSpellButton = findViewById<ImageButton>(R.id.newSpell_imageButton)
        newSpellButton.setOnClickListener {
            newSpellRow.visibility = View.VISIBLE
            newSpellRow.layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 425f, resources.displayMetrics).toInt()
        }

        val createSpell = findViewById<Button>(R.id.dialog_create_button_spell)
        createSpell.setOnClickListener {
            if (canContinue_spell()) {
                val tempSpell = Magical()
                tempSpell._name = findViewById<EditText>(R.id.dialog_name_editText_spell).text.toString()
                tempSpell._special = findViewById<EditText>(R.id.dialog_special_editText_spell).text.toString()
                tempSpell._level = findViewById<EditText>(R.id.dialog_level_editText_spell).text.toString().toInt()
                tempSpell._time_in_actions = findViewById<EditText>(R.id.dialog_time_editText_spell).text.toString().toInt()
                tempSpell._range = findViewById<EditText>(R.id.dialog_range_editText_spell).text.toString().toInt()
                tempSpell._duration = findViewById<EditText>(R.id.dialog_duration_editText_spell).text.toString()

                currentPlayer.add_attack(tempSpell)

                Toast.makeText(this, "Spell Added Successfully.",
                    Toast.LENGTH_SHORT).show()

                newSpellRow.visibility = View.GONE
                newSpellRow.layoutParams.height = 0



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

                val radioGroupsArray = Array<RadioGroup>(10, {i -> RadioGroup(this) })
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

                val slotsEditTextsArray = Array<EditText>(10, { i -> EditText(this) })
                slotsEditTextsArray[1] = findViewById<EditText>(R.id.spell1_total_editText)
                slotsEditTextsArray[2] = findViewById<EditText>(R.id.spell2_total_editText)
                slotsEditTextsArray[3] = findViewById<EditText>(R.id.spell3_total_editText)
                slotsEditTextsArray[4] = findViewById<EditText>(R.id.spell4_total_editText)
                slotsEditTextsArray[5] = findViewById<EditText>(R.id.spell5_total_editText)
                slotsEditTextsArray[6] = findViewById<EditText>(R.id.spell6_total_editText)
                slotsEditTextsArray[7] = findViewById<EditText>(R.id.spell7_total_editText)
                slotsEditTextsArray[8] = findViewById<EditText>(R.id.spell8_total_editText)
                slotsEditTextsArray[9] = findViewById<EditText>(R.id.spell9_total_editText)

                val usedslotsEditTextsArray = Array<EditText>(10, { i -> EditText(this)})
                usedslotsEditTextsArray[1] = findViewById<EditText>(R.id.spell1_expended_editText)
                usedslotsEditTextsArray[2] = findViewById<EditText>(R.id.spell2_expended_editText)
                usedslotsEditTextsArray[3] = findViewById<EditText>(R.id.spell3_expended_editText)
                usedslotsEditTextsArray[4] = findViewById<EditText>(R.id.spell4_expended_editText)
                usedslotsEditTextsArray[5] = findViewById<EditText>(R.id.spell5_expended_editText)
                usedslotsEditTextsArray[6] = findViewById<EditText>(R.id.spell6_expended_editText)
                usedslotsEditTextsArray[7] = findViewById<EditText>(R.id.spell7_expended_editText)
                usedslotsEditTextsArray[8] = findViewById<EditText>(R.id.spell8_expended_editText)
                usedslotsEditTextsArray[9] = findViewById<EditText>(R.id.spell9_expended_editText)

                for (h in radioGroupsArray.indices) {
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

                    slotsEditTextsArray[h].text = Editable.Factory.getInstance().newEditable(currentPlayer._spell_slots[h].toString())
                    usedslotsEditTextsArray[h].text = Editable.Factory.getInstance().newEditable(currentPlayer._used_spell_slots[h].toString())
                }

            }
            else {
                Toast.makeText(this, "Spell Not Added: One or more values missing.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        val cancelSpell = findViewById<Button>(R.id.dialog_cancel_button_spell)
        cancelSpell.setOnClickListener {
            newSpellRow.visibility = View.GONE
            newSpellRow.layoutParams.height = 0
        }
    }

    override fun onStart() {
        super.onStart()

        previousActivity = intent.getStringExtra("INFO FROM").toString()
        charName = intent.getStringExtra("CHARACTER NAME").toString()
        charType = intent.getStringExtra("CHARACTER TYPE").toString()
        gameMode = intent.getStringExtra("CHARACTER GAME").toString()
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////         Retrieve from enums          //////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        val class_spinner = findViewById<Spinner>(R.id.class_spinner_actual)
        class_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Player.Classes.values())

        val race_spinner = findViewById<Spinner>(R.id.race_spinner_actual)
        race_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Player.Race.values())

        val alignment_spinner = findViewById<Spinner>(R.id.alignment_spinner_actual)
        alignment_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Character.Alignment.values())

        val prof_skill_spinner = findViewById<Spinner>(R.id.dialog_skill_spinner_prof)
        val tempArray = Array<Character.Skill?>(45,{i -> null})
        var i = 0
        for (prof in Character.Skill.values()) {
            if (prof != Character.Skill.ACROBATICS
                && prof != Character.Skill.ANIMAL_HANDLING
                && prof != Character.Skill.ARCANA
                && prof != Character.Skill.ATHLETICS
                && prof != Character.Skill.DECEPTION
                && prof != Character.Skill.HISTORY
                && prof != Character.Skill.INSIGHT
                && prof != Character.Skill.INTIMIDATION
                && prof != Character.Skill.INVESTIGATION
                && prof != Character.Skill.MEDICINE
                && prof != Character.Skill.NATURE
                && prof != Character.Skill.PERCEPTION
                && prof != Character.Skill.PERFORMANCE
                && prof != Character.Skill.PERSUASION
                && prof != Character.Skill.RELIGION
                && prof != Character.Skill.SLEIGHT_OF_HAND
                && prof != Character.Skill.STEALTH
                && prof != Character.Skill.SURVIVAL
            ) {
                tempArray[i] = prof
                i++
            }
            prof_skill_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tempArray)
        }

        val prof_stat_spinner = findViewById<Spinner>(R.id.dialog_stat_spinner_prof)
        prof_stat_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Character.Base_Stats_Enum.values())

        val attack_die_spinner = findViewById<Spinner>(R.id.dialog_die_spinner_attack)
        attack_die_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Die.values())

        val attack_weapon_spinner = findViewById<Spinner>(R.id.dialog_weapon_spinner_attack)
        attack_weapon_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Physical.Weapon_Enum.values())

        val attack_hitDie_spinner = findViewById<Spinner>(R.id.dialog_hitDie_spinner_attack)
        attack_hitDie_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Die.values())

        val weapon_die_spinner = findViewById<Spinner>(R.id.dialog_die_spinner_weapon)
        weapon_die_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Die.values())

        val weapon_weapon_spinner = findViewById<Spinner>(R.id.dialog_weapon_spinner_weapon)
        weapon_weapon_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Physical.Weapon_Enum.values())

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
                        else if (previousActivity == "VIEW CHARACTER") {
                            setMadeInformation(currentPlayer)
                        }
                    }
            }
            else {
                tempDNDmonsters.document(charName).get()
                    .addOnCompleteListener{ document ->
                        currentMonster = Monster(document.result?.data as java.util.HashMap<String, String>)

                        if (previousActivity == "NEW CHARACTER") {
                            setNewInformation(currentMonster)
                        }
                        else if (previousActivity == "VIEW CHARACTER") {
                            setMadeInformation(currentPlayer)
                        }
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun canContinue_prof(): Boolean {
        var doCont = true

        if (findViewById<EditText>(R.id.dialog_playerLevel_editText_prof).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_special_editText_prof).text.toString() == "") {
            doCont = false
        }

        return doCont
    }
    private fun canContinue_item(): Boolean {
        var doCont = true

        if (findViewById<EditText>(R.id.dialog_item_editText_item).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_copper_editText_item).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_silver_editText_item).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_electrum_editText_item).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_gold_editText_item).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_platinum_editText_item).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_weight_editText_item).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_attributes_editText_item).text.toString() == "") {
            doCont = false
        }

        return doCont
    }
    private fun canContinue_attack(): Boolean {
        var doCont = true

        if (findViewById<EditText>(R.id.dialog_name_editText_attack).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_special_editText_attack).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_bonus_editText_attack).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_numDie_editText_attack).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_numHitDie_editText_attack).text.toString() == "") {
            doCont = false
        }

        return doCont
    }
    private fun canContinue_weapon(): Boolean {
        var doCont = true

        if (findViewById<EditText>(R.id.dialog_numDie_editText_weapon).text.toString() == "") {
            doCont = false
        }

        return doCont
    }
    private fun canContinue_spell(): Boolean {
        var doCont = true

        if (findViewById<EditText>(R.id.dialog_name_editText_spell).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_special_editText_spell).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_level_editText_spell).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_time_editText_spell).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_range_editText_spell).text.toString() == "") {
            doCont = false
        }
        if (findViewById<EditText>(R.id.dialog_duration_editText_spell).text.toString() == "") {
            doCont = false
        }

        return doCont
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
                    " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
                " ",
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
        
        // TODO: MOVE TO SET INFORMATION

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
        var temp = ""
        var tempInt = 0

        // INTRO TOOLBAR
        val name_toolbar = findViewById<Toolbar>(R.id.characterName_toolbar)
        name_toolbar.title = _player._name
        temp = "${_player._game_mode} ${_player._char_type}"
        name_toolbar.subtitle = temp

        // CHARACTER INFORMATION
        val class_spinner = findViewById<Spinner>(R.id.class_spinner_actual)
        tempInt = 0
        enumValues<Player.Classes>().forEach { classe ->
            if (classe == _player._classe) {
                tempInt = classe.ordinal
            }
        }
        class_spinner.setSelection(tempInt, false)

        val background_editText = findViewById<TextView>(R.id.background_editText_actual)
        background_editText.text = _player._background.toString()

        val race_spinner = findViewById<Spinner>(R.id.race_spinner_actual)
        tempInt = 0
        enumValues<Player.Race>().forEach { race ->
            if (race == _player._race) {
                tempInt = race.ordinal
            }
        }
        race_spinner.setSelection(tempInt, false)

        val alignment_spinner = findViewById<Spinner>(R.id.alignment_spinner_actual)
        tempInt = 0
        enumValues<Character.Alignment>().forEach { align ->
            if (align == _player._alignment) {
                tempInt = align.ordinal
            }
        }
        alignment_spinner.setSelection(tempInt, false)

        // GENERIC STAT INFORMATION
        val xp_editText = findViewById<TextView>(R.id.xp_editText_actual)
        xp_editText.text = _player._experience_points.toString()

        val level_editText = findViewById<TextView>(R.id.level_editText_actual)
        level_editText.text = _player._level.toString()

        val ac_editText = findViewById<TextView>(R.id.ac_editText_actual)
        ac_editText.text = _player._armor_rating.toString()

        val speed_editText = findViewById<TextView>(R.id.speed_editText_actual)
        speed_editText.text = _player._speed.toString()

        val mhp_editText = findViewById<TextView>(R.id.maxHealthPoints_editText_actual)
        mhp_editText.text = _player._health_points.toString()

        val hd_editText = findViewById<TextView>(R.id.hd_editText_actual)
        temp = "${_player._total_hp_dice}${_player._hp_die}"
        hd_editText.text = temp

        val dsF_editText = findViewById<TextView>(R.id.ds_failures_editText_actual)
        dsF_editText.text = _player._death_saves.failures.toString()
        val dsS_editText = findViewById<TextView>(R.id.ds_successes_editText_actual)
        dsS_editText.text = _player._death_saves.successes.toString()

        val insp_editText = findViewById<TextView>(R.id.insp_editText_actual)
        insp_editText.text = _player._inspiration.toString()

        val profBonus_editText = findViewById<TextView>(R.id.profBonus_editText_actual)
        profBonus_editText.text = _player._proficiency_bonus.toString()

        // BASE STAT INFORMATION
        val str_editText = findViewById<TextView>(R.id.strength_editText_actual)
        str_editText.text = _player._statistics.Strength.toString()
        val str_mod_editText = findViewById<TextView>(R.id.strength_editText_mod_actual)
        if (_player._statistics.Strength - 10 >= 0) { temp = "+" }
        else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Strength).toString()
        str_mod_editText.text = temp

        val dex_editText = findViewById<TextView>(R.id.dexterity_editText_actual)
        dex_editText.text = _player._statistics.Dexterity.toString()
        val dex_mod_editText = findViewById<TextView>(R.id.dexterity_editText_mod_actual)
        if (_player._statistics.Dexterity - 10 >= 0) { temp = "+" }
        else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Dexterity).toString()
        dex_mod_editText.text = temp

        val const_editText = findViewById<TextView>(R.id.constitution_editText_actual)
        const_editText.text = _player._statistics.Constitution.toString()
        val const_mod_editText = findViewById<TextView>(R.id.constitution_editText_mod_actual)
        if (_player._statistics.Constitution - 10 >= 0) { temp = "+" }
        else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Constitution).toString()
        const_mod_editText.text = temp

        val intel_editText = findViewById<TextView>(R.id.intelligence_editText_actual)
        intel_editText.text = _player._statistics.Intelligence.toString()
        val intel_mod_editText = findViewById<TextView>(R.id.intelligence_editText_mod_actual)
        if (_player._statistics.Intelligence - 10 >= 0) { temp = "+" }
        else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Intelligence).toString()
        intel_mod_editText.text = temp

        val wis_editText = findViewById<TextView>(R.id.wisdom_editText_actual)
        wis_editText.text = _player._statistics.Wisdom.toString()
        val wis_mod_editText = findViewById<TextView>(R.id.wisdom_editText_mod_actual)
        if (_player._statistics.Wisdom - 10 >= 0) { temp = "+" }
        else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Wisdom).toString()
        wis_mod_editText.text = temp

        val charisma_editText = findViewById<TextView>(R.id.charisma_editText_actual)
        charisma_editText.text = _player._statistics.Charisma.toString()
        val charisma_mod_editText = findViewById<TextView>(R.id.charisma_editText_mod_actual)
        if (_player._statistics.Charisma - 10 >= 0) { temp = "+" }
        else { temp = "-" }
        temp += Character.Base_Stats_Struct.calculate_modifier(_player._statistics.Charisma).toString()
        charisma_mod_editText.text = temp

        val perc_editText = findViewById<TextView>(R.id.perception_editText_actual)
        perc_editText.text = _player._statistics.Perception.toString()

        // SAVING THROWS INFORMATION
        val stStr_radio = findViewById<RadioButton>(R.id.save_throw_str)
        if (Utility.checkForEnumValueInArray(
                Character.Base_Stats_Enum.STRENGTH,
                _player._saving_throws
            )
        ) {
            stStr_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.STRENGTH, _player._saving_throws)}  Strength"
            stStr_radio.text = temp
        }

        val stDex_radio = findViewById<RadioButton>(R.id.save_throw_dex)
        if (Utility.checkForEnumValueInArray(
                Character.Base_Stats_Enum.DEXTERITY,
                _player._saving_throws
            )
        ) {
            stDex_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.DEXTERITY, _player._saving_throws)}  Dexterity"
            stDex_radio.text = temp
        }

        val stConst_radio = findViewById<RadioButton>(R.id.save_throw_const)
        if (Utility.checkForEnumValueInArray(
                Character.Base_Stats_Enum.CONSTITUTION,
                _player._saving_throws
            )
        ) {
            stConst_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.CONSTITUTION, _player._saving_throws)}  Constitution"
            stConst_radio.text = temp
        }

        val stInt_radio = findViewById<RadioButton>(R.id.save_throw_int)
        if (Utility.checkForEnumValueInArray(
                Character.Base_Stats_Enum.INTELLIGENCE,
                _player._saving_throws
            )
        ) {
            stInt_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.INTELLIGENCE, _player._saving_throws)}  Intelligence"
            stInt_radio.text = temp
        }

        val stWis_radio = findViewById<RadioButton>(R.id.save_throw_wis)
        if (Utility.checkForEnumValueInArray(
                Character.Base_Stats_Enum.WISDOM,
                _player._saving_throws
            )
        ) {
            stWis_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.WISDOM, _player._saving_throws)}  Wisdom"
            stWis_radio.text = temp
        }

        val stChar_radio = findViewById<RadioButton>(R.id.save_throw_char)
        if (Utility.checkForEnumValueInArray(
                Character.Base_Stats_Enum.CHARISMA,
                _player._saving_throws
            )
        ) {
            stChar_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Base_Stats_Enum.CHARISMA, _player._saving_throws)}  Charisma"
            stChar_radio.text = temp
        }

        // SKILLS INFORMATION
        val acro_radio = findViewById<RadioButton>(R.id.prof_acrobatics)
        if (Utility.checkForEnumValueInArray(Character.Skill.ACROBATICS, _player._proficiencies)) {
            acro_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ACROBATICS, _player._proficiencies)}  Acrobatics (Dex)"
            acro_radio.text = temp
        }

        val AH_radio = findViewById<RadioButton>(R.id.prof_animal_handling)
        if (Utility.checkForEnumValueInArray(
                Character.Skill.ANIMAL_HANDLING,
                _player._proficiencies
            )
        ) {
            AH_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ANIMAL_HANDLING, _player._proficiencies)}  Animal Handling (Wis)"
            AH_radio.text = temp
        }

        val arcana_radio = findViewById<RadioButton>(R.id.prof_arcana)
        if (Utility.checkForEnumValueInArray(Character.Skill.ARCANA, _player._proficiencies)) {
            arcana_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ARCANA, _player._proficiencies)}  Arcana (Int)"
            arcana_radio.text = temp
        }

        val athl_radio = findViewById<RadioButton>(R.id.prof_athletics)
        if (Utility.checkForEnumValueInArray(Character.Skill.ATHLETICS, _player._proficiencies)) {
            athl_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.ATHLETICS, _player._proficiencies)}  Athletics (Str)"
            athl_radio.text = temp
        }

        val dec_radio = findViewById<RadioButton>(R.id.prof_deception)
        if (Utility.checkForEnumValueInArray(Character.Skill.DECEPTION, _player._proficiencies)) {
            dec_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.DECEPTION, _player._proficiencies)}  Deception (Cha)"
            dec_radio.text = temp
        }

        val his_radio = findViewById<RadioButton>(R.id.prof_history)
        if (Utility.checkForEnumValueInArray(Character.Skill.HISTORY, _player._proficiencies)) {
            his_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.HISTORY, _player._proficiencies)}  History (Int)"
            his_radio.text = temp
        }

        val Insight_radio = findViewById<RadioButton>(R.id.prof_insight)
        if (Utility.checkForEnumValueInArray(Character.Skill.INSIGHT, _player._proficiencies)) {
            Insight_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INSIGHT, _player._proficiencies)}  Insight (Wis)"
            Insight_radio.text = temp
        }

        val int_radio = findViewById<RadioButton>(R.id.prof_intimidation)
        if (Utility.checkForEnumValueInArray(Character.Skill.INTIMIDATION, _player._proficiencies)) {
            int_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INTIMIDATION, _player._proficiencies)}  Intimidation (Cha)"
            int_radio.text = temp
        }

        val inv_radio = findViewById<RadioButton>(R.id.prof_investigation)
        if (Utility.checkForEnumValueInArray(Character.Skill.INVESTIGATION, _player._proficiencies)) {
            inv_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.INVESTIGATION, _player._proficiencies)}  Investigation (Int)"
            inv_radio.text = temp
        }

        val med_radio = findViewById<RadioButton>(R.id.prof_medicine)
        if (Utility.checkForEnumValueInArray(Character.Skill.MEDICINE, _player._proficiencies)) {
            med_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.MEDICINE, _player._proficiencies)}  Medicine (Wis)"
            med_radio.text = temp
        }

        val nat_radio = findViewById<RadioButton>(R.id.prof_nature)
        if (Utility.checkForEnumValueInArray(Character.Skill.NATURE, _player._proficiencies)) {
            nat_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.NATURE, _player._proficiencies)}  Nature (Int)"
            nat_radio.text = temp
        }

        val perc_radio = findViewById<RadioButton>(R.id.prof_perception)
        if (Utility.checkForEnumValueInArray(Character.Skill.PERCEPTION, _player._proficiencies)) {
            perc_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERCEPTION, _player._proficiencies)}  Perception (Wis)"
            perc_radio.text = temp
        }

        val perf_radio = findViewById<RadioButton>(R.id.prof_performance)
        if (Utility.checkForEnumValueInArray(Character.Skill.PERFORMANCE, _player._proficiencies)) {
            perf_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERFORMANCE, _player._proficiencies)}  Performance (Cha)"
            perf_radio.text = temp
        }

        val pers_radio = findViewById<RadioButton>(R.id.prof_persuasion)
        if (Utility.checkForEnumValueInArray(Character.Skill.PERSUASION, _player._proficiencies)) {
            pers_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.PERSUASION, _player._proficiencies)}  Persuasion (Cha)"
            pers_radio.text = temp
        }

        val rel_radio = findViewById<RadioButton>(R.id.prof_religion)
        if (Utility.checkForEnumValueInArray(Character.Skill.RELIGION, _player._proficiencies)) {
            rel_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.RELIGION, _player._proficiencies)}  Religion (Int)"
            rel_radio.text = temp
        }

        val soh_radio = findViewById<RadioButton>(R.id.prof_sleight_of_hand)
        if (Utility.checkForEnumValueInArray(
                Character.Skill.SLEIGHT_OF_HAND,
                _player._proficiencies
            )
        ) {
            soh_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.SLEIGHT_OF_HAND, _player._proficiencies)}  Sleight of Hand (Dex)"
            soh_radio.text = temp
        }

        val st_radio = findViewById<RadioButton>(R.id.prof_stealth)
        if (Utility.checkForEnumValueInArray(Character.Skill.STEALTH, _player._proficiencies)) {
            st_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.STEALTH, _player._proficiencies)}  Stealth (Dex)"
            st_radio.text = temp
        }

        val sur_radio = findViewById<RadioButton>(R.id.prof_survival)
        if (Utility.checkForEnumValueInArray(Character.Skill.SURVIVAL, _player._proficiencies)) {
            sur_radio.isChecked = true
            temp = "  +${Utility.getModifierForArrayValue(Character.Skill.SURVIVAL, _player._proficiencies)}  Survival (Wis)"
            sur_radio.text = temp
        }

        // OTHER PROFICIENCIES INFORMATION
        val otherProf_editText = findViewById<TextView>(R.id.other_proficiencies_editText)
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
        otherProf_editText.text = temp

        // INITIATIVE INFORMATION
        val init_editText = findViewById<TextView>(R.id.initiative_editText_actual)
        init_editText.text = _player._initiative.toString()

        // ATTACK INFORMATION
        val name_TVA = Array<TextView>(3, {m -> TextView(this)})
        name_TVA[0] = findViewById<TextView>(R.id.attack1_name_editText)
        name_TVA[1] = findViewById<TextView>(R.id.attack2_name_editText)
        name_TVA[2] = findViewById<TextView>(R.id.attack3_name_editText)

        val bonus_TVA = Array<TextView>(3, {m -> TextView(this)})
        bonus_TVA[0] = findViewById<TextView>(R.id.attack1_bonus_editText)
        bonus_TVA[1] = findViewById<TextView>(R.id.attack2_bonus_editText)
        bonus_TVA[2] = findViewById<TextView>(R.id.attack3_bonus_editText)

        val type_TVA = Array<TextView>(3, {m -> TextView(this)})
        type_TVA[0] = findViewById<TextView>(R.id.attack1_damageType_editText)
        type_TVA[1] = findViewById<TextView>(R.id.attack2_damageType_editText)
        type_TVA[2] = findViewById<TextView>(R.id.attack3_damageType_editText)

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
        val weapons_editText = findViewById<TextView>(R.id.otherAttacks_editText)
        temp = ""
        for (k in _player._weapons.indices) {
            if (_player._weapons[k] != null) {
                val weapon: Physical.Weapon_Struct = _player._weapons[k]

                temp += "${weapon.weapon}  :  ${weapon.num_die}${weapon.die}\n\n"
            }
        }
        weapons_editText.text = temp

        // EQUIPMENT INFORMATION
        val cp_editText = findViewById<TextView>(R.id.copper_editText)
        cp_editText.text = _player._money.copper.toString()

        val sp_editText = findViewById<TextView>(R.id.silver_editText)
        sp_editText.text = _player._money.silver.toString()

        val ep_editText = findViewById<TextView>(R.id.electrum_editText)
        ep_editText.text = _player._money.electrum.toString()

        val gp_editText = findViewById<TextView>(R.id.gold_editText)
        gp_editText.text = _player._money.gold.toString()

        val pp_editText = findViewById<TextView>(R.id.platinum_editText)
        pp_editText.text = _player._money.platinum.toString()

        val equip_editText = findViewById<TextView>(R.id.equipment_editText)
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
        equip_editText.text = temp

        // SPELLS INFORMATION
        val spells_section = findViewById<ConstraintLayout>(R.id.spells_section)
        val ability = findViewById<TextView>(R.id.spell_ability_editText_actual)
        val dc = findViewById<TextView>(R.id.spell_dc_editText_actual)
        val bonus = findViewById<TextView>(R.id.spell_bonus_editText_actual)

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
            slotsTextViewsArray[1] = findViewById<TextView>(R.id.spell1_total_editText)
            slotsTextViewsArray[2] = findViewById<TextView>(R.id.spell2_total_editText)
            slotsTextViewsArray[3] = findViewById<TextView>(R.id.spell3_total_editText)
            slotsTextViewsArray[4] = findViewById<TextView>(R.id.spell4_total_editText)
            slotsTextViewsArray[5] = findViewById<TextView>(R.id.spell5_total_editText)
            slotsTextViewsArray[6] = findViewById<TextView>(R.id.spell6_total_editText)
            slotsTextViewsArray[7] = findViewById<TextView>(R.id.spell7_total_editText)
            slotsTextViewsArray[8] = findViewById<TextView>(R.id.spell8_total_editText)
            slotsTextViewsArray[9] = findViewById<TextView>(R.id.spell9_total_editText)

            val usedslotsTextViewsArray = Array<TextView>(10, { m -> TextView(this) })
            usedslotsTextViewsArray[1] = findViewById<TextView>(R.id.spell1_expended_editText)
            usedslotsTextViewsArray[2] = findViewById<TextView>(R.id.spell2_expended_editText)
            usedslotsTextViewsArray[3] = findViewById<TextView>(R.id.spell3_expended_editText)
            usedslotsTextViewsArray[4] = findViewById<TextView>(R.id.spell4_expended_editText)
            usedslotsTextViewsArray[5] = findViewById<TextView>(R.id.spell5_expended_editText)
            usedslotsTextViewsArray[6] = findViewById<TextView>(R.id.spell6_expended_editText)
            usedslotsTextViewsArray[7] = findViewById<TextView>(R.id.spell7_expended_editText)
            usedslotsTextViewsArray[8] = findViewById<TextView>(R.id.spell8_expended_editText)
            usedslotsTextViewsArray[9] = findViewById<TextView>(R.id.spell9_expended_editText)

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

    }

    companion object {
        private const val TAG1_s = "SUCCESS_LOADDATA:NCA"
        private const val TAG1_f = "FAILURE_LOADDATA:NCA"
    }
}