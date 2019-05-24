package chip8.app;

import chip8.CHIP8;
import chip8.Screen;
import chip8.app.screen.ScreenApp;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Before;
import org.junit.Test;

public class ScreenAppTest {

    double widthOfScreenApp = 800;
    double heightOfScreenApp = 400;

    CHIP8 chip8;
    ScreenApp screenApp;

    @Before
    public void initialize() {
        chip8 = new CHIP8();
        screenApp = new ScreenApp(chip8, widthOfScreenApp, heightOfScreenApp);
    }

    @Test
    public void render() {
        screenApp.render();
        GraphicsContext gcActual =  screenApp.getGraphicsContext2D();

        Screen screen = chip8.getCpu().getScreen();
        int x = 3;
        int y = 12;
        screen.setPixel(3, 12);

        screenApp.render();
    }
}