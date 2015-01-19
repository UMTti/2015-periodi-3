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
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Preprocessor p = new Preprocessor();
        p.separate("kuva.rgb", "rgb", 64, 64);
        p.decreaseBlocks(127);
        p.printBlocks();
    }
    
}
