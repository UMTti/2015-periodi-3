/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancoding;

import datastructures.*;
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

    /**
     * Minimum heap comparator class
     */
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
     * @param blocks blocks
     * @param x y
     * @param y y
     */
    private String[] koodiarvot;
    private int nollat;
    private int kokonaismaara;
    private String filename;

    /**
     *
     * @param blocks blocks
     * @param x x
     * @param y y
     * @param filename filename
     */
    public HuffmanCoder(double[][][][] blocks, int x, int y, String filename) {
        this.blocks = blocks;
        this.x = x;
        this.y = y;
        this.koodiarvot = new String[2 * x * y * 3]; // x*y*3 on nolla
        this.nollat = nollat;
        this.kokonaismaara = 0;
        this.filename = filename;
    }

    /**
     * General "main" function which calls makeFreqsTable and constructs the
     * tree of Nodes
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void makeHuffmanCoding() throws FileNotFoundException, IOException {
        int[] frequencies = makeFreqsTable(this.blocks);
        PQ minimumheap = makeMinimumHeap(frequencies);
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
     * @param blocks blocks 
     * @return frequencies table
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
                        } else {
                            this.kokonaismaara++;
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
     * Makes minimum heap of frequencies - heap sort can be applied later
     *
     * @param frequencies
     * @return minimumheap
     */
    public PQ makeMinimumHeap(int[] frequencies) {       
        PQ minimumheap = new PQ();
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

    /**
     * Build code values -table
     * @param n n
     * @param s code value 
     */
    public void buildKoodiarvot(Node n, String s) {
        if (!n.isLeaf()) {
            buildKoodiarvot(n.left, s + '0');
            buildKoodiarvot(n.right, s + '1');
        } else {
            this.koodiarvot[n.value + x * y * 3] = s;
        }
    }

    /**
     * Number 255 is added for every value, so everything is positive
     *
     * @param n n
     * @throws FileNotFoundException exception
     * @throws IOException exception
     */
    public void writeTrie(Node n) throws FileNotFoundException, IOException {
        if (n.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(n.value + 255, 16); // 9 bittiä, koska jos joku on esim alussa 200 nii menee yli 255 lisäyksessä
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(n.left);
        writeTrie(n.right);
    }

    /**
     * Write values after writing a trie
     * @throws FileNotFoundException exception
     * @throws IOException exception
     */
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
                writeOneValue((int)arvot[j]);
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
                    position += (x / 8);
                    alkuposition = position;
                    py = 0;
                }
            }
        }
    }

    /**
     * Write one value to file
     * @param arvo arvo
     */
    public void writeOneValue(int arvo) {
        if (arvo == 0) {
            this.nollat++;
        } else {
            BinaryStdOut.write(this.nollat);
            this.nollat = 0;
            String koodi = giveCodeValue((int) arvo);
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

    /**
     * Give code value of one value
     * @param value value
     * @return code value
     */
    public String giveCodeValue(int value) {
        return this.koodiarvot[value + x * y * 3];
    }

    /**
     * General function to write the whole image to file
     * @param vika root of trie
     * @throws FileNotFoundException exception
     * @throws IOException exception 
     */
    public void writeToFile(Node vika) throws FileNotFoundException, IOException {
        String filename = this.filename;
        BinaryStdOut.instantiateFileoutput(filename);
        BinaryStdOut.write(this.x, 16);
        BinaryStdOut.write(this.y, 16);
        writeTrie(vika);
        BinaryStdOut.write(this.kokonaismaara, 32);
        writeValues();
        BinaryStdOut.flush();
    }

}
