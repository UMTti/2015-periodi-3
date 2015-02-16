/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import huffmancoding.*;
import java.io.IOException;
import java.util.Arrays;
import jpeg.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pihla
 */
public class Testi {
    
    @Test
    public void testaaHuffman() throws IOException {
        String outputfile = "tulos.gpeg";
        String inputfile = "kuva4.rgb";
        Preprocessor p = new Preprocessor();
        p.separate(inputfile, "rgb", 256, 256);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        t.blocks = t.doForBlocks(t.blocks, "DCT");
     
        HuffmanCoder h = new HuffmanCoder(t.blocks, 256, 256, outputfile);
        h.makeHuffmanCoding();
        
        Decoder d = new Decoder(256, 256, outputfile);
        d.readAll();
        
        assertFalse(Arrays.equals(t.blocks, d.blocks));
        
    }
    
    @Test
    public void testaaRGB(){
        double[] testi = new double[]{0, 100, 200};
        Preprocessor p = new Preprocessor();
        testi = p.convertToYCbCr(testi);
        double[] uusi = p.convertToRgb(testi);
        assertFalse(Arrays.equals(testi, uusi));
    }
    
    @Test
    public void testaaKvantisointi(){
        double[][] testi = new double[8][8];
        for(int i = 0;i<8;i++){
            for(int j = 0;j<8;j++){
                testi[i][j] = i;
            }
        }
        Transformer t = new Transformer(new double[100][8][8][3]);
        testi = t.quantize(testi);
        double[][] uusi = t.deQuantize(testi);
        assertArrayEquals(testi, uusi);
    }
    
    /*@Test
    public void testaaTransformaatio() throws IOException{
        double[][] testi = new double[8][8];
        for(int i = 0;i<8;i++){
            for(int j = 0;j<8;j++){
                testi[i][j] = i;
            }
        }
        Transformer t = new Transformer(new double[100][8][8][3]);
        double[][] uusi = t.DCT(testi);
        
        uusi = t.applyIDCT(testi);
        for(int i = 0;i<8;i++){
            for(int j = 0;j<8;j++){
                System.out.println(uusi[i][j]);
            }
        }
        assertArrayEquals(testi, uusi);
    }*/
    
    @Test
    public void testaaPQ(){
        PQ pq = new PQ();
        pq.add(new Node(1, 2, null, null));
        pq.add(new Node(1, 3, null, null));
        pq.add(new Node(1, 4, null, null));
        assertSame(2, pq.remove().frequency);
    }
}
