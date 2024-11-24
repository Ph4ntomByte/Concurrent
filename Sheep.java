import java.util.Random;

public class Sheep implements Runnable {
    private final Farm farm;
    private String name;
    private int x;
    private int y;

    public Sheep(Farm farm, int x, int y, String name) {
        this.farm = farm;
        this.x = x;
        this.y = y;
        this.name = name.toUpperCase();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {
        Random random = new Random();
        Thread.currentThread().setName(name); 
        while (!Farm.escaped) {
            int dx = 0, dy = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nx = x + i, ny = y + j;
                    if (nx >= 0 && ny >= 0 && nx < farm.getHeight() && ny < farm.getWidth() && farm.getGrid()[nx][ny].getOccupant() instanceof Dog) {
                        dx -= i;
                        dy -= j;
                    }
                }
            }
            if (dx == 0 && dy == 0) {
                dx = random.nextInt(3) - 1;
                dy = random.nextInt(3) - 1;
            }
            farm.moveEntity(this, x + dx, y + dy);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
