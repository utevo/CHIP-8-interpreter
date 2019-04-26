package chip8;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Keyboard {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean keys[] = new boolean[16];

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


