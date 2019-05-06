package chip8;

public class Screen {

    public final static int HEIGHT = 32;
    public final static int WIDTH = 64;

    private boolean pixels[][] = new boolean[HEIGHT][WIDTH];


    public void setPixel(int x, int y) {
        pixels[y][x] = true;
    }

    public void flipPixel(int x, int y) {
        if (pixels[y][x] == true)
            pixels[y][x] = false;
        else
            pixels[y][x] = true;
    }

    public boolean getPixel(int x, int y) {
        return pixels[y][x];
    }



}
