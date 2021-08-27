package com.clubbpc.cgen

import AttackPackage.Physical
import CharacterPackage.Character
import CharacterPackage.Monster
import CharacterPackage.Player
import Utility.Die
import Utility.Utility.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_character)

        // HIDE NEW ITEMS
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

        // DEAL WITH BUTTONS
        val backButton: ImageButton = findViewById<ImageButton>(R.id.back_imageButton)
        backButton.setOnClickListener {
            val myIntent = Intent(this, CharacterGridActivity::class.java)
            startActivity(myIntent)
            finish()
        }

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
                    Utility.Die.valueOf(findViewById<Spinner>(R.id.dialog_hitDie_spinner_attack).selectedItem.toString()),
                )

                currentPlayer.add_attack(tempAttack)

                Toast.makeText(this, "Attack Added Successfully.",
                    Toast.LENGTH_SHORT).show()

                newAttackRow.visibility = View.GONE
                newAttackRow.layoutParams.height = 0

                val a1n_textView = findViewById<TextView>(R.id.attack1_name_editText)
                val a1b_textView = findViewById<TextView>(R.id.attack1_bonus_editText)
                val a1dt_textView = findViewById<TextView>(R.id.attack1_damageType_editText)
                var i : Int = 0
                var temp = ""
                while (i < currentPlayer._attacks.size) {
                    if (currentPlayer._attacks[i] != null) {
                        if (currentPlayer._attacks[i] is Physical) {
                            val tempPhysical: Physical = currentPlayer._attacks[i] as Physical
                            a1n_textView.text = tempPhysical._name
                            temp = "+${tempPhysical._bonus}"
                            a1b_textView.text = temp
                            temp = "${tempPhysical._num_dice}${tempPhysical._die}  /  "
                            temp += currentPlayer._attacks[i]._special   // TODO: CHANGE TO DAMAGE TYPE
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
                while (i < currentPlayer._attacks.size) {
                    if (currentPlayer._attacks[i] != null) {
                        if (currentPlayer._attacks[i] is Physical) {
                            val tempPhysical: Physical = currentPlayer._attacks[i] as Physical
                            a2n_textView.text = tempPhysical._name
                            temp = "+${tempPhysical._bonus}"
                            a2b_textView.text = temp
                            temp = "${tempPhysical._num_dice}${tempPhysical._die}  /  "
                            temp += currentPlayer._attacks[i]._special   // TODO: CHANGE TO DAMAGE TYPE
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
                while (i < currentPlayer._attacks.size) {
                    if (currentPlayer._attacks[i] != null) {
                        if (currentPlayer._attacks[i] is Physical) {
                            val tempPhysical: Physical = currentPlayer._attacks[i] as Physical
                            a3n_textView.text = tempPhysical._name
                            temp = "+${tempPhysical._bonus}"
                            a3b_textView.text = temp
                            temp = "${tempPhysical._num_dice}${tempPhysical._die}  /  "
                            temp += currentPlayer._attacks[i]._special   // TODO: CHANGE TO DAMAGE TYPE
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
                    Utility.Die.valueOf(findViewById<Spinner>(R.id.dialog_die_spinner_weapon).selectedItem.toString()),
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
            newAttackRow.visibility = View.GONE
            newAttackRow.layoutParams.height = 0
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
        alignment_spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Character.Alignment.values())

        val prof_skill_spinner = findViewById<Spinner>(R.id.dialog_skill_spinner_prof)
        var tempArray = Array<Character.Skill?>(45,{i -> null})
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
            prof_skill_spinner.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, tempArray)
        }

        val prof_stat_spinner = findViewById<Spinner>(R.id.dialog_stat_spinner_prof)
        prof_stat_spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Character.Base_Stats_Enum.values())

        val attack_die_spinner = findViewById<Spinner>(R.id.dialog_die_spinner_attack)
        attack_die_spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Utility.Die.values())

        val attack_weapon_spinner = findViewById<Spinner>(R.id.dialog_weapon_spinner_attack)
        attack_weapon_spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Physical.Weapon_Enum.values())

        val attack_hitDie_spinner = findViewById<Spinner>(R.id.dialog_hitDie_spinner_attack)
        attack_hitDie_spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Utility.Die.values())

        val weapon_die_spinner = findViewById<Spinner>(R.id.dialog_die_spinner_weapon)
        weapon_die_spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Utility.Die.values())

        val weapon_weapon_spinner = findViewById<Spinner>(R.id.dialog_weapon_spinner_weapon)
        weapon_weapon_spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Physical.Weapon_Enum.values())

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