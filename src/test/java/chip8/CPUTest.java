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

        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
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
        assertEquals(PC + 2, memory.stack[memory.SP]);
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

        cpu.tick();
        assertEquals(NNN, memory.PC);
        assertEquals(oldSP + 1, memory.SP);
        assertEquals(PC + 2, memory.stack[memory.SP]);
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
        cpu.tick();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = notNN; //shouldn't skip
        cpu.tick();
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
        cpu.tick();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = NN; //shouldn't skip
        cpu.tick();
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
        cpu.tick();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;

        memory.V[X] = 0x03;
        memory.V[Y] = 0x73; //shouldn't skip
        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b00101101;
        memory.V[Y] =             0b01001110;
        corectResult =      (byte)0b01111011; // without overflow

        cpu.tick();
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

        cpu.tick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b00101101;
        memory.V[Y] =             0b01001110;
        corectResult =      (byte)0b11011111; // without not borrow

        cpu.tick();
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

        cpu.tick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b00101100;
        corectResult =      (byte)0b00010110;

        cpu.tick();
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

        cpu.tick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[Y] =       (byte)0b00101101;
        memory.V[X] =       (byte)0b01001110;
        corectResult =      (byte)0b11011111; // without not borrow

        cpu.tick();
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

        cpu.tick();
        assertEquals(corectResult, memory.V[X]);
        assertEquals(0x1, memory.V[0xF]);
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] =       (byte)0b01001110;
        corectResult =      (byte)0b10011100;

        cpu.tick();
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

        cpu.tick();
        assertEquals(PC + 2, memory.PC);


        memory.PC = PC;

        memory.V[X] = (byte) 0b10101010;
        memory.V[Y] = (byte) 0b00101010;

        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
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

        cpu.tick();
        int VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);
        memory.PC = PC;

        cpu.tick();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);
        memory.PC = PC;

        cpu.tick();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);
        memory.PC = PC;

        cpu.tick();
        VX = memory.V[X];
        assertEquals(0, VX & negationOfNN);
    }

    @Test
    public void opcodeDXYN() {
//        memory.I = 123;
//        memory.RAM[memory.I] =      (byte) 0b10101010;
//        memory.RAM[memory.I + 1] =  (byte) 0b00000000;
//        memory.RAM[memory.I + 2] =  (byte) 0b01101011;
//        memory.RAM[memory.I + 3] =  (byte) 0b10101010;
//        memory.RAM[memory.I + 4] =  (byte) 0b10101010;
//
//        memory.PC = 0;
//        memory.V[0] = 62;
//        memory.V[1] = 12;
//
//        memory.RAM[0] = (byte) (0xD0 | 0x00);
//        memory.RAM[1] = (byte) (0x10 | 0x05);
    }

    @Test
    public void opcodeEX9E() {
        basicInitialization();

        byte X = 0x9;
        char PC = 0x0734;
        byte VX = 0xA;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xE0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x9E);
        memory.V[X] = VX;

        keyboard.setKeyDown(VX);

        cpu.opcodeEX9E();
        assertEquals(PC + 2, memory.PC);

        memory.PC = PC;
        keyboard.setKeyUp(VX);

        cpu.opcodeEX9E();
        assertEquals(PC + 4, memory.PC);
    }

    @Test
    public void opcodeEX9Ever2() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0234;
        byte VX = 0x1;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xE0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x9E);
        memory.V[X] = VX;

        keyboard.setKeyDown(VX);

        cpu.tick();
        assertEquals(PC + 2, memory.PC);

        memory.PC = PC;
        keyboard.setKeyUp(VX);

        cpu.tick();
        assertEquals(PC + 4, memory.PC);
    }

    @Test
    public void opcodeEXA1() {
        basicInitialization();

        byte X = 0x9;
        char PC = 0x0734;
        byte VX = 0xA;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xE0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0xA1);
        memory.V[X] = VX;

        keyboard.setKeyDown(VX);

        cpu.opcodeEXA1();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;
        keyboard.setKeyUp(VX);

        cpu.opcodeEXA1();
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeEXA1ver2() {
        basicInitialization();

        byte X = 0x9;
        char PC = 0x0734;
        byte VX = 0xA;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xE0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0xA1);
        memory.V[X] = VX;

        keyboard.setKeyDown(VX);

        cpu.tick();
        assertEquals(PC + 4, memory.PC);

        memory.PC = PC;
        keyboard.setKeyUp(VX);

        cpu.tick();
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX07() {
        basicInitialization();

        byte X = 0x9;
        char PC = 0x0734;
        char delayTimer = 123;
        byte VX = 0x12;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x07);
        memory.delayTimer = delayTimer;
        memory.V[X] = VX;

        cpu.opcodeFX07();
        assertEquals(delayTimer, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX07ver2() {
        basicInitialization();

        byte X = 0x1;
        char PC = 0x0334;
        char delayTimer = 0xF3;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x07);
        memory.delayTimer = delayTimer;

        cpu.tick();
        assertEquals(delayTimer, (memory.V[X] & 0xFF) );
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX0A() {
        basicInitialization();

        byte X = 0x7;
        char PC = 0x0334;
        byte VX = (byte) 0x87;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x0A);
        memory.V[X] = VX;

        cpu.opcodeFX0A();
        assertEquals(VX, memory.V[X]);
        assertEquals(PC, memory.PC);


        byte pressedKey = 0xF;
        keyboard.setKeyUp(pressedKey);

        cpu.opcodeFX0A();
        assertEquals(pressedKey, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX0Aver2() {
        basicInitialization();

        byte X = 0x0;
        char PC = 0x0334;
        byte VX = (byte) 0x87;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x0A);
        memory.V[X] = VX;

        cpu.tick();
        assertEquals(VX, memory.V[X]);
        assertEquals(PC, memory.PC);


        byte pressedKey = 0xF;
        keyboard.setKeyUp(pressedKey);

        cpu.tick();
        assertEquals(pressedKey, memory.V[X]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX15() {
        basicInitialization();

        byte X = 0x9;
        char PC = 0x0734;
        byte VX = 0x39;
        char delayTimer = 0x72;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x15);
        memory.V[X] = VX;
        memory.delayTimer = delayTimer;

        cpu.opcodeFX15();
        assertEquals(VX, memory.delayTimer);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX15ver2() {
        basicInitialization();

        byte X = 0x9;
        char PC = 0x0734;
        byte VX = 0x39;
        char delayTimer = 0x72;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x15);
        memory.V[X] = VX;
        memory.delayTimer = delayTimer;

        cpu.tick();
        assertEquals(VX, memory.delayTimer);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX18() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        byte VX = 0x72;
        char soundTimer = 0x72;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x18);
        memory.V[X] = VX;
        memory.soundTimer = soundTimer;

        cpu.opcodeFX18();
        assertEquals(VX, memory.soundTimer);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX18ver2() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        byte VX = 0x72;
        char soundTimer = 0x72;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x18);
        memory.V[X] = VX;
        memory.soundTimer = soundTimer;

        cpu.tick();
        assertEquals(VX, memory.soundTimer);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX1E() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        int VX = 0xF2;
        char I = 0x1213;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x1E);
        memory.I = I;
        memory.V[X] = (byte) VX;

        cpu.opcodeFX1E();
        assertEquals(I + VX, memory.I);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX1Ever2() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        int VX = 0xF2;
        char I = 0x1213;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x1E);
        memory.I = I;
        memory.V[X] = (byte) VX;

        cpu.tick();
        assertEquals(I + VX, memory.I);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX29() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        int VX = 0xE;
        char I = 0x1213;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x29);
        memory.I = I;
        memory.V[X] = (byte) VX;

        cpu.opcodeFX29();
        assertEquals(5 * VX, memory.I);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX29ver2() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        int VX = 0xE;
        char I = 0x1213;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x29);
        memory.I = I;
        memory.V[X] = (byte) VX;

        cpu.tick();
        assertEquals(5 * VX, memory.I);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX33() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        int VX = 123;
        char I = 0x213;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x33);
        memory.I = I;
        memory.V[X] = (byte) VX;

        cpu.opcodeFX33();
        // VX = 123
        assertEquals(1, memory.RAM[I]);
        assertEquals(2, memory.RAM[I + 1]);
        assertEquals(3, memory.RAM[I + 2]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX33ver2() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        int VX = 249;
        char I = 0x0713;

        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x33);
        memory.I = I;
        memory.V[X] = (byte) VX;

        cpu.opcodeFX33();
        // VX = 249
        assertEquals(2, memory.RAM[I]);
        assertEquals(4, memory.RAM[I + 1]);
        assertEquals(9, memory.RAM[I + 2]);
        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX55() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        char I = 0x213;

        byte []V = {
                (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90,
                (byte) 0xF0, (byte) 0x20, (byte) 0x60, (byte) 0x20,
                (byte) 0x20, (byte) 0x70, (byte) 0xF0, (byte) 0x10,
                (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0xF0
        };


        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x55);
        memory.I = I;

        System.arraycopy(V,0, memory.V,0, V.length);

        cpu.opcodeFX55();
        for (int i = 0; i <= X; ++i)
            assertEquals(memory.V[i], memory.RAM[I + i]);

        for (int i = X + 1; i < 16; ++i)
            assertEquals(0, memory.RAM[I + i]);

        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX55ver2() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        char I = 0x213;

        byte []V = {
                (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90,
                (byte) 0xF0, (byte) 0x20, (byte) 0x60, (byte) 0x20,
                (byte) 0x20, (byte) 0x70, (byte) 0xF0, (byte) 0x10,
                (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0xF0
        };


        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x55);
        memory.I = I;

        System.arraycopy(V,0, memory.V,0, V.length);

        cpu.tick();
        for (int i = 0; i <= X; ++i)
            assertEquals(memory.V[i], memory.RAM[I + i]);

        for (int i = X + 1; i < 16; ++i)
            assertEquals(0, memory.RAM[I + i]);

        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX65() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        char I = 0x213;

        byte []data = {
                (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90,
                (byte) 0xF0, (byte) 0x20, (byte) 0x60, (byte) 0x20,
                (byte) 0x20, (byte) 0x70, (byte) 0xF0, (byte) 0x10,
                (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0xF0
        };


        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x65);
        memory.I = I;

        System.arraycopy(data,0, memory.RAM,I, data.length);

        cpu.opcodeFX65();
        for (int i = 0; i <= X; ++i)
            assertEquals(memory.RAM[I + i], memory.V[i]);

        for (int i = X + 1; i < 16; ++i)
            assertEquals(0, memory.V[i]);

        assertEquals(PC + 2, memory.PC);
    }

    @Test
    public void opcodeFX65ver2() {
        basicInitialization();

        byte X = 0xC;
        char PC = 0x0124;
        char I = 0x213;

        byte []data = {
                (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90,
                (byte) 0xF0, (byte) 0x20, (byte) 0x60, (byte) 0x20,
                (byte) 0x20, (byte) 0x70, (byte) 0xF0, (byte) 0x10,
                (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0xF0
        };


        memory.PC = PC;
        memory.RAM[memory.PC] = (byte)(0xF0 | X );
        memory.RAM[memory.PC + 1] = (byte)(0x65);
        memory.I = I;

        System.arraycopy(data,0, memory.RAM,I, data.length);

        cpu.tick();
        for (int i = 0; i <= X; ++i)
            assertEquals(memory.RAM[I + i], memory.V[i]);

        for (int i = X + 1; i < 16; ++i)
            assertEquals(0, memory.V[i]);

        assertEquals(PC + 2, memory.PC);
    }

}