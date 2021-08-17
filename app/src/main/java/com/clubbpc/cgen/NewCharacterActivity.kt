package com.clubbpc.cgen

import CharacterPackage.Monster
import CharacterPackage.Player
import Utility.Utility.TAG

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class NewCharacterActivity : AppCompatActivity() {

    private var newPlayer: Player = Player("")
    private var newMonster: Monster = Monster("")
    private var charType: String = ""

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_character)

        // TODO: MAKE THE ACTUAL ACTIVITY FOR NEW CHARACTER
    }

    override fun onStart() {
        super.onStart()

        val tempHash: HashMap<String, String>? = intent.getSerializableExtra("NEW CHARACTER") as? HashMap<String, String>
        Log.e("TEMPHASH:", tempHash.toString())

        if (tempHash?.get(TAG.GAME_MODE) == "DND") {
            charType = "DND"
            newPlayer = Player(tempHash)
            if(tempHash == newPlayer._hashMap) {
                Log.e(TAG1_s, newPlayer._hashMap.toString())
            }
            else {
                Log.e(TAG1_f, newPlayer._hashMap.toString())
            }
        }
        else {
            charType = "Pathfinder"
            newMonster = Monster(tempHash)
            if(tempHash == newMonster._hashMap) {
                Log.e(TAG1_s, newMonster._hashMap.toString())
            }
            else {
                Log.e(TAG1_f, newMonster._hashMap.toString())
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    companion object {
        private const val TAG1_s = "SUCCESS_LOADDATA:NCA"
        private const val TAG1_f = "FAILURE_LOADDATA:NCA"
    }
}