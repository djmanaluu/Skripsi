/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menu_Function;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author DavidJourdanManalu
 */
public class ShowResult {
    JTable table;
    JFrame frame = new JFrame();
    public ShowResult(double[][] data){
        String[] column = {
            "X", "Y", "Z"
        };
        String[][] newData = new String[data.length][3];
        for(int i = 0; i < data.length; i++){
            newData[i] = java.util.Arrays.toString(data[i]).split("[\\[\\]]")[1].split(", ");
        }
        
        table = new JTable(newData, column);
        table.setEnabled(false);
        table.setBounds(30, 40, 200, 300);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        frame.setVisible(true);
        frame.setLocation(0, 70);
        frame.setSize(300, 400);
        frame.setTitle("Data Rapat Arus Ekuivalen - RESULT");
        frame.setAlwaysOnTop(true);
        
    }
}
