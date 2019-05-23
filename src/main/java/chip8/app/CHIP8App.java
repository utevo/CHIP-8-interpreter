package chip8.app;

import chip8.*;
import chip8.app.debug.RegistersInfoApp;
import chip8.app.keyboard.KeyboardApp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class CHIP8App extends Application {

    private CPU cpu;
    private Keyboard keyboard;
    private Memory memory;
    private Screen screen;

    private CHIP8 chip8;

    private RegistersInfoApp registersInfoApp;
    private KeyboardApp keyboardApp;

    private ScreenApp screenApp;


    private MenuBar menuBar;

    private boolean emulatorRunning = false;

    private Timeline cpuLoop;
    private Timeline timersLoop;

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
                chip8.loadProgram(file);
                registersInfoApp.refresh();
                screen.clear();
                screenApp.render();
            }
        });
        menuFile.getItems().add(itemOpenRom);

        MenuItem itemSave = new MenuItem("Save...");
        MenuItem itemLoad = new MenuItem("Load...");
        menuFile.getItems().add(itemSave);
        menuFile.getItems().add(itemLoad);

        menuBar.getMenus().add(menuFile);


        Menu menuEmulation = new Menu("Emulation");

        MenuItem itemRun = new MenuItem("Run");
        itemRun.setOnAction(e -> emulatorRunning = true);
        MenuItem itemPause = new MenuItem("Pause");
        itemPause.setOnAction(e -> emulatorRunning = false);

        menuEmulation.getItems().add(itemRun);
        menuEmulation.getItems().add(itemPause);

        menuBar.getMenus().add(menuEmulation);


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
            cpu.tick();
            screenApp.render();
            registersInfoApp.refresh(); }
        );
        layout.getChildren().add(button1);
        /*                  ***                  */

        /*                  ***                  */
        Button button2 = new Button("Next 50 ticks");
        button2.setOnAction(e -> {
            for (int i = 0; i < 50; ++i)
                cpu.tick();
            screenApp.render();
            registersInfoApp.refresh(); }
        );
        layout.getChildren().add(button2);
        /*                  ***                  */

        /*                  ***                  */
        Button button3 = new Button("30 ticks of clocks");
        button3.setOnAction(e -> {
                    for (int i = 0; i < 30; ++i)
                        chip8.timersTick();
                    registersInfoApp.refresh();
                }
        );
        layout.getChildren().add(button3);
        /*                  ***                  */
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox layout = new VBox();
        primaryStage.setTitle("CHIP-8 emulator");

        menuBar = createMenuBar(primaryStage);
        layout.getChildren().add(menuBar);


        memory = new Memory();
        keyboard = new Keyboard();
        screen = new Screen();
        cpu = new CPU(memory, keyboard, screen);
        chip8 = new CHIP8(cpu);


        keyboardApp = new KeyboardApp(keyboard);
        screenApp = new ScreenApp(screen, SCREEN_WIDTH, SCREEN_HEIGHT);
        layout.getChildren().add(screenApp);
        screenApp.render();
        registersInfoApp = new RegistersInfoApp(cpu);
        registersInfoApp.refresh();


        Scene scene = new Scene(layout);

        scene.setOnKeyPressed(keyboardApp.getEventHandlerForKeyPressed());
        scene.setOnKeyReleased(keyboardApp.getEventHandlerForKeyReleased());


        cpuLoop = new Timeline();
        cpuLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame cpuFrame = new KeyFrame(Duration.seconds(0.003), e -> {

            if (emulatorRunning == true) {
                registersInfoApp.refresh();
                chip8.cpuTick();
                registersInfoApp.refresh();
                screenApp.render();
            }
        });
        cpuLoop.getKeyFrames().add(cpuFrame);
        cpuLoop.play();



        timersLoop = new Timeline();
        timersLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame timersFrame = new KeyFrame(Duration.seconds(1.0/60), e -> {

            if (emulatorRunning == true) {
                chip8.timersTick();
                registersInfoApp.refresh();
            }
        });

        timersLoop.getKeyFrames().add(timersFrame);
        timersLoop.play();



        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}