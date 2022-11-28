package com.company;

import java.util.HashMap;

public class MinHeap {
    private int[] Heap;
    private int index;
    private int size;
    private HashMap<Integer, Integer> idToIndex;
    private float [] traffic;
    private HashMap<Integer, Integer> idToIndexInTraffic;

    public MinHeap(int size, float [] traffic, HashMap<Integer, Integer> idToIndexInTraffic) {
        super();
        this.size = size;
        this.index = 0;
        this.traffic = traffic;
        Heap = new int[size];
        idToIndex = new HashMap<Integer, Integer>(size);
        this.idToIndexInTraffic = idToIndexInTraffic;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int leftChild(int i) {
        return (i * 2) + 1;
    }

    private int rightChild(int i) {
        return (i * 2) + 2;
    }

    private boolean isLeaf(int i) {
        return rightChild(i) >= size || leftChild(i) >= size;
    }

    public void insert(int id) {
        if (index >= size) {
            return;
        }

        Heap[index] = id;
        int current = index;
        idToIndex.put(id, index);

        while (traffic[idToIndexInTraffic.get(Heap[current])] < traffic[idToIndexInTraffic.get(Heap[parent(current)])] ) {
            int id_c = Heap[current];
            int id_p = Heap[parent(current)];
            idToIndex.put(id_c, parent(current));
            idToIndex.put(id_p, current);
            swap(current, parent(current));
            current = parent(current);
        }
        index++;
    }

    private void swap(int x, int y) {
        int tmp = Heap[x];
        Heap[x] = Heap[y];
        Heap[y] = tmp;
    }

    public int pop() {
        int poppedId = Heap[0];
        Heap[0] = Heap[index-1];
        Heap[--index] = Integer.MAX_VALUE;
        heapDown(0);
//        int returnedIndex = idToIndex.get(poppedId);
        return poppedId;
    }


    private void heapify(int i) {
        if (!isLeaf(i)) {
            if (Heap[i] < Integer.MAX_VALUE/2 && Heap[leftChild(i)] < Integer.MAX_VALUE/2 && Heap[rightChild(i)] < Integer.MAX_VALUE/2 &&
                    (traffic[idToIndexInTraffic.get(Heap[i])] >= traffic[idToIndexInTraffic.get(Heap[leftChild(i)])] ||
                    traffic[idToIndexInTraffic.get(Heap[i])] >= traffic[idToIndexInTraffic.get(Heap[rightChild(i)])])) {
                if (traffic[idToIndexInTraffic.get(Heap[leftChild(i)])]
                        <= traffic[idToIndexInTraffic.get(Heap[rightChild(i)])]) {
                    int id_i = Heap[i];
                    int id_leftChild_i = Heap[leftChild(i)];
                    idToIndex.put(id_i, leftChild(i));
                    idToIndex.put(id_leftChild_i, i);
                    swap(i, leftChild(i));
                    heapify(leftChild(i));
                } else {
                    int id_i = Heap[i];
                    int id_leftChild_i = Heap[leftChild(i)];
                    idToIndex.put(id_i, leftChild(i));
                    idToIndex.put(id_leftChild_i, i);
                    swap(i, rightChild(i));
                    heapify(rightChild(i));
                }
            }
        }
    }

    public void update(int id){

        int c = idToIndex.get(id);
        if (index <= 1)
            return;
        int p = parent(c);
        int new_id = Heap[index-1];
        Heap[c] = Heap[index-1];
        idToIndex.put(new_id, c);
        Heap[index-1] = Integer.MAX_VALUE;
        idToIndex.remove(id);
        index--;

        insert(id);
        heapDown(c);

    }

    private void heapDown(int k){
        while (2 * k +1 < index){
            int lc = leftChild(k);
            int rc = rightChild(k);
            if (2 * k + 2 < index){
                if(traffic[idToIndexInTraffic.get(Heap[k])] < traffic[idToIndexInTraffic.get(Heap[lc])] &&
                        traffic[idToIndexInTraffic.get(Heap[k])] < traffic[idToIndexInTraffic.get(Heap[rc])])
                    break;
                else {
                    if (traffic[idToIndexInTraffic.get(Heap[rc])] < traffic[idToIndexInTraffic.get(Heap[lc])]){
                        int id_k = Heap[k];
                        int id_rc = Heap[rc];
                        idToIndex.put(id_k, rc);
                        idToIndex.put(id_rc, k);
                        swap(rc, k);
                        k = rc;
                    } else {
                        int id_k = Heap[k];
                        int id_lc = Heap[lc];
                        idToIndex.put(id_k, lc);
                        idToIndex.put(id_lc, k);
                        swap(lc, k);
                        k = lc;
                    }
                }
            } else {
                if (traffic[idToIndexInTraffic.get(Heap[k])] < traffic[idToIndexInTraffic.get(Heap[lc])])
                    break;
                else {
                    int id_k = Heap[k];
                    int id_lc = Heap[lc];
                    idToIndex.put(id_k, lc);
                    idToIndex.put(id_lc, k);
                    swap(k, lc);
                    k = lc;
                }
            }
        }
    }

    public boolean isEmpty(){
        return index == 0;
    }
}
