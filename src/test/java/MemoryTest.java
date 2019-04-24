import org.junit.Test;

import javax.swing.plaf.synth.SynthEditorPaneUI;

import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void Constructor() throws Exception {
        Memory memory = new Memory();

        assertArrayEquals(memory.getRAM(), new byte[4096]);
        assertArrayEquals(memory.getV(), new byte[16]);
        assertEquals(memory.getI(), 0);
        assertEquals(memory.getPC(), 0);

        assertArrayEquals(memory.getStack(), new char[16]);
        assertEquals(memory.getSP(), 0);

        assertEquals(memory.getDelayTimer(), 0);
        assertEquals(memory.getSoundTimer(), 0);
    }

    @Test
    public void setRAM() throws Exception {
        Memory memory = new Memory();

        byte tempRAM[] = memory.getRAM();
        tempRAM[0] = 0x01;
        tempRAM[1] = 0x23;

        memory.setRAM(tempRAM);

        assertEquals(memory.getRAM(), tempRAM);
    }

    @Test
    public void setV() throws Exception {
        Memory memory = new Memory();

        byte tempV[] = memory.getV();
        tempV[0] = 0x45;
        tempV[1] = 0x67;

        memory.setV(tempV);

        assertEquals(memory.getV(), tempV);
    }

    @Test
    public void setStack() throws Exception {
        Memory memory = new Memory();

        char tempStack[] = memory.getStack();
        tempStack[0] = 0x89;
        tempStack[1] = 0xAB;

        memory.setStack(tempStack);

        assertEquals(memory.getStack(), tempStack);
    }


}