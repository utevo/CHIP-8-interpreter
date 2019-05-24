
package chip8.app.keyboard;

import chip8.CHIP8;
import chip8.Keyboard;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;


public class KeyboardApp {

    private CHIP8 chip8;
    private KeyboardAppConfig keyboardAppConfig;

    
    public KeyboardApp(CHIP8 chip8, KeyboardAppConfig keyConfiguration) {
        this.chip8 = chip8;
        this.keyboardAppConfig = keyConfiguration;
    }

    public KeyboardApp(CHIP8 chip8) {
        this.chip8 = chip8;
        this.keyboardAppConfig = new KeyboardAppConfig();
    }

    public EventHandler<KeyEvent> getEventHandlerForKeyPressed() {
        return e->{
            byte index = keyboardAppConfig.convertKeyCodeToIndex(e.getCode());
            Keyboard keyboard = chip8.getCpu().getKeyboard();

            if (index != -1)
                keyboard.setKeyUp(index);
        };
    }

    public EventHandler<KeyEvent> getEventHandlerForKeyReleased() {
        return e->{
            byte index = keyboardAppConfig.convertKeyCodeToIndex(e.getCode());
            Keyboard keyboard = chip8.getCpu().getKeyboard();

            if (index != -1)
                keyboard.setKeyDown(index);
        };
    }
}
