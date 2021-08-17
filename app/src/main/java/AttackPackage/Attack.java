package AttackPackage;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import Utility.Die;
import Utility.Utility;

public class Attack {
    /////////////////////////////////////////////////////////////////////////

    private String name;
    private int num_dice;
    private Die die;
    private String special;
    protected String[] tempArray;

    ///////////////////////////////////////////////////////////////////////////

    public Attack() {
        name = "";
        num_dice = 0;
        die = Die.d4;
        special = "";
        tempArray = null;
    }
    public Attack(Attack _attack) {
        this.name = _attack.get_name();
        this.num_dice = _attack.get_num_dice();
        this.die = _attack.get_die();
        this.special = _attack.get_special();
        tempArray = null;
    }
    public Attack(String _string) {
        tempArray = _string.split(";");
        name = tempArray[1];
        num_dice = Integer.parseInt(tempArray[2]);
        die = Die.valueOf(tempArray[3]);
        special = tempArray[4];
    }

    /////////////////////////////////////////////////////////////////////////

    public String get_name() { return name; }
    public void set_name(String _name) { this.name = _name; }

    public int get_num_dice() { return num_dice; }
    public void set_num_dice(int _dice) { this.num_dice = _dice; }

    public Die get_die() { return die; }
    public void set_die(Die _die) { this.die = _die; }

    public String get_special() { return special; }
    public void set_special(String _special) { this.special = _special; }

    public String[] get_array() { return tempArray; }
    public void set_array(String[] _array) { this.tempArray = _array; }

    //////////////////////////////////////////////////////////////////////////

    public HashMap<String, String> get_hash_map() {
        HashMap<String, String> temp = new HashMap<String, String>(0);

        temp.put(Utility.TAG.NAME, name);
        temp.put(Utility.TAG.NUMBER_OF_DICE, String.valueOf(num_dice));
        temp.put(Utility.TAG.DIE, String.valueOf(die));
        temp.put(Utility.TAG.SPECIAL, special);

        return temp;
    }

    @NotNull
    @Override
    public String toString()
    {
        return ": Attack: ;" + this.name + ";" + this.num_dice + ";" + this.die + ";" + this.special + ";";
    }
}