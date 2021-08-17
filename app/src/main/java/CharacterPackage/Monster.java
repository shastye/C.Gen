package CharacterPackage;

import Utility.Die;
import Utility.Utility.TAG;

import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class Monster extends Character {
    /////////////////////////////////////////////////////////////////////////////

    public enum Sense {
        BLINDSIGHT,     DARKVISION,
        TREMORSENSE,    TRUESIGHT,
    }

    ////////////////////////////////////////////////////////////////////////////

    public enum Type {
        ABERRATION,        BEAST,     CELESTIAL,        CONSTRUCT,
        DRAGON,        ELEMENTAL,     FEY,              FIEND,
        GIANT,         HUMANOIDS,     MONSTROSITY,      OOZE,
        PLANT,         UNDEAD,
    }

    //////////////////////////////////////////////////////////////////////////

    public enum Size {
        TINY, SMALL, MEDIUM, LARGE, HUGE, GARGANTUAN,
    }

    //////////////////////////////////////////////////////////////////////

    private Type type;
    private Size size;
    private String tag;
    private int burrow_speed;
    private int climbing_speed;
    private int flying_speed;
    private int swimming_speed;
    private double challenge_rating;
    private Vector<Sense> senses;
    private int experience_points;

    ///////////////////////////////////////////////////////////////////////////

    public Monster(String _name) {
        super(_name);

        type = Type.BEAST;
        size = Size.TINY;
        tag = "";
        burrow_speed = 0;
        climbing_speed = 0;
        flying_speed = 0;
        swimming_speed = 0;
        challenge_rating = 0.0;
        senses = new Vector<>(0);
        experience_points = 0;

        set_hp_die(Calculate_HP_Die());
        set_health_points(this.Equation_HP(this.get_hp_die()));
    }
    public Monster(Character _character) {
        super(_character);

        type = Type.BEAST;
        size = Size.TINY;
        tag = "";
        burrow_speed = 0;
        climbing_speed = 0;
        flying_speed = 0;
        swimming_speed = 0;
        challenge_rating = 0.0;
        senses = new Vector<>(0);
        experience_points = 0;

        set_hp_die(Calculate_HP_Die());
        set_health_points(this.Equation_HP(this.get_hp_die()));
    }
    public Monster(Monster _monster) {
        super(_monster);

        type = _monster.get_type();
        size = _monster.get_size();
        tag = _monster.get_tag();
        burrow_speed = _monster.get_burrow_speed();
        climbing_speed = _monster.get_climbing_speed();
        flying_speed = _monster.get_flying_speed();
        swimming_speed = _monster.get_swimming_speed();
        challenge_rating = _monster.get_challenge_rating();
        senses.clear();
        senses = _monster.get_senses();
        experience_points = _monster.get_XP();
    }
    public Monster(HashMap<String, String> _hash) {
        super(_hash);

        type = Monster.Type.valueOf(_hash.get(TAG.TYPE));
        size = Size.valueOf(_hash.get(TAG.SIZE));
        tag = _hash.get(TAG.TAG);
        burrow_speed = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.SPEED_BURROW)));
        climbing_speed = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.SPEED_CLIMBING)));
        flying_speed = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.SPEED_FLYING)));
        swimming_speed = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.SPEED_SWIMMING)));
        challenge_rating = Double.parseDouble(Objects.requireNonNull(_hash.get(TAG.CHALLENGE_RATING)));
        senses = new Vector<>(0);
        //senses = _hash.get(TAG.TAG_SENSES_VECTOR);  // TODO: EXTRACT SENSES FROM STRING
        experience_points = Integer.parseInt(Objects.requireNonNull(_hash.get(TAG.EXPERIENCE_POINTS)));
    }

    ///////////////////////////////////////////////////////////////////////////

    public Type get_type() { return type; }
    public void set_type(Type _type) { this.type = _type; }

    public Size get_size() { return size; }
    public void set_size(Size _size) { this.size = _size; }

    public String get_tag() { return tag; }
    public void set_tag(String _tag) { this.tag = _tag; }

    public int get_burrow_speed() { return burrow_speed; }
    public void set_burrow_speed(int _speed) { this.burrow_speed = _speed; }

    public int get_climbing_speed() { return climbing_speed; }
    public void set_climbing_speed(int _speed) { this.climbing_speed = _speed; }

    public int get_flying_speed() { return flying_speed; }
    public void set_flying_speed(int _speed) { this.flying_speed = _speed; }

    public int get_swimming_speed() { return swimming_speed; }
    public void set_swimming_speed(int _speed) { this.swimming_speed = _speed; }

    public double get_challenge_rating() { return challenge_rating; }
    public void set_challenge_rating(double _rate) { this.challenge_rating = _rate; }

    public Vector<Sense> get_senses() { return senses; }
    public void set_senses(Vector<Sense> _senses) { this.senses = _senses; }
    public void clear_senses() { senses.clear(); }
    public boolean add_sense(Sense _sense) { return senses.add(_sense); }
    public boolean remove_sense(Sense _sense) { return senses.remove(_sense); }

    public int get_XP() { return experience_points; }
    public void set_XP(int _xp) { this.experience_points = _xp; }

    //////////////////////////////////////////////////////////////////////////

    public Die Calculate_HP_Die() {
        Die _die = Die.d4;

        switch(this.size) {
            case TINY:
                //_die = Die.d4;
                break;

            case SMALL:
                _die = Die.d6;
                break;

            case MEDIUM:
                _die = Die.d8;
                break;

            case LARGE:
                _die = Die.d10;
                break;

            case HUGE:
                _die = Die.d12;
                break;

            case GARGANTUAN:
                _die = Die.d20;
                break;
        }

        return _die;
    }

    // TODO: Experience points calculation

    @Override
    public HashMap<String, String> get_hashMap() {
        HashMap<String, String> temp = super.get_hashMap();

        temp.put(TAG.TYPE, String.valueOf(this.type));
        temp.put(TAG.SIZE, String.valueOf(this.size));
        temp.put(TAG.TAG, this.tag);
        temp.put(TAG.SPEED_BURROW, String.valueOf(this.burrow_speed));
        temp.put(TAG.SPEED_CLIMBING, String.valueOf(this.climbing_speed));
        temp.put(TAG.SPEED_FLYING, String.valueOf(this.flying_speed));
        temp.put(TAG.SPEED_SWIMMING, String.valueOf(this.swimming_speed));

        temp.put(TAG.CHALLENGE_RATING, String.valueOf(this.challenge_rating));
        temp.put(TAG.SENSES_VECTOR, String.valueOf(this.senses));
        temp.put(TAG.EXPERIENCE_POINTS, String.valueOf(this.experience_points));

        return temp;
    }
}
