package AttackPackage;

import org.jetbrains.annotations.NotNull;

import Utility.Die;

public class Physical extends Attack {
    //////////////////////////////////////////////////////////////////////////////

    public enum Weapon_Enum {    // TODO: MAKE AS ENUM CLASS?
                            //       That way each weapons hit die/etc can be saved per weapon?
        NONE,       RAPIER,             CLUB,       DAGGER,     GREATCLUB,      HANDAXE,
        JAVELIN,    LIGHT_HAMMER,       MACE,

        // TODO: ADD MORE WEAPONS
    }
    public static class Weapon_Struct {
        public Weapon_Enum weapon;
        public int num_die;
        public Die die;

        ////////////////////////////////////////////////////////////////////////////////

        public Weapon_Struct(Weapon_Enum _weapon) {
            weapon = _weapon;
            num_die = 1;
            die = Die.d6;
        }
        public Weapon_Struct(Weapon_Enum _weapon, int _numDie, Die _die) {
            weapon = _weapon;
            num_die = _numDie;
            die = _die;
        }
        public Weapon_Struct(String _string) {
            String[] temp = _string.split(";");
            weapon = Weapon_Enum.valueOf(temp[1]);
            num_die = Integer.parseInt(temp[2]);
            die = Die.valueOf(temp[3]);
        }

        ////////////////////////////////////////////////////////////////////////

        public boolean equals (Weapon_Struct _other) {
            boolean is_same = true;

            if (_other != null) {
                if (this.weapon != _other.weapon) {
                    is_same = false;
                }
                else if (this.num_die != _other.num_die) {
                    is_same = false;
                }
                else if (this.die != _other.die) {
                    is_same = false;
                }
            }

            return is_same;
        }

        @NotNull
        @Override
        public String toString()
        {
            return ": Weapon : ;" + this.weapon.toString() + ";" + this.num_die + ";" + this.die.toString() + ";";
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    private int bonus;      // TODO: MOVE TO PHYSICAL
    private Die die;        // TODO: MOVE TO PHYSICAL
    private int num_dice;   // TODO: MOVE TO PHYSICAL
    private Weapon_Struct weapon_info;

    //////////////////////////////////////////////////////////////////////////////

    public Physical() {
        super();

        bonus = 0;
        die = Die.d4;
        num_dice = 0;
        weapon_info = new Weapon_Struct(Weapon_Enum.RAPIER);
    }
    public Physical(Attack _attack) {
        super(_attack);

        bonus = 0;
        die = Die.d4;
        this.num_dice = 0;
        weapon_info = new Weapon_Struct(Weapon_Enum.RAPIER);
    }
    public Physical(String _string) {
        super(_string);

        bonus = Integer.parseInt(tempArray[tempArray.length - 7]);
        die = Die.valueOf(tempArray[tempArray.length - 6]);
        num_dice = Integer.parseInt(tempArray[tempArray.length - 5]);
        weapon_info = new Weapon_Struct("Weapon: " + ";" + tempArray[tempArray.length - 3] + ";" + tempArray[tempArray.length - 2] + ";" + tempArray[tempArray.length - 1]);
    }

    ////////////////////////////////////////////////////////////////////////////

    public int get_bonus() { return bonus; }
    public void set_bonus(int _bonus) { this.bonus = _bonus; }

    public Die get_die() { return die; }
    public void set_die(Die _die) { this.die = _die; }

    public int get_num_dice() { return num_dice; }
    public void set_num_dice(int _dice) { this.num_dice = _dice; }

    public Weapon_Struct get_weapon_info() { return weapon_info; }
    public void set_weapon_info(Weapon_Struct _weapon) { this.weapon_info = _weapon; }

    // TODO: more getters and setters

    ////////////////////////////////////////////////////////////////////////////

    // TODO: methods
    // TODO: GET HASHMAP

    @NotNull
    @Override
    public String toString()
    {
        return ": Physical: ;" + this.get_name() + ";" + this.get_special() + ";" + this.get_bonus() + ";" + this.die + ";" + this.get_num_dice() + ";" + this.weapon_info.toString() + ";";
    }
}
