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
        String inputfile;
        String outputfile;
        int sizex;
        int sizey;
        
        String command = args[0];
        if(command.equals("compress")){
            inputfile = args[1];
            outputfile = args[2];
            sizex = Integer.parseInt(args[3]);
            sizey = Integer.parseInt(args[4]);
            
            compress(inputfile, outputfile, sizex, sizey);
        }
        if(command.equals("show")){
            outputfile = args[1];
            sizex = Integer.parseInt(args[2]);
            sizey = Integer.parseInt(args[3]);
            show(outputfile, sizex, sizey);
        }
        
        //SuorituskykyTestit.varillinenPieni();
        
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
    public static void compress(String inputfile, String outputfile, int filex, int filey) throws IOException{
        Preprocessor p = new Preprocessor();
        p.separate(inputfile, "rgb", filex, filey);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        
        t.blocks = t.doForBlocks(t.blocks, "DCT");
     
        HuffmanCoder h = new HuffmanCoder(t.blocks, filex, filey, outputfile);
        h.makeHuffmanCoding();      
    }
    
    /**
     * Show image from output .gpeg file
     * @param outputfile
     * @param filesize
     * @throws FileNotFoundException
     */
    public static void show(String outputfile, int filex, int filey) throws FileNotFoundException{
        Preprocessor p = new Preprocessor();
        Decoder d = new Decoder(filex, filey, outputfile);
        d.readAll();
        
        Transformer t = new Transformer(d.blocks);
        d.blocks = t.doForBlocks(d.blocks, "applyIDCT");
        p.blocks = d.blocks;
        p.increaseBlocks(127);
        SwingModule s = new SwingModule(filex, filey, p.blocks, p);
        s.teeKuva();
    }
    
}
