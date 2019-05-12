package chip8;

import lombok.ToString;
import org.junit.Test;

import static org.junit.Assert.*;

public class CPUTest {

    private CPU cpu;
    private Memory memory;
    private Keyboard keyboard;
    private Screen screen;

    private void basicInitialization() {
        memory = new Memory();
        screen = new Screen();
        keyboard = new Keyboard();

        cpu = new CPU(memory, keyboard ,screen);
    }

    @Test
    public void fetchOpcode(){
        basicInitialization();

        memory.PC = 0;
        memory.RAM[memory.PC] = (byte)0xAA;
        memory.RAM[memory.PC + 1] = (byte)0xBB;

        System.out.println((int)cpu.fetchOpcode());
        assertEquals(0xAABB, cpu.fetchOpcode() );
    }

    @Test
    public void opcode00E0() {
        basicInitialization();

        char oldPC = 0;

        memory.PC = oldPC;

        screen.setPixel(3,2);

        cpu.opcode00E0();
        assertEquals(new Screen(), screen);
        assertEquals(oldPC + 2, memory.PC);
    }

    @Test
    public void opcode00EE() {
        basicInitialization();

        final char jumpAddress = 524;
        final byte oldSP = 3;

        memory.stack[oldSP] = jumpAddress;
        memory.SP = oldSP;

        cpu.opcode00EE();
        assertEquals(jumpAddress, memory.PC);
        assertEquals(oldSP - 1, memory.SP);
    }

    @Test
    public void opcode1NNN() {
        basicInitialization();

        memory.RAM[0] = 0x17;
        memory.RAM[1] = 0x65;
        memory.PC = 0;

        cpu.opcode1NNN();
        assertEquals(0x765, memory.PC);
    }

    @Test
    public void opcode2NNN() {
        basicInitialization();


        char NNN = 0x0865;
        char oldPC = 0x0479;
        byte oldSP = 3;

        memory.SP = oldSP;
        memory.PC = oldPC;

        memory.RAM[memory.PC] = (byte)(0x20 | ((0x0F00 & NNN) >> 8));
        memory.RAM[memory.PC + 1] = (byte)(NNN & 0x00FF);

        cpu.opcode2NNN();
        assertEquals(NNN, memory.PC);
        assertEquals(oldSP + 1, memory.SP);
        assertEquals(oldPC, memory.stack[memory.SP]);
    }

    @Test
    public void opcode3XNN() {
        basicInitialization();

        byte NN = 0x2A;
        byte notNN = 0x1;
        byte X = 0xB;
        char oldPC = 0x0321;

        memory.PC = oldPC;
        memory.RAM[memory.PC] = (byte)(0x30 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);

        memory.V[X] = NN; //should skip
        cpu.opcode3XNN();
        assertEquals(oldPC + 4, memory.PC);

        memory.PC = oldPC;

        memory.V[X] = notNN; //shouldn't skip
        cpu.opcode3XNN();
        assertEquals(oldPC + 2, memory.PC);
    }

    @Test
    public void opcode4XNN() {
        basicInitialization();

        byte NN = 0x2A;
        byte notNN = 0x1;
        byte X = 0xB;
        char oldPC = 0x0321;

        memory.PC = oldPC;
        memory.RAM[memory.PC] = (byte)(0x40 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);

        memory.V[X] = notNN; //should skip
        cpu.opcode4XNN();
        assertEquals(oldPC + 4, memory.PC);

        memory.PC = oldPC;

        memory.V[X] = NN; //shouldn't skip
        cpu.opcode4XNN();
        assertEquals(oldPC + 2, memory.PC);
    }

    @Test
    public void opcode5XY0() {
        basicInitialization();

        byte X = 0xB;
        byte Y = 0X8;
        char oldPC = 0x0321;

        memory.PC = oldPC;
        memory.RAM[memory.PC] = (byte)(0x50 | X);
        System.out.println(memory.RAM[memory.PC]);
        memory.RAM[memory.PC + 1] = (byte)(Y << 4);

        System.out.println(memory.RAM[memory.PC]);
        System.out.println(memory.RAM[memory.PC + 1]);
        System.out.println(cpu.fetchOpcode());

        System.out.println(-7 << 2);

        memory.V[X] = 0x12;
        memory.V[Y] = 0x12; //should skip
        cpu.opcode5XY0();
        assertEquals(oldPC + 4, memory.PC);

        memory.PC = oldPC;

        memory.V[X] = 0x03;
        memory.V[Y] = 0x73; //shouldn't skip
        cpu.opcode5XY0();
        assertEquals(oldPC + 2, memory.PC);
    }

    @Test
    public void opcode6XNN() {
    }

    @Test
    public void opcode7XNN() {
    }

    @Test
    public void opcode8XY0() {
    }

    @Test
    public void opcode8XY1() {
    }

    @Test
    public void opcode8XY2() {
    }

    @Test
    public void opcode8XY3() {
    }

    @Test
    public void opcode8XY4() {
    }

    @Test
    public void opcode8XY5() {
    }

    @Test
    public void opcode8XY6() {
    }

    @Test
    public void opcode8XY7() {
    }

    @Test
    public void opcode8XYE() {
    }

    @Test
    public void opcode9XY0() {
    }

    @Test
    public void opcodeANNN() {
    }

    @Test
    public void opcodeBNNN() {
    }

    @Test
    public void opcodeCXNN() {
    }

    @Test
    public void opcodeDXYN() {
    }

    @Test
    public void opcodeEX9E() {
    }

    @Test
    public void opcodeEXA1() {
    }

    @Test
    public void opcodeFX07() {
    }

    @Test
    public void opcodeFX0A() {
    }

    @Test
    public void opcodeFX15() {
    }

    @Test
    public void opcodeFX18() {
    }

    @Test
    public void opcodeFX1E() {
    }

    @Test
    public void opcodeFX29() {
    }

    @Test
    public void opcodeFX33() {
    }

    @Test
    public void opcodeFX55() {
    }

    @Test
    public void opcodeFX65() {
    }

}