/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Samson Danziger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Constants to determine the Jenga tower setup.
 */
class Constants {

    public static final int STABILITY = 100;
    public static final int STABILITY_DIVIDER_REMOVED = 10; /** The number of blocks to remove before losing a stability point. */
    public static final int STABILITY_DIVIDER_ADDED = 10; /** The number of layers to add before losing a stability point. */

    public static final int LAYER_BLOCKS = 3; /** The number of blocks per layer. */
    public static final boolean[][] LAYER_STABLE = { /** Options for stable layers */
                    {true, true, true},
                    {true, true, false},
                    {true, false, true},
                    {false, true, true},
                  /*{true, false, false},
                    {false, false, true},
                    {false, false, false},*/
                    {false, true, false}
            };
}

/**
 * A data structure organised a bit like a tower of Jenga.
 * Data is added in layers. Each layer can have 3 blocks of data.
 * Each block has a friction constant between 1 and 10, where 10
 * is lots of friction. More friction makes it harder to remove a
 * block of data, without the tower crashing down.
 * Whether the tower will fall or not is random, but it is more
 * likely to fall if the the stability is low, or the friction of
 * the block is high.
 * You are not allowed to remove a block if it is in the top 3
 * layers.
 */
public class Jenga<E> {

    int stability = Constants.STABILITY; // initial stability of the tower.
    private ArrayList<Layer<E>> layers = new ArrayList<Layer<E>>(); // layers of the tower. index 0 is the first layer.
    int height = 0; // the current height of the tower in layers.
    private int removedBlocks = 0; // the number of blocks that have been removed.
    int cheatingChances; // the number of chances for cheating.

    /**
     * A new Jenga tower. By default you have 3 chances to cheat.
     */
    public Jenga() {
        this(3);
    }

    /**
     * A new Jenga tower.
     * @param cheatingChances The number of times you can cheat before you lose all your data.
     */
    public Jenga(int cheatingChances) {
        this.cheatingChances = cheatingChances;
    }

    /**
     * Add a element to the tower.
     * @param e The element to add to the tower.
     * @param lindex The layer index. The position to add it to in the layer.
     * @return True is the add was successful, otherwise false.
     * @throws NonExistentBlockException The layer index did not exist.
     */
    public boolean add(E e, int lindex) throws NonExistentBlockException {
        Layer<E> layer;
        if (layers.isEmpty()) {
            // create the first layer and add
            layer = new Layer<E>();
            layers.add(layer);
            height++;
        }
        layer = layers.get(layers.size());
        if (layer.full()) {
            // create a new layer and add
            layer = new Layer<E>();
            layers.add(layer);
            height++;
        } else {
            // add to the current highest layer
            layer.add(e, lindex);
        }
        boolean successfulAdd = layer.add(e, lindex);
        if (successfulAdd) {
            updateStability();
        }
        return successfulAdd;
    }

    /**
     * Remove a block from the tower.
     * @param layer The index of the layer to remove from, starting with the lowest.
     * @param lindex The layer index for the block to remove.
     * @return The data that was removed.
     * @throws TowerCollapseError You have expended all your chances for cheating, or the tower was too unstable, or the friction for the chosen block was too high.
     * @throws CheatingAttemptException You have attempted to remove a block in the first 3 layers.
     * @throws NonExistentBlockException You have attempted to remove a block at an index that does not exit.
     */
    public E remove(int layer, int lindex) throws TowerCollapseError, CheatingAttemptException, NonExistentBlockException {
        if (height - layer <= 3) {
            // cheating
            if (cheatingChances == 0) {
                // used up all chances
                throw new TowerCollapseError("You have no chances left with which to cheat. You are disqualified. The tower has fallen over. All data is lost.");
            } else {
                // lose a chance
                cheatingChances--;
                throw new CheatingAttemptException(cheatingChances);
            }
        }
        Layer<E> chosenLayer = layers.get(layer);
        int blockFriction = chosenLayer.friction(lindex);
        boolean stableCollapse = collapseCheck(blockFriction);
        boolean obviousCollapse = chosenLayer.checkFeasibility(lindex);
        E data = chosenLayer.remove(lindex);
        if (stableCollapse || obviousCollapse) {
            // collapse tower if too unstable or not structurally sound
            collapse();
        }
        removedBlocks++;
        updateStability();
        return data;
    }

