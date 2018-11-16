/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menu_File;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFileChooser;

/**
 *
 * @author DavidJourdanManalu
 */
public class LoadData {
    String namaFile = "";
    JFileChooser fileChooser = new JFileChooser(new File("/Users/macintosh/"
            + "Documents/Data Kuliah/SKRIPSI/Data"));
    int inVal;
    File file;
    double[][] data;
    boolean param = false;
    public LoadData(){
        inVal = fileChooser.showOpenDialog(null);
        if(inVal == JFileChooser.APPROVE_OPTION){
            file = fileChooser.getSelectedFile();
            param = true;                                                       
        }
        data = konversiListArray(file);
        namaFile = file.toString();
    }
    
    
    public String getNamaFile(){
        return namaFile;
    }
    
    public double[][] getData(){
        return data;
    }
    
    // METHOD ADA TIDAKNYA FILE YANG DI LOAD
    public boolean isEmpty(){                                                   
        return !param;
    }
    
    // METHOD CONVERT LIST KE ARRAY
    public double[][] konversiListArray(File file){
        Scanner scanner;
        double[][] array;
        List<double[]> list = new ArrayList<>();
        double[] result;
        try{
            scanner = new Scanner(file);
            while(scanner.hasNext()){
                String line = scanner.next();
                result = new double[3];
                int i = 0;
                for(String s: line.split(",")){
                    //result.add(Double.parseDouble(s.trim()));
                    result[i] = Double.parseDouble(s.trim());
                    i++;
                }
                list.add(result);
            }
        }
        catch(FileNotFoundException e){
            // NANTI DITAMBAHKAN KALAU PERLU
        }
        array = new double[list.size()][3];
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < 3; j++){
                array[i][j] = list.get(i)[j];
            }
        }
        return array;
    }
}