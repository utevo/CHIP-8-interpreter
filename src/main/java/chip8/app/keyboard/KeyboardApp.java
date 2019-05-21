
package chip8.app.keyboard;

import chip8.Keyboard;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;


public class KeyboardApp {

    private Keyboard keyboard;
    private KeyboardAppConfig keyboardAppConfig;

    
    public KeyboardApp(Keyboard keyboard, KeyboardAppConfig keyConfiguration) {
        this.keyboard = keyboard;
        this.keyboardAppConfig = keyConfiguration;
    }

    public KeyboardApp(Keyboard keyboard) {
        this.keyboard = keyboard;
        this.keyboardAppConfig = new KeyboardAppConfig();
    }

    public EventHandler<KeyEvent> getEventHandlerForKeyPressed() {
        return e->{
            byte index = keyboardAppConfig.convertKeyCodeToIndex(e.getCode());

            if (index != -1)
                keyboard.setKeyUp(index);
        };
    }

    public EventHandler<KeyEvent> getEventHandlerForKeyReleased() {
        return e->{
            byte index = keyboardAppConfig.convertKeyCodeToIndex(e.getCode());

            if (index != -1)
                keyboard.setKeyDown(index);
        };
    }
}
