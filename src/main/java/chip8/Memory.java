package chip8;

import lombok.Data;

@Data
public class Memory {
    private byte[] RAM = new byte[4096];
    private byte[] V = new byte[16];
    private char I;
    private char PC;

    private char[] stack = new char[16];
    private int SP;

    private byte delayTimer;
    private byte soundTimer;
}
