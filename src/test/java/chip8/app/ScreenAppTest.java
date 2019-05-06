package chip8.app;

import chip8.Screen;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScreenAppTest {

    double widthOfScreenApp = 800;
    double heightOfScreenApp = 400;

    Screen screen;
    ScreenApp screenApp;

    @Before
    public void initialize() {
        screen = new Screen();
        screenApp = new ScreenApp(screen, widthOfScreenApp, heightOfScreenApp);
    }

    @Test
    public void render() {
        screenApp.render();
        GraphicsContext gcActual =  screenApp.getGraphicsContext2D();

        int x = 3;
        int y = 12;
        screen.setPixel(3, 12);

        screenApp.render();
    }
}