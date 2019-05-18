package chip8.app.debug;

import chip8.CPU;
import chip8.Memory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RegistersInfoApp extends Stage {

    private CPU cpu;

    private TableView<Register> table;
    private StackPane layout;
    private Scene scene;

    public RegistersInfoApp(CPU cpu) {
        this.cpu = cpu;

        TableColumn<Register, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Register, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setMinWidth(100);
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));


        table = new TableView<>();

        refresh();
        table.getColumns().addAll(nameColumn, valueColumn);

        layout = new StackPane();
        layout.getChildren().add(table);

        scene = new Scene(layout);
        this.setTitle("Debug Informations");
        this.setScene(scene);
    }

    public void refresh(){
        Memory memory = cpu.getMemory();

        ObservableList<Register> registers = FXCollections.observableArrayList();
        String valueOfRegisterAsHexString;
        Register newRegister;

        // PC
        valueOfRegisterAsHexString = "0x" + Integer.toHexString(memory.PC).toUpperCase();
        newRegister = new Register("PC", valueOfRegisterAsHexString);
        registers.add(newRegister);

        // I
        valueOfRegisterAsHexString = "0x" + Integer.toHexString(memory.I).toUpperCase();
        newRegister = new Register("I", valueOfRegisterAsHexString);
        registers.add(newRegister);

        // SP
        valueOfRegisterAsHexString = "0x" + Integer.toHexString(memory.SP).toUpperCase();
        newRegister = new Register("SP", valueOfRegisterAsHexString);
        registers.add(newRegister);

        // V
        for (int i = 0; i <= 0xF; ++i) {
            String iAsHexString = Integer.toHexString(i).toUpperCase();
            valueOfRegisterAsHexString = "0x" + Integer.toHexString(memory.V[i]).toUpperCase();

            newRegister = new Register("V" + iAsHexString, valueOfRegisterAsHexString);
            registers.add(newRegister);
        }

        table.setItems(registers);
    }
}
