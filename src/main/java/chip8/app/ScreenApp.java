package chip8.app;

import chip8.Screen;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ScreenApp extends Canvas {

    private final Screen screen;

    private final double width;
    private final double height;

    private final double scaleHeight;
    private final double scaleWidth;

    private final GraphicsContext gc;

    public ScreenApp(Screen screen, double width, double height){
        super(width, height);
        this.screen = screen;

        this.width = width;
        this.height = height;

        scaleWidth = width / screen.WIDTH;
        scaleHeight = height / screen.HEIGHT;

        System.out.println(scaleWidth);
        System.out.println(scaleHeight);

        gc = this.getGraphicsContext2D();
    }

    public void render() {
        for(int x = 0; x < screen.WIDTH; ++x) {
            for(int y = 0; y < screen.HEIGHT; ++y) {
                if (screen.getPixel(x, y) == true)
                    gc.setFill(Color.BLACK);
                else
                    gc.setFill(Color.YELLOW);

                    gc.fillRect(x * scaleWidth, y * scaleHeight, scaleWidth, scaleHeight);
            }
        }
    }


}
