package chip8.app;

import chip8.*;
import chip8.app.debug.RegistersInfoApp;
import chip8.app.keyboard.KeyboardApp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class CHIP8App extends Application {

    private CPU cpu;
    private Keyboard keyboard;
    private Memory memory;
    private Screen screen;

    private CHIP8 chip8;

    private RegistersInfoApp registersInfoApp;
    private KeyboardApp keyboardApp;

    ScreenApp screenApp;


    private MenuBar menuBar;

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 400;

    private MenuBar createMenuBar(Stage stage) {

        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");

        MenuItem itemOpenRom = new MenuItem("Open ROM");
        itemOpenRom.setOnAction(e ->{
            String path;
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                System.out.println(file.getAbsolutePath());
                chip8.loadProgram(file);
                registersInfoApp.refresh();
            }
        });

        menuFile.getItems().add(itemOpenRom);
        menuBar.getMenus().add(menuFile);

        Menu menuSaveLoad = new Menu("Save/Load");
        MenuItem itemSave = new MenuItem("Save...");
        MenuItem itemLoad = new MenuItem("Load...");
        menuSaveLoad.getItems().add(itemSave);
        menuSaveLoad.getItems().add(itemLoad);
        menuBar.getMenus().add(menuSaveLoad);

        Menu menuDebug = new Menu("Debug");
        MenuItem itemRegistersInfo = new MenuItem("Registers Info");

        itemRegistersInfo.setOnAction(e -> registersInfoApp.show());
        menuDebug.getItems().add(itemRegistersInfo);
        menuBar.getMenus().add(menuDebug);


        return menuBar;
    }

    private void createDebugButtons (Stage stage, VBox layout) {
        /*                  ***                  */
        Button button1 = new Button("Next tick");
        button1.setOnAction(e -> {
            cpu.nextTick();
            screenApp.render();
            registersInfoApp.refresh(); }
        );
        layout.getChildren().add(button1);
        /*                  ***                  */

        /*                  ***                  */
        Button button2 = new Button("Next 50 ticks");
        button2.setOnAction(e -> {
            for (int i = 0; i < 50; ++i)
                cpu.nextTick();
            screenApp.render();
            registersInfoApp.refresh(); }
        );
        layout.getChildren().add(button2);
        /*                  ***                  */

        /*                  ***                  */
        Button button3 = new Button("30 ticks of clocks");
        button3.setOnAction(e -> {
                    for (int i = 0; i < 30; ++i)
                        chip8.tickOfClocks();
                    registersInfoApp.refresh();
                }
        );
        layout.getChildren().add(button3);
        /*                  ***                  */
    }

    private int convert(KeyEvent e) {

        switch (e.getCode()) {
            case DIGIT0:
                return 0;
            case DIGIT1:
                return 1;
            case DIGIT2:
                return 2;
            case DIGIT3:
                return 3;
            case DIGIT4:
                return 4;
            case DIGIT5:
                return 5;
            case DIGIT6:
                return 6;
            case DIGIT7:
                return 7;
            case DIGIT8:
                return 8;
            case DIGIT9:
                return 9;
            case A:
                return 10;
            case B:
                return 11;
            case C:
                return 12;
            case D:
                return 13;
            case E:
                return 14;
            case F:
                return 15;
            default:
                return -1;
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox layout = new VBox();
        primaryStage.setTitle("CHIP-8 emulator");

        menuBar = createMenuBar(primaryStage);
        layout.getChildren().add(menuBar);


        Memory memory = new Memory();
        Keyboard keyboard = new Keyboard();
        Screen screen = new Screen();
        cpu = new CPU(memory, keyboard, screen);
        chip8 = new CHIP8(cpu);


        keyboardApp = new KeyboardApp(keyboard);
        screenApp = new ScreenApp(screen, SCREEN_WIDTH, SCREEN_HEIGHT);
        layout.getChildren().add(screenApp);
        screenApp.render();
        registersInfoApp = new RegistersInfoApp(cpu);
        registersInfoApp.refresh();


        createDebugButtons(primaryStage, layout);


        Scene scene = new Scene(layout);

        scene.setOnKeyPressed(keyboardApp.getEventHandlerForKeyPressed());
        scene.setOnKeyReleased(keyboardApp.getEventHandlerForKeyReleased());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}