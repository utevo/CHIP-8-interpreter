package chip8;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KeyboardTest {

    Keyboard keyboard;

    @Before public void initialize() {
        keyboard = new Keyboard();
    }

    @Test
    public void consturctor() {

        for (int i = 0; i < 16; ++i) {
            assertEquals(keyboard.isPressed(i), false);
        }
    }

    @Test
    public void setKeyUp() {

        assertEquals(keyboard.isPressed(0), false);
        assertEquals(keyboard.isPressed(3), false);
        assertEquals(keyboard.isPressed(12), false);

        assertEquals(keyboard.isPressed(1), false);
        assertEquals(keyboard.isPressed(5), false);
        assertEquals(keyboard.isPressed(15), false);


        keyboard.setKeyUp(0);
        assertEquals(keyboard.isPressed(0), true);
        assertEquals(keyboard.isPressed(3), false);
        assertEquals(keyboard.isPressed(12), false);

        assertEquals(keyboard.isPressed(1), false);
        assertEquals(keyboard.isPressed(5), false);
        assertEquals(keyboard.isPressed(15), false);


        keyboard.setKeyUp(3);
        keyboard.setKeyUp(12);
        assertEquals(keyboard.isPressed(0), true);
        assertEquals(keyboard.isPressed(3), true);
        assertEquals(keyboard.isPressed(12), true);

        assertEquals(keyboard.isPressed(1), false);
        assertEquals(keyboard.isPressed(5), false);
        assertEquals(keyboard.isPressed(15), false);

    }

    @Test
    public void setKeyDown() {

        keyboard.setKeyUp(1);
        assertEquals(keyboard.isPressed(1), true);
        assertEquals(keyboard.isPressed(4), false);
        assertEquals(keyboard.isPressed(13), false);

        keyboard.setKeyUp(13);
        assertEquals(keyboard.isPressed(1), true);
        assertEquals(keyboard.isPressed(4), false);
        assertEquals(keyboard.isPressed(13), true);

        keyboard.setKeyDown(1);
        assertEquals(keyboard.isPressed(1), false);
        assertEquals(keyboard.isPressed(4), false);
        assertEquals(keyboard.isPressed(13), true);

        keyboard.setKeyDown(13);
        assertEquals(keyboard.isPressed(1), false);
        assertEquals(keyboard.isPressed(4), false);
        assertEquals(keyboard.isPressed(13), false);
    }
}