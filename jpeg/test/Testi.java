/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
        Preprocessor p = new Preprocessor();
        p.separate("kuva4.rgb", "rgb", 256, 256);
        p.decreaseBlocks(127);
        Transformer t = new Transformer(p.blocks);
        t.blocks = t.doForBlocks(t.blocks, "DCT");
     
        HuffmanCoder h = new HuffmanCoder(t.blocks, 256, 256);
        h.makeHuffmanCoding();
        
        Decoder d = new Decoder(256, 256);
        d.readAll();
        
        assertFalse(Arrays.equals(t.blocks, d.blocks));
        
    }
}
