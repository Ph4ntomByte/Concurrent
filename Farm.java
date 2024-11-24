import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Farm {
    private final Cell[][] grid;
    private final int width;
    private final int height;
    private final List<Sheep> sheep;
    private final List<Dog> dogs;
    public static volatile boolean escaped = false;
    private String escapePoint = "";
  
    public int getWidth() {
      return width;
  }

  public int getHeight() {
      return height;
  }

  public Cell[][] getGrid() {
      return grid;
  }
    public Farm() {
        this.width = 14;
        this.height = 14;
        this.grid = new Cell[height][width];
        this.sheep = new ArrayList<>();
        this.dogs = new ArrayList<>();
        initFarm();
    }

    private void initFarm() {
        Random random = new Random();
        int topGate = 1 + random.nextInt(width - 2);
        int bottomGate = 1 + random.nextInt(width - 2);
        int leftGate = 1 + random.nextInt(height - 2);
        int rightGate = 1 + random.nextInt(height - 2);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(i, j);
                if (i == 0 && j == topGate) {
                    grid[i][j].setType(CellType.GATE);
                } else if (i == height - 1 && j == bottomGate) {
                    grid[i][j].setType(CellType.GATE);
                } else if (j == 0 && i == leftGate) {
                    grid[i][j].setType(CellType.GATE);
                } else if (j == width - 1 && i == rightGate) {
                    grid[i][j].setType(CellType.GATE);
                } else if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    grid[i][j].setType(CellType.WALL);
                }
            }
        }

        for (int i = 1; i <= 10; i++) {
            int x = height / 3 + random.nextInt(height / 3);
            int y = width / 3 + random.nextInt(width / 3);
            Sheep s = new Sheep(this, x, y, String.valueOf((char) ('A' + i - 1)));
            sheep.add(s);
            grid[x][y].setOccupant(s);
        }
        for (int i = 1; i <= 5; i++) {
            int x = random.nextInt(height);
            int y = random.nextInt(width);
            while (grid[x][y].getType() != CellType.EMPTY || (x >= height / 3 && x < 2 * height / 3 && y >= width / 3 && y < 2 * width / 3)) {
                x = random.nextInt(height);
                y = random.nextInt(width);
            }
            Dog d = new Dog(this, x, y, String.valueOf(i));
            dogs.add(d);
            grid[x][y].setOccupant(d);
        }
    }

    public boolean moveEntity(Object entity, int newX, int newY) {
        if (newX < 0 || newY < 0 || newX >= height || newY >= width) return false;
        int currentX, currentY;
        if (entity instanceof Sheep) {
            currentX = ((Sheep) entity).getX();
            currentY = ((Sheep) entity).getY();
        } else if (entity instanceof Dog) {
            currentX = ((Dog) entity).getX();
            currentY = ((Dog) entity).getY();
        } else {
            return false;
        }
        Cell current = grid[currentX][currentY];
        Cell target = grid[newX][newY];

        ReentrantLock currentLock = current.getLock();
        ReentrantLock targetLock = target.getLock();

        boolean locked = false;
        try {
            if (currentLock.tryLock() && targetLock.tryLock()) {
                locked = true;
                if (target.getType() == CellType.WALL) return false;
                if (entity instanceof Sheep && target.getType() == CellType.GATE) {
                    escaped = true;
                    escapePoint = "Sheep escaped through gate at (" + newX + ", " + newY + ")";
                    target.markAsEscapedGate();
                    return false;
                }
                if (target.getOccupant() == null) {
                    target.setOccupant(entity);
                    current.setOccupant(null);
                    if (entity instanceof Sheep) {
                        ((Sheep) entity).setPosition(newX, newY);
                    } else if (entity instanceof Dog) {
                        ((Dog) entity).setPosition(newX, newY);
                    }
                    return true;
                }
            }
        } finally {
            if (locked) {
                targetLock.unlock();
                currentLock.unlock();
            }
        }
        return false;
    }

    public void startSimulation() {
        sheep.forEach(s -> new Thread(s).start());
        dogs.forEach(d -> new Thread(d).start());
        while (!escaped) {
            renderFarm();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("A sheep escaped!");
        System.out.println(escapePoint);
    }

    private void renderFarm() {
        StringBuilder sb = new StringBuilder("\033[H\033[2J\u001B[0;0H");
        sb.append("\u001B[37m#\u001B[0m Wall, \u001B[33mS\u001B[0m Sheep, \u001B[31mD\u001B[0m Dog, \u001B[34mE\u001B[0m Escaped Gate\n");
        sb.append("  +");
        for (int j = 0; j < width; j++) {
            sb.append("-");
        }
        sb.append("+\n");

        for (int i = 0; i < height; i++) {
            sb.append("  |");
            for (int j = 0; j < width; j++) {
                sb.append(grid[i][j].toString());
            }
            sb.append("|  \n");
        }
        sb.append("  +");
        for (int j = 0; j < width; j++) {
            sb.append("-");
        }
        sb.append("+\n");
        System.out.print(sb);
    }
}