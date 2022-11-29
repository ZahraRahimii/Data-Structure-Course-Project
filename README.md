# Data Structure Course Project
The map of Tehran and the graph corresponding to it is as shown below. We want to find the shortest path between two arbitrary nodes in this graph.

<p align="center">
<img src="https://user-images.githubusercontent.com/93929227/204488691-e6f63f41-4977-4b05-822d-d9abad0dc991.png" width="70%" height="70%">
<p/>

The most important methods and classes are:
* Vertex class: Initialize every vertex with the x, y, and the id
* Edge class: Define the edge between two vertexes with source vertex, destination vertex, amount of traffic, and weight
* RootingGraph:
  * initialize(): Initialize the vertexes and edges by reading from the input texts
  * dijkstra(): Run the Dijkstra algorithhm for the input source and destination vertex
<p align="center">
<img src="https://user-images.githubusercontent.com/93929227/204496732-8a81923e-804e-4eb6-9336-27ee43e53f5d.png">
<p/>

* MinHeap: Keep unexplored nodes in a MinHeap so that in the Dijkstra algorithm (line 7), the next node would be found in O(1)
  * idToIndexInTraffic: HashMap will help us find the node that will be updated in O(1) by keeping that node's index in the MinHeap in idToIndexInTraffic.
