package chip8;

import lombok.EqualsAndHashCode;


@EqualsAndHashCode
public class Memory {

    public byte[] RAM = new byte[4096];
    public byte[] V = new byte[16];
    public char I;
    public char PC;

    public char[] stack = new char[16];
    public byte SP;

    public char delayTimer;
    public char soundTimer;

    public Memory() {
        System.arraycopy(FontsData.data, 0, RAM, 0, FontsData.data.length);
    }

    public void clear() {
        for (int i = 0x200; i < RAM.length; ++i)
            RAM[i] = 0;
        for (int i = 0; i < V.length; ++i)
            V[i] = 0;
        I = 0;
        PC = 0;
        for (int i = 0; i < stack.length; ++i)
            stack[i] = 0;
        SP = 0;
        delayTimer = 0;
        soundTimer = 0;
    }

    public void loadProgram(byte[] program) {
        clear();
        System.arraycopy(program, 0, RAM, 512, program.length);
        PC = 0x200;
    }


}
