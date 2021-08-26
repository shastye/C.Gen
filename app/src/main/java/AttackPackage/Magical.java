package AttackPackage;

import org.jetbrains.annotations.NotNull;

public class Magical extends Attack {
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // TODO: enums and structs

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private int level;
    private int time_in_actions;
    private int range;
    private String duration;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Magical() {
        super();

        level = 0;
        time_in_actions = 1;
        range = 0;
        duration = "Instantaneous";
    }
    public Magical(Attack _attack) {
        super(_attack);

        level = 0;
        time_in_actions = 1;
        range = 0;
        duration = "Instantaneous";
    }
    public Magical(String _string) {
        super(_string);

        level = Integer.parseInt(tempArray[tempArray.length - 4]);
        time_in_actions = Integer.parseInt(tempArray[tempArray.length - 3]);
        range = Integer.parseInt(tempArray[tempArray.length - 2]);
        duration = tempArray[tempArray.length - 1];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int get_level() { return level; }
    public void set_level(int _level) { level = _level; }

    public int get_time_in_actions() { return time_in_actions; }
    public void set_time_in_actions(int _time) { time_in_actions = _time; }

    public int get_range() { return range; }
    public void set_range(int _range) { range = _range; }

    public String get_duration() { return duration; }
    public void set_duration(String _duration) { duration = _duration; }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // TODO: methods

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @NotNull
    @Override
    public String toString()
    {
        return ": Magical: ;" + this.get_name() + ";" + this.get_special() + ";" + level + ";"
                + time_in_actions + ";" + range + ";" + duration + ";";
    }
}
