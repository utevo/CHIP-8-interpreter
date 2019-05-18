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

        assertEquals(0xAABB, cpu.fetchOpcode() );
    }

    @Test
    public void opcode00E0() {
        basicInitialization();

        char PC = 0x0479;

        memory.PC = PC;
        memory.RAM[memory.PC] = 0x00;
        memory.RAM[memory.PC + 1] = (byte)0xE0;

        screen.setPixel(3,2);

        cpu.opcode00E0();
        assertEquals(new Screen(), screen);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode00E0ver2() {
        basicInitialization();

        char PC = 0x0479;

        memory.PC = PC;
        memory.RAM[memory.PC] = 0x00;
        memory.RAM[memory.PC + 1] = (byte)0xE0;

        screen.setPixel(3,2);

        cpu.nextTick();
        assertEquals(new Screen(), screen);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode00EE() {
        basicInitialization();

        final char jumpAddress = 524;
        final byte oldSP = 3;
        char PC = 0x432;

        memory.PC = PC;
        memory.RAM[memory.PC] = 0x00;
        memory.RAM[memory.PC + 1] = (byte)0xEE;

        memory.stack[oldSP] = jumpAddress;
        memory.SP = oldSP;

        cpu.opcode00EE();
        assertEquals(jumpAddress, memory.PC);
        assertEquals(oldSP - 1, memory.SP);
    }

    @Test
    public void opcode00EEver2() {
        basicInitialization();

        final char jumpAddress = 524;
        final byte oldSP = 3;
        char PC = 0x432;

        memory.PC = PC;
        memory.RAM[memory.PC] = 0x00;
        memory.RAM[memory.PC + 1] = (byte)0xEE;

        memory.stack[oldSP] = jumpAddress;
        memory.SP = oldSP;

        cpu.nextTick();
        assertEquals(jumpAddress, memory.PC);
        assertEquals(oldSP - 1, memory.SP);
    }

    @Test
    public void opcode1NNN() {
        basicInitialization();

        char PC = 0x0479;

        memory.PC = PC;
        memory.RAM[memory.PC] = 0x17;
        memory.RAM[memory.PC + 1] = 0x65;

        cpu.opcode1NNN();
        assertEquals(0x765, memory.PC);
    }

    @Test
    public void opcode1NNNver2() {
        basicInitialization();

        char PC = 0x0479;

        memory.PC = PC;
        memory.RAM[memory.PC] = 0x17;
        memory.RAM[memory.PC + 1] = 0x65;

        cpu.nextTick();
        assertEquals(0x765, memory.PC);
    }

    @Test
    public void opcode2NNN() {
        basicInitialization();


        char NNN = 0x0865;
        byte oldSP = 3;
        char PC = 0x0479;

        memory.SP = oldSP;
        memory.PC = PC;

        memory.RAM[memory.PC] = (byte)(0x20 | ((0x0F00 & NNN) >> 8));
        memory.RAM[memory.PC + 1] = (byte)(NNN & 0x00FF);

        cpu.opcode2NNN();
        assertEquals(NNN, memory.PC);
        assertEquals(oldSP + 1, memory.SP);
        assertEquals(PC, memory.stack[memory.SP]);
    }

    @Test
    public void opcode2NNNver2() {
        basicInitialization();


        char NNN = 0x0865;
        byte oldSP = 3;
        char PC = 0x0479;

        memory.SP = oldSP;
        memory.PC = PC;

        memory.RAM[memory.PC] = (byte)(0x20 | ((0x0F00 & NNN) >> 8));
        memory.RAM[memory.PC + 1] = (byte)(NNN & 0x00FF);

        cpu.nextTick();
        assertEquals(NNN, memory.PC);
        assertEquals(oldSP + 1, memory.SP);
        assertEquals(PC, memory.stack[memory.SP]);
    }

    @Test
    public void opcode3XNN() {
        basicInitialization();

        byte NN = 0x2A;
        byte notNN = 0x1;
        byte X = 0xB;
        char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x30 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);

        memory.V[X] = NN; //should skip
        cpu.opcode3XNN();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = notNN; //shouldn't skip
        cpu.opcode3XNN();
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode3XNNver2() {
        basicInitialization();

        byte NN = 0x2A;
        byte notNN = 0x1;
        byte X = 0xB;
        char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x30 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);

        memory.V[X] = NN; //should skip
        cpu.nextTick();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = notNN; //shouldn't skip
        cpu.nextTick();
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode4XNN() {
        basicInitialization();

        byte NN = 0x2A;
        byte notNN = 0x1;
        byte X = 0xB;
        char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x40 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);

        memory.V[X] = notNN; //should skip
        cpu.opcode4XNN();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = NN; //shouldn't skip
        cpu.opcode4XNN();
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode4XNNver2() {
        basicInitialization();

        byte NN = 0x2A;
        byte notNN = 0x1;
        byte X = 0xB;
        char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x40 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);

        memory.V[X] = notNN; //should skip
        cpu.nextTick();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = NN; //shouldn't skip
        cpu.nextTick();
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode5XY0() {
        basicInitialization();

        byte X = 0xB;
        byte Y = 0X8;
        char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x50 | X);
        memory.RAM[memory.PC + 1] = (byte)(Y << 4);

        memory.V[X] = 0x12;
        memory.V[Y] = 0x12; //should skip
        cpu.opcode5XY0();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = 0x03;
        memory.V[Y] = 0x73; //shouldn't skip
        cpu.opcode5XY0();
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode5XY0ver2() {
        basicInitialization();

        byte X = 0xB;
        byte Y = 0X8;
        char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x50 | X);
        memory.RAM[memory.PC + 1] = (byte)(Y << 4);

        memory.V[X] = 0x12;
        memory.V[Y] = 0x12; //should skip
        cpu.nextTick();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = 0x03;
        memory.V[Y] = 0x73; //shouldn't skip
        cpu.nextTick();
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode6XNN() {
        basicInitialization();

        final byte X = 0xB;
        final byte NN = 0x2A;
        final byte VX = 0x12;
        final char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x60 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);
        memory.V[X] = VX; // V[X] != NN

        cpu.opcode6XNN();
        assertEquals(NN, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode6XNNver2() {
        basicInitialization();

        final byte X = 0x9;
        final byte NN = 0x2A;
        final byte VX = 0x12;
        final char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x60 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);
        memory.V[X] = VX; // V[X] != NN

        cpu.nextTick();
        assertEquals(NN, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode7XNN() {
        basicInitialization();

        final byte X = 0xB;
        final byte NN = 0x2A;
        final byte VX = 0x12;
        final char PC = 0x0321;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x70 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);
        memory.V[X] = VX;

        char corectResult = (char) VX + NN;

        cpu.opcode7XNN();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode7XNNver2() {
        basicInitialization();

        final byte X = 0x7;
        final byte NN = 0x1A;
        final byte VX = 0x13;
        final char PC = 0x0311;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x70 | X);
        memory.RAM[memory.PC + 1] = (byte)(NN & 0x00FF);
        memory.V[X] = VX;

        char corectResult = (char) VX + NN;

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY0() {
        basicInitialization();

        byte X = 0x3;
        byte Y = 0xA;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4)| 0x00);

        memory.V[X] = 3;
        memory.V[Y] = 7;

        cpu.opcode8XY0();
        assertEquals(7, memory.V[Y]);
        assertEquals(memory.V[Y], memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY0ver2() {
        basicInitialization();

        byte X = 0x3;
        byte Y = 0xA;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4)| 0x00);

        memory.V[X] = 3;
        memory.V[Y] = 7;

        cpu.nextTick();
        assertEquals(7, memory.V[Y]);
        assertEquals(memory.V[Y], memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY1() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x01);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =             0b01000110;
        byte corectResult = (byte)0b11101111;

        cpu.opcode8XY1();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY1ver2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x01);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =             0b01000110;
        byte corectResult = (byte)0b11101111;

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x02);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =             0b01000110;
        byte corectResult = (byte)0b01000100;

        cpu.opcode8XY2();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY2ver2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x02);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =             0b01000110;
        byte corectResult = (byte)0b01000100;

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY3() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x03);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =             0b01000110;
        byte corectResult = (byte)0b10101011;

        cpu.opcode8XY3();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY3ver2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x03);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =             0b01000110;
        byte corectResult = (byte)0b10101011;

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY4() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x04);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =             0b01000110;
        byte corectResult = (byte)0b00110011; //with overflow

        cpu.opcode8XY4();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b00101101;
        memory.V[Y] =             0b01001110;
        corectResult =      (byte)0b01111011; // with overflow

        cpu.opcode8XY4();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY4ver2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x04);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =             0b01000110;
        byte corectResult = (byte)0b00110011; // without overflow

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b00101101;
        memory.V[Y] =             0b01001110;
        corectResult =      (byte)0b01111011; // without overflow

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY5() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x05);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =       (byte)0b01000110;
        byte corectResult = (byte)0b10100111; // with not borrow

        cpu.opcode8XY5();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);

        memory.PC = PC;

        memory.V[X] =       (byte)0b00101101;
        memory.V[Y] =             0b01001110;
        corectResult =      (byte)0b11011111; // without not borrow

        cpu.opcode8XY5();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY5ver2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x05);

        memory.V[X] =       (byte)0b11101101;
        memory.V[Y] =       (byte)0b01000110;
        byte corectResult = (byte)0b10100111; // with not borrow

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b00101101;
        memory.V[Y] =             0b01001110;
        corectResult =      (byte)0b11011111; // without not borrow

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY6() {
        basicInitialization();

        byte X = 0xA;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x06);

        memory.V[X] =       (byte)0b11101101;
        byte corectResult = (byte)0b01110110;

        cpu.opcode8XY6();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b00101100;
        corectResult =      (byte)0b00010110;

        cpu.opcode8XY6();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY6ver2() {
        basicInitialization();

        byte X = 0xA;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x06);

        memory.V[X] =       (byte)0b11101101;
        byte corectResult = (byte)0b01110110;

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b00101100;
        corectResult =      (byte)0b00010110;

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY7() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x07);

        memory.V[Y] =       (byte)0b11101101;
        memory.V[X] =       (byte)0b01000110;
        byte corectResult = (byte)0b10100111; // with not borrow

        cpu.opcode8XY7();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[Y] =       (byte)0b00101101;
        memory.V[X] =       (byte)0b01001110;
        corectResult =      (byte)0b11011111; // without not borrow

        cpu.opcode8XY7();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XY7ver2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x07);

        memory.V[Y] =       (byte)0b11101101;
        memory.V[X] =       (byte)0b01000110;
        byte corectResult = (byte)0b10100111; // with not borrow

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[Y] =       (byte)0b00101101;
        memory.V[X] =       (byte)0b01001110;
        corectResult =      (byte)0b11011111; // without not borrow

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XYE() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x0E);

        memory.V[X] =       (byte)0b11000110;
        byte corectResult = (byte)0b10001100;

        cpu.opcode8XYE();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b01001110;
        corectResult =      (byte)0b10011100;

        cpu.opcode8XYE();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode8XYEver2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x80 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x0E);

        memory.V[X] =       (byte)0b11000110;
        byte corectResult = (byte)0b10001100;

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b01001110;
        corectResult =      (byte)0b10011100;

        cpu.nextTick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x0, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcode9XY0() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x90 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x00);

        memory.V[X] = (byte) 245;
        memory.V[Y] = (byte) 245;

        cpu.opcode9XY0();
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] = (byte) 245;
        memory.V[Y] = (byte) 232;

        cpu.opcode9XY0();
        assertEquals(PC + 4, memory.PC);
}

    @Test
    public void opcode9XY0ver2() {
        basicInitialization();

        byte X = 0xE;
        byte Y = 0xC;
        char PC = 0x0234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0x90 | X);
        memory.RAM[memory.PC + 1] = (byte)((Y << 4) | 0x00);

        memory.V[X] = (byte) 125;
        memory.V[Y] = (byte) 125;

        cpu.nextTick();
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] = (byte) 0b10101010;
        memory.V[Y] = (byte) 0b00101010;

        cpu.nextTick();
        assertEquals(PC + 4, memory.PC);
    }

    @Test
    public void opcodeANNN() {
        basicInitialization();

        char NNN = 0x0F13;
        char PC = 0x0734;
        char I = 0x1234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xA0 | (NNN >>> 8) );
        memory.RAM[memory.PC + 1] = (byte)(NNN & 0x0FF);

        memory.I = I;

        cpu.opcodeANNN();
        assertEquals(NNN, memory.I);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeANNNver2() {
        basicInitialization();

        char NNN = 0x0F13;
        char PC = 0x0734;
        char I = 0x1234;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xA0 | (NNN >>> 8) );
        memory.RAM[memory.PC + 1] = (byte)(NNN & 0x0FF);

        memory.I = I;

        cpu.nextTick();
        assertEquals(NNN, memory.I);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeBNNN() {
        basicInitialization();

        char NNN = 0x0F13;
        char PC = 0x0734;
        char V0 = 0xF1;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xB0 | (NNN >>> 8) );
        memory.RAM[memory.PC + 1] = (byte)(NNN & 0x0FF);

        memory.V[0] = (byte) V0;

        cpu.opcodeBNNN();
        assertEquals(NNN + V0, memory.PC);
    }

    @Test
    public void opcodeBNNNver2() {
        basicInitialization();

        char NNN = 0x0F13;
        char PC = 0x0734;
        char V0 = 0xF1;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xB0 | (NNN >>> 8) );
        memory.RAM[memory.PC + 1] = (byte)(NNN & 0x0FF);

        memory.V[0] = (byte) V0;

        cpu.nextTick();
        assertEquals(NNN + V0, memory.PC);
    }

    @Test
    public void opcodeCXNN() {
        basicInitialization();

        int X = 0xF;
        char PC = 0x0734;

        byte NN = (byte) 0b10101010;
        byte negationOfNN = (byte) ~NN;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xC0 | X );
        memory.RAM[memory.PC + 1] = (byte)(NN & 0xFF);

        cpu.opcodeCXNN();
        int VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);

        cpu.opcodeCXNN();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);

        cpu.opcodeCXNN();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);

        cpu.opcodeCXNN();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);
    }

    @Test
    public void opcodeCXNNver2() {
        basicInitialization();

        int X = 0xF;
        char PC = 0x0734;

        byte NN = (byte) 0b10101010;
        byte negationOfNN = (byte) ~NN;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xC0 | X );
        memory.RAM[memory.PC + 1] = (byte)(NN & 0xFF);

        cpu.nextTick();
        int VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);

        cpu.nextTick();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);

        cpu.nextTick();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);

        cpu.nextTick();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);
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