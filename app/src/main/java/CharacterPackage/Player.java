package CharacterPackage;

import java.util.HashMap;
import java.util.Objects;

import Utility.Die;
import Utility.Utility.TAG;

public class Player extends Character {
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Death_Saves {  //struct
        public int successes;
        public int failures;

                                            ////////////////////////////////////////////////////////

        public Death_Saves() {
            successes = 0;
            failures = 0;
        }
        public Death_Saves(int _num) {
            if (_num > 3) {
                _num = 3;
            }

            successes = _num;
            failures = _num;
        }
        public Death_Saves(int _win, int _fail) {
            successes = _win;
            failures = _fail;
        }

                                           /////////////////////////////////////////////////////////

        public boolean equals (Death_Saves _other) {
            boolean is_same = true;

            if (this.successes != _other.successes) {
                is_same = false;
            }
            else if (this.failures != _other.failures) {
                is_same = false;
            }

            return is_same;
        }
        public boolean equals (int _other) {
            boolean is_same = true;

            if (this.successes != _other) {
                is_same = false;
            }
            else if (this.failures != _other) {
                is_same = false;
            }

            return is_same;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public enum Classes {
        BARBARIAN,      BARD,       CLERIC,         DRUID,
        FIGHTER,        MONK,       PALADIN,        ROGUE,
        SORCERER,       WARLOCK,    WIZARD,         ARTIFICER,
        BLOOD_HUNTER,
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public enum Race {
        HUMAN,      DRAGONBORN,     DWARF,      // TODO: Add rest of races
    }

    public static Race string_to_race(String _race) {
        Race temp = Race.DRAGONBORN;

        try {
            temp = Race.valueOf(_race);
        } catch (Exception e) {
            temp = Race.HUMAN;
        }

        return temp;
    }

    ////////////////////////////////////////////////////////////

    private Death_Saves death_saves;
    private int total_hit_dice; // TODO: DELETE; LOGIC ERROR
    private Die hit_die;        // TODO: DELETE; LOGIC ERROR
    private Race race;
    private Classes classe;
    private String background;
    private int experience_points;
    private int inspiration;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Player(String _name) {
        super(_name);

        death_saves = new Death_Saves(0);
        total_hit_dice = 0;
        hit_die = Die.d6;
        race = Race.HUMAN;
        classe = Classes.CLERIC;
        experience_points = 0;
        background = "-=- NONE -=-";
        inspiration = 0;
    }
    public Player(Character _character) {
        super(_character);

        death_saves = new Death_Saves(0);
        total_hit_dice = 0;
        hit_die = Die.d4;
        race = Race.DRAGONBORN;
        classe = Classes.BARD;
        experience_points = 0;
        background = "-=- NONE -=-";
        inspiration = 0;
    }
    public Player(Player _player) {
        super(_player);

        death_saves = _player.get_death_saves();
        total_hit_dice = _player.get_total_hit_dice();
        hit_die = _player.get_hit_die();
        race = _player.get_race();
        classe = _player.get_classe();
        experience_points = 0;
        background = _player.get_background();
        inspiration = _player.get_inspiration();
    }
    public Player(HashMap<String, String> _hash) {
        super(_hash);

        death_saves = new Death_Saves(0);
        death_saves.failures = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.DEATH_SAVES_FAILURES)));
        death_saves.successes = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.DEATH_SAVES_SUCCESSES)));
        total_hit_dice = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.TOTAL_HIT_DICE)));
        hit_die = Die.valueOf(_hash.get(TAG.HIT_DIE));
        race = Race.valueOf(_hash.get(TAG.RACE));
        classe = Classes.valueOf(_hash.get(TAG.CLASS));
        experience_points = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.EXPERIENCE_POINTS)));
        background = _hash.get(TAG.BACKGROUND);
        inspiration = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.INSPIRATION)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Death_Saves get_death_saves() { return death_saves; }
    public void set_death_saves(Death_Saves _saves) { this.death_saves = _saves; }
    public void reset_death_saves() {
        this.death_saves.failures = 0;
        this.death_saves.successes = 0;
    }
    public void add_failed_death_save() { this.death_saves.failures++; }
    public void add_succeeded_death_save() {this.death_saves.successes++; }

    public int get_total_hit_dice() { return total_hit_dice; }
    public void set_total_hit_dice(int _dice) { this.total_hit_dice = _dice; }

    public Die get_hit_die() { return hit_die; }
    public void set_hit_die(Die _die) { this.hit_die = _die; }

    public Race get_race() { return race; }
    public void set_race(Race race) { this.race = race; }

    public Classes get_classe() { return classe; }
    public void set_classe(Classes _class) { classe = _class; }

    public Integer get_experience_points() { return experience_points; }
    public void set_experience_points(Integer _xp) { experience_points = _xp; }

    public String get_background() { return background; }
    public void set_background(String _background) { background = _background; }

    public int get_inspiration() { return inspiration; }
    public void set_inspiration(int _insp) { inspiration = _insp; }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean equals (Player _other) {
        boolean is_equal = true;

        if (super.equals(_other)) {
            if (!this.death_saves.equals(_other.get_death_saves())) {
                is_equal = false;
            }
            else if (this.total_hit_dice != _other.get_total_hit_dice()) {
                is_equal = false;
            }
            else if (this.hit_die != _other.get_hit_die()) {
                is_equal = false;
            }
            else if (this.race != _other.race ) {
                is_equal = false;
            }
            else if (this.classe != _other.get_classe()) {
                is_equal = false;
            }
            else if (this.experience_points != _other.get_experience_points()) {
                is_equal = false;
            }
            else if (!this.background.equals(_other.get_background())) {
                is_equal = false;
            }
            else if (this.inspiration != _other.get_inspiration()) {
                is_equal = false;
            }
        }
        else {
            is_equal = false;
        }

        return is_equal;
    }

    @Override
    public HashMap<String, String> get_hashMap() {
        HashMap<String, String> temp = super.get_hashMap();

        temp.put(TAG.DEATH_SAVES_SUCCESSES, String.valueOf(this.death_saves.successes));
        temp.put(TAG.DEATH_SAVES_FAILURES, String.valueOf(this.death_saves.failures));
        temp.put(TAG.TOTAL_HIT_DICE, String.valueOf(this.total_hit_dice));
        temp.put(TAG.HIT_DIE, String.valueOf(this.hit_die));
        temp.put(TAG.RACE, String.valueOf(this.race));
        temp.put(TAG.CLASS, String.valueOf(this.classe));
        temp.put(TAG.EXPERIENCE_POINTS, String.valueOf(this.experience_points));
        temp.put(TAG.BACKGROUND, this.background);
        temp.put(TAG.INSPIRATION, String.valueOf(this.inspiration));

        return temp;
    }
}
