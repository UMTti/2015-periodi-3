/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 *
 * @author pihla
 */
public class HuffmanCoder {

    /**
     * Blocks
     */
    public double[][][][] blocks;

    /**
     * Size of the x-axel
     */
    public int x;

    /**
     * Size of the y-axel
     */
    public int y;

    private static class Node {

        public int frequency;
        int value;
        Node left;
        Node right;

        public Node(int value, int frequency, Node left, Node right) {
            this.frequency = frequency;
            this.value = value;
            this.left = left;
            this.right = right;
        }
        
        /**
         * Checks if node is leaf 
        */

        private boolean isLeaf() {
            if(this.right == null && this.left == null){
                return true;
            } else {
                return false;
            }
        }
    }

    private static class HeapComparator implements Comparator<Node> {

        @Override
        public int compare(Node x, Node y) {

            if (x.frequency < y.frequency) {
                return -1;
            }
            if (x.frequency > y.frequency) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Object which handles operations related to huffman coding
     *
     * @param blocks
     * @param x
     * @param y
     */
    private String[] koodiarvot;
    public HuffmanCoder(double[][][][] blocks, int x, int y) {
        this.blocks = blocks;
        this.x = x;
        this.y = y;
        this.koodiarvot = new String[2*x*y*3]; // x*y*3 on nolla
    }

    /**
     * General "main" function which calls makeFreqsTable and constructs the tree of
     * Nodes
     */
    public void makeHuffmanCoding() throws FileNotFoundException, IOException {
        int[] frequencies = makeFreqsTable(this.blocks);
        PriorityQueue<Node> minimumheap = makeMinimumHeap(frequencies);
        while (minimumheap.size() >= 2) {
            Node eka = minimumheap.remove();
            Node toka = minimumheap.remove();
            int freqsum = eka.frequency + toka.frequency;
            Node uusi = new Node(9000, freqsum, eka, toka);
            minimumheap.add(uusi);
        }

        Node vika = minimumheap.remove();
        buildKoodiarvot(vika, "");
        writeToFile(vika);
 
    }

    /**
     * Makes table of frequencies. x*y*3 is zero in values
     *
     * @param blocks
     * @return
     */
    public int[] makeFreqsTable(double[][][][] blocks) {
        HashSet<Integer> erilaiset = new HashSet<Integer>();
        x = this.x;
        y = this.y;
        int nollat = 0;
        int[] frequencies = new int[2 * x * y * 3];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                for (int k = 0; k < blocks[i][j].length; k++) {
                    for (int z = 0; z < 3; z++) {
                        double value = blocks[i][j][k][z];
                        int intti = (int) value;
                        if (intti == 0) {
                            continue;
                        }
                        frequencies[intti + x * y * 3] += 1;
                        erilaiset.add(intti);
                    }
                }
            }
        }
        return frequencies;
    }

    /**
     * Makes minimum heap of frequencies -> heap sort can be applied later
     *
     * @param frequencies
     * @return
     */
    public PriorityQueue<Node> makeMinimumHeap(int[] frequencies) {
        Comparator<Node> comparator = new HeapComparator();
        PriorityQueue<Node> minimumheap = new PriorityQueue<Node>(2 * 3 * this.x * this.y, comparator);
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] == 0) {
                continue;
            }
            int[] pair = new int[2];
            int value = i - this.x * this.y * 3;
            int frequency = frequencies[i];
            Node uusi = new Node(value, frequency, null, null);
            minimumheap.add(uusi);
        }
        return minimumheap;
    }
    
    public void buildKoodiarvot(Node n, String s) {
        if (! n.isLeaf()) {
            buildKoodiarvot(n.left,  s + '0');
            buildKoodiarvot(n.right, s + '1');
        }
        else {
            this.koodiarvot[n.value + x*y*3] = s;
        }
    }
    
    /**
     * Number 255 is added for every value, so everything is positive > 
     * @param n
     * @param os
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void writeTrie(Node n, DataOutputStream os) throws FileNotFoundException, IOException{
        if (n.isLeaf()) {
            os.writeBoolean(true);
            BigInteger arvo = BigInteger.valueOf((long)n.value + 255);
            os.write(arvo.byteValue());
            return;
        }
        os.writeBoolean(false);
        writeTrie(n.left, os);
        writeTrie(n.right, os);
    }
    
    public void writeValues(DataOutputStream os) throws FileNotFoundException, IOException{
        
    }
    
    public void writeToFile(Node vika) throws FileNotFoundException, IOException{
        String filename = "tulos.gpeg";
        DataOutputStream os = new DataOutputStream(new FileOutputStream(filename));
        writeTrie(vika, os);
        writeValues(os);
    }
    
    

}
