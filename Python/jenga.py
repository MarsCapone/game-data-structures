"""
    The MIT License (MIT)

    Copyright (c) 2015 Samson Danziger

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
"""

global LAYER_BLOCKS = 3
global STABLE_LAYERS = [
    [1, 1, 1],
    [1, 1, 0],
    [1, 0, 1],
    [0, 1, 1],
    [0, 1, 0]
]
global STABILITY = 100

class NonExistentBlockException(Exception):
    def __str__(self):
        return repr("That block does not exist.")

class HandOfGodError(Exception):
    def __str__(self):
        return repr("You have knocked down the tower.")

class TowerCollapseError(Exception):
    def __str__(self):
        return repr("The tower has become too unstable and has collapsed.")

class CheatingAttemptExeption(Exception):
    def __init__(self, chances):
        self.chances = chances

    def __str__(self):
        return repr("You attempted to cheat, you have %d chances remaining." % self.chances)

class _Block(object):
    def __init__(self, data):
        self.data = data
        self.friction = random.randint(1, 10)

    def getData(self):
        return self.data

    def getFriction(self):
        return self.friction

class _Layer(object):
    blocks = []

    def __init__(self):
        for i in range(LAYER_BLOCKS):
            self.blocks.append(None)

    def add(self, data, index):
        if index not in range(LAYER_BLOCKS):
            raise NonExistentBlockException
        if blocks[index] != None:
            return False
        else:
            blocks[index] = data
            return True

    def remove(self, index):
        if index not in range(LAYER_BLOCKS) or blocks[index] == None:
            raise NonExistentBlockException
        d = blocks[index]
        blocks[index] = None
        return d.getData();

    def peek(self, index):
        if index not in range(LAYER_BLOCKS) or blocks[index] == None:
            raise NonExistentBlockException
        d = blocks[index]
        return d.getData()

    def friction(self, index):
        if index not in range(LAYER_BLOCKS) or blocks[index] == None:
            raise NonExistentBlockException
        d = blocks[index]
        return f.getFriction()

class Jenga(object):
    layers = []
    height = 0
    removed_blocks = 0
    stability = STABILITY

    def add(self, data, layer_index=None):
        """ Add some data to the tower. If layer index is specified, data is added at that index, otherwise data is added in next available space.
        returns:
            True if the data is added successfully.
            False if the data is not added.
        raises:
        """
        pass

    def remove(self, layer, layer_index):
        """ Removes the block at the specified layer and layer index.
        returns:
            The data of the block.
        raises:
            NonExistentBlockException if the the specified block does not exist.
            TowerCollapseError if removing the block causes the tower to collapse.
        """
        pass

    def peek(self, layer, layer_index):
        """ Look at the data at the spcified layer and layer index.
        returns:
            The data of the block.
        raises:
            NonExistentBlockException if the specified block does not exist.
        """
        pass

    def friction(self, layer, layer_index):
        """ Test the friction for the block at the specified index. Friction can be in the range 1..10.
        returns:
            INT between 1 and 10 (inclusive) for the block at the specified location.
        raises:
            NonExistentBlockException if the specified block does not exit.
        """
        pass

    def destroy(self):
        """ Knock over the tower.
        returns:
            void
        raises:
            HandOfGodError
        """
        self.layers = []
        height = 0
        removed_blocks = 0
        stability = STABILITY
        raise HandOfGodError

    def _collapse(self):
        """ The tower falls over.
        returns:
            void
        raises:
            TowerCollapseError
        """
        self.layers = []
        height = 0
        removed_blocks = 0
        stability = STABILITY
        raise TowerCollapseError


    def _check_stability(self):
        """ Check the stability of the tower based on 
        pass

    def _check_structure(self, layer, remove_layer_index):
        pass
