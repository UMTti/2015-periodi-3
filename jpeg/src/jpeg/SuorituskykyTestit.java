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
public class SuorituskykyTestit {
    
    public static void mustatPieni() throws IOException{
        String outputfile = "mustapieni.gpeg";
        String inputfile = "mustapieni.rgb";
        int filesize = 256;
        System.out.println(testaa(inputfile, outputfile, filesize));  
    }
    
    public static void mustatIso() throws IOException{
        String outputfile = "mustaiso.gpeg";
        String inputfile = "mustaiso.rgb";
        int filesize = 1024;
        System.out.println(testaa(inputfile, outputfile, filesize));      
    }
    
    public static void varillinenPieni(){
        
    }
    
    public static void varillinenIso(){
        
    }
    
    public static long testaa(String inputfile, String outputfile, int filesize) throws IOException{
        long t1 = System.currentTimeMillis();
        Preprocessor p = new Preprocessor();
        p.separate(inputfile, "rgb", filesize, filesize);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        t.blocks = t.doForBlocks(t.blocks, "DCT");
     
        HuffmanCoder h = new HuffmanCoder(t.blocks, filesize, filesize, outputfile);
        h.makeHuffmanCoding();
        
        Decoder d = new Decoder(filesize, filesize, outputfile);
        d.readAll();
        d.blocks = t.doForBlocks(d.blocks, "applyIDCT");
        
        p.blocks = d.blocks;
        p.increaseBlocks(127);
        SwingModule s = new SwingModule(filesize, filesize, p.blocks, p);
        s.teeKuva();
        long t2 = System.currentTimeMillis();
        long erotus = t2 - t1;
        return erotus;
    }
}
