import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class CHIP8 extends Application {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 430;

    MenuBar menuBar;
    Screen screen;

    private MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        MenuItem itemOpenRom = new MenuItem("Open ROM");
        MenuItem itemExit = new MenuItem("Exit");
        menuFile.getItems().add(itemOpenRom);
        menuFile.getItems().add(itemExit);
        menuBar.getMenus().add(menuFile);

        Menu menuSaveLoad = new Menu("Save/Load");
        MenuItem itemSave = new MenuItem("Save...");
        MenuItem itemLoad = new MenuItem("Load...");
        menuSaveLoad.getItems().add(itemSave);
        menuSaveLoad.getItems().add(itemLoad);
        menuBar.getMenus().add(menuSaveLoad);

        return menuBar;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("CHIP-8 emulator");

        VBox layout = new VBox();

        menuBar = createMenuBar();
        layout.getChildren().add(menuBar);

        screen = new Screen();

        layout.getChildren().add(screen);

        Scene scene = new Scene(layout, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}