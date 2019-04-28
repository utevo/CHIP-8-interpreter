package chip8;

public class Screen {

    final static int HEIGHT = 32;
    final static int WIDTH = 64;

    private boolean pixels[][] = new boolean[HEIGHT][WIDTH];


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
