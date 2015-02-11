/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pihla
 */
public class Decoder {

    private int x;
    private int y;
    public double[][][][] blocks;
    private int px;
    private int py;
    private int i;
    private int position;
    private int alkuposition;
    private int j;
    public BinaryIn b;
    private String filename;

    public Decoder(int x, int y, String filename) throws FileNotFoundException { 
        this.x = x;
        this.y = y;
        int count = (x * y) / (8 * 8);
        this.blocks = new double[count][8][8][3];
        this.i = 0;
        this.position = 0;
        this.px = 0;
        this.py = 0;
        this.alkuposition = 0;
        this.j = 0;
        this.filename = filename;
    }

    public void readAll() throws FileNotFoundException{     
        b = new BinaryIn(this.filename);
        //BinaryStdIn.instantiateFileinput();
        //BinaryStdIn.instantiateFileinput();
        Node juuri = readTree();
        readDataToBlocks(juuri);
        //BinaryStdIn.close();
    }

    public Node readTree() {
        boolean totuusarvo = b.readBoolean();
        if (totuusarvo == true) {
            return new Node(b.readShort(), -1, null, null);
        } else {
            return new Node("9000", -1, readTree(), readTree());
        }
    }

    public void taytaNollat(int nollat) {
        for (int i = 0; i < nollat; i++) {
            laitaArvo(0);
        }

    }

    public void laitaArvo(int arvo) {

        this.blocks[position][px][py][j] = arvo;
        j++;
        if (j == 3) {
            j =0;
            px++;
            if (px == 8) {
                px = 0;
                position++;
            }
            if (position == alkuposition + (x / 8)) {
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

    /**
     * Read data to blocks[][], then quantizing and DCT
     */
    public void readDataToBlocks(Node juuri) {
        int length = b.readInt();
        for (int i = 0; i < length; i++) {
            int nollat = b.readInt();
            taytaNollat(nollat);
            Node x = juuri;
            while (!x.isLeaf()) {
                boolean bit = b.readBoolean();
                if (bit) {
                    x = x.right;
                } else {
                    x = x.left;
                }
            }
            BinaryStdOut.write(x.value, 16);
            laitaArvo(x.value - 255);
        }
    }
}
