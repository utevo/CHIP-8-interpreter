package chip8.app;

import chip8.*;
import chip8.app.debug.RegistersInfoApp;
import chip8.app.keyboard.KeyboardApp;
import chip8.app.screen.ScreenApp;
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


    private VBox layout;
    private Scene scene;
    private MenuBar menuBar;


    private Timeline cpuTimeline;
    private Timeline timersTimeline;


    private boolean emulatorRunning = false;

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 400;

    private static final int CPU_RATE = 420;
    private static final int TIMERS_RATE = 60;

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

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("CHIP-8 emulator");

        /* basic initialization */
        memory = new Memory();
        keyboard = new Keyboard();
        screen = new Screen();
        cpu = new CPU(memory, keyboard, screen);
        chip8 = new CHIP8(cpu);


        layout = new VBox();

        /* create menuBar*/
        menuBar = createMenuBar(primaryStage);
        layout.getChildren().add(menuBar);

        /* create keyboardApp*/
        keyboardApp = new KeyboardApp(keyboard);

        /* create screeApp*/
        screenApp = new ScreenApp(screen, SCREEN_WIDTH, SCREEN_HEIGHT);
        layout.getChildren().add(screenApp);
        screenApp.render();


        /* create screeApp*/
        registersInfoApp = new RegistersInfoApp(cpu);
        registersInfoApp.refresh();

        scene = new Scene(layout);


        /* config keyboardAPP */
        scene.setOnKeyPressed(keyboardApp.getEventHandlerForKeyPressed());
        scene.setOnKeyReleased(keyboardApp.getEventHandlerForKeyReleased());


        /* config cpuTimeline */
        cpuTimeline = new Timeline();
        cpuTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame cpuFrame = new KeyFrame(Duration.seconds(1.0 / CPU_RATE), e -> {

            if (emulatorRunning == true) {
                chip8.cpuTick();
                registersInfoApp.refresh();
                screenApp.render();
            }
        });
        cpuTimeline.getKeyFrames().add(cpuFrame);
        cpuTimeline.play();


        /* config timersTimeline */
        timersTimeline = new Timeline();
        timersTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame timersFrame = new KeyFrame(Duration.seconds(1.0 / TIMERS_RATE), e -> {

            if (emulatorRunning == true) {
                chip8.timersTick();
                registersInfoApp.refresh();
            }
        });

        timersTimeline.getKeyFrames().add(timersFrame);
        timersTimeline.play();



        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}