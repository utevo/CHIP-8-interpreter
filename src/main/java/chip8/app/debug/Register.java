package chip8.app.debug;

import lombok.Data;

@Data
public class Register {
        private String name;
        private String value; // value is type String for easy writing as hex

        Register(String name, String value) {
            this.name = name;
            this.value = value;
        }
}

