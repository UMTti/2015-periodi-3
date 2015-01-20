/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.io.*;
import java.nio.*;
import java.util.*;

/**
 *
 * @author pihla
 */
public class Preprocessor {

    /**
     * Blocks, 4D array which stores all data
     */
    public double blocks[][][][];

    /**
     * Constructor for Preprocessor object
     */
    public Preprocessor() {
        this.blocks = blocks;
    }

    /**
     * Read byte data and separate it into 8*8 arrays.
     * @param filename
     * @param datatype
     * @param x
     * @param y
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void separate(String filename, String datatype, int x, int y) throws FileNotFoundException, IOException {
        //  read the file into a byte array
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        //byte[] arr = new byte[(int) file.length()];
        int count = (x * y) / (8 * 8);
        //System.out.println("Count: " + count);
        this.blocks = new double[count][8][8][3];
        int maara = 0;
        int position = 0;
        int px = 0;
        int py = 0;
        int alkuposition = 0;
        for (int i = 0; i < x * y ; i++) {
            for (int j = 0; j < 3; j++) {
                int b = fis.read();
                this.blocks[position][px][py][j] = b & 0xff;
            }
            this.blocks[position][px][py] = convertToYCbCr(this.blocks[position][px][py]);
            px++;
            if (px == 8) {
                px = 0;
                position++;
            }
            if (position == alkuposition + (x/8)) {
                py++;
                position = alkuposition;
                if(py == 8){
                    // pitaa siirtaa positionia jotenkin
                    position += (x/8);
                    alkuposition = position;
                    py = 0;
                }
            }
        }

        //bb.order(ByteOrder.LITTLE_ENDIAN);*/
    }

    /**
     * Decrease all values of block with value given a parameter
     * @param value
     */
    public void decreaseBlocks(int value) {
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                for (int k = 0; k < this.blocks[i][j].length; k++) {
                    for(int z = 0; z<3;z++){
                        this.blocks[i][j][k][z] -= 127;
                    }
                }
            }
        }
    }

    /**
     * Prints all values of double[][][][] blocks. 
     */
    public void printBlocks(double[][][][] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                for (int k = 0; k < blocks[i][j].length; k++) {
                    for(int z=0;z<3;z++){
                        System.out.println(blocks[i][j][k][z]);
                    }
                    System.out.println("");
                }
                System.out.println("");
            }
        }
    }
    
    /**
     * Converts RGB-value block to YCrCb block
     * @param block
     * @return
     */
    public double[] convertToYCbCr(double[] block){
        double r = block[0];
        double g = block[1];
        double b = block[2];
        int Y = (int)(0.257 * r + 0.50 * g + 0.098 * b + 16);

        int Cb = (int)(-0.148 * r - 0.291 * g + 0.439 * b + 128);

        int Cr = (int)(0.439 * r - 0.368 * g - 0.071 * b + 128);
        
        block[0] = Y;
        block[1] = Cb;
        block[2] = Cr;
        return block;
    }

}
