/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OlahData;

/**
 *
 * @author DavidJourdanManalu
 */
public class SplineKubik {
    double[][] data;
    double[][] hasilInter;
    double[][] dataTraining, dataTesting;
    double[][] predict;
    
    public SplineKubik(double[][] data, String grid, double nilaiGrid, 
            double min, double max, double skalaDetil){
        this.data = data;
        int size = data.length;
        double[] d = new double[size];
        double[] c = new double[size - 1];
        double[] b = new double[size];
        double[] a = new double[size - 1];
        double[] h = new double[size - 1];
        double[][] matriks_a = new double[size][size];
        double[] matriks_b = new double[size];
        if(grid == "x"){
            // MENCARI KOEFISIEN Ai
            for(int i = 0; i < size; i++){
                d[i] = data[i][2];
            }
            // MENCARI H = Yn - Y(n-1)
            for(int i = 0; i < size - 1; i++){
                h[i] = data[i + 1][1] - data[i][1];
            }
            // MENCARI MATRIK A UNTUK Ax = B
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    if(i == 0){
                        if(j == 0){
                            matriks_a[i][j] = 1;
                        }
                        else{
                            matriks_a[i][j] = 0;
                        }
                    }
                    else if(i == size - 1){
                        if(j == size - 1){
                            matriks_a[i][j] = 1;
                        }
                        else{
                            matriks_a[i][j] = 0;
                        }
                    }
                    else{
                        if(j == i - 1){
                            matriks_a[i][j] = h[i - 1];
                        }
                        else if(j == i){
                            matriks_a[i][j] = 2 * (h[i - 1] + h [i]);
                        }
                        else if(j == i + 1){
                            matriks_a[i][j] = h[i];
                        }
                        else{
                            matriks_a[i][j] = 0;
                        }
                    }
                }
            }
            // MENCARI MATRIK B UNTUK Ax = B
            matriks_b[0] = matriks_b[size - 1] = 0;
            for(int i = 1; i < size - 1; i++){
                matriks_b[i] = 3 / h[i] * (d[i + 1] - d[i]) - 3 / h[i - 1] * 
                        (d[i] - d[i - 1]);
            }
            // GAUSS JORDAN MENCARI NILAI Ci
            for(int i = 1; i < size - 1; i++){
                double faktor = (double) (matriks_a[i][i - 1] / 
                        matriks_a[i - 1][i - 1]);
                for(int j = i - 1; j <= i + 1; j++){
                    matriks_a[i][j] -= faktor * matriks_a[i - 1][j];
                }
                matriks_b[i] -= faktor * matriks_b[i - 1];
            } 
            for(int i = size - 2; i > 0; i--){
                double faktor = (double) (matriks_a[i][i + 1] / 
                        matriks_a[i + 1][i + 1]);
                for(int j = i + 1; j >= i - 1; j--){
                    matriks_a[i][j] -= faktor * matriks_a[i + 1][j];
                }
                matriks_b[i] -= faktor * matriks_b[i + 1];
            }
            for(int i = 0; i < matriks_b.length; i++){
                b[i] = matriks_b[i] / matriks_a[i][i];
            }
            // MENCARI KOEFISIEN Bi
            for(int i = 0; i < size - 1; i++){
                c[i] = 1 / h[i] * (d[i + 1] - d[i]) - h[i] / 3 * 
                        (2 * b[i] + b[i + 1]);
            }
            // MENCARI KOEFISIEN Di
            for(int i = 0; i < size - 1; i++){
                a[i] = (b[i + 1] - b[i]) / (3 * h[i]);
            }
            // HASIL INTERPOLASI
            int pjgArray = (int) ((data[data.length - 1][1] - data[0][1]) / 
                    skalaDetil);
            hasilInter = new double[pjgArray + 1][3];                                   
            int index = 0;
            int idFungsi = 0;
            int idGrid1 = 1, idGrid2 = 0;
            for(double i = min; i <= max; i += skalaDetil){
                while(i > data[idFungsi + 1][1]){
                    idFungsi++;
                }
                hasilInter[index][idGrid1] = i;
                hasilInter[index][idGrid2] = nilaiGrid;
                hasilInter[index][2] = d[idFungsi] + c[idFungsi] * 
                        (i - data[idFungsi][1]) + b[idFungsi] * 
                        Math.pow(i - data[idFungsi][1], 2) + a[idFungsi] * 
                        Math.pow(i - data[idFungsi][1], 3);
                index++;
            }
        }
        else if(grid == "y"){
            // MENCARI KOEFISIEN Ai
            for(int i = 0; i < size; i++){
                d[i] = data[i][2];
            }
            // MENCARI H = Xn - X(n-1)
            for(int i = 0; i < size - 1; i++){
                h[i] = data[i + 1][0] - data[i][0];
            }
            // MENCARI MATRIK A UNTUK Ax = B
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    if(i == 0){
                        if(j == 0){
                            matriks_a[i][j] = 1;
                        }
                        else{
                            matriks_a[i][j] = 0;
                        }
                    }
                    else if(i == size - 1){
                        if(j == size - 1){
                            matriks_a[i][j] = 1;
                        }
                        else{
                            matriks_a[i][j] = 0;
                        }
                    }
                    else{
                        if(j == i - 1){
                            matriks_a[i][j] = h[i - 1];
                        }
                        else if(j == i){
                            matriks_a[i][j] = 2 * (h[i - 1] + h [i]);
                        }
                        else if(j == i + 1){
                            matriks_a[i][j] = h[i];
                        }
                        else{
                            matriks_a[i][j] = 0;
                        }
                    }
                }
            }
            // MENCARI MATRIK B UNTUK Ax = B
            matriks_b[0] = matriks_b[size - 1] = 0;
            for(int i = 1; i < size - 1; i++){
                matriks_b[i] = 3 / h[i] * (d[i + 1] - d[i]) - 3 / h[i - 1] * 
                        (d[i] - d[i - 1]);
            }
            // GAUSS JORDAN MENCARI NILAI Ci
            for(int i = 1; i < size - 1; i++){
                double faktor = (double) (matriks_a[i][i - 1] / 
                        matriks_a[i - 1][i - 1]);
                for(int j = i - 1; j <= i + 1; j++){
                    matriks_a[i][j] -= faktor * matriks_a[i - 1][j];
                }
                matriks_b[i] -= faktor * matriks_b[i - 1];
            } 
            for(int i = size - 2; i > 0; i--){
                double faktor = (double) (matriks_a[i][i + 1] / 
                        matriks_a[i + 1][i + 1]);
                for(int j = i + 1; j >= i - 1; j--){
                    matriks_a[i][j] -= faktor * matriks_a[i + 1][j];
                }
                matriks_b[i] -= faktor * matriks_b[i + 1];
            }
            for(int i = 0; i < matriks_b.length; i++){
                b[i] = matriks_b[i] / matriks_a[i][i];
            }
            // MENCARI KOEFISIEN Bi
            for(int i = 0; i < size - 1; i++){
                c[i] = 1 / h[i] * (d[i + 1] - d[i]) - h[i] / 3 * 
                        (2 * b[i] + b[i + 1]);
            }
            for(int i = 0; i < size - 1; i++){
                a[i] = (b[i + 1] - b[i]) / (3 * h[i]);
            }
            // HASIL INTERPOLASI
            int pjgArray = (int) ((data[data.length - 1][0] - data[0][0]) / 
                    skalaDetil);
            hasilInter = new double[pjgArray + 1][3];                                   
            int index = 0;
            int idFungsi = 0;
            int idGrid1 = 0, idGrid2 = 1;
            for(double i = min; i <= max; i += skalaDetil){
                while(i > data[idFungsi + 1][0]){
                    idFungsi++;
                }
                hasilInter[index][idGrid1] = i;
                hasilInter[index][idGrid2] = nilaiGrid;
                hasilInter[index][2] = d[idFungsi] + c[idFungsi] * 
                        (i - data[idFungsi][0]) + b[idFungsi] * 
                        Math.pow(i - data[idFungsi][0], 2) + a[idFungsi] * 
                        Math.pow(i - data[idFungsi][0], 3);
                index++;
            }
        }
    }
    public SplineKubik(double[][] dataTraining, double[][] dataTesting){
        this.dataTraining = dataTraining;
        this.dataTesting = dataTesting;
        predict = new double[dataTesting.length][3];
        int size = dataTraining.length;
        double[] d = new double[size];
        double[] c = new double[size - 1];
        double[] b = new double[size];
        double[] a = new double[size - 1];
        double[] h = new double[size - 1];
        double[][] matriks_a = new double[size][size];
        double[] matriks_b = new double[size];
        for(int i = 0; i < size; i++){
            d[i] = dataTraining[i][2];
        }
        // MENCARI H = Xn - X(n-1)
        for(int i = 0; i < size - 1; i++){
            h[i] = dataTraining[i + 1][0] - dataTraining[i][0];
        }
        // MENCARI MATRIK A UNTUK Ax = B
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(i == 0){
                    if(j == 0){
                        matriks_a[i][j] = 1;
                    }
                    else{
                        matriks_a[i][j] = 0;
                    }
                }
                else if(i == size - 1){
                    if(j == size - 1){
                        matriks_a[i][j] = 1;
                    }
                    else{
                        matriks_a[i][j] = 0;
                    }
                }
                else{
                    if(j == i - 1){
                        matriks_a[i][j] = h[i - 1];
                    }
                    else if(j == i){
                        matriks_a[i][j] = 2 * (h[i - 1] + h [i]);
                    }
                    else if(j == i + 1){
                        matriks_a[i][j] = h[i];
                    }
                    else{
                        matriks_a[i][j] = 0;
                    }
                }
            }
        }
        // MENCARI MATRIK B UNTUK Ax = B
        matriks_b[0] = matriks_b[size - 1] = 0;
        for(int i = 1; i < size - 1; i++){
            matriks_b[i] = 3 / h[i] * (d[i + 1] - d[i]) - 3 / 
                    h[i - 1] * (d[i] - d[i - 1]);
        }
        // GAUSS JORDAN MENCARI NILAI Ci
        for(int i = 1; i < size - 1; i++){
            double faktor = (double) (matriks_a[i][i - 1] / 
                    matriks_a[i - 1][i - 1]);
            for(int j = i - 1; j <= i + 1; j++){
                matriks_a[i][j] -= faktor * matriks_a[i - 1][j];
            }
            matriks_b[i] -= faktor * matriks_b[i - 1];
        } 
        for(int i = size - 2; i > 0; i--){
            double faktor = (double) (matriks_a[i][i + 1] / 
                    matriks_a[i + 1][i + 1]);
            for(int j = i + 1; j >= i - 1; j--){
                matriks_a[i][j] -= faktor * matriks_a[i + 1][j];
            }
            matriks_b[i] -= faktor * matriks_b[i + 1];
        }
        for(int i = 0; i < matriks_b.length; i++){
            b[i] = matriks_b[i] / matriks_a[i][i];
        }
        // MENCARI KOEFISIEN Bi
        for(int i = 0; i < size - 1; i++){
            c[i] = 1 / h[i] * (d[i + 1] - d[i]) - h[i] / 3 * 
                    (2 * b[i] + b[i + 1]);
        }
        for(int i = 0; i < size - 1; i++){
            a[i] = (b[i + 1] - b[i]) / (3 * h[i]);
        }
        // HASIL PREDIKSI 
        for(int i = 0; i < predict.length; i++){
            predict[i][0] = dataTesting[i][0];
            predict[i][1] = dataTesting[i][1];
            predict[i][2] = d[i] + c[i] * (dataTesting[i][0] - dataTraining[i][0])
                    + b[i] * Math.pow(dataTesting[i][0] - dataTraining[i][0], 2) 
                    + a[i] * Math.pow(dataTesting[i][0] - dataTraining[i][0], 3);
        }
    }
    // FUNGSI GETTER UNTUK HASIL INTERPLASI DALAM ARAY 2D
    public double[][] getHasilInterpolasi(){
        return hasilInter;
    }
    public double[][] getPredict(){
        return predict;
    }
}