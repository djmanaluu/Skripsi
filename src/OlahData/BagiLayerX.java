/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OlahData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author DavidJourdanManalu
 */
public class BagiLayerX {
    List<Double> layerIndeks;                                                   
    List<Integer> indeksLayer;
    double[][] akumulasiHasil;
    double[][] newData;
    SplineKubik splineKubik;
    public BagiLayerX(double[][] data, double skalaDetil){
        layerIndeks = new ArrayList<>();
        indeksLayer = new ArrayList<>();
        akumulasiHasil = new double[0][3];

        // MENYUSUN DATA SESUAI DENGAN NILAI X DENGAN MENGGUNAKAN SORTING ARRAY
        java.util.Arrays.sort(data, new Comparator<double[]>(){
            @Override
            public int compare(double[] o1, double[] o2) {
                return Double.compare(o1[0], o2[0]);
            }
        });
        
        // MENCARI LIST INDEKS LAYER SESUAI DENGAN NILAI X (layerIndeks -> List)    
        layerIndeks.add(data[0][0]);
        indeksLayer.add(0);
        int jml = 1;
        for(int i = 0; i < data.length; i++){
            if(data[i][0] != layerIndeks.get(jml - 1)){
                layerIndeks.add(data[i][0]);
                indeksLayer.add(i);
                jml++;
            }
        }
        indeksLayer.add(data.length);
        for(int i = 0; i < jml; i++){
            newData = new double[indeksLayer.get(i + 1) - indeksLayer.get(i)][3];
            for(int j = indeksLayer.get(i); j < indeksLayer.get(i + 1); j++){
                newData[j - indeksLayer.get(i)][0] = data[j][0];
                newData[j - indeksLayer.get(i)][1] = data[j][1];
                newData[j - indeksLayer.get(i)][2] = data[j][2];
            }
            java.util.Arrays.sort(newData, new Comparator<double[]>(){
                @Override
                public int compare(double[] o1, double[] o2) {
                    return Double.compare(o1[1], o2[1]);
                }
            });
            if(newData.length >= 2){
                splineKubik = new SplineKubik(newData, "x", layerIndeks.get(i), 
                        newData[0][1], newData[newData.length - 1][1], skalaDetil);
                akumulasiHasil = append(akumulasiHasil, 
                        splineKubik.getHasilInterpolasi());
            }
            else{
                akumulasiHasil = append(akumulasiHasil, newData);
            }
        }
    }
    public double[][] getHasil(){
        return akumulasiHasil;
    }
    public double[][] append(double[][] a, double[][] b){
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
