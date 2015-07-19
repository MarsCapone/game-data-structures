# game-data-structures
A collection of mostly (un)helpful data structures based on games.

### Jenga
Arranged like a jenga tower. 
Each layer can contain at most 3 elements. When adding elements, if the top layer contains fewer than 3 elements, the element is added there, otherwise a new layer is started. 
Any element may be removed as long as it is not in the firs 3 layers. If too many elements are removed from each layer, the entire tower collapses, and all data is lost. If too many elements are removed from near the bottom of the tower, the tower collapses and all elements are lost. 

METHODS:
* add(e): add an element to the top.
* remove(layer, i): remove a specific block from a layer and index from that layer.
* prod(layer, i): check if a specific block can be removed. this sometimes results in collapse.
* destroy(): knock over the tower, and delete all data

ERRORS:
* TowerCollapseError: the tower has fallen over, all data lost
* CheatingAttemptException: you have tried to cheat (by removing blocks that are in the first 3 layers)


### PickupSticks
Arranged like a game of pickup sticks.
Given a group of elements, they are randomly added to a stack. To add an element, all elements are reshuffled. You can remove the top element. Or attempt to remove any other element, but then other sticks would move, so the game is lost, as is all data.

METHODS:
* add(e): add an element.
* add(e[]): add a collection of elements.
* remove(): remove the top element.
* remove(e): if 'e' is the top element, remove it, else, depending on how many sticks have previously been removed, allow, the removal, or delete all data.
* quit(): quit the game: delete all data.

ERRORS:
* StickMovedException: a stick moved, delete all data.


__*If you have any more ideas for games to implement as data structures, please let me know.*__
