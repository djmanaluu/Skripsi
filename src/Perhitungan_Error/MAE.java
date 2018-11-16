/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Perhitungan_Error;

/**
 *
 * @author DavidJourdanManalu
 */
public class MAE {
    private double errorMAE;
    public MAE(double[][] predict, double[][] dataTesting){
        // errorMAE = pembilangMAE / n
        double pembilangMAE = 0;
        for(int i = 0; i < predict.length; i++){
            pembilangMAE += Math.abs(predict[i][2] - dataTesting[i][2]);
        }
        errorMAE = pembilangMAE / predict.length;
    }
    public double getMAE(){
        return errorMAE;
    }
}
