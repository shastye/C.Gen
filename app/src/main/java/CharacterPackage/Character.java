package CharacterPackage;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import AttackPackage.Physical;
import Utility.Utility;
import Utility.Utility.TAG;
import Utility.Die;
import AttackPackage.Attack;
import AttackPackage.Physical.Weapon_Enum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Character {
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public enum Base_Stats_Enum {
        STRENGTH,       DEXTERITY,      CONSTITUTION,
        INTELLIGENCE,   WISDOM,         CHARISMA,
        PERCEPTION
    }

                    ////////////////////////////////////////////////////////////////////////////////

    public static class Base_Stats_Struct {    // struct
        public int Strength;
        public int Dexterity;
        public int Constitution;
        public int Intelligence;
        public int Wisdom;
        public int Charisma;
        public int Perception;

                    ////////////////////////////////////////////////////////////////////////////////

        public Base_Stats_Struct() {
            Strength = 10;
            Dexterity = 10;
            Constitution = 10;
            Intelligence = 10;
            Wisdom = 10;
            Charisma = 10;
            Perception = 10;
        }
        public Base_Stats_Struct(int _num) {
            Strength = _num;
            Dexterity = _num;
            Constitution = _num;
            Intelligence = _num;
            Wisdom = _num;
            Charisma = _num;
            Perception = _num;
        }
        public Base_Stats_Struct(int _str, int _dex, int _con, int _int, int _wis, int _char, int _perc) {
            Strength = _str;
            Dexterity = _dex;
            Constitution = _con;
            Intelligence = _int;
            Wisdom = _wis;
            Charisma = _char;
            Perception = _perc;
        }

                            ////////////////////////////////////////////////////////////////////////

        public boolean equals (Base_Stats_Struct _other) {
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

    public enum Skill {
        ACROBATICS,         ANIMAL_HANDLING,    ARCANA,             ATHLETICS,      DECEPTION,
        HISTORY,            INSIGHT,            INTIMIDATION,       INVESTIGATION,  MEDICINE,
        NATURE,             PERCEPTION,         PERFORMANCE,        PERSUASION,     RELIGION,
        SLEIGHT_OF_HAND,    STEALTH,            SURVIVAL,           VEHICLE,        GAMES,
        MUSICAL,            TOOLS,              ARTISAN_TOOLS,      LIGHT_ARMOR,    MEDIUM_ARMOR,
        SHIELDS,            SIMPLE_WEAPONS,     MARTIAL_WEAPONS,    HAND_CROSSBOWS, LONGSWORDS,
        RAPIERS,            SHORTSWORDS,        INSTRUMENTS,        ALCHEMIST,      CLUBS,
        DAGGERS,            DARTS,              JAVELINS,           MACES,          QUARTERSTAFFS,
        SCIMITAR,           SICKLES,            SLINGS,             SPEARS,         HERBALISM,
        ALL_ARMOR,          LIGHT_CROSSBOWS,    COMMON,             DWARVISH,       ELVISH,
        GIANT,              GNOMISH,            GOBLIN,             HAFLING,        ORC,
        ABYSSAL,            CELESTIAL,          DRACONIC,           DEEP_SPEECH,    INFERNAL,
        PRIMORDIAL,         SYLVAN,             UNDERCOMMON,
    }

                    ////////////////////////////////////////////////////////////////////////////////

    public static class Proficient_In {
        public Skill proficiency;
        public int bonus;
        public String special;
        public Base_Stats_Enum stat_used;

                    ////////////////////////////////////////////////////////////////////////////////

        public Proficient_In() {
            proficiency = Skill.ATHLETICS;
            bonus = 0;
            special = "";
            stat_used = Base_Stats_Enum.CHARISMA;
        }
        public Proficient_In(Skill _TYPE) {
            proficiency = _TYPE;
            bonus = 0;
            special = "";
            stat_used = Base_Stats_Enum.CONSTITUTION;
        }
        public Proficient_In(Skill _TYPE, int _characterLevel, String _special, Base_Stats_Enum _stat) {
            proficiency = _TYPE;
            set_bonus(_characterLevel);
            special = _special;
            stat_used = _stat;
        }
        public Proficient_In(Proficient_In _prof) {
            proficiency = _prof.proficiency;
            bonus = _prof.bonus;
            special = _prof.special;
            stat_used = _prof.stat_used;
        }
        public Proficient_In(String _string) {
            String[] tempArray = _string.split(";");

            proficiency = Skill.valueOf(tempArray[1]);
            bonus = Integer.parseInt(tempArray[2]);
            stat_used = Base_Stats_Enum.valueOf(tempArray[3]);
            special = tempArray[4];
        }

        // TODO MAKE METHOD FOR SETTING STAT_USED IF ONE OF THE SKILLS FROM THE PAGE IS CHOSEN
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

        public boolean equals (Proficient_In _other) {
            boolean is_same = true;

            if (_other != null) {
                if (this.stat_used != _other.stat_used) {
                    is_same = false;
                }
                else if (this.bonus != _other.bonus) {
                    is_same = false;
                }
                else if (this.proficiency != _other.proficiency) {
                    is_same = false;
                }
                else if (!this.special.equals(_other.special)) {
                    is_same = false;
                }
            }
            else {
                return false;
            }

            return is_same;
        }

        @NotNull
        @Override
        public String toString()
        {
            return ": Proficient: ;" + this.proficiency + ";" + this.bonus + ";" + this.stat_used + ";" + this.special + ";";
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Saving_Throw {
        public Base_Stats_Enum statistic;
        public int modifier;

                    ////////////////////////////////////////////////////////////////////////////////

        public Saving_Throw() {
            statistic = Base_Stats_Enum.WISDOM;
            modifier = 0;
        }
        public Saving_Throw(Base_Stats_Enum _stat) {
            statistic = _stat;
            modifier = 0;
        }
        public Saving_Throw(Base_Stats_Enum _stat, int _mod) {
            statistic = _stat;
            modifier = _mod;
        }
        public Saving_Throw(Saving_Throw _throw) {
            statistic = _throw.statistic;
            modifier = _throw.modifier;
        }
        public Saving_Throw(String _string) {
            String[] tempArray = _string.split(";");

            statistic = Base_Stats_Enum.valueOf(tempArray[1]);
            modifier = Integer.parseInt(tempArray[2]);
        }

                    ////////////////////////////////////////////////////////////////////////////////

        public boolean equals (Saving_Throw _other) {
            boolean is_same = true;

            if (_other != null) {
                if (this.statistic != _other.statistic) {
                    is_same = false;
                }
                else if (this.modifier != _other.modifier) {
                    is_same = false;
                }
            }
            else {
                return false;
            }

            return is_same;
        }

        @NotNull
        @Override
        public String toString()
        {
            return ": Saving Throw: ;" + this.statistic + ";" + this.modifier + ";";
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public enum Type {
        PLAYER,     MONSTER,
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Money {
        public int copper;
        public int silver;
        public int electrum;
        public int gold;
        public int platinum;

        ////////////////////////////////////////////////////////////////////////////////////////////

        public Money() {
            copper = 0;
            silver = 0;
            electrum = 0;
            gold = 0;
            platinum = 0;
        }
        public Money(int _num) {
            copper = _num;
            silver = _num;
            electrum = _num;
            gold = _num;
            platinum = _num;

            convertMoney(this);
        }
        public Money(int _c, int _s, int _e, int _g, int _p) {
            copper = _c;
            silver = _s;
            electrum = _e;
            gold = _g;
            platinum = _p;

            convertMoney(this);
        }
        public Money(String _string) {
            String[] temp = _string.split(";");
            copper = Integer.parseInt(temp[1]);
            silver = Integer.parseInt(temp[2]);
            electrum = Integer.parseInt(temp[3]);
            gold = Integer.parseInt(temp[4]);
            platinum = Integer.parseInt(temp[5]);

            convertMoney(this);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        public static void convertMoney(Money _current) {
            // TODO: IF VALUE IS NEGATIVE, BREAK UP LARGER PIECES

            while (_current.copper >= 10) {
                _current.copper -= 10;
                _current.silver++;
            }

            while (_current.silver >= 5) {
                _current.silver -= 5;
                _current.electrum++;
            }

            while (_current.electrum >= 2) {
                _current.electrum -= 2;
                _current.gold++;
            }

            while (_current.gold >= 10) {
                _current.gold -= 10;
                _current.platinum++;
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        @NotNull
        @Override
        public String toString()
        {
            return ": Money : ;" + this.copper + ";" + this.silver + ";" + this.electrum + ";" + this.gold + ";" + this.platinum + ";";
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Item {
        public String item;
        public Money cost;
        public int weight;
        public String attributes;

        ////////////////////////////////////////////////////////////////////////////////////////////

        public Item() {
            item = "";
            cost = new Money();
            weight = 0;
            attributes = "";
        }
        public Item(String _string) {
            String[] temp = _string.split(";");
            item = temp[1];
            cost = new Money(Integer.parseInt(temp[3]), Integer.parseInt(temp[4]),
                    Integer.parseInt(temp[5]), Integer.parseInt(temp[6]), Integer.parseInt(temp[7]));
            weight = Integer.parseInt(temp[9]);
            attributes = temp[8];
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        @NotNull
        @Override
        public String toString()
        {
            return ": Item : ;" + this.item + ";" + this.cost.toString() + ";" + this.weight + ";" + this.attributes + ";";
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean template_char;

    private String name;
    private Character.Game_Mode game_mode;
    private Character.Type char_type;
    private int level;
    private int initiative;
    private int health_points;
    private Alignment alignment;
    private Base_Stats_Struct statistics;
    private int speed;
    private int armor_rating;
    private int total_hp_dice;
    private Die hp_die;
    private Physical.Weapon_Struct[] weapons;
    private Attack[] attacks;
    private Proficient_In[] proficiencies;
    private int proficiency_bonus;
    private Saving_Throw[] saving_throws;
    private Item[] items;
    private Money money;
    private int[] spell_slots;
    private int[] used_spell_slots;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Character(String _name) {
        template_char = false;
        name = _name;
        game_mode = Game_Mode.DND;
        char_type = Type.PLAYER;
        initiative = 0;
        level = 1;
        alignment = Alignment.CHAOTIC_EVIL;
        statistics = new Base_Stats_Struct(10);
        speed = 5;
        armor_rating = 10;
        total_hp_dice = 0;
        hp_die = Die.d4;
        weapons = new Physical.Weapon_Struct[12];
        attacks = new Attack[12];
        proficiencies = new Proficient_In[12];
        proficiency_bonus = 0;
        saving_throws = new Saving_Throw[12];
        items = new Item[12];
        money = new Money();
        spell_slots = new int[10];
        used_spell_slots = new int[10];

        health_points = Equation_HP(hp_die);
    }
    public Character(Character _char) {
        template_char = _char.is_template_char();
        name = _char.get_name();
        game_mode = _char.get_game_mode();
        char_type = _char.get_char_type();
        initiative = _char.initiative;
        level = _char.get_level();
        health_points = _char.get_health_points();
        alignment = _char.get_alignment();
        statistics = _char.get_statistics();
        speed = _char.get_speed();
        armor_rating = _char.get_armor_rating();
        total_hp_dice = _char.get_total_hp_dice();
        hp_die = _char.get_hp_die();
        weapons = new Physical.Weapon_Struct[12];
        weapons = _char.get_weapons();
        attacks = new Attack[12];
        attacks = _char.get_attacks();
        proficiencies = new Proficient_In[12];
        proficiencies = _char.get_proficiencies();
        proficiency_bonus = _char.get_proficiency_bonus();
        saving_throws = new Saving_Throw[12];
        saving_throws = _char.get_saving_throws();
        items = new Item[12];
        items = _char.get_items();
        money = _char.get_money();
        spell_slots = new int[10];
        spell_slots = _char.get_spell_slots();
        used_spell_slots = new int[10];
        used_spell_slots = _char.get_spell_slots();
    }
    public Character(HashMap<String, String> _hash) {
        template_char = Boolean.parseBoolean(Objects.requireNonNull(_hash.get(TAG.TEMPLATE_CHARACTER)));
        name = _hash.get(TAG.NAME);
        game_mode = Game_Mode.valueOf(_hash.get(TAG.GAME_MODE));
        char_type = Type.valueOf(_hash.get(TAG.CHARACTER_TYPE));
        initiative = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.INITIATIVE)));
        level = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.LEVEL)));
        health_points = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.HEALTH_POINTS)));
        alignment = Alignment.valueOf(_hash.get(TAG.ALIGNMENT));
        statistics = new Base_Stats_Struct(0);
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
        proficiency_bonus = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.PROFICIENCY_BONUS)));
        money = new Money(Objects.requireNonNull(_hash.get(TAG.MONEY)));

        weapons = new Physical.Weapon_Struct[12];
        weapons = Utility.convertToArrayOfWeapon(_hash);
        attacks = new Attack[12];
        attacks = Utility.convertToArrayOfAttack(_hash);
        proficiencies = new Proficient_In[12];
        proficiencies = Utility.convertToArrayOfProficiencies(_hash);
        saving_throws = new Saving_Throw[12];
        saving_throws = Utility.convertToArrayOfSavingThrows(_hash);
        items = new Item[12];
        items = Utility.convertToArrayOfItem(_hash);
        spell_slots = new int[12];
        spell_slots = Utility.convertToArrayOfInt(_hash, TAG.SPELL_SLOTS, 10);
        used_spell_slots = new int[12];
        used_spell_slots = Utility.convertToArrayOfInt(_hash, TAG.USED_SPELL_SLOTS, 10);
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

    public int get_initiative() { return initiative; }
    public void set_initiative(int _init) { initiative = _init; }

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

    public Base_Stats_Struct get_statistics() {
        return statistics;
    }
    public void set_statistics(Base_Stats_Struct statistics) {
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

    public Physical.Weapon_Struct[] get_weapons() { return weapons; }
    public void set_weapons(Physical.Weapon_Struct[] _weapons) { this.weapons = _weapons; }
    public void clear_weapons() { weapons = new Physical.Weapon_Struct[weapons.length]; }
    public void add_weapon(Physical.Weapon_Struct _weapon) {
        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) {
                weapons[i] = _weapon;
                break;
            }
        }
    }

    public Attack[] get_attacks() { return attacks; }
    public void set_attacks(Attack[] _attacks) { this.attacks = _attacks; }
    public void clear_attacks() { attacks = new Attack[attacks.length]; }
    public void add_attack(Attack _attack) {
        for (int i = 0; i < attacks.length; i++) {
            if (attacks[i] == null) {
                attacks[i] = _attack;
                break;
            }
        }
    }

    public Proficient_In[] get_proficiencies() { return proficiencies; }
    public void set_proficiencies(Proficient_In[] _proficiencies) { this.proficiencies = _proficiencies; }
    public void clear_proficiencies() { proficiencies = new Proficient_In[proficiencies.length]; }
    public void add_proficiency(Proficient_In _prof) {
        for (int i = 0; i < proficiencies.length; i++) {
            if (proficiencies[i] == null) {
                proficiencies[i] = _prof;
                break;
            }
        }
    }

    public int get_proficiency_bonus() {
        return proficiency_bonus;
    }
    public void set_proficiency_bonus(int _bonus) {
        this.proficiency_bonus = _bonus;
    }

    public Saving_Throw[] get_saving_throws() { return saving_throws; }
    public void set_saving_throws(Saving_Throw[] _throw) { this.saving_throws = _throw; }
    public void clear_saving_throws() { saving_throws = new Saving_Throw[saving_throws.length]; }
    public void add_saving_throw(Saving_Throw _throw) {
        for (int i = 0; i < saving_throws.length; i++) {
            if (saving_throws[i] == null) {
                saving_throws[i] = _throw;
                break;
            }
        }
    }

    public Item[] get_items() { return items; }
    public void set_items(Item[] _items) { this.items = _items; }
    public void clear_items() { items = new Item[items.length]; }
    public void add_item(Item _item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = _item;
                break;
            }
        }
    }

    public Money get_money() {
        return money;
    }
    public void set_money(Money _money) { this.money = _money; }

    public int[] get_spell_slots() { return spell_slots; }
    public void set_spell_slots(int[] _slots) { this.spell_slots = _slots; }
    public void clear_spell_slots() { spell_slots = new int[spell_slots.length]; }

    public int[] get_used_spell_slots() { return used_spell_slots; }
    public void set_used_spell_slots(int[] _used_slots) { this.used_spell_slots = _used_slots; }
    public void clear_used_spell_slots() { used_spell_slots = new int[used_spell_slots.length]; }


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
            if (this.template_char != _other.template_char) {
                is_same = false;
            }
            else if (!this.name.equals(_other.name)) {
                is_same = false;
            }
            else if (!this.game_mode.equals(_other.game_mode)) {
                is_same = false;
            }
            else if (!this.char_type.equals(_other.char_type)) {
                is_same = false;
            }
            else if (this.initiative != _other.initiative) {
                is_same = false;
            }
            else if (this.level != _other.level) {
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
            else if (this.proficiency_bonus != _other.proficiency_bonus) {
                is_same = false;
            }
            else if (!Arrays.equals(this.weapons, _other.weapons)) {
                is_same = false;
            }
            else if (!Arrays.equals(this.attacks, _other.attacks)) {
                is_same = false;
            }
            else if (!Arrays.equals(this.proficiencies, _other.proficiencies)) {
                is_same = false;
            }
            else if (!Arrays.equals(this.items, _other.items)) {
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
        temp.put(TAG.INITIATIVE, String.valueOf(this.initiative));
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
        temp.put(TAG.PROFICIENCIES_VECTOR, Arrays.toString(this.proficiencies));
        temp.put(TAG.PROFICIENCY_BONUS, String.valueOf(this.proficiency_bonus));
        temp.put(TAG.SAVING_THROWS_VECTOR, Arrays.toString(this.saving_throws));
        temp.put(TAG.ITEMS_VECTOR, Arrays.toString(this.items));
        temp.put(TAG.MONEY, String.valueOf(this.money));
        temp.put(TAG.SPELL_SLOTS, Arrays.toString(this.spell_slots));
        temp.put(TAG.USED_SPELL_SLOTS, Arrays.toString(this.used_spell_slots));

        return temp;
    }
}
