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

    void setKeyUp(int i) {
        keys[i] = true;
    }

    void setKeyDown(int i) {
        keys[i] = false;
    }

    boolean isPressed(int i) {
        return keys[i];
    }
}


