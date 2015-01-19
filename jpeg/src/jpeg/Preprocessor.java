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

    public int blocks[][][][];

    public Preprocessor() {
        this.blocks = blocks;
    }

    public void separate(String filename, String datatype, int x, int y) throws FileNotFoundException, IOException {
        //  read the file into a byte array
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        //byte[] arr = new byte[(int) file.length()];
        int count = (x * y) / (8 * 8);
        System.out.println("Count: " + count);
        this.blocks = new int[count][8][8][3];
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

    public void printBlocks() {
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                for (int k = 0; k < this.blocks[i][j].length; k++) {
                    for(int z=0;z<3;z++){
                        System.out.println(this.blocks[i][j][k][z]);
                    }
                    System.out.println("");
                }
                System.out.println("");
            }
        }
    }

}
