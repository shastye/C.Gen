package AttackPackage;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import Utility.Die;
import Utility.Utility;

public class Attack {
    /////////////////////////////////////////////////////////////////////////

    private String name;
    private String special;
    protected String[] tempArray;

    ///////////////////////////////////////////////////////////////////////////

    public Attack() {
        name = "";
        special = "";
        tempArray = null;
    }
    public Attack(Attack _attack) {
        this.name = _attack.get_name();
        this.special = _attack.get_special();
        tempArray = _attack.tempArray;
    }
    public Attack(String _name, int _numDie, Die _die, String _special, String[] _tempArray, int _bonus) {
        this.name = _name;
        this.special = _special;
        tempArray = _tempArray;
    }
    public Attack(String _string) {
        tempArray = _string.split(";");
        name = tempArray[1];
        special = tempArray[2];
    }

    /////////////////////////////////////////////////////////////////////////

    public String get_name() { return name; }
    public void set_name(String _name) { this.name = _name; }

    public String get_special() { return special; }
    public void set_special(String _special) { this.special = _special; }

    public String[] get_array() { return tempArray; }
    public void set_array(String[] _array) { this.tempArray = _array; }

    //////////////////////////////////////////////////////////////////////////

    @NotNull
    @Override
    public String toString()
    {
        return ": Attack: ;" + this.name + ";" + this.special + ";";
    }
}