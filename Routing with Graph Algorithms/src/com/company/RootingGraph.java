package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class RootingGraph {

    public int n, m;
    public LinkedList<Edge>[] adjList;
    public HashMap<Integer, Integer> idToIndexInAdjList;
    public int[][] startTime;
    public float[] request_time;
    public int[][] stored_prevs;
    public float[] calculated_time;
    public Vertex[] vertexes;
    public static int prev_counter = 0;
    public int[] destinations;

    public RootingGraph(){

        initialize();

    }

    public void initialize(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("m2.txt"));
            String line;
            int counter = 0;
            while ((line = br.readLine()) != null){
                if (counter == 0){
                    n = Integer.parseInt(line.split(" ")[0]);
                    m = Integer.parseInt(line.split(" ")[1]);
                    init_fields();
                } else if (counter <= n){
                    int id = Integer.parseInt(line.split(" ")[0]);
                    float x = Float.parseFloat(line.split(" ")[1]);
                    float y = Float.parseFloat(line.split(" ")[2]);
                    vertexes[counter-1] = new Vertex(id, x, y);
                    idToIndexInAdjList.put(id, counter-1);
                } else {
                    int src_id = Integer.parseInt(line.split(" ")[0]);
                    int dst_id = Integer.parseInt(line.split(" ")[1]);
                    addEdge(src_id, dst_id);
                }
                counter++;
            }

        } catch (IOException | NumberFormatException e){
            e.printStackTrace();
        }

    }

    private void init_fields(){
        vertexes = new Vertex[n];
        adjList = new LinkedList[n];
        idToIndexInAdjList = new HashMap<>(n);
        startTime = new int[n][n];
        request_time = new float[1000];
        destinations = new int[1000];
        stored_prevs = new int[1000][n];
        calculated_time = new float[1000];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjList[i] = new LinkedList<>();
            }
        }
    }

    public void addEdge(int sourceId, int destinationId){

        Vertex src = vertexes[idToIndexInAdjList.get(sourceId)];
        Vertex dst = vertexes[idToIndexInAdjList.get(destinationId)];
        Edge edge = new Edge(src, dst);
        adjList[idToIndexInAdjList.get(sourceId)].addFirst(edge);
        adjList[idToIndexInAdjList.get(destinationId)].addFirst(edge);

    }

    public void getInput(){
        int j = 0;

        while (true) {
            Scanner sc = new Scanner(System.in);
            String t = sc.next();
            float time = Float.parseFloat(t);
            int start = sc.nextInt();
            int destination = sc.nextInt();
            request_time[j] = time;
            dijkstra4(start, destination, j);
            j++;
        }
    }

    public void dijkstra4(int sourceId, int dstId, int t){
        int tt = t;
        int sourceIndex = idToIndexInAdjList.get(sourceId);
        destinations[t] = dstId;

        while (tt >= 1 && request_time[t] > calculated_time[tt-1]) {
            int [] prev = stored_prevs[tt-1];
            for (int i = idToIndexInAdjList.get(destinations[tt-1]); prev[i] != -2; i = prev[i]) {

                LinkedList<Edge> list = adjList[idToIndexInAdjList.get(vertexes[i].id)];
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).traffic > 0) {
                        list.get(j).traffic--;
                    }
                }
            }
            tt--;
        }
        int [] prev = new int[n];
        float [] dist = new float[n];
        MinHeap heap = new MinHeap(n, dist, idToIndexInAdjList);
        stored_prevs[prev_counter++] = prev;

        dist[sourceIndex] = 0;

        for (int i = 0; i < n; i++) {
            if (i != sourceIndex){
                dist[i] = (float) Integer.MAX_VALUE / 3;
                prev[i] = -1;
            }
            heap.insert(vertexes[i].id);
        }

        while (!heap.isEmpty()){
            int id = heap.pop();
            int u = idToIndexInAdjList.get(id);
            LinkedList<Edge> list = adjList[u];

            for (int i = 0; i < list.size(); i++) {
                float a = dist[u];
                float b = list.get(i).weight;
                float alt = a + b;
                int c = idToIndexInAdjList.get(list.get(i).dst.id);
                if (list.get(i).dst.id == id)
                    c = idToIndexInAdjList.get(list.get(i).src.id);

                if (alt < dist[c]){
                    dist[c] = alt;
                    prev[c] = u;
                    int idd = vertexes[c].id;
                    heap.update(idd);
                }

            }
        }

        prev[sourceIndex] = -2;
        for (int i = idToIndexInAdjList.get(dstId); prev[i] != -2;  i = prev[i]) {

            LinkedList<Edge> list = adjList[idToIndexInAdjList.get(vertexes[i].id)];
            for (int j = 0; j < list.size(); j++) {
                if ((list.get(j).src.id == vertexes[i].id && list.get(j).dst.id == vertexes[prev[i]].id)
                        || (list.get(j).dst.id == vertexes[i].id && list.get(j).src.id == vertexes[prev[i]].id)) {
                    list.get(j).traffic++;
                }
            }

        }

        // update weights
        for (int i = 0; i < vertexes.length; i++) {
            LinkedList<Edge> l = adjList[idToIndexInAdjList.get(vertexes[i].id)];
            for (int j = 0; j < l.size(); j++) {
                l.get(j).updateWeight();

            }
        }

        System.out.println("The shortest path contains nodes:");
        ArrayList<Integer> shortestPath = new ArrayList<>();
        shortestPath.add(vertexes[idToIndexInAdjList.get(dstId)].id);
        for (int i = idToIndexInAdjList.get(dstId); prev[i] >= 0 ; i = prev[i]) {
            shortestPath.add(vertexes[prev[i]].id);
        }
        for (int i = shortestPath.size()-1; i >= 0 ; i--) {
            System.out.print(shortestPath.get(i) + "  ");
        }


        calculated_time[t] = 120 * dist[idToIndexInAdjList.get(dstId)] ;
        System.out.println("\nThe duration time between source Vertex:  " + vertexes[sourceIndex].id
                + " to vertex " + dstId + " is : " +  calculated_time[t]);

    }


    public static void main (String[] args){

        RootingGraph graph = new RootingGraph();

        graph.getInput();

    }
}


class Edge{
    public Vertex src;
    public Vertex dst;
    public int traffic;
    public float weight;

    public Edge(Vertex src, Vertex dst) {
        this.src = src;
        this.dst = dst;
        weight = (float) (getLength(src.x, src.y, dst.x, dst.y));
        traffic = 0;
    }

    public void updateWeight(){
        weight = (float) (getLength(src.x, src.y, dst.x, dst.y) * (1 + 0.3 * traffic));

    }

    public double getLength(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));

    }

}

class Vertex{
    public int id;
    public float x;
    public float y;

    public Vertex(int id, float x, float y){
        this.id = id;
        this.x = x;
        this.y = y;
    }
}