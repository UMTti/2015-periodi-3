/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import huffmancoding.*;
import java.io.FileNotFoundException;
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
        
        String inputfile = "kuva4.rgb";
        String outputfile = "kuva4.gpeg";
        compress(inputfile, outputfile, 256);
        show(outputfile, 256);
        
    }
    
    /**
     *
     * @throws IOException
     */
    public static void test() throws IOException{
        Preprocessor p = new Preprocessor();
        p.separate("kuva4.rgb", "rgb", 256, 256);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        t.blocks = t.doForBlocks(t.blocks, "DCT");
        t.blocks = t.doForBlocks(t.blocks, "applyIDCT");
        p.writeToRgbFile(t.blocks);
    }
    
    /**
     * Compress image from .rgb file to .gpeg file
     * @param inputfile
     * @param outputfile
     * @param filesize
     * @throws IOException
     */
    public static void compress(String inputfile, String outputfile, int filesize) throws IOException{
        Preprocessor p = new Preprocessor();
        p.separate(inputfile, "rgb", filesize, filesize);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        
        t.blocks = t.doForBlocks(t.blocks, "DCT");
     
        HuffmanCoder h = new HuffmanCoder(t.blocks, 256, 256, outputfile);
        h.makeHuffmanCoding();      
    }
    
    /**
     * Show image from output .gpeg file
     * @param outputfile
     * @param filesize
     * @throws FileNotFoundException
     */
    public static void show(String outputfile, int filesize) throws FileNotFoundException{
        Preprocessor p = new Preprocessor();
        Decoder d = new Decoder(filesize, filesize, outputfile);
        d.readAll();
        
        Transformer t = new Transformer(d.blocks);
        d.blocks = t.doForBlocks(d.blocks, "applyIDCT");
        p.blocks = d.blocks;
        p.increaseBlocks(127);
        SwingModule s = new SwingModule(256, 256, p.blocks, p);
        s.teeKuva();
    }
    
}
