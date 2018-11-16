/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cicilan.skripsi.pkg1;

import OlahData.BagiLayerX;
import OlahData.BagiLayerY;
import java.awt.BorderLayout;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author DavidJourdanManalu
 */
public class PanelButton extends JPanel{
    BagiLayerX bagiLayerX;
    BagiLayerY bagiLayerY;
    JPanel panelKiri = new JPanel();
    JPanel panelTengah =  new JPanel();
    JPanel panelKanan = new JPanel();
    JButton zoomIn = new JButton();
    JButton zoomOut = new JButton();
    JButton plusDetil = new JButton("+");
    JButton minusDetil =  new JButton("-");
    JButton splineInterpolasi = new JButton("Interpolasi");
    JButton titikAwal = new JButton("Ulangi Posisi");
    JLabel labelSkala;
    JLabel labelDetil;
    double paramSkalaDetil;
    double minX, minY, minZ, maxX, maxY, maxZ;
    double rangeZ;
    
    public PanelButton(Frame frame){
        paramSkalaDetil = frame.skalaDetil;
        // PENGATURAN UTAMA UNTUK TOMBOL
        setLayout(new BorderLayout());
        zoomIn.setIcon(new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/zoom in.png")));
        panelKiri.add(zoomIn);
        zoomOut.setIcon(new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/zoom out.png")));
        panelKiri.add(zoomOut);
        labelSkala = new JLabel("Skala : " + frame.panelGambar.skala);
        panelKiri.add(labelSkala);
        panelKanan.add(titikAwal);
        splineInterpolasi.setIcon(new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/interpolasi.png")));
        panelKanan.add(splineInterpolasi);
        splineInterpolasi.setEnabled(false);
        panelTengah.add(minusDetil);
        panelTengah.add(plusDetil);
        labelDetil = new JLabel("Detil Perskala : " + frame.skalaDetil);
        panelTengah.add(labelDetil);
        
        // MENAMBAHKAN PANEL DALAM PANEL BUTTON
        add(panelKiri, BorderLayout.WEST);
        add(panelKanan, BorderLayout.EAST);
        add(panelTengah, BorderLayout.CENTER);
        
        // ACTION PADA TIAP BUTTON
        zoomIn.addActionListener((e) -> {
            frame.panelGambar.zoomIn();
            labelSkala.setText("Skala : " + frame.panelGambar.skala);
        });
        zoomOut.addActionListener((e) -> {
            frame.panelGambar.zoomOut();
            labelSkala.setText("Skala : " + frame.panelGambar.skala);
        });
        plusDetil.addActionListener((e) -> {
            if(paramSkalaDetil < 1.0){
                paramSkalaDetil *= 2;
                labelDetil.setText("Detil Perskala : " + paramSkalaDetil);
            }
        });
        minusDetil.addActionListener((e) -> {
            if(paramSkalaDetil > 0.05){
                paramSkalaDetil /= 2;
                labelDetil.setText("Detil Perskala : " + paramSkalaDetil);
            }
        });
        titikAwal.addActionListener((e) -> {
            frame.panelGambar.x1 = frame.panelGambar.x2 = frame.panelGambar.xSave = 0;
            frame.panelGambar.y1 = frame.panelGambar.y2 = frame.panelGambar.ySave = 0;
            frame.panelGambar.repaint();
        });
        splineInterpolasi.addActionListener((e) -> {
            
            
            frame.skalaDetil = paramSkalaDetil;
            frame.panelMenu.func_ViewResult.setEnabled(true);
            frame.panelMenu.func_View3D.setEnabled(true);
            double[][] hasilSementara;
            frame.canBeVisualized = true;
            bagiLayerY = new BagiLayerY(frame.data, frame.skalaDetil);
            bagiLayerX = new BagiLayerX(bagiLayerY.getHasil(), frame.skalaDetil);
            frame.hasilInterpolasi = bagiLayerX.getHasil();
            
            // MENGURUTKAN HASIL INTERPOLASI BERDASARKAN NILAI X
            java.util.Arrays.sort(frame.hasilInterpolasi, new Comparator<double[]>(){
                @Override
                public int compare(double[] o1, double[] o2) {
                    return Double.compare(o1[0], o2[0]);
                }
            });
            
            // MENGURUTKAN HASIL INTERPOLASI BERDASARKAN NILAI Y
            java.util.Arrays.sort(frame.hasilInterpolasi, new Comparator<double[]>(){
                @Override
                public int compare(double[] o1, double[] o2) {
                    return Double.compare(o1[1], o2[1]);
                }
            });
            
            // .. mencari min max untuk X
            minX = maxX = frame.data[0][0];
            for(int i = 1; i < frame.data.length; i++){
                if(minX > frame.data[i][0]){
                    minX = frame.data[i][0];
                }
                else if(maxX < frame.data[i][0]){
                    maxX = frame.data[i][0];
                }
            }
            // .. mencari min max untuk Z
            minZ = maxZ = frame.hasilInterpolasi[0][2];
            for(int i = 1; i < frame.hasilInterpolasi.length; i++){
                if(minZ > frame.hasilInterpolasi[i][2])
                    minZ = frame.hasilInterpolasi[i][2];
                if(maxZ < frame.hasilInterpolasi[i][2])
                    maxZ = frame.hasilInterpolasi[i][2];
            }
            rangeZ = maxZ - minZ;
            // .. menentukan min max pada Y
            minY = frame.data[0][1];
            maxY = frame.data[frame.data.length - 1][1];
            // .. menampung untuk nilai Z (hasilInterpolasi) dan warna untuk tiap titik (frame.warnaVisualisasi)
            frame.dataVisualisasi = new double[(int) ((maxY - minY) / frame.skalaDetil) + 1][(int) ((maxX - minX) / frame.skalaDetil) + 1];
            frame.warnaVisualisasi = new int[(int) ((maxY - minY) / frame.skalaDetil) + 1][(int) ((maxX - minX) / frame.skalaDetil) + 1][3];
            int paramID = 0;
            for(double i = minY; i <= maxY; i += frame.skalaDetil){
                if(frame.hasilInterpolasi[paramID][0] != minX){
                    double paramX = minX;
                    while(paramX != frame.hasilInterpolasi[paramID][0]){
                        frame.dataVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] = -999999.0f;
                        frame.dataVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((maxX - paramX) / frame.skalaDetil)] = -999999.0f;
                        paramX += frame.skalaDetil;
                    }
                    while(paramX == frame.hasilInterpolasi[paramID][0]){
                        frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] = frame.hasilInterpolasi[paramID][2];
                        
                        // .. menentukan nilai RGB pada arrray frae.frame.warnaVisualisasi
                        if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 1 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 153;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 102;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 2 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 102;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 68;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 3 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 51;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 34;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 4 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 5 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 85;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 170;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 6 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 170;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 85;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 7 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 8 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 85;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 9 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 170;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 10 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 11 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 204;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 12 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 153;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 13 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 102;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 14 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 68;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 15 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 34;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else {
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][1] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((paramX - minX) / frame.skalaDetil)][2] = 0;
                        }
                        paramX += frame.skalaDetil;
                        paramID++;
                    }
                }
                else{
                    for(double j = minX; j <= maxX; j+= frame.skalaDetil){
                        frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] = frame.hasilInterpolasi[paramID][2];
                        
                        // .. menentukan nilai RGB pada arrray frae.frame.warnaVisualisasi
                        if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 1 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 153;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 102;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 2 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 102;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 68;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 3 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 51;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 34;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 4 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 5 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 85;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 170;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 6 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 170;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 85;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 7 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 8 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 85;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 9 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 170;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 10 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 11 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 204;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 12 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 153;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 13 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 102;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 14 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 68;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int)((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)] <= minZ + (rangeZ * 15 / 16)){
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 34;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        else {
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][0] = 255;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][1] = 0;
                            frame.warnaVisualisasi[(int) ((maxY - i) / frame.skalaDetil)][(int) ((j - minX) / frame.skalaDetil)][2] = 0;
                        }
                        paramID++;
                    }
                }
            }
            frame.minX = minX;
            frame.maxX = maxX;
            frame.minY = minY;
            frame.maxY = maxY;
            frame.minZ = minZ;
            frame.maxZ = maxZ;
            frame.panelGambar.repaint();
//            for(int i = 0; i < frame.hasilInterpolasi.length; i++){
//                System.out.println(java.util.Arrays.toString(frame.hasilInterpolasi[i]));
//            }
        });
    }
}
