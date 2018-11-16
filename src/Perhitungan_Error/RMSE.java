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
public class RMSE {
    private double errorRMSE;
    public RMSE(double[][] predict, double[][] dataTesting){
        // errorRMSE = pow ( pembilangRMSE^2 / n , 0.5 )
        double pembilangRMSE = 0;
        for(int i = 0; i < predict.length; i++){
            pembilangRMSE += Math.pow((predict[i][2] - dataTesting[i][2]), 2);
        }
        errorRMSE = Math.pow((pembilangRMSE / predict.length), 0.5);
    }
    public double getRMSE(){
        return errorRMSE;
    }
}
