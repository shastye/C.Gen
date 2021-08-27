package Utility;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.clubbpc.cgen.CharacterGridActivity;
import com.clubbpc.cgen.EditCharacterActivity;
import com.clubbpc.cgen.IntroActivity;
import com.clubbpc.cgen.ViewCharacterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.protobuf.Any;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import AttackPackage.Attack;
import AttackPackage.Magical;
import AttackPackage.Physical;
import CharacterPackage.Character;
import CharacterPackage.Monster;
import CharacterPackage.Player;

public class Utility {
    public static class MENU {
        public static void SignOut(AppCompatActivity _activity) {
            FirebaseAuth.getInstance().signOut();

            Intent myIntent = new Intent(_activity, IntroActivity.class);
            _activity.startActivity(myIntent);
            _activity.finish();
        }
        public static void GoBack(AppCompatActivity _activity) {
            Intent myIntent = new Intent(_activity, CharacterGridActivity.class);
            _activity.startActivity(myIntent);
            _activity.finish();
        }
        public static void SaveAndGoBack(AppCompatActivity _activity, String gameMode, String charType, Character character) {
            Save(gameMode, charType, character);
            GoBack(_activity);
        }
        public static void CancelAndView(AppCompatActivity _activity,String gameMode, String charType, Character character) {
            Intent myIntent = new Intent(_activity, ViewCharacterActivity.class);
            myIntent.putExtra("CHARACTER NAME", character.get_name());
            myIntent.putExtra("CHARACTER TYPE", character.get_char_type().toString());
            myIntent.putExtra("CHARACTER GAME", character.get_game_mode().toString());
            _activity.startActivity(myIntent);
            _activity.finish();
        }
        public static void SaveAndView(AppCompatActivity _activity,String gameMode, String charType, Character character) {
            Save(gameMode, charType, character);

            Intent myIntent = new Intent(_activity, ViewCharacterActivity.class);
            myIntent.putExtra("CHARACTER NAME", character.get_name());
            myIntent.putExtra("CHARACTER TYPE", character.get_char_type().toString());
            myIntent.putExtra("CHARACTER GAME", character.get_game_mode().toString());
            _activity.startActivity(myIntent);
            _activity.finish();
        }
        public static void Edit(AppCompatActivity _activity, String gameMode, String charType, Character character) {
            Intent myIntent = new Intent(_activity, EditCharacterActivity.class);
            myIntent.putExtra("CHARACTER NAME", character.get_name());
            myIntent.putExtra("CHARACTER TYPE", character.get_char_type().toString());
            myIntent.putExtra("CHARACTER GAME", character.get_game_mode().toString());
            myIntent.putExtra("INFO FROM", "VIEW CHARACTER");
            _activity.startActivity(myIntent);
            _activity.finish();
        }

        private static void Save(String gameMode, String charType, Character character) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference tempUsers = db.collection(TAG.USERS_COLLECTION);
            DocumentReference tempUserDoc = tempUsers.document(Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()));

            CollectionReference tempDNDplayers = tempUserDoc.collection(TAG.DND_PLAYERSHEETS_DOCUMENT);
            CollectionReference tempPFplayers = tempUserDoc.collection(TAG.PATHFINDER_PLAYERSHEETS_DOCUMENT);
            CollectionReference tempDNDmonsters = tempUserDoc.collection(TAG.DND_MONSTERSHEETS_DOCUMENT);
            CollectionReference tempPFmonsters = tempUserDoc.collection(TAG.PATHFINDER_MONSTERSHEETS_DOCUMENT);

