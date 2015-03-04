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
            
        } else if(command.equals("show")){
            outputfile = args[1];
            show(outputfile);
            
        } else if(command.equals("extract")){
            inputfile = args[1];
            outputfile = args[2];
            
            extract(inputfile, outputfile);
        }
        
        //SuorituskykyTestit.varillinenPieni();
        
    }
    
    /**
     * Compress image from .rgb file to .gpeg file
     * @param inputfile inputfile
     * @param outputfile outputfile
     * @param filex filex
     * @param filey filey
     * @throws IOException exception
     */
    public static void compress(String inputfile, String outputfile, int filex, int filey) throws IOException{
        Preprocessor p = new Preprocessor(filex, filey);
        p.separate(inputfile, "rgb", filex, filey);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        
        t.blocks = t.doForBlocks(t.blocks, "DCT");
     
        HuffmanCoder h = new HuffmanCoder(t.blocks, filex, filey, outputfile);
        h.makeHuffmanCoding();      
        System.out.println("Pakattu " + inputfile);
    }
    
    /**
     * Show image from output .gpeg file
     * @param outputfile outputfile
     * @throws FileNotFoundException exception
     */
    public static void show(String outputfile) throws FileNotFoundException{
        Preprocessor p = new Preprocessor();
        Decoder d = new Decoder(outputfile);
        d.readAll();
        
        Transformer t = new Transformer(d.blocks);
        d.blocks = t.doForBlocks(d.blocks, "applyIDCT");
        p.blocks = d.blocks;
        p.increaseBlocks(127);
        SwingModule s = new SwingModule(d.x, d.y, p.blocks, p);
        s.teeKuva();
    }
    
    /**
     * Extract inputfile .gpeg and write rgb-values to .rgb-file given as outpufile parameter.
     * @param inputfile inputfile
     * @param outputfile outputfile
     * @throws FileNotFoundException exception
     * @throws IOException exception
     */
    public static void extract(String inputfile, String outputfile) throws FileNotFoundException, IOException{
        Preprocessor p = new Preprocessor();
        Decoder d = new Decoder(inputfile);
        d.readAll();     
        Transformer t = new Transformer(d.blocks);
        d.blocks = t.doForBlocks(d.blocks, "applyIDCT");
        p.blocks = d.blocks;
        p.increaseBlocks(127);
        p.writeToRgbFile(p.blocks, outputfile, d.x, d.y);   
        System.out.println("Purettu " + inputfile);
    }
    
}
