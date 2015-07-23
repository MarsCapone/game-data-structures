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
        pass

    def remove(self, layer, layer_index):
        pass

    def peek(self, layer, layer_index):
        pass

    def friction(self, layer, layer_index):
        pass

    def destroy(self):
        pass

    def _collapse(self):
        pass

    def _check_stability(self):
        pass

    def _check_structure(self, layer, layer_index):
        pass
