package chip8;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CHIP8 {

    @Getter @Setter
    private CPU cpu;


    public CHIP8() {
        cpu = new CPU(new Memory(), new Keyboard(), new Screen());
    }

    public CHIP8(CPU cpu) {
        this.cpu = cpu;
    }

    public void timersTick() {
        cpu.timersTick();
    }

    public void cpuTick() throws IllegalStateException {
        cpu.tick();
    }

    public void loadProgram(File file) {
        cpu.getScreen().clear();

        try {
            DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

            byte[] programAsArrayOfByte = new byte[(int) file.length()];
            input.read(programAsArrayOfByte);

            cpu.getMemory().loadProgram(programAsArrayOfByte);

            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveState(File file) throws IOException {
        String json = cpu.toString();
        FileUtils.writeStringToFile(file, json);
    }

    public void loadState(File file) throws IOException {
        String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        cpu = new CPU(json);
    }
}
