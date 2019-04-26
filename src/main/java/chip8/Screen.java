package chip8;

import lombok.Data;

@Data
public class Screen {

    final static int HEIGHT = 32;
    final static int WIDTH = 64;

    private boolean pixels[][] = new boolean[HEIGHT][WIDTH];

}
