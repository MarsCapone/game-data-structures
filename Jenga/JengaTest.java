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

import junit.framework.TestCase;
import org.junit.Test;

public class JengaTest extends TestCase {

    protected Jenga<Integer> jenga;
    protected boolean ex;

    @Override
    protected void setUp() {
        jenga = new Jenga<Integer>();
        ex = false;
    }

    @Test
    public void testAddFirst() throws NonExistentBlockException {
        boolean oneAdd = jenga.add(1, 0);
        assertTrue("Adding the first element should succeed.", oneAdd);
        boolean secondAdd = jenga.add(2);
        assertTrue("Next element should be added in next available space.", secondAdd);
    }

    @Test
    public void testMultipleAdd() throws NonExistentBlockException {
        int blocksToAdd = 3 * Constants.LAYER_BLOCKS;
        for (int i = 0; i < blocksToAdd; i++) {
            jenga.add(i);
        }
        assertTrue(String.format("Adding %d blocks should create 3 layers, each with %d blocks.", blocksToAdd, Constants.LAYER_BLOCKS), jenga.height == 3);
    }

    @Test
    public void testNonExistentBlockException() {
        try {
            jenga.add(1, 4);
        } catch (NonExistentBlockException e) {
            ex = true;
        }
        assertTrue("Exception should be thrown when adding an element to the 5th space out of 3.", ex);
    }

    @Test
    public void testHandOfGodError() {
        boolean ex = false;
        try {
            jenga.destroy();
        } catch (HandOfGodError e) {
            ex = true;
        }
        assertTrue("Error should be thrown when deliberately knocking down the tower.", ex);
    }

}
