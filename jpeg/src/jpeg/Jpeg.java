/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import huffmancoding.*;
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
        
        SuorituskykyTestit.mustatIso();
        SuorituskykyTestit.mustatPieni();
        SuorituskykyTestit.varillinenPieni();
        //testi2();
        
    }
    
    public static void test() throws IOException{
        Preprocessor p = new Preprocessor();
        p.separate("kuva4.rgb", "rgb", 256, 256);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        t.blocks = t.doForBlocks(t.blocks, "DCT");
        t.blocks = t.doForBlocks(t.blocks, "applyIDCT");
        p.writeToRgbFile(t.blocks);
    }
    
    public static void testi2() throws IOException{
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
        
        d.blocks = t.doForBlocks(d.blocks, "applyIDCT");
        p.blocks = d.blocks;
        p.increaseBlocks(127);
        //p.increaseBlocks(127);
        SwingModule s = new SwingModule(256, 256, p.blocks, p);
        s.teeKuva();
        
    }
    
}
