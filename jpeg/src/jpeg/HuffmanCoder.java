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
            if (this.right == null && this.left == null) {
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
        this.koodiarvot = new String[2 * x * y * 3]; // x*y*3 on nolla
    }

    /**
     * General "main" function which calls makeFreqsTable and constructs the
     * tree of Nodes
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
        if (!n.isLeaf()) {
            buildKoodiarvot(n.left, s + '0');
            buildKoodiarvot(n.right, s + '1');
        } else {
            this.koodiarvot[n.value + x * y * 3] = s;
        }
    }

    /**
     * Number 255 is added for every value, so everything is positive >
     *
     * @param n
     * @param os
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void writeTrie(Node n) throws FileNotFoundException, IOException {
        if (n.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(n.value + 255, 9); // 9 bittiä, koska jos joku on esim alussa 200 nii menee yli 255 lisäyksessä
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(n.left);
        writeTrie(n.right);
    }

    public void writeValues() throws FileNotFoundException, IOException {
        int i = 0;
        int position = 0;
        int px = 0;
        int py = 0;
        int alkuposition = 0;
        int nollat = 0;

        while (i < (x * y)) {
            double[] arvot = this.blocks[position][px][py];

            for (int j = 0; j < arvot.length; j++) {

                if (arvot[j] == 0) {
                    nollat++;
                } else {
                    BinaryStdOut.write(nollat + "", 10);
                    nollat = 0;
                    String koodi = giveCodeValue((int) arvot[j]);
                    for (int a = 0; a < koodi.length(); a++) {
                        if (koodi.charAt(a) == '0') {
                            BinaryStdOut.write(false);
                        } else if (koodi.charAt(a) == '1') {
                            BinaryStdOut.write(true);
                        } else {
                            throw new IllegalStateException("Illegal state");
                        }
                    }
                }
            }

            px++;
            i++;
            if (px == 8) {
                px = 0;
                position++;
            }
            if (position == alkuposition + (this.x / 8)) {
                py++;
                position = alkuposition;
                if (py == 8) {
                    // pitaa siirtaa positionia jotenkin
                    position += (x / 8);
                    alkuposition = position;
                    py = 0;
                }
            }
        }
    }

    public String giveCodeValue(int value) {
        return this.koodiarvot[value + x * y * 3];
    }

    public void writeToFile(Node vika) throws FileNotFoundException, IOException {
        String filename = "tulos.gpeg";
        BinaryStdOut.instantiateFileoutput();
        writeTrie(vika);
        writeValues();
        BinaryStdOut.flush();
    }

}