    /**
     * Find out the data for a specific block.
     * @param layer The index of the layer starting from the bottom.
     * @param lindex The index of the block.
     * @return The data stored in the block.
     * @throws NonExistentBlockException There is no block at the specified index.
     */
    public E peek(int layer, int lindex) throws NonExistentBlockException {
        Layer<E> chosenLayer = layers.get(layer);
        return chosenLayer.peek(lindex);
    }

    /**
     * Find out the friction for a specific block.
     * @param layer The index of the layer starting from the bottom.
     * @param lindex The index of the block.
     * @return The friction value of the block. This is in the range 1..10.
     * @throws NonExistentBlockException There is no block at the specified index.
     */
    public int friction(int layer, int lindex) throws NonExistentBlockException {
        Layer<E> chosenLayer = layers.get(layer);
        return chosenLayer.friction(lindex);
    }

    /**
     * Get a specific Block object.
     * @param layer The index if the layer starting from the bottom.
     * @param lindex The index of the block.
     * @return The Block at that location.
     * @throws NonExistentBlockException There is no block at the specified index.
     */
    public Block getBlock(int layer, int lindex) throws NonExistentBlockException {
        Layer<E> chosenLayer = layers.get(layer);
        return chosenLayer.getBlock(lindex);
    }

    /**
     * Knock down the tower.
     * @throws HandOfGodError You have knocked down the tower.
     */
    public void destroy() throws HandOfGodError {
        layers.clear();
        throw new HandOfGodError();
    }

    /**
     * Get an iterator for the layers starting from the bottom.
     * @return An iterator for the layers.
     */
    public Iterator<Layer<E>> layerIterator() {
        return layers.iterator();
    }

    /**
     * Print a representation of the tower.
     * @return A string containing a representation of the tower.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Layer<E> layer : layers) {
            boolean[] layout = layer.getLayout();
            for (boolean b : layout) {
                if (b) {
                    sb.append('#');
                } else {
                    sb.append('0');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    /**
     * The tower collapses.
     * @throws TowerCollapseError The tower has collapsed.
     */
    private void collapse() throws TowerCollapseError {
        layers.clear();
        throw new TowerCollapseError();
    }

    /**
     * Check if a collapse will happen.
     * @param friction The friction of the block to check for.
     * @return True if the tower will collapse, otherwise false.
     */
    private boolean collapseCheck(int friction) {
        return CollapseChecker.checkCollapse(stability, friction);
    }

    /**
     * Update the stability value.
     */
    private void updateStability() {
        stability = Constants.STABILITY - (removedBlocks/Constants.STABILITY_DIVIDER_REMOVED) - (height/ Constants.STABILITY_DIVIDER_ADDED);
    }

}

/**
 * Blocks make up Jenga towers.
 */
class Block<E> {
    public final E data;
    public final int friction;

    /**
     * An individual block for a Jenga tower.
     * @param data The data to store.
     */
    public Block(E data) {
        this.data = data;
        // the friction value is randomly assigned between 1 and 10.
        this.friction = new Random().nextInt(10) + 1;
    }
}

/**
 * In Jenga, the layers are stacked on top of one another. Layers contain blocks.
 */
class Layer<E> {

    Block[] blocks;

    Layer() {
        blocks = new Block[Constants.LAYER_BLOCKS];
        for (int i = 0; i < Constants.LAYER_BLOCKS; i++) {
            blocks[i] = null;
        }
    }

    /**
     * Add a piece of data to a layer.
     * @param e The data to add.
     * @param i The index to add the data to.
     * @return True if the data was added successfully, else false.
     * @throws NonExistentBlockException The specified index is out of bounds.
     */
    public boolean add(E e, int i) throws NonExistentBlockException {
        if (i >= Constants.LAYER_BLOCKS || i < 0) {
            throw new NonExistentBlockException();
        }
        if (blocks[i] == null) {
            blocks[i] = new Block<E>(e);
            return true;
        }
        return false;
    }

    /**
     * Get the friction for a specific block.
     * @param i The index of the block.
     * @return The friction value of the block. Values in range 1..10.
     * @throws NonExistentBlockException The specified index is out of bounds.
     */
    public int friction(int i) throws NonExistentBlockException {
        if (blocks[i] == null || i >= Constants.LAYER_BLOCKS || i < 0) {
            throw new NonExistentBlockException();
        }
        return blocks[i].friction;
    }

