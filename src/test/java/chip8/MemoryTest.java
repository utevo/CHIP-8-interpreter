package chip8;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryTest {

    Memory memory;

    @Before
    public void initialize() {
        memory = new Memory();
    }

    @Test
    public void constructor() {
        assertArrayEquals(memory.RAM, new byte[4096]);
        assertArrayEquals(memory.V, new byte[16]);
        assertEquals(memory.I, 0);
        assertEquals(memory.PC, 0);

        assertArrayEquals(memory.stack, new char[16]);
        assertEquals(memory.SP, 0);

        assertEquals(memory.delayTimer, 0);
        assertEquals(memory.soundTimer, 0);
    }

    @Test
    public void clear() {
        memory.RAM[1] = 32;
        memory.RAM[12] = 123;

        memory.PC = 71;

        memory.SP = 3;

        memory.clear();

        assertArrayEquals(memory.RAM, new byte[4096]);
        assertEquals(memory.PC, 0);
        assertEquals(memory.SP, 0);
    }


}