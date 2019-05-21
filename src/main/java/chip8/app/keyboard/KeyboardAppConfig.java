package chip8.app.keyboard;

import javafx.scene.input.KeyCode;

import java.util.HashMap;


public class KeyboardAppConfig {

    private HashMap<KeyCode, Byte> map = new HashMap<>();


    public byte convertKeyCodeToIndex(KeyCode keyCode) {
        Byte result = map.get(keyCode);

        if (result == null)
            return -1;
        else
            return result;
    }

    public KeyboardAppConfig(HashMap<KeyCode, Byte> map) {
        this.map = map;
    }

    public KeyboardAppConfig() {
        map.put(KeyCode.DIGIT1, (byte) 0x0);
        map.put(KeyCode.DIGIT2, (byte) 0x1);
        map.put(KeyCode.DIGIT3, (byte) 0x2);
        map.put(KeyCode.DIGIT4, (byte) 0x3);

        map.put(KeyCode.Q, (byte) 0x4);
        map.put(KeyCode.W, (byte) 0x5);
        map.put(KeyCode.E, (byte) 0x6);
        map.put(KeyCode.R, (byte) 0x7);

        map.put(KeyCode.A, (byte) 0x8);
        map.put(KeyCode.S, (byte) 0x9);
        map.put(KeyCode.D, (byte) 0xA);
        map.put(KeyCode.F, (byte) 0xB);

        map.put(KeyCode.Z, (byte) 0xC);
        map.put(KeyCode.X, (byte) 0xD);
        map.put(KeyCode.C, (byte) 0xE);
        map.put(KeyCode.V, (byte) 0xF);
    }

}