import java.util.concurrent.locks.ReentrantLock;

public class Cell {
    private final int x;
    private final int y;
    private CellType type;
    private Object occupant;
    private boolean escapedGate = false;
    private final ReentrantLock lock = new ReentrantLock();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = CellType.EMPTY;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public Object getOccupant() {
        return occupant;
    }

    public void setOccupant(Object occupant) {
        this.occupant = occupant;
    }

    public void markAsEscapedGate() {
        this.escapedGate = true;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        if (escapedGate) return "\u001B[34mE\u001B[0m";
        if (type == CellType.WALL) return "\u001B[37m#\u001B[0m"; 
        if (type == CellType.GATE) return " "; 
        if (occupant instanceof Sheep) return "\u001B[33mS\u001B[0m"; 
        if (occupant instanceof Dog) return "\u001B[31mD\u001B[0m"; 
        return " "; 
    }
}