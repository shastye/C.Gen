package Utility;

import android.util.Log;

import com.google.protobuf.Any;
import com.google.protobuf.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import AttackPackage.Attack;
import AttackPackage.Magical;
import AttackPackage.Physical;

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

                        stringAttacks = new String[12];
                        stringAttacks[i] = tempAttacks;
                    }
                }

                for (int i = 0; i < stringAttacks.length; i++) {
                    if (stringAttacks[i] != null && !stringAttacks[i].equals("null")) {
                        String[] temp = stringAttacks[i].split(": ");

                        if (temp[1] != null  && !temp[1].equals("[null") && !temp[1].equals("null"))
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
        else {
            return null;
        }

        return _array;
    }

    public static Physical.Weapon[] convertToArrayOfWeapon(HashMap<String, String> _hashMap) {
        Physical.Weapon[] _array = new Physical.Weapon[12];

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

                        stringWeapons = new String[12];
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
                            Physical.Weapon weapon = Physical.Weapon.valueOf(temp[0]);
                            _array[i] = weapon;
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
}
