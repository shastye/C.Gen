package Utility;

import android.util.Log;

import Utility.Utility.TAG;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class User {
    ////////////////////////////////////////////////////////////////////////////////

    public String email;
    public String name;
    public int defaultCharacterLevel;

    ////////////////////////////////////////////////////////////////////////////////

    public User() {
        email = "";
        name = "";
        defaultCharacterLevel = -1;
    }
    public User(String _email, String _name, int _charLevel) {
        email = _email;
        name = _name;
        defaultCharacterLevel = _charLevel;
    }
    public User(User _user) {
        email = _user.get_email();
        name = _user.get_name();
        defaultCharacterLevel = _user.get_default_character_level();
    }
    public User(Serializable _serUser) {
        HashMap<String, String> _hashUser = ((HashMap<String, String>) _serUser);

        try {
            email = _hashUser.get(TAG.EMAIL);
        } catch (Exception e) {
            email = "";
            Log.e("Email was Null", "Email now set to \"\"");
        }

        try {
            name = _hashUser.get(TAG.NAME);
        } catch (Exception e) {
            name = "";
            Log.e("Name was Null", "Name now set to \"\"");
        }

        try {
            defaultCharacterLevel = Integer.parseInt(Objects.requireNonNull(_hashUser.get(TAG.DEFAULT_CHARACTER_LEVEL)));
        } catch (Exception e) {
            defaultCharacterLevel = -1;
            Log.e("CharacterLevel was Null", "Level now set to -1");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    public String get_email() { return email; }
    public void set_email(String _email) { email = _email; }

    public String get_name() { return name; }
    public void set_name(String _name) { name = _name; }

    public int get_default_character_level() {return defaultCharacterLevel; }
    public void set_default_character_level(int _characterLevel) { defaultCharacterLevel = _characterLevel; }

    /////////////////////////////////////////////////////////////////////////////////

    public HashMap<String, String> get_hashMap() {
        HashMap<String, String> temp = new HashMap<>(0);

        temp.put(TAG.NAME, this.name);
        temp.put(TAG.DEFAULT_CHARACTER_LEVEL, String.valueOf(this.defaultCharacterLevel));
        temp.put(TAG.EMAIL, this.email);

        return temp;
    }
}
