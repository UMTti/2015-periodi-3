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
    
    public int[][][][] blocks;
    public int[][] cosinematrix;
    private int n;
    private double[] c;
    /**
     *
     * @param blocks
     */
    public Transformer(int[][][][] blocks){
        this.blocks = blocks;
        this.n= 8;
        this.initializeCoefficients();
        
    }
    
    private void initializeCoefficients() {
        this.c = new double[this.n];
        for (int i=1;i<this.n;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
    }

    
    
    /**
     * Makes DCT transform block by block for blocks array
     */
    public void transform(){
        
    }
    
    /**
     * Multiply a single block with cosines
     * @param block
     * @return
     */
    public double[][] multiplyWithCosines(int[][] block, int n){
        double[][] matrix = new double[n][n];
        // k on rivinumero, siis jokaiselle riville
        for(int u = 0;u<n;u++){
            for(int v = 0;v<n;v++){
                matrix[u][v] = 0;
                for(int i = 0;i<n;i++){
                    for(int j = 0;j<n;j++){
                        matrix[u][v] += block[i][j] * Math.cos(Math.PI/(n))*(i+0.5)*u *Math.cos(Math.PI/(n))*(j+0.5)*v;
                    }
                }
                System.out.println("arvo: " + matrix[u][v]);
                
            }
        }
        return matrix;
    }
    
    public double[][] applyDCT(int[][] f) {
        int n = this.n;
        double[][] F = new double[n][n];
        for (int u=0;u<n;u++) {
          for (int v=0;v<n;v++) {
            double sum = 0.0;
            for (int i=0;i<n;i++) {
              for (int j=0;j<n;j++) {
                sum+=Math.cos(((2*i+1)/(2.0*n))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*n))*v*Math.PI)*f[i][j];
              }
            }
            sum*=((c[u]*c[v])/4.0);
            F[u][v]=sum;
          }
        }
        return F;
    }

    
}
