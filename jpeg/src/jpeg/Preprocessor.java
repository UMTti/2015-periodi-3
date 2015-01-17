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

    public Preprocessor() {

    }

    public void separate(String filename, String datatype, int x, int y) throws FileNotFoundException, IOException {
        //  read the file into a byte array
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        //byte[] arr = new byte[(int) file.length()];
        int count = (x*y*3) / (8*8);
        int[][][] blocks = new int[count][8][8];
        int position = 0;
        int px = 0;
        int py = 0;
        for (int i = 0; i < x * y * 3; i++) {
            int b = fis.read();
            blocks[position][px][py] = b & 0xff;
            px++;
            if(px == 8){
                px = 0;
                position++;
            }
            if(position == count){
                py++;
                position = 0;
            }
        }
        
         //bb.order(ByteOrder.LITTLE_ENDIAN);*/
    }

}
