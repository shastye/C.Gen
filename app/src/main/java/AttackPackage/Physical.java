package AttackPackage;

import org.jetbrains.annotations.NotNull;

public class Physical extends Attack {
    //////////////////////////////////////////////////////////////////////////////

    public enum Weapon {    // TODO: MAKE AS ENUM CLASS?
                            //       That way each weapons hit die/etc can be saved per weapon?
        RAPIER,     CLUB,               DAGGER,     GREATCLUB,      HANDAXE,
        JAVELIN,    LIGHT_HAMMER,       MACE,

        // TODO: ADD MORE WEAPONS
    }

    //////////////////////////////////////////////////////////////////////////////

    private Weapon weapon_used;

    //////////////////////////////////////////////////////////////////////////////

    public Physical() {
        super();

        weapon_used = Weapon.RAPIER;
    }
    public Physical(Attack _attack) {
        super(_attack);

        weapon_used = Weapon.RAPIER;
    }
    public Physical(String _string) {
        super(_string);

        weapon_used = Weapon.valueOf(tempArray[5]);
        // TODO: new variable definitions
    }

    ////////////////////////////////////////////////////////////////////////////

    Weapon get_weapon_used() { return weapon_used; }
    void set_weapon_used(Weapon _weapon) { this.weapon_used = _weapon; }

    // TODO: more getters and setters

    ////////////////////////////////////////////////////////////////////////////

    // TODO: methods
    // TODO: GET HASHMAP

    @NotNull
    @Override
    public String toString()
    {
        return ": Physical: ;" + this.weapon_used.toString() + ";";
    }
}