    /**
     * Peek at the data for a specific block.
     * @param i The index of the block.
     * @return The daa stored in the block.
     * @throws NonExistentBlockException The specified index is out of bounds.
     */
    public E peek(int i) throws NonExistentBlockException {
        if (blocks[i] == null || i >= Constants.LAYER_BLOCKS || i < 0) {
            throw new NonExistentBlockException();
        }
        return (E) blocks[i].data;
    }

    /**
     * Remove a block from the layer.
     * @param i The index of the block.
     * @return The data of the block that was removed.
     * @throws NonExistentBlockException The specified index is out of bounds.
     */
    public E remove(int i) throws NonExistentBlockException {
        if (blocks[i] == null || i >= Constants.LAYER_BLOCKS || i < 0) {
            throw new NonExistentBlockException();
        }
        Block block = blocks[i];
        blocks[i] = null;
        return (E) block.data;
    }

    /**
     * Get the layout of the layer.
     * @param print Whether or not to also print out the layout of the layer.
     * @return List of booleans, which show false when the is no block, and true when there is.
     */
    public boolean[] getLayout(boolean print) {
        boolean[] layout = new boolean[Constants.LAYER_BLOCKS];
        for (int i = 0; i < Constants.LAYER_BLOCKS; i++) {
            if (blocks[i] == null) {
                layout[i] = false;
            } else {
                layout[i] = true;
            }
        }
        if (print) {
            System.out.println(layout);
        }
        return layout;
    }

    public boolean[] getLayout() {
        return getLayout(false);
    }

    /**
     * Check if removing a block is structurally sound. Only works if Constants.LAYER_BLOCKS is 3.
     * @param i The index of the block which will be removed.
     * @return True if the layer will still be structurally sound after the removal of the block, else false.
     */
    public boolean checkFeasibility(int i) {
        boolean[] layout = getLayout();
        layout[i] = !layout[i];
        for (boolean[] stableLayout : Constants.LAYER_STABLE) {
            return layout == stableLayout;
        }
        return false;
    }

    /**
     * Check if the layer is full.
     * @return True if the layer is full, otherwise false.
     */
    public boolean full() {
        for (int i=0; i<Constants.LAYER_BLOCKS; i++) {
            if (blocks[i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a specific block.
     * @param i The index of the block.
     * @return The block.
     * @throws NonExistentBlockException The specified index is out of bounds.
     */
    public Block getBlock(int i) throws NonExistentBlockException {
        if (i >= Constants.LAYER_BLOCKS || i < 0) {
            throw new NonExistentBlockException();
        }
        return blocks[i];
    }
}

/**
 * The tower has fallen down.
 */
class TowerCollapseError extends Error {
    public TowerCollapseError() {
        super("The tower has become too unstable. Tower collapsed and all data destroyed.");
    }

    public TowerCollapseError(String message) {
        super(message);
    }
}

/**
 * Cheating was attempted by removing a block in the first 3 layers.
 */
class CheatingAttemptException extends Exception {
    public CheatingAttemptException(int chancesRemaining) {
        super(String.format("You have been caught cheating. No removing from the top 3 layers. You have %d chances remaining.", chancesRemaining));
    }
}

/**
 * You knocked the tower over.
 */
class HandOfGodError extends Error {
    public HandOfGodError() {
        super("You have knocked over the tower, all data is lost.");
    }
}

/**
 * You reached for a block that didn't exist.
 */
class NonExistentBlockException extends Exception {
    public NonExistentBlockException() {
        super("There is no block there, so you cannot do anything with it.");
    }
}

/**
 * Check for tower collapses.
 */
class CollapseChecker {
    /**
     * Check if a tower collapses.
     * @param stability The stability of the tower.
     * @param friction The friction of the block being pulled.
     * @return True if the tower collapses, else false.
     */
    public static boolean checkCollapse(int stability, int friction) {
        return randomNumberPicker(stability) <= friction;
    }

    /**
     * Pick a random number in a range (inclusive).
     * @param min The minimum number.
     * @param max The maximum number.
     * @return A number in the range min..max.
     */
    private static int randomNumberPicker(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    /**
     * Pick a random number in the range 1..max.
     * @param max The maximum number.
     * @return A number in the range 1..max.
     */
    private static int randomNumberPicker(int max) {
        return randomNumberPicker(1, max);
    }
}