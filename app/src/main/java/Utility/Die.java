package Utility;

public enum Die {
    d4,         d6,
    d8,         d10,
    d12,       d20;

    public int get_int() {
        switch (this) {
            case d4:
                return 4;
            case d6:
                return 6;
            case d8:
                return 8;
            case d10:
                return 10;
            case d12:
                return 12;
            case d20:
                return 20;
        }

        return 0;
    }
}
