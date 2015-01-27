/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.io.*;
import java.math.*;
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
    public int x;
    public int y;

    /**
     * Constructor for Preprocessor object
     */
    public Preprocessor() {
        this.blocks = blocks;

    }

    /**
     * Read byte data and separate it into 8*8 arrays.
     *
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
        this.x = x;
        this.y = y;
        //System.out.println("Count: " + count);
        this.blocks = new double[count][8][8][3];
        int maara = 0;
        int position = 0;
        int px = 0;
        int py = 0;
        int alkuposition = 0;
        for (int i = 0; i < x * y; i++) {
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

        //bb.order(ByteOrder.LITTLE_ENDIAN);*/
    }

    /**
     * Decrease all values of block with value given a parameter
     *
     * @param value
     */
    public void decreaseBlocks(int value) {
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                for (int k = 0; k < this.blocks[i][j].length; k++) {
                    for (int z = 0; z < 3; z++) {
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
                    for (int z = 0; z < 3; z++) {
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
     *
     * @param block
     * @return
     */
    public double[] convertToYCbCr(double[] block) {

        double r = block[0];
        double g = block[1];
        double b = block[2];

        double Y = 0.299 * r + 0.587 * g + 0.114 * b;
        double cb = -0.1687 * r - 0.3313 * g + 0.5 * b + 128;
        double cr = 0.5 * r - 0.4187 * g - 0.0813 * b + 128;

        block[0] = Y;
        block[1] = cb;
        block[2] = cr;
        return block;
    }

    public double[] convertToRgb(double[] block) {
        double[] uusi = new double[3];
        double Y = block[0];
        double cb = block[1];
        double cr = block[2];

        double r = Y + 1.402 * (cr - 128);
        double g = Y - 0.34414*(cb - 128) - 0.71414*(cr - 128);
        double b = Y + 1.772*(cb - 128);
        
        r = validateRGB(r);
        g = validateRGB(g);
        b = validateRGB(b);
        uusi[0] = r;
        uusi[1] = g;
        uusi[2] = b;
        return uusi;
    }
    
    public double validateRGB(double a){
        if(a > 255){
            return 255;
        }
        if(a < 0){
            return 0;
        }
        return Math.floor(a);
    }

    public void writeToRgbFile(double[][][][] blocks) throws IOException {
        int i = 0;
        int position = 0;
        int px = 0;
        int py = 0;
        int alkuposition = 0;
        int maara = 0;

        //File file = new File("tulos.rgb");
        //BufferedWriter output = new BufferedWriter(new FileWriter(file));
        DataOutputStream os = new DataOutputStream(new FileOutputStream("tulos2.rgb"));
        while (i < (x * y)) {
            double[] arvot = blocks[position][px][py];
            for (int j = 0; j < arvot.length; j++) {
                arvot[j] += 127;
            }
            arvot = convertToRgb(arvot);
            for (int j = 0; j < arvot.length; j++) {
                BigInteger bigInt = BigInteger.valueOf((int) arvot[j]);
                //output.write(bigInt.byteValue());
                os.write(bigInt.byteValue());
                maara++;
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
        //output.close();
        os.close();

    }

}
