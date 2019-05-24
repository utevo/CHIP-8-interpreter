package chip8.app.screen;

import chip8.CHIP8;
import chip8.Screen;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class ScreenApp extends Canvas {

    private final CHIP8 chip8;

    private final double scaleHeight;
    private final double scaleWidth;

    private final GraphicsContext gc;

    @Getter
    private final Color colorOfGlowingPixel = Color.BLACK;
    @Getter
    private final Color colorOfNotGlowingPixel = Color.YELLOW;

    public ScreenApp(CHIP8 chip8, double width, double height){
        super(width, height);

        this.chip8 = chip8;
        Screen screen = chip8.getCpu().getScreen();

        scaleWidth = getWidth() / screen.WIDTH;
        scaleHeight = getHeight() / screen.HEIGHT;

        gc = this.getGraphicsContext2D();
    }

    public void render() {
        Screen screen = chip8.getCpu().getScreen();
        for(int x = 0; x < screen.WIDTH; ++x) {
            for(int y = 0; y < screen.HEIGHT; ++y) {
                if (screen.getPixel(x, y) == true)
                    gc.setFill(colorOfGlowingPixel);
                else
                    gc.setFill(colorOfNotGlowingPixel);

                    gc.fillRect(x * scaleWidth, y * scaleHeight, scaleWidth, scaleHeight);
            }
        }
    }


}
