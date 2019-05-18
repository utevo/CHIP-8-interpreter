package chip8.app;

import chip8.CPU;
import chip8.Memory;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import lombok.Data;

@Data
class Register {
  public String name;
  public int value;

  Register(String name, int value) {
      this.name = name;
      this.value = value;
  }
};

public class DebugInfoApp extends Stage {

    CPU cpu;

    TableView<Register> table;


    StackPane layout;
    Scene scene;

    DebugInfoApp(CPU cpu) {
        this.cpu = cpu;

        TableColumn<Register, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Price column
        TableColumn<Register, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setMinWidth(200);
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));


        table = new TableView<>();
        table.getColumns().addAll(nameColumn, valueColumn);

        refresh();

        layout = new StackPane();
        layout.getChildren().add(table);

        scene = new Scene(layout);
        this.setTitle("Debug Informations");
        this.setScene(scene);


    }

    public void refresh(){
        Memory memory = cpu.getMemory();

        ObservableList<Register> registers = FXCollections.observableArrayList();

        registers.add(new Register("V0", memory.V[0x0]));
        registers.add(new Register("V1", memory.V[0x1]));
        registers.add(new Register("V2", memory.V[0x2]));
        registers.add(new Register("V3", memory.V[0x3]));
        registers.add(new Register("V4", memory.V[0x4]));
        registers.add(new Register("V5", memory.V[0x5]));
        registers.add(new Register("V6", memory.V[0x6]));

        table.setItems(registers);

    }
}
