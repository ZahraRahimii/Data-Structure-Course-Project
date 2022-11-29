# Data Structure Course Project
If the map of Tehran city is as shown below, we can save it as a graph. Then we solve our problem, which is finding the shortest path between two arbitrary nodes in this graph.
<p align="center">
<img src="https://user-images.githubusercontent.com/93929227/204488691-e6f63f41-4977-4b05-822d-d9abad0dc991.png" width="70%" height="70%">
<p/>

The most important methods and classes are:
* Vertex class: Initialize every vertex with the x, y, and the id
* Edge class: Define the edge between two vertex with source vertex, destination vertex, amount of traffic and weight
* RootingGraph:
 * dijkstra(): Run the Dijkstra algorithhm for the input source and destination vertex
 ![image](https://user-images.githubusercontent.com/93929227/204491693-490bfb1a-f8e8-4af1-88e6-b8c9fa24b596.png)

* MinHeap: Keep unexplored nodes in a MinHeap so that in the dijkstra algorithm (line 7), the next node would be found in O(1)
 * idToIndexInTraffic: HashMap will help us find the node that is gonna be updated in O(1) with keeping the index of that node in the MinHeap in idToIndexInTraffic.

* ..
