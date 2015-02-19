/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

/**
 *
 * @author pihla
 */
public class Transformer {

    /**
     *
     */
    public double[][][][] blocks;
    private double[][] quantizationmatrix;
    private int n;
    private double[] c;
    private double[][] dpDCT;
    private double[][] dpIDCT;

    /**
     * Initialize transformer object
     * @param blocks
     */
    public Transformer(double[][][][] blocks) {
        this.blocks = blocks;
        this.quantizationmatrix = new double[][]{{16, 11, 10, 16, 24, 40, 51, 61}, {12, 12, 14, 19, 26, 58, 60, 55}, {14, 13, 16, 24, 40, 57, 69, 56}, {14, 17, 22, 29, 51, 87, 80, 62}, {18, 22, 37, 56, 68, 109, 103, 77}, {24, 35, 55, 64, 81, 104, 113, 92}, {49, 64, 78, 87, 103, 121, 120, 101}, {72, 92, 95, 98, 112, 100, 103, 99}};
        this.n = 8;
        this.initializeCoefficients();
        this.dpDCT = new double[n + 1][n + 1];
        this.dpIDCT = new double[n + 1][n + 1];

    }

    /**
     * Initialize coefficients. Those are used in DCT and IDCT later.
     */
    private void initializeCoefficients() {
        this.c = new double[this.n];
        for (int i = 1; i < this.n; i++) {
            c[i] = 1;
        }
        c[0] = 1 / Math.sqrt(2.0);
    }

    /**
     * Does a function given a parameter for all 8*8 blocks and saves block back
     *
     * @param blocks
     * @param command
     * @return
     */
    public double[][][][] doForBlocks(double[][][][] blocks, String command) {
        for (int i = 0; i < blocks.length; i++) {
            //täällä on elikkä niitä 8 arrayta joissa kaikissa on 3 tasoa
            double[][] y = new double[8][8];
            double[][] cb = new double[8][8];
            double[][] cr = new double[8][8];
            for (int j = 0; j < blocks[i].length; j++) {
                for (int k = 0; k < blocks[j].length; k++) {
                    y[j][k] = blocks[i][j][k][0];
                    cb[j][k] = blocks[i][j][k][1];
                    cr[j][k] = blocks[i][j][k][2];
                }
            }
            if (command.equals("applyIDCT")) {
                y = deQuantize(y);
                y = applyIDCT(y);
                cb = deQuantize(cb);
                cb = applyIDCT(cb);
                cr = deQuantize(cr);
                cr = applyIDCT(cr);
            } else if (command.equals("DCT")) {
                y = DCT(y);
                y = quantize(y);
                cb = DCT(cb);
                cb = quantize(cb);
                cr = DCT(cr);
                cr = quantize(cr);

            }

            for (int j = 0; j < blocks[i].length; j++) {
                for (int k = 0; k < blocks[j].length; k++) {
                    blocks[i][j][k][0] = y[j][k];
                    blocks[i][j][k][1] = cb[j][k];
                    blocks[i][j][k][2] = cr[j][k];
                }
            }
        }
        return blocks;
    }

    /**
     * Do discrete cosine transformation for 8*8px unit
     *
     * @param f
     * @return
     */
    public double[][] DCT(double[][] f) {
        int n = this.n;
        double[][] F = new double[n][n];
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                double sum = 0.0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        double eka = giveDpvalue(i, u);
                        double toka = giveDpvalue(j, v);
                        
                        sum += eka * toka * f[i][j];
                    }
                }
                sum *= ((c[u] * c[v]) / 4.0);
                F[u][v] = sum;
            }
        }
        //System.out.println(t2-t1);
        return F;
    }

    /**
     * Give cosine equation value from dynamic programming table if it exists there. This saves time. 
     * @param i
     * @param j
     * @return
     */
    public double giveDpvalue(int i, int j) {
        double value;
        if (dpDCT[i][j] != 0) {
            value = dpDCT[i][j];
        } else {
            value = Math.cos(((2 * i + 1) / (2.0 * n)) * j * Math.PI);
            dpDCT[i][j] = value;
        }
        return value;
    }

    /**
     * Do inverse cosine transform for 8*8px unit
     *
     * @param F
     * @return
     */
    public double[][] applyIDCT(double[][] F) {
        int n = this.n;
        double[][] f = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int u = 0; u < n; u++) {
                    for (int v = 0; v < n; v++) {
                        double eka = giveDpvalue(i, u);
                        double toka = giveDpvalue(j, v);
                               
                        sum += (c[u] * c[v]) / 4.0 * eka * toka * F[u][v];
                    }
                }
                if (sum >= 0) {
                    f[i][j] = Math.floor(sum);
                } else {
                    f[i][j] = Math.ceil(sum);
                }
            }
        }
        return f;
    }

    /**
     * Function to quantize a block after DCT. It multiplies value (i, j) of block with value (i, j) of quantization matrice.
     *
     * @param block
     * @return
     */
    public double[][] quantize(double[][] block) {
        int n = this.n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double arvo = block[i][j] / this.quantizationmatrix[i][j];
                if (arvo >= 0) {
                    block[i][j] = Math.floor(arvo);
                } else {
                    block[i][j] = Math.ceil(arvo);
                }
                //block[i][j] = Math.round(arvo);
            }
        }
        return block;
    }

    /**
     * Function to dequantize  8*8 block before IDCT. It divides value (i, j) with quantization matrice value (i, j)
     *
     * @param block
     * @return
     */
    public double[][] deQuantize(double[][] block) {
        int n = this.n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                block[i][j] = block[i][j] * this.quantizationmatrix[i][j];
            }
        }
        return block;
    }

}
