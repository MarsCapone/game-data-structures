import java.util.ArrayList;
import java.util.Random;

public class Jenga<E> {

    int stability = 100;
    ArrayList<Layer<E>> layers = new ArrayList<Layer<E>>();
    int layerCount = 0;
    int removedBlocks = 0;

    public Jenga() {

    }

    public boolean add(E e) {
        return true;
    }

    public boolean remove(int layer, int index) throws TowerCollapseError, HandOfGodError, CheatingAttemptException {
        return true;
    }

    public int prod(int layer, int index) {
        return -1;
    }

    public void destroy() throws HandOfGodError {
        layers.clear();
        throw new HandOfGodError();
    }

    private boolean collapseCheck() {
        return CollapseChecker.checkCollapse(stability);
    }

}

class Block<E> {
    public final E data;
    public final int friction;

    public Block(E data) {
        this.data = data;
        this.friction = new Random().nextInt(10 + 1);
    }
}

class Layer<E> {

    Block[] blocks = new Block[3];

    Layer() {
        for (int i = 0; i < 3; i++) {
            blocks[i] = null;
        }
    }

    public boolean add(E e, int i) {
        if (blocks[i] == null) {
            blocks[i] = new Block(e);
            return true;
        }
        return false;
    }

    public int friction(int i) throws NonExistentBlockException {
        if (blocks[i] == null) {
            throw new NonExistentBlockException();
        }
        return blocks[i].friction;
    }

    public E prod(int i) throws NonExistentBlockException {
        if (blocks[i] == null) {
            throw new NonExistentBlockException();
        }
        return (E) blocks[i].data;
    }

    public E remove(int i) throws NonExistentBlockException {
        if (blocks[i] == null) {
            throw new NonExistentBlockException();
        }
        Block block = blocks[i];
        blocks[i] = null;
        return (E) block.data;
    }

    public boolean full() {
        for (int i=0; i<3; i++) {
            if (blocks[i] == null) {
                return false;
            }
        }
        return true;
    }
}

class TowerCollapseError extends Error {
    public TowerCollapseError() {
        super("The tower has become too unstable. Tower collapsed and all data destroyed.");
    }
}

class CheatingAttemptException extends Exception {
    public CheatingAttemptException() {
        super("You have been caught cheating. No removing from the top 3 layers.");
    }
}

class HandOfGodError extends Error {
    public HandOfGodError() {
        super("You have knocked over the tower, all data is lost.");
    }
}

class NonExistentBlockException extends Exception {
    public NonExistentBlockException() {
        super("There is no block there, so you cannot do anything with it.");
    }
}

class CollapseChecker {
    public static boolean checkCollapse(int stability) {
        return randomNumberPicker() == stability + 1;
    }

    private static int randomNumberPicker(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    private static int randomNumberPicker() {
        return randomNumberPicker(0, 100);
    }
}