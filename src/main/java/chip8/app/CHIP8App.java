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
import java.io.IOException;


public class CHIP8App extends Application {

    private CHIP8 chip8;

    private RegistersInfoApp registersInfoApp;
    private KeyboardApp keyboardApp;
    private ScreenApp screenApp;


    private Scene scene;
    private VBox layout;
    private MenuBar menuBar;


    private Timeline cpuTimeline;
    private Timeline timersTimeline;


    private boolean emulatorRunning = false;

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 400;

    private static final int CPU_RATE = 420;
    private static final int TIMERS_RATE = 60;

    private MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");

        MenuItem itemOpenRom = new MenuItem("Open ROM");
        itemOpenRom.setOnAction(actionEvent ->{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                chip8.loadProgram(file);
                registersInfoApp.refresh();
                Screen screen = chip8.getCpu().getScreen();
                screen.clear();
                screenApp.render();
            }
        });
        menuFile.getItems().add(itemOpenRom);

        MenuItem itemSave = new MenuItem("Save...");
        itemSave.setOnAction(actionEvent ->{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    chip8.saveState(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        menuFile.getItems().add(itemSave);

        MenuItem itemLoad = new MenuItem("Load...");
        itemLoad.setOnAction(actionEvent ->{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    chip8.loadState(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                screenApp.render();
                registersInfoApp.refresh();
            }
        });
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

        primaryStage.setTitle("CHIP-8 interpreter");

        /* basic initialization */
        chip8 = new CHIP8();

        layout = new VBox();

        /* create menuBar*/
        menuBar = createMenuBar();
        layout.getChildren().add(menuBar);

        /* create keyboardApp*/
        keyboardApp = new KeyboardApp(chip8);

        /* create screeApp*/
        screenApp = new ScreenApp(chip8, SCREEN_WIDTH, SCREEN_HEIGHT);
        layout.getChildren().add(screenApp);
        screenApp.render();


        /* create registersInfoApp*/
        registersInfoApp = new RegistersInfoApp(chip8);
        registersInfoApp.refresh();

        scene = new Scene(layout);


        /* config keyboardApp */
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