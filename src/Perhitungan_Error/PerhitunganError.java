/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Perhitungan_Error;

import OlahData.SplineKubik;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author DavidJourdanManalu
 */
public class PerhitunganError {
    String namaFile;
    double[][] dataTraining, dataTesting, predict;
    List<Double> layerIndeks;
    List<Integer> indeksLayer;
    SplineKubik splineKubik;
    public RMSE rmse;
    public MAE mae;
    public PerhitunganError(double[][] data, String namaFile){
        // SPLITTING DATA MENJADI DATA TRAINING DAN DATA TESTING
        
        this.namaFile = namaFile;
        
        dataTraining = new double[0][3];
        dataTesting = new double[0][3];
        layerIndeks = new ArrayList<>();
        indeksLayer = new ArrayList<>();
        predict = new double[0][3];
        // MENYUSUN DATA SESUAI DENGAN NILAI Y DENGAN MENGGUNAKAN SORTING ARRAY
        java.util.Arrays.sort(data, new Comparator<double[]>(){
            @Override
            public int compare(double[] o1, double[] o2) {
                return Double.compare(o1[1], o2[1]);
            }
        });
        
        // MENCARI LIST INDEKS LAYER SESUAI DENGAN NILAI Y (layerIndeks -> List)    
        layerIndeks.add(data[0][1]);
        indeksLayer.add(0);
        int jml = 1;
        for(int i = 0; i < data.length; i++){
            if(data[i][1] != layerIndeks.get(jml - 1)){
                layerIndeks.add(data[i][1]);
                indeksLayer.add(i);
                jml++;
            }
        }
        indeksLayer.add(data.length);
        
        for(int i = 0; i < jml - 1; i++){
            int panjangLayer = indeksLayer.get(i + 1) - indeksLayer.get(i);

            if(panjangLayer > 2){
                double[][] dataTrainingSementara = new double[(int) 
                        ((panjangLayer + 1) / 2)][3];
                double[][] dataTestingSementara = new double[(int) 
                        ((panjangLayer - 1) / 2)][3];
                for(int j = indeksLayer.get(i); j < indeksLayer.get(i) + 
                        dataTrainingSementara.length 
                        + dataTestingSementara.length ; j++){
                    if((j - indeksLayer.get(i)) % 2 == 0){
                        dataTrainingSementara[(j - indeksLayer.get(i)) / 2][0] 
                                = data[j][0];
                        dataTrainingSementara[(j - indeksLayer.get(i)) / 2][1] 
                                = data[j][1];
                        dataTrainingSementara[(j - indeksLayer.get(i)) / 2][2]
                                = data[j][2];
                    }
                    else{
                        dataTestingSementara[(j - indeksLayer.get(i) - 1) / 2]
                                [0] = data[j][0];
                        dataTestingSementara[(j - indeksLayer.get(i) - 1) / 2]
                                [1] = data[j][1];
                        dataTestingSementara[(j - indeksLayer.get(i) - 1) / 2]
                                [2] = data[j][2];
                    }
                }
                splineKubik = new SplineKubik(dataTrainingSementara, 
                        dataTestingSementara);
                dataTraining = append(dataTraining, dataTrainingSementara);
                dataTesting = append(dataTesting, dataTestingSementara);
                predict = append(predict, splineKubik.getPredict());
            }
        }
//        for(int i = 0; i < dataTraining.length; i++){
//            System.out.println(java.util.Arrays.toString(dataTraining[i]));
//        }
//        System.out.println("");
//        for(int i = 0; i < predict.length; i++){
//            System.out.println(java.util.Arrays.toString(predict[i]));
//        }
//        System.out.println("");
        rmse = new RMSE(predict, dataTesting);
        mae = new MAE(predict, dataTesting);
        
        
//        // MASUKAN PADA DATABASE
//        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
//        final String DB_URL = "jdbc:mysql://localhost/vlf?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
//        //  Database credentials
//        final String USER = "root";
//        final String PASS = "firdam351";
//        Connection conn = null;
//        Statement stmt = null;
//        try{
//           //STEP 2: Register JDBC driver
//           Class.forName("com.mysql.cj.jdbc.Driver");
//
//           //STEP 3: Open a connection
//           System.out.println("Connecting to database...");
//           conn = DriverManager.getConnection(DB_URL,USER,PASS);
//
//           //STEP 4: Execute a query
//           System.out.println("Creating statement...");
//           stmt = conn.createStatement();
//           String sql;
//           
//           sql = "INSERT into hasilInterpolasi values ('" + namaFile + "'" +  "," + (float) rmse.getRMSE() + "," + (float) mae.getMAE() + ");";
//           stmt.executeUpdate(sql);
//           
//           sql = "SELECT * from hasilInterpolasi";
//           ResultSet rs = stmt.executeQuery(sql);
//           
//            System.out.println("\n---Database VLF - EM---");
//            System.out.println("Alamat File\t\t\t\t\t\t\tNilaiRMSE\tNilaiMAE");
//           
//           while(rs.next()){
//               String a = rs.getString("name");
//               float rmse = rs.getFloat("nilaiRMSE");
//               float mae = rs.getFloat("nilaiMAE");
//               
//               System.out.println(a + " \t" + rmse + " \t" + mae);
//           }
//           
//           rs.close();
//           stmt.close();
//           conn.close();
//        }catch(SQLException se){
//           //Handle errors for JDBC
//           se.printStackTrace();
//        }catch(Exception e){
//           //Handle errors for Class.forName
//           e.printStackTrace();
//        }finally{
//           //finally block used to close resources
//           try{
//              if(stmt!=null)
//                 stmt.close();
//           }catch(SQLException se2){
//           }// nothing we can do
//           try{
//              if(conn!=null)
//                 conn.close();
//           }catch(SQLException se){
//              se.printStackTrace();
//           }//end finally try
//        }//end try
//        System.out.println("Goodbye!");
    }
    
    public double[][] append(double[][] a, double[][] b){
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    
}
