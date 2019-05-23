package chip8;


import lombok.Getter;
import lombok.Setter;

import java.io.*;

public class CHIP8 {

    @Getter @Setter
    private CPU cpu;
    @Getter @Setter
    private Memory memory;
    @Getter @Setter
    private Keyboard keyboard;
    @Getter @Setter
    private Screen screen;

    public CHIP8() {
        memory = new Memory();
        keyboard = new Keyboard();
        screen = new Screen();

        cpu = new CPU(memory, keyboard, screen);
    }

    public CHIP8(CPU cpu) {
        memory = cpu.getMemory();
        keyboard = cpu.getKeyboard();
        screen = cpu.getScreen();

        this.cpu = cpu;
    }

    public void timersTick() {
        cpu.timersTick();
    }

    public void cpuTick() throws IllegalStateException {
        cpu.tick();
    }

    public void loadProgram(File file) {
        screen.clear();

        try {
            DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

            byte[] programAsArrayOfByte = new byte[(int) file.length()];
            input.read(programAsArrayOfByte);

            memory.loadProgram(programAsArrayOfByte);

            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
