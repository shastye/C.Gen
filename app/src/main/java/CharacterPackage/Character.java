package CharacterPackage;

import android.util.Log;

import Utility.Utility;
import Utility.Utility.TAG;
import Utility.Die;
import AttackPackage.Attack;
import AttackPackage.Physical.Weapon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;
import java.util.Random;

public class Character {
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Base_Stats {    // struct
        public int Strength;
        public int Dexterity;
        public int Constitution;
        public int Intelligence;
        public int Wisdom;
        public int Charisma;
        public int Perception;

                    ////////////////////////////////////////////////////////////////////////////////

        public Base_Stats() {
            Strength = 10;
            Dexterity = 10;
            Constitution = 10;
            Intelligence = 10;
            Wisdom = 10;
            Charisma = 10;
            Perception = 10;
        }
        public Base_Stats(int _num) {
            Strength = _num;
            Dexterity = _num;
            Constitution = _num;
            Intelligence = _num;
            Wisdom = _num;
            Charisma = _num;
            Perception = _num;
        }
        public Base_Stats(int _str, int _dex, int _con, int _int, int _wis, int _char, int _perc) {
            Strength = _str;
            Dexterity = _dex;
            Constitution = _con;
            Intelligence = _int;
            Wisdom = _wis;
            Charisma = _char;
            Perception = _perc;
        }

                            ////////////////////////////////////////////////////////////////////////

        public boolean equals (Base_Stats _other) {
            boolean is_same = true;

            if (_other != null) {
                if (this.Strength != _other.Strength) {
                    is_same = false;
                }
                else if (this.Dexterity != _other.Dexterity) {
                    is_same = false;
                }
                else if (this.Constitution != _other.Constitution) {
                    is_same = false;
                }
                else if (this.Intelligence != _other.Intelligence) {
                    is_same = false;
                }
                else if (this.Wisdom != _other.Wisdom) {
                    is_same = false;
                }
                else if (this.Charisma != _other.Charisma) {
                    is_same = false;
                }
                else if (this.Perception != _other.Perception) {
                    is_same = false;
                }
            }

            return is_same;
        }
        public boolean equals (int _other) {
            boolean is_same = true;

            if (this.Strength != _other) {
                is_same = false;
            }
            else if (this.Dexterity != _other) {
                is_same = false;
            }
            else if (this.Constitution != _other) {
                is_same = false;
            }
            else if (this.Intelligence != _other) {
                is_same = false;
            }
            else if (this.Wisdom != _other) {
                is_same = false;
            }
            else if (this.Charisma != _other) {
                is_same = false;
            }
            else if (this.Perception != _other) {
                is_same = false;
            }


            return is_same;
        }

        public static int calculate_modifier(int _stat) {
            return (_stat - 10) / 2;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public enum Proficiency_Types {
        ACROBATICS,         ANIMAL_HANDLING,    ARCANA,             ATHLETICS,      DECEPTION,
        HISTORY,            INSIGHT,            INTIMIDATION,       INVESTIGATION,  MEDICINE,
        NATURE,             PERCEPTION,         PERFORMANCE,        PERSUASION,     RELIGION,
        SLEIGHT_OF_HAND,    STEALTH,            SURVIVAL,           VEHICLE,        GAMES,
        MUSICAL,            TOOLS,              ARTISAN_TOOLS,      LIGHT_ARMOR,    MEDIUM_ARMOR,
        SHIELDS,            SIMPLE_WEAPONS,     MARTIAL_WEAPONS,    HAND_CROSSBOWS, LONGSWORDS,
        RAPIERS,            SHORTSWORDS,        INSTRUMENTS,        ALCHEMIST,      CLUBS,
        DAGGERS,            DARTS,              JAVELINS,           MACES,          QUARTERSTAFFS,
        SCIMITAR,           SICKLES,            SLINGS,             SPEARS,         HERBALISM,
        ALL_ARMOR,          LIGHT_CROSSBOWS,
    }

                    ////////////////////////////////////////////////////////////////////////////////

    public class Proficient_In {
        public Proficiency_Types proficiency;
        public int bonus;
        public String special;

                    ////////////////////////////////////////////////////////////////////////////////

        Proficient_In() {
            proficiency = Proficiency_Types.ATHLETICS;
            bonus = 1;
            special = "";
        }
        Proficient_In(Proficiency_Types _TYPE) {
            proficiency = _TYPE;
            bonus = 1;
            special = "";
        }
        Proficient_In(Proficiency_Types _TYPE, int _bonus, String _special) {
            proficiency = _TYPE;
            bonus = _bonus;
            special = _special;
        }
        Proficient_In(Proficient_In _prof) {
            proficiency = _prof.proficiency;
            bonus = _prof.bonus;
            special = _prof.special;
        }

                    ////////////////////////////////////////////////////////////////////////////////

        public void set_bonus(int _characterLevel) {
            switch (_characterLevel) {
                case -1:
                case 0:
                    bonus = 0;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    bonus = 2;
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    bonus = 3;
                    break;
                case 10:
                case 11:
                case 12:
                case 13:
                    bonus = 4;
                    break;
                case 14:
                case 15:
                case 16:
                    bonus = 5;
                    break;
                default:
                    bonus = 6;
                    break;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public enum Alignment {
        LAWFUL_GOOD,        NEUTRAL_GOOD,       CHAOTIC_GOOD,
        LAWFUL_NEUTRAL,     NEUTRAL_NEUTRAL,    CHAOTIC_NEUTRAL,
        LAWFUL_EVIL,        NEUTRAL_EVIL,       CHAOTIC_EVIL,
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public enum Game_Mode {
        DND,        PATHFINDER,
    }

    public static Game_Mode string_to_game_mode(String _mode) {
        Game_Mode temp = Game_Mode.PATHFINDER;

        try {
            temp = Game_Mode.valueOf(_mode);
        } catch (Exception e) {
            temp = Game_Mode.DND;
        }

        return temp;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public enum Type {
        PLAYER,     MONSTER,
    }

    public static Type string_to_type(String _type) {
        Type temp = Type.MONSTER;

        try {
            temp = Type.valueOf(_type);
        } catch (Exception e) {
            temp = Type.PLAYER;
        }

        return temp;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // TODO: Enum for items carried

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean template_char;

    private String name;
    private Character.Game_Mode game_mode;
    private Character.Type char_type;
    private int level;
    private int health_points;
    private Alignment alignment;
    private Base_Stats statistics;
    private int speed;
    private int armor_rating;
    private int total_hp_dice;
    private Die hp_die;
    private Weapon[] weapons;
    private Attack[] attacks;
    // TODO: Vector for items carried

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Character(String _name) {
        template_char = false;
        name = _name;
        game_mode = Game_Mode.DND;
        char_type = Type.PLAYER;
        level = 1;
        alignment = Alignment.CHAOTIC_EVIL;
        statistics = new Base_Stats(10);
        speed = 5;
        armor_rating = 10;
        total_hp_dice = 0;
        hp_die = Die.d4;
        weapons = new Weapon[12];
        attacks = new Attack[12];

        health_points = Equation_HP(hp_die);
    }
    public Character(Character _char) {
        template_char = _char.is_template_char();
        name = _char.get_name();
        game_mode = _char.get_game_mode();
        char_type = _char.get_char_type();
        level = _char.get_level();
        health_points = _char.get_health_points();
        alignment = _char.get_alignment();
        statistics = _char.get_statistics();
        speed = _char.get_speed();
        armor_rating = _char.get_armor_rating();
        total_hp_dice = _char.get_total_hp_dice();
        hp_die = _char.get_hp_die();
        weapons = new Weapon[12];
        weapons = _char.get_weapons();
        attacks = new Attack[12];
        attacks = _char.get_attacks();
    }
    public Character(HashMap<String, String> _hash) {
        template_char = Boolean.parseBoolean(Objects.requireNonNull(_hash.get(TAG.TEMPLATE_CHARACTER)));
        name = _hash.get(TAG.NAME);
        game_mode = Game_Mode.valueOf(_hash.get(TAG.GAME_MODE));
        char_type = Type.valueOf(_hash.get(TAG.CHARACTER_TYPE));
        level = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.LEVEL)));
        health_points = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.HEALTH_POINTS)));
        alignment = Alignment.valueOf(_hash.get(TAG.ALIGNMENT));
        statistics = new Base_Stats(0);
        statistics.Charisma = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.STATS_CHARISMA)));
        statistics.Constitution = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.STATS_CONSTITUTION)));
        statistics.Dexterity = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.STATS_DEXTERITY)));
        statistics.Intelligence = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.STATS_INTELLIGENCE)));
        statistics.Perception = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.STATS_PERCEPTION)));
        statistics.Strength = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.STATS_STRENGTH)));
        statistics.Wisdom = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.STATS_WISDOM)));
        speed = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.SPEED)));
        armor_rating = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.ARMOR_RATING)));
        total_hp_dice = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.TOTAL_HEALTH_POINTS_DICE)));
        hp_die = Die.valueOf(_hash.get(TAG.HEALTH_POINTS_DIE));

        weapons = new Weapon[12];
        weapons = Utility.convertToArrayOfWeapon(_hash);

        attacks = new Attack[12];
        attacks = Utility.convertToArrayOfAttack(_hash);

    }

        // TODO: GET MONSTER INFO FROM API if game_mode == DND
    //       http://www.dnd5eapi.co/

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean is_template_char() {
        return template_char;
    }
    public void set_template_char(boolean template_char) {
        this.template_char = template_char;
    }

    public String get_name() {
        return name;
    }
    public void set_name(String name) {
        this.name = name;
    }

    public Game_Mode get_game_mode() { return game_mode; }
    public void set_game_mode(Game_Mode _mode) { game_mode = _mode; }

    public Type get_char_type() { return char_type; }
    public void set_char_type(Type _type) { char_type = _type; }

    public int get_level() { return level; }
    public void set_level(int _level) { level = _level; }

    public int get_health_points() {
        return health_points;
    }
    public void set_health_points(int health_points) {
        this.health_points = health_points;
    }

    public Alignment get_alignment() {
        return alignment;
    }
    public void set_alignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public Base_Stats get_statistics() {
        return statistics;
    }
    public void set_statistics(Base_Stats statistics) {
        this.statistics = statistics;
    }

    public int get_speed() {
        return speed;
    }
    public void set_speed(int speed) { this.speed = speed; }

    public int get_armor_rating() {
        return armor_rating;
    }
    public void set_armor_rating(int armor_rating) {
        this.armor_rating = armor_rating;
    }

    public int get_total_hp_dice() {
        return total_hp_dice;
    }
    public void set_total_hp_dice(int hp_dice) {
        this.total_hp_dice = hp_dice;
    }

    public Die get_hp_die() {
        return hp_die;
    }
    public void set_hp_die(Die die) {
        this.hp_die = die;
    }

    public Weapon[] get_weapons() { return weapons; }
    public void set_weapons(Weapon[] _weapons) { this.weapons = _weapons; }
    public void clear_weapons() { weapons = new Weapon[weapons.length]; }

    public Attack[] get_attacks() { return attacks; }
    public void set_attacks(Attack[] _attacks) { this.attacks = _attacks; }
    public void clear_attacks() { attacks = new Attack[attacks.length]; }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int Equation_HP(Die _die) {
        Random rand = new Random();
        int hp = 0;
        int _upper = _die.get_int();

        for (int i = 0; i < this.get_total_hp_dice(); i++) {
            hp += rand.nextInt(_upper) + 1;
            hp += this.get_statistics().Constitution;
        }

        return hp;
    }

    public boolean equals (Character _other) {
        boolean is_same = true;

        if (_other != null) {
            if (!this.name.equals(_other.name)) {
                is_same = false;
            }
            else if (this.health_points != _other.health_points) {
                is_same = false;
            }
            else if (this.alignment != _other.alignment) {
                is_same = false;
            }
            else if (!this.statistics.equals(_other.statistics)) {
                is_same = false;
            }
            else if (this.speed != _other.speed) {
                is_same = false;
            }
            else if (this.armor_rating != _other.armor_rating) {
                is_same = false;
            }
            else if (this.total_hp_dice != _other.total_hp_dice) {
                is_same = false;
            }
            else if (this.hp_die != _other.hp_die) {
                is_same = false;
            }
            else if (!this.weapons.equals(_other.weapons)) {
                is_same = false;
            }
            else if (!this.attacks.equals(_other.attacks)) {
                is_same = false;
            }
        }
        else {
            is_same = false;
            Log.e("OTHER IS NULL", "Other is Null");
        }

        return is_same;
    }

    public HashMap<String, String> get_hashMap() {
        HashMap<String, String> temp = new HashMap<>(0);

        temp.put(TAG.TEMPLATE_CHARACTER, String.valueOf(this.template_char));
        temp.put(TAG.NAME, this.name);
        temp.put(TAG.GAME_MODE, String.valueOf(this.game_mode));
        temp.put(TAG.CHARACTER_TYPE, String.valueOf(this.char_type));
        temp.put(TAG.LEVEL, String.valueOf(this.level));
        temp.put(TAG.HEALTH_POINTS, String.valueOf(this.health_points));
        temp.put(TAG.ALIGNMENT, String.valueOf(this.alignment));
        temp.put(TAG.STATS_CHARISMA, String.valueOf(this.statistics.Charisma));
        temp.put(TAG.STATS_CONSTITUTION, String.valueOf(this.statistics.Constitution));
        temp.put(TAG.STATS_DEXTERITY, String.valueOf(this.statistics.Dexterity));
        temp.put(TAG.STATS_INTELLIGENCE, String.valueOf(this.statistics.Intelligence));
        temp.put(TAG.STATS_PERCEPTION, String.valueOf(this.statistics.Perception));
        temp.put(TAG.STATS_STRENGTH, String.valueOf(this.statistics.Strength));
        temp.put(TAG.STATS_WISDOM, String.valueOf(this.statistics.Wisdom));
        temp.put(TAG.SPEED, String.valueOf(this.speed));
        temp.put(TAG.ARMOR_RATING, String.valueOf(this.armor_rating));
        temp.put(TAG.TOTAL_HEALTH_POINTS_DICE, String.valueOf(this.total_hp_dice));
        temp.put(TAG.HEALTH_POINTS_DIE, String.valueOf(this.hp_die));
        temp.put(TAG.WEAPONS_VECTOR, Arrays.toString(this.weapons));
        temp.put(TAG.ATTACKS_VECTOR, Arrays.toString(this.attacks));

        return temp;
    }
}
