package chip8;

import lombok.Data;
import lombok.Getter;

@Data
public class CPU {

    private Memory memory;
    private Keyboard keyboard;
    private Screen screen;

    @Getter
    boolean changeOnScreen = false;

    public CPU(Memory memory, Keyboard keyboard, Screen screen) {
        this.memory = memory;
        this.keyboard = keyboard;
        this.screen = screen;
    }

    private char fetchOpcode() {
        return  (char)((memory.RAM[memory.PC] << 8) | (memory.RAM[memory.PC + 1]));
    }

    public void nextTick() {
        char opcode = fetchOpcode();

        switch (opcode) {

            //  Opcode: 00E0
            //  Explanation: Clears the screen.
            case 0x00E0:

                return;

            //  Opcode: 00E0
            //  Explanation: Returns from a subroutine.
            case 0x00EE:

                return;
        }

        switch (opcode & 0xF000) {

            //  Opcode: 1NNN
            //  Explanation: Jumps to address NNN.
            case 0x1000:

                return;

            //  Opcode: 2NNN
            //  Explanation: Calls subroutine at NNN.
            case 0x2000:

                return;

            //  Opcode: 3XNN
            //  Explanation: Skips the next instruction if VX equals NN.
            //  (Usually the next instruction is a jump to skip a code block)
            case 0x3000:

                return;

            //  Opcode: 4XNN
            //  Explanation: Skips the next instruction if VX doesn't equal NN.
            //  (Usually the next instruction is a jump to skip a code block)
            case 0x4000:

                return;

        }

        }


    }



}