            if (gameMode.equals("DND")) {
                if (charType.equals("PLAYER")) {
                    Player currentPlayer = (Player) character;
                    tempDNDplayers.document(currentPlayer.get_name()).set(currentPlayer.get_hashMap());
                }
                else {
                    Monster currentMonster = (Monster) character;
                    tempDNDmonsters.document(currentMonster.get_name()).set(currentMonster.get_hashMap());
                }
            }
            else if (gameMode.equals("PATHFINDER")) {
                if (charType.equals("PLAYER")) {
                    Player currentPlayer = (Player) character;
                    tempDNDplayers.document(currentPlayer.get_name()).set(currentPlayer.get_hashMap());
                }
                else {
                    Monster currentMonster = (Monster) character;
                    tempDNDmonsters.document(currentMonster.get_name()).set(currentMonster.get_hashMap());
                }
            }
        }
    }

    public static final class TAG {
        // from User Class
        public static final String EMAIL = "email";
        public static final String DEFAULT_CHARACTER_LEVEL = "default character level";

        // from Character Class
        public static final String TEMPLATE_CHARACTER = "template char";
        public static final String NAME = "name";
        public static final String GAME_MODE = "game mode";
        public static final String CHARACTER_TYPE = "char type";
        public static final String RACE = "race";
        public static final String LEVEL = "level";
        public static final String HEALTH_POINTS = "health points";
        public static final String ALIGNMENT = "alignment";
        public static final String STATS_CHARISMA = "stats.charisma";
        public static final String STATS_CONSTITUTION = "stats.constitution";
        public static final String STATS_DEXTERITY = "stats.dexterity";
        public static final String STATS_INTELLIGENCE = "stats.intelligence";
        public static final String STATS_PERCEPTION = "stats.perception";
        public static final String STATS_STRENGTH = "stats.strength";
        public static final String STATS_WISDOM = "stats.wisdom";
        public static final String SPEED = "speed";
        public static final String ARMOR_RATING = "armor rating";
        public static final String TOTAL_HEALTH_POINTS_DICE = "total hp dice";
        public static final String HEALTH_POINTS_DIE = "hp die";
        public static final String WEAPONS_VECTOR = "weapons";
        public static final String ATTACKS_VECTOR = "attacks";
        public static final String PROFICIENCIES_VECTOR = "proficiencies";
        public static final String PROFICIENCY_BONUS = "proficiency_bonus";
        public static final String SAVING_THROWS_VECTOR = "saving throws";
        public static final String INITIATIVE = "initiative";
        public static final String ITEMS_VECTOR = "items";
        public static final String MONEY = "money";
        public static final String SPELL_SLOTS = "spell slots";
        public static final String USED_SPELL_SLOTS = "used spell slots";

        // from Monster Class
        public static final String TYPE = "type";
        public static final String SIZE = "size";
        public static final String TAG = "tag";
        public static final String SPEED_BURROW = "speed.burrowing";
        public static final String SPEED_FLYING = "speed.flying";
        public static final String SPEED_CLIMBING = "speed.climbing";
        public static final String SPEED_SWIMMING = "speed.swimming";
        public static final String CHALLENGE_RATING = "challenge rating";
        public static final String SENSES_VECTOR = "senses";
        public static final String EXPERIENCE_POINTS = "experience points"; // also for Player Class

        // from Player Class
        public static final String DEATH_SAVES_SUCCESSES = "death saves.successes";
        public static final String DEATH_SAVES_FAILURES = "death saves.failures";
        public static final String TOTAL_HIT_DICE = "total hit dice";
        public static final String HIT_DIE = "hit die";
        public static final String CLASS = "class";
        public static final String BACKGROUND = "background";
        public static final String INSPIRATION = "inspiration";

        // from Attack Class
        public static final String NUMBER_OF_DICE = "number of dice";
        public static final String DIE = "die";
        public static final String SPECIAL = "special";

        // from Activities
        public static final String USERS_COLLECTION = "users";
        public static final String DND_PLAYERSHEETS_DOCUMENT = "DND_playerSheets";
        public static final String PATHFINDER_PLAYERSHEETS_DOCUMENT = "PathFinder_playerSheets";
        public static final String DND_MONSTERSHEETS_DOCUMENT = "DND_monsterSheets";
        public static final String PATHFINDER_MONSTERSHEETS_DOCUMENT = "PathFinder_monsterSheets";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static Player setInformationForDebugging(Player _player) {
        Player temp = new Player(_player);

        Magical spell = new Magical();

        spell.set_name("Commune");
        spell.set_level(5);
        spell.set_special("Divination");
        spell.set_duration("1 minutes");
        spell.set_range(0);
        spell.set_time_in_actions(1);

        temp.add_attack(spell);

        Physical hit = new Physical();

        hit.set_name("Punch");
        hit.set_special("Finesse");
        hit.set_die(Die.d4);
        hit.set_num_dice(2);
        hit.set_weapon_info(new Physical.Weapon_Struct(Physical.Weapon_Enum.NONE, 0, Die.d4));

        temp.add_attack(hit);

        return temp;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("rawtypes")
    public static HashMap<String, String> convertToHashMapStringString(HashMap<String, Any> _temp) {
        HashMap<String, String> temp = new HashMap<String, String>(0);

        Iterator iter = _temp.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry element = (Map.Entry)iter.next();

            temp.put(element.getKey().toString(), element.getValue().toString());
        }

        return temp;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static int[] convertToArrayOfInt(HashMap<String, String> _hashMap, String _tag, int _size) {
        int[] _array = new int[_size];

        String tempInt = "";
        if (_hashMap.get(_tag) != null && !Objects.equals(_hashMap.get(_tag), "[]")) {
            tempInt = _hashMap.get(_tag);

            String[] stringInt = new String[24];
            if (tempInt != null) {
                stringInt = tempInt.split(", ");
            }

            if (stringInt.length > 1) {
                for (int i = 0; i < stringInt.length; i++) {
                    if (stringInt[i] != null) {
                        if (stringInt[i].contains("]") || stringInt[i].contains("[")) {
                            stringInt[i] = stringInt[i].replace("[", "");
                            stringInt[i] = stringInt[i].replace("]", "");

                            _array[i] = Integer.parseInt(stringInt[i]);
                        }
                    }
                }
            }
        }

        return _array;
    }
    public static Attack[] convertToArrayOfAttack(HashMap<String, String> _hashMap) {
        Attack[] _array = new Attack[12];

        String tempAttacks = "";
        if (_hashMap.get(TAG.ATTACKS_VECTOR) != null && !_hashMap.get(TAG.ATTACKS_VECTOR).equals("[]")) {
            tempAttacks = _hashMap.get(TAG.ATTACKS_VECTOR);

            String[] stringAttacks = new String[24];
            if (tempAttacks != null) {
                stringAttacks = tempAttacks.split(", ");
            }

            if (stringAttacks.length > 1) {
                for (int i = 0; i < stringAttacks.length; i++) {
                    if (stringAttacks[i] != null && !stringAttacks[i].equals("null")) {
                        String[] temp = stringAttacks[i].split(": ");
                        tempAttacks = "";

                        for (int j = 0; j < temp.length; j++) {
                            if (temp[j] != null && !temp[j].equals("null")) {
                                if (temp[j].equals("Magical") || temp[j].equals("Physical")) {
                                    tempAttacks += temp[j];
                                    tempAttacks += ": ";
                                }
                            }
                        }

                        if (tempAttacks.equals("")) {
                            tempAttacks += "Attack: ";
                        }

                        for (int k = 0; k < temp.length; k++) {
                            if (temp[k] != null && !temp[k].equals("null")) {
                                if (!temp[k].equals("[") && !temp[k].equals("Attack") && !temp[k].equals("Magical") && !temp[k].equals("Physical")) {
                                    tempAttacks += temp[k];
                                }
                            }
                        }

                        stringAttacks[i] = tempAttacks;
                    }
                }

                for (int i = 0; i < stringAttacks.length; i++) {
                    if (stringAttacks[i] != null && !stringAttacks[i].equals("null")) {
                        String[] temp = stringAttacks[i].split(": ");

                        if (temp[1] != null  && !temp[1].equals("[null") && !temp[1].equals("null") && !temp[1].equals("null]")) {
                            switch (temp[0]) {
                                case "Attack":
                                    Attack att = new Attack(stringAttacks[i]);
                                    _array[i] = att;
                                    break;
                                case "Magical":
                                    Magical mag = new Magical(stringAttacks[i]);
                                    _array[i] = mag;
                                    break;
                                case "Physical":
                                    Physical phy = new Physical(stringAttacks[i]);
                                    _array[i] = phy;
                                    break;
                            }
                        }
                    }
                }
            }
        }
        else {
            return null;
        }

        return _array;
    }

    public static Physical.Weapon_Struct[] convertToArrayOfWeapon(HashMap<String, String> _hashMap) {
        Physical.Weapon_Struct[] _array = new Physical.Weapon_Struct[12];

        String tempWeapons = "";
        if (_hashMap.get(TAG.WEAPONS_VECTOR) != null && !_hashMap.get(TAG.WEAPONS_VECTOR).equals("[]")) {
            tempWeapons = _hashMap.get(TAG.WEAPONS_VECTOR);

            String[] stringWeapons = new String[24];
            if (tempWeapons != null) {
                stringWeapons = tempWeapons.split(", ");
            }

            if (stringWeapons.length > 1) {
                for (int i = 0; i < stringWeapons.length; i++) {
                    if (stringWeapons[i] != null && !stringWeapons[i].equals("null")) {
                        String[] temp = stringWeapons[i].split(": ");
                        tempWeapons = "";

                        for (int j = 0; j < temp.length; j++) {
                            if (temp[j] != null && !temp[j].equals("null") && tempWeapons.equals("")) {
                                if (temp[j].equals("Weapon")) {
                                    tempWeapons += temp[j];
                                    tempWeapons += "s: ";
                                }
                            }
                        }

                        for (int k = 0; k < temp.length; k++) {
                            if (temp[k] != null && !temp[k].equals("null") && !temp[k].equals("[null") && !temp[k].equals("null]") ) {
                                if (!temp[k].equals("[") && !temp[k].equals("Weapon")) {
                                    tempWeapons += temp[k];
                                }
                            }
                        }

                        stringWeapons[i] = tempWeapons;
                    }
                }

                for (int i = 0; i < stringWeapons.length; i++) {
                    if (stringWeapons[i] != null && !stringWeapons[i].equals("null")) {
                        if (stringWeapons[i].contains("[")) {
                            stringWeapons[i] = stringWeapons[i].replace("[", "");
                        }
                        if (stringWeapons[i].contains("]")) {
                            stringWeapons[i] = stringWeapons[i].replace("]", "");
                        }

                        String[] temp = stringWeapons[i].split(": ");

                        if (!temp[0].equals("")) {
                            _array[i] = new Physical.Weapon_Struct(temp[0]);
                        }
                    }
                }
            }
        }
        else {
            return null;
        }

        return _array;
    }

    public static Character.Item[] convertToArrayOfItem(HashMap<String, String> _hashMap) {
        Character.Item[] _array = new Character.Item[12];

        String tempItems = "";
        if (_hashMap.get(TAG.ITEMS_VECTOR) != null && !_hashMap.get(TAG.ITEMS_VECTOR).equals("[]")) {
            tempItems = _hashMap.get(TAG.ITEMS_VECTOR);

            String[] stringItems = new String[24];
            if (tempItems != null) {
                stringItems = tempItems.split(", ");
            }

            if (stringItems.length > 1) {
                for (int i = 0; i < stringItems.length; i++) {
                    if (stringItems[i] != null && !stringItems[i].equals("null")) {
                        String[] temp = stringItems[i].split(": ");
                        tempItems = "";

                        for (int j = 0; j < temp.length; j++) {
                            if (temp[j] != null && !temp[j].equals("null") && tempItems.equals("")) {
                                if (temp[j].equals("Item")) {
                                    tempItems += temp[j];
                                    tempItems += "s: ";
                                }
                            }
                        }

                        for (int k = 0; k < temp.length; k++) {
                            if (temp[k] != null && !temp[k].equals("null") && !temp[k].equals("[null") && !temp[k].equals("null]") ) {
                                if (!temp[k].equals("[") && !temp[k].equals("Item")) {
                                    tempItems += temp[k];
                                }
                            }
                        }

                        stringItems[i] = tempItems;
                    }
                }

                for (int i = 0; i < stringItems.length; i++) {
                    if (stringItems[i] != null && !stringItems[i].equals("null")) {
                        if (stringItems[i].contains("[")) {
                            stringItems[i] = stringItems[i].replace("[", "");
                        }
                        if (stringItems[i].contains("]")) {
                            stringItems[i] = stringItems[i].replace("]", "");
                        }

                        String[] temp = stringItems[i].split(": ");

                        if (!temp[0].equals("")) {
                            _array[i] = new Character.Item(temp[0]);
                        }
                    }
                }
            }
        }
        else {
            return null;
        }

        return _array;
    }

    public static Character.Proficient_In[] convertToArrayOfProficiencies(HashMap<String, String> _hashMap) {
        Character.Proficient_In[] _array = new Character.Proficient_In[12];

        String tempProficiencies = "";
        if (_hashMap.get(TAG.PROFICIENCIES_VECTOR) != null && !_hashMap.get(TAG.PROFICIENCIES_VECTOR).equals("[]")) {
            tempProficiencies = _hashMap.get(TAG.PROFICIENCIES_VECTOR);

            String[] stringProficiencies = new String[24];
            if (tempProficiencies != null) {
                stringProficiencies = tempProficiencies.split(", ");
            }

            if (stringProficiencies.length > 1) {
                for (int i = 0; i < stringProficiencies.length; i++) {
                    if (stringProficiencies[i] != null && !stringProficiencies[i].equals("null")) {
                        String[] temp = stringProficiencies[i].split(": ");
                        tempProficiencies = "Proficient: ";

                        for (int k = 0; k < temp.length; k++) {
                            if (temp[k] != null && !temp[k].equals("null")) {
                                if (!temp[k].equals("[") && !temp[k].equals("Proficient")) {
                                    tempProficiencies += temp[k];
                                }
                            }
                        }

                        stringProficiencies[i] = tempProficiencies;
                    }
                }

                for (int i = 0; i < stringProficiencies.length; i++) {
                    if (stringProficiencies[i] != null && !stringProficiencies[i].equals("null")) {
                        String[] temp = stringProficiencies[i].split(": ");

                        if (temp[1] != null  && !temp[1].equals("null]") && !temp[1].equals("null") && !temp[1].equals("[null")) {
                            Character.Proficient_In _prof = new Character.Proficient_In(temp[1]);
                            _array[i] = _prof;
                        }
                    }
                }
            }
        }
        else {
            return null;
        }

        return _array;
    }

    public static Character.Saving_Throw[] convertToArrayOfSavingThrows(HashMap<String, String> _hashMap) {
        Character.Saving_Throw[] _array = new Character.Saving_Throw[12];

        String tempThrows = "";
        if (_hashMap.get(TAG.SAVING_THROWS_VECTOR) != null && !_hashMap.get(TAG.SAVING_THROWS_VECTOR).equals("[]")) {
            tempThrows = _hashMap.get(TAG.SAVING_THROWS_VECTOR);

            String[] stringThrows = new String[24];
            if (tempThrows != null) {
                stringThrows = tempThrows.split(", ");
            }

            if (stringThrows.length > 1) {
                for (int i = 0; i < stringThrows.length; i++) {
                    if (stringThrows[i] != null && !stringThrows[i].equals("null")) {
                        String[] temp = stringThrows[i].split(": ");
                        tempThrows = "Saving Throw: ";

                        for (int k = 0; k < temp.length; k++) {
                            if (temp[k] != null && !temp[k].equals("null")) {
                                if (!temp[k].equals("[") && !temp[k].equals("Saving Throw")) {
                                    tempThrows += temp[k];
                                }
                            }
                        }

                        stringThrows[i] = tempThrows;
                    }
                }

                for (int i = 0; i < stringThrows.length; i++) {
                    if (stringThrows[i] != null && !stringThrows[i].equals("null")) {
                        String[] temp = stringThrows[i].split(": ");

                        if (temp[1] != null  && !temp[1].equals("null]") && !temp[1].equals("null") && !temp[1].equals("[null")) {
                            Character.Saving_Throw _prof = new Character.Saving_Throw(temp[1]);
                            _array[i] = _prof;
                        }
                    }
                }
            }
        }
        else {
            return null;
        }

        return _array;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean checkForEnumValueInArray(Character.Base_Stats_Enum _throw, Character.Saving_Throw[] _array) {
        boolean contains = false;

        for (Character.Saving_Throw saveThrow : _array) {
            if (saveThrow != null && saveThrow.statistic == _throw) {
                contains = true;
            }
        }

        return contains;
    }
    public static boolean checkForEnumValueInArray(Character.Skill _skill, Character.Proficient_In[] _array) {
        boolean contains = false;

        for (Character.Proficient_In prof : _array) {
            if (prof != null && prof.proficiency == _skill) {
                contains = true;
            }
        }

        return contains;
    }

    public static int getModifierForArrayValue(Character.Base_Stats_Enum _throw, Character.Saving_Throw[] _array) {
        int mod = 0;

        for (Character.Saving_Throw saveThrow : _array) {
            if (saveThrow != null && saveThrow.statistic == _throw) {
                mod = saveThrow.modifier;
            }
        }

        return mod;
    }
    public static int getModifierForArrayValue(Character.Skill _skill, Character.Proficient_In[] _array) {
        int mod = 0;

        for (Character.Proficient_In prof : _array) {
            if (prof != null && prof.proficiency == _skill) {
                mod = prof.bonus;
            }
        }

        return mod;
    }
}
