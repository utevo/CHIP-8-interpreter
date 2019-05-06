package chip8;

public class Screen {

    public final static int HEIGHT = 32;
    public final static int WIDTH = 64;

    private boolean pixels[][] = new boolean[WIDTH][HEIGHT];


    public void setPixel(int x, int y) {
        pixels[x][y] = true;
    }

    public void flipPixel(int x, int y) {
        if (pixels[x][y] == true)
            pixels[x][y] = false;
        else
            pixels[x][y] = true;
    }

    public boolean getPixel(int x, int y) {
        return pixels[x][y];
    }



}
