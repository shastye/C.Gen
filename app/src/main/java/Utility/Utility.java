package Utility;

import com.google.protobuf.Any;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import AttackPackage.Attack;
import AttackPackage.Magical;
import AttackPackage.Physical;
import CharacterPackage.Character;
import CharacterPackage.Player;

public class Utility {
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

        Character.Item item = new Character.Item();
        item.item = "Amulet";
        item.cost = new Character.Money(0,0,0,5,0);
        item.weight = 1;
        item.attributes = "";

        temp.add_item(item);

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

                        if (temp[1] != null  && !temp[1].equals("null]") && !temp[1].equals("null")) {
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

                        if (temp[1] != null  && !temp[1].equals("null]") && !temp[1].equals("null")) {
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
