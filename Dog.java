import java.util.*;
public class Dog implements Runnable {
    private final Farm farm;
    private String name;
    private int x;
    private int y;

    public Dog(Farm farm, int x, int y, String name) {
        this.farm = farm;
        this.x = x;
        this.y = y;
        this.name = name;
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
        while (!Farm.escaped) {
            int dx = random.nextInt(3) - 1;
            int dy = random.nextInt(3) - 1;
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