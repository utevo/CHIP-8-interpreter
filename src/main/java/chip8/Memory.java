package chip8;

import lombok.EqualsAndHashCode;

/*
    Group of sprites representing the hexadecimal digits 0 through F.
 */
final class FONTS_DATA {
    public static final byte[] data = {
            (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xF0, // 0
            (byte) 0x20, (byte) 0x60, (byte) 0x20, (byte) 0x20, (byte) 0x70, // 1
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // 2
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 3
            (byte) 0x90, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0x10, // 4
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 5
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 6
            (byte) 0xF0, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x50, // 7
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 8
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 9
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0x90, // A
            (byte) 0xE0, (byte) 0x90, (byte) 0xE0, (byte) 0x90, (byte) 0xE0, // B
            (byte) 0xF0, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0xF0, // C
            (byte) 0xE0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xE0, // D
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // E
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0x80  // F
    };
}

@EqualsAndHashCode
public class Memory {

    public byte[] RAM = new byte[4096];
    public byte[] V = new byte[16];
    public char I;
    public char PC;

    public char[] stack = new char[16];
    public byte SP;

    public byte delayTimer;
    public byte soundTimer;

    public Memory () {
        System.arraycopy(FONTS_DATA.data, 0, RAM, 0, FONTS_DATA.data.length);
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
