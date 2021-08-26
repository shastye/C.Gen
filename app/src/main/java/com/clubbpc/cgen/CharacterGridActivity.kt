package com.clubbpc.cgen

import CharacterPackage.Monster
import CharacterPackage.Player
import Utility.Utility.TAG
import Utility.Utility.convertToHashMapStringString

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.clubbpc.cgen.NewCharacterDialog
import com.clubbpc.cgen.IntroActivity
import com.clubbpc.cgen.ViewCharacterActivity


class CharacterGridActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth

    private var tempUsers = Firebase.firestore.collection(TAG.USERS_COLLECTION)
    private var tempUserDoc = tempUsers.document(auth.currentUser?.email.toString())

    private var tempDNDplayers = tempUserDoc.collection(TAG.DND_PLAYERSHEETS_DOCUMENT)
    private var tempPFplayers = tempUserDoc.collection(TAG.PATHFINDER_PLAYERSHEETS_DOCUMENT)
    private var tempDNDmonsters = tempUserDoc.collection(TAG.DND_MONSTERSHEETS_DOCUMENT)
    private var tempPFmonsters = tempUserDoc.collection(TAG.PATHFINDER_MONSTERSHEETS_DOCUMENT)


    private var playerAdapter: PlayerAdapter? = null
    private var monsterAdapter: MonsterAdapter? = null
    private var playerList = ArrayList<Player>(0)
    private var monsterList = ArrayList<Monster>(0)

    private var OGcharType: Int = 0
    private var OGgameType: Int = 0

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()

        OGcharType = intent.getIntExtra(TAG2_pom, 0)
        findViewById<Spinner>(R.id.characterType_spinner).setSelection(OGcharType)

        OGgameType = intent.getIntExtra(TAG2_dop, 0)
        findViewById<Spinner>(R.id.gameType_spinner).setSelection(OGgameType)

        ////////////////////////////////////////////////////////////////////////////////////////////

        if (OGgameType == 0) {
            if (OGcharType == 0) {
                tempDNDplayers.get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val tempHash: HashMap<String, String> = convertToHashMapStringString(document.data as java.util.HashMap<String, com.google.protobuf.Any>)

                            val tempPlayer = Player(tempHash)
                            playerList.add(tempPlayer)

                            playerAdapter = PlayerAdapter(this, playerList)
                            findViewById<GridView>(R.id.character_gridView).adapter = playerAdapter
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("${TAG3_f}DND_P", "Error getting documents.", exception)
                    }
            }
            else {
                tempDNDmonsters.get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val tempHash: HashMap<String, String> = convertToHashMapStringString(document.data as java.util.HashMap<String, com.google.protobuf.Any>)

                            val tempMonster = Monster(tempHash)
                            monsterList.add(tempMonster)

                            monsterAdapter = MonsterAdapter(this, monsterList)
                            findViewById<GridView>(R.id.character_gridView).adapter = monsterAdapter
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("${TAG3_f}DND_M", "Error getting documents.", exception)
                    }

            }
        }
        else if (OGgameType == 1) {
            if (OGcharType == 0) {
                tempPFplayers.get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val tempHash: HashMap<String, String> = convertToHashMapStringString(document.data as java.util.HashMap<String, com.google.protobuf.Any>)

                            val tempPlayer = Player(tempHash)
                            playerList.add(tempPlayer)

                            playerAdapter = PlayerAdapter(this, playerList)
                            findViewById<GridView>(R.id.character_gridView).adapter = playerAdapter
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("${TAG3_f}PF_P", "Error getting documents.", exception)
                    }
            }
            else {
                tempPFmonsters.get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val tempHash: HashMap<String, String> = convertToHashMapStringString(document.data as java.util.HashMap<String, com.google.protobuf.Any>)

                            val tempMonster = Monster(tempHash)
                            monsterList.add(tempMonster)

                            monsterAdapter = MonsterAdapter(this, monsterList)
                            findViewById<GridView>(R.id.character_gridView).adapter = monsterAdapter
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("${TAG3_f}PF_M", "Error getting documents.", exception)
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_grid)

        OGcharType = findViewById<Spinner>(R.id.characterType_spinner).selectedItemPosition
        OGgameType = findViewById<Spinner>(R.id.gameType_spinner).selectedItemPosition

        update()

        val grid = findViewById<GridView>(R.id.character_gridView)
        grid.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            val characterName : String

            if (OGcharType == 0) {
                val selectedPlayer: Player = playerList[position]

                Toast.makeText(this, " Opening Player: " + selectedPlayer._name,
                    Toast.LENGTH_SHORT).show()

                val myIntent = Intent(this, ViewCharacterActivity::class.java)
                myIntent.putExtra("CHARACTER NAME", selectedPlayer._name)
                myIntent.putExtra("CHARACTER TYPE", selectedPlayer._char_type.toString())
                myIntent.putExtra("CHARACTER GAME", selectedPlayer._game_mode.toString())

                startActivity(myIntent)
                finish()
            }
            else {
                val selectedMonster: Monster = monsterList[position]

                Toast.makeText(this, " Opening Monster: " + selectedMonster._name,
                    Toast.LENGTH_SHORT).show()

                val myIntent = Intent(this, ViewCharacterActivity::class.java)
                myIntent.putExtra("CHARACTER NAME", selectedMonster._name)
                myIntent.putExtra("CHARACTER TYPE", selectedMonster._char_type.toString())
                myIntent.putExtra("CHARACTER GAME", selectedMonster._game_mode.toString())

                startActivity(myIntent)
                finish()
            }
        }


        ////////////////////////////////////////////////////////////////////////////////////////////
        ///////         HANDLES BUTTON PRESS (at bottom of screen)          ////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        val newChar_button: ImageButton = findViewById(R.id.addCharacter_button)
        newChar_button.setOnClickListener {
            val myIntent = Intent(this, NewCharacterDialog::class.java)

            //alert dialog
            NewCharacterDialog().show(supportFragmentManager, "New Character Dialog")
        }

        // TODO: MAKE SIGN OUT AS A MENU ITEM NOT A BUTTON ON WINDOW
        val signOut_button: ImageButton = findViewById(R.id.signOut_button)
        signOut_button.setOnClickListener {
            auth.signOut()

            val myIntent = Intent(this, IntroActivity::class.java)

            startActivity(myIntent)
            finish()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun refresh() {
        val myIntent = intent
        myIntent.putExtra(TAG2_pom, OGcharType)
        myIntent.putExtra(TAG2_dop, OGgameType)
        startActivity(myIntent)
        finish()
    }

    private fun update() {
        val charTypeSpinner: Spinner = findViewById(R.id.characterType_spinner)
        charTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (OGcharType != charTypeSpinner.selectedItemPosition) {
                    OGcharType = charTypeSpinner.selectedItemPosition
                    refresh()
                }
                else {
                    return
                }
            }
        }

        val gameTypeSpinner: Spinner = findViewById(R.id.gameType_spinner)
        gameTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (OGgameType != gameTypeSpinner.selectedItemPosition) {
                    OGgameType = gameTypeSpinner.selectedItemPosition
                    refresh()
                }
                else {
                    return
                }
            }
        }
    }

    companion object {
        private const val TAG1_s = "SUCCESS_LOADDATA:CGA"
        private const val TAG1_f = "FAILURE_LOADDATA:CGA"

        private const val TAG2_pom = "PLAYER OR MONSTER:CGA"
        private const val TAG2_dop = "DND OR PF:CGA"

        private const val TAG3_s = "SUCCESS_LOADDATA"
        private const val TAG3_f = "FAILURE_LOADDATA"
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    class PlayerAdapter : BaseAdapter {
        var playerList = ArrayList<Player>()
        var context: Context? = null

        constructor(_context: Context, _playerList: ArrayList<Player>) : super() {
            this.context = _context
            this.playerList = _playerList
        }

        override fun getCount(): Int {
            return playerList.size
        }

        override fun getItem(position: Int): Any {
            return playerList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val player = this.playerList[position]

            val inflater =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val playerView = inflater.inflate(R.layout.image_character_entry, null)
            playerView.findViewById<ImageView>(R.id.gridChild_frame_imageView)
                .setImageResource(R.drawable.temp_gridview_item/*player.image!!*/)
            playerView.findViewById<TextView>(R.id.gridChild_characterName_textView).text =
                player._name

            return playerView
        }
    }
    class MonsterAdapter : BaseAdapter {
        var monsterList = ArrayList<Monster>()
        var context: Context? = null

        constructor(_context: Context, _monsterList: ArrayList<Monster>) : super() {
            this.context = _context
            this.monsterList = _monsterList
        }

        override fun getCount(): Int {
            return monsterList.size
        }

        override fun getItem(position: Int): Any {
            return monsterList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val monster = this.monsterList[position]

            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val monsterView = inflater.inflate(R.layout.image_character_entry, null)
            monsterView.findViewById<ImageView>(R.id.gridChild_frame_imageView).setImageResource(R.drawable.temp_gridview_item/*player.image!!*/)
            monsterView.findViewById<TextView>(R.id.gridChild_characterName_textView).text = monster._name

            return monsterView
        }
    }
}
