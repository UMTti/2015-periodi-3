/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.io.IOException;

/**
 *
 * @author pihla
 */
public class Jpeg {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Preprocessor p = new Preprocessor();
        p.separate("kuva4.rgb", "rgb", 256, 256);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        t.blocks = t.doForBlocks(t.blocks, "DCT");
        p.printBlocks(t.blocks);
        //t.blocks = t.doForBlocks(t.blocks, "applyIDCT");
        //p.printBlocks(t.blocks);
        //p.writeToRgbFile(t.blocks);
        /*double[] uusi = new double[]{2, 2, 2};
        uusi = p.convertToYCbCr(uusi);
        uusi = p.convertToRgb(uusi);
        for(int i = 0;i<uusi.length;i++){
            System.out.println(uusi[i]);
        }*/
        HuffmanCoder h = new HuffmanCoder(t.blocks, 256, 256);
        h.makeHuffmanCoding();
        
    }
    
}
