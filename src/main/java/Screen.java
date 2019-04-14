import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Screen extends Canvas {

    public Screen() {
        super(800, 400);

        //only to show that screen work
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, 800, 400);
    }

}
