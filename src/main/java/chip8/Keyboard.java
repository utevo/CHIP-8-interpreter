package chip8;

import lombok.*;

@Data @EqualsAndHashCode
public class Keyboard {

    static public final int NUMBER_OF_KEYS = 16;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean keys[] = new boolean[NUMBER_OF_KEYS];

    public Keyboard() {
        for (int i = 0; i < keys.length; ++i) {
            keys[i] = false;
        }
    }

    public void setKeyUp(int i) {
        if (i >= 0 && i <= 15)
            keys[i] = true;
    }

    public void setKeyDown(int i) {
        if (i >= 0 && i <= 15)
            keys[i] = false;
    }

    public boolean isPressed(int i) {
        return keys[i];
    }
}


