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
    
    /**
     * Testaa mustaa pientä kuvaa, 256*256px. Printtaa ajan millisekunteina
     * @throws IOException
     */
    public static void mustatPieni() throws IOException{
        String outputfile = "mustapieni.gpeg";
        String inputfile = "mustapieni.rgb";
        int filesize = 256;
        System.out.println(testaa(inputfile, outputfile, filesize));  
    }
    
    /**
     * Musta iso 1024*1024 kuva, printtaa ajan millisekunteina
     * @throws IOException exception
     */
    public static void mustatIso() throws IOException{
        String outputfile = "mustaiso.gpeg";
        String inputfile = "mustaiso.rgb";
        int filesize = 1024;
        System.out.println(testaa(inputfile, outputfile, filesize));      
    }
    
    /**
     * Värillinen pieni kuva, 256*256px, printtaa ajan millisekunteina
     * @throws IOException exception
     */
    public static void varillinenPieni() throws IOException{
        String inputfile = "homo256.rgb";
        String outputfile = "homo256.gpeg";
        int filesize = 256;
        System.out.println(testaa(inputfile, outputfile, filesize)); 
    }
    
    /**
     * Värillinen iso 1024*1024px kuva, printtaa ajan millisekunteina
     * @throws java.io.IOException exception
     */
    public static void varillinenIso() throws IOException{
        String inputfile = "homo1024.rgb";
        String outputfile = "homo1024.gpeg";
        int filesize = 1024;
        System.out.println(testaa(inputfile, outputfile, filesize)); 
    }
    
    /**
     * Testipohja. Tätä kutsutaan testistä, tämä ajaa pakkaamisen ja purkamisen kyseisen testin kuvalle. 
     * @param inputfile inputfile
     * @param outputfile outputfile
     * @param filesize filesize
     * @return erotus, aika millisekunteina
     * @throws IOException exception
     */
    public static long testaa(String inputfile, String outputfile, int filesize) throws IOException{
        long t1 = System.currentTimeMillis();
        Preprocessor p = new Preprocessor(filesize, filesize);
        p.separate(inputfile, "rgb", filesize, filesize);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        t.blocks = t.doForBlocks(t.blocks, "DCT");
     
        HuffmanCoder h = new HuffmanCoder(t.blocks, filesize, filesize, outputfile);
        h.makeHuffmanCoding();
        
        Decoder d = new Decoder(outputfile);
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
