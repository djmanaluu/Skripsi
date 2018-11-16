/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cicilan.skripsi.pkg1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author DavidJourdanManalu
 */
public class Frame extends JFrame{
    String namaFile;
    double[][] data;
    double[][] hasilInterpolasi;
    double[][] dataVisualisasi;
    int[][][] warnaVisualisasi;
    boolean canBeVisualized = false;
    boolean dataAvailable = false;
    double minX, minY, minZ, maxX, maxY, maxZ;
    double skalaDetil = 1.0;
    PanelGambar panelGambar = new PanelGambar(this);
    PanelButton panelButton = new PanelButton(this);
    PanelMenu panelMenu = new PanelMenu(this);
    public Frame() {
        setLayout(new BorderLayout());
                        // tambahan doang
        // MENAMBAHKAN PANEL PADA FRAME
        add(panelGambar, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);
        
        // PENGATURAN UMUM UNTUK FRAME
        setJMenuBar(panelMenu);
        setTitle("SKRIPSI - DAVID JOURDAN - INTERPOLASI SPLINE BIKUBIK");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setSize(1200, 1200);
        setMinimumSize(new Dimension(800, 600));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
