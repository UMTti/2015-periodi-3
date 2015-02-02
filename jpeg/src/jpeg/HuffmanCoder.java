/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.util.*;

/**
 *
 * @author pihla
 */
public class HuffmanCoder {

    /**
     *
     */
    public double[][][][] blocks;
    public int x;
    public int y;

    private static class Node {

        int frequency;
        int value;
        Node left;
        Node right;

        public Node(int value, int frequency, Node left, Node right, int x, int y) {
            this.frequency = frequency;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    /**
     *
     * @param blocks
     */
    public HuffmanCoder(double[][][][] blocks, int x, int y) {
        this.blocks = blocks;
        this.x = x;
        this.y = y;
    }

    public void makeHuffmanCoding() {
        int[] frequencies = makeFreqsTable(this.blocks);
        PriorityQueue<int[]> minimumheap = makeMinimumHeap(frequencies);
        while(!minimumheap.isEmpty()){
            int[] pair = minimumheap.remove();
            System.out.println("keon pienin: " + pair[0] + " " + pair[1]);
        }
    }

    /**
     * Makes table of frequencies. x*y*3 is zero in values
     *
     * @param blocks
     * @param x
     * @param y
     */
    public int[] makeFreqsTable(double[][][][] blocks) {
        x = this.x;
        y = this.y;
        int[] frequencies = new int[2 * x * y * 3];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                for (int k = 0; k < blocks[i][j].length; k++) {
                    for (int z = 0; z < 3; z++) {
                        double value = blocks[i][j][k][z];
                        int intti = (int) value;
                        frequencies[intti + x * y * 3] += 1;
                    }
                }
            }
        }
        return frequencies;
    }
    
    public PriorityQueue<int[]> makeMinimumHeap(int[] frequencies){
        Comparator<int[]> comparator = new HeapComparator();
        PriorityQueue<int[]> minimumheap = new PriorityQueue<int[]>(2*3*this.x*this.y, comparator);
        for(int i = 0;i<frequencies.length;i++){
            if(frequencies[i] == 0){
                continue;
            }
            int[] pair = new int[2];
            pair[0] = i - this.x * this.y * 3;
            pair[1] = frequencies[i];
            minimumheap.add(pair);
        }
        return minimumheap;
    }

}

class HeapComparator implements Comparator<int[]> {
    @Override
    public int compare(int[] x, int[] y) {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (x[1] > y[1]) {
            return -1;
        }
        if (x[1] < y[1]) {
            return 1;
        }
        return 0;
    }
}
