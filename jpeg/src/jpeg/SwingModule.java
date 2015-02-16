/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 *
 * @author pihla
 */
public class SwingModule {

    private int x;
    private int y;
    private double[][][][] blocks;
    private int px;
    private int py;
    private int position;
    private int alkuposition;
    private int m;
    private Preprocessor p;

    /**
     * Swing module to show image
     * @param x
     * @param y
     * @param blocks
     * @param p
     */
    public SwingModule(int x, int y, double[][][][] blocks, Preprocessor p) {
        this.x = x;
        this.y = y;
        this.px = 0;
        this.py = 0;
        this.position = 0;
        this.alkuposition = 0;
        this.m = 0;
        this.blocks = blocks;
        this.p = p;
    }

    /**
     * Creates BufferedImage of blocks[][][][]
     */
    public void teeKuva() {
        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image = new BufferedImage(x, y, type);

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                double[] arvot = haeSeuraava();
                arvot = p.convertToRgb(arvot);
                Color col = new Color((int)arvot[0], (int)arvot[1], (int)arvot[2]);
                image.setRGB(j, i, col.getRGB());
            }
        }

        nayta(image);

    }

    /**
     * Gets next rgb -value from blocks
     * @return
     */
    public double[] haeSeuraava() {
        double[] arvot = this.blocks[position][px][py];
        px++;
        if (px == 8) {
            px = 0;
            position++;
        }
        if (position == alkuposition + (x / 8)) {
            py++;
            position = alkuposition;
            if (py == 8) {
                // pitaa siirtaa positionia jotenkin
                position += (x / 8);
                alkuposition = position;
                py = 0;
            }
        }

        return arvot;
    }

    /**
     * Show image with JFrame
     * @param image
     */
    public void nayta(BufferedImage image) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        String img;
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }
}
