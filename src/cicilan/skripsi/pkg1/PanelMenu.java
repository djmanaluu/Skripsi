/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cicilan.skripsi.pkg1;

import Menu_File.LoadData;
import Menu_Function.ShowData;
import Menu_Function.ShowResult;
import OlahData.BagiLayerX;
import OlahData.BagiLayerY;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Comparator;
import javax.swing.*;


/**
 *
 * @author DavidJourdanManalu
 */
public class PanelMenu extends JMenuBar{
    JMenu menuFile = new JMenu("File");
    JMenu menuFunction = new JMenu("Fucntion");
    JMenuItem file_Load = new JMenuItem("Load Data", new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/load.png")));
    JMenuItem file_Exit = new JMenuItem("Exit", new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/exit.png")));
    JMenuItem func_Interpolation = new JMenuItem("Interpolation", new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/interpolasi.png")));
    JMenuItem func_Clear = new JMenuItem("Clear Data", new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/clear.png")));
    JMenuItem func_ViewData = new JMenuItem("View Data", new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/data.png")));
    JMenuItem func_ViewResult = new JMenuItem("View Result", new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/data.png")));
    JMenuItem func_View3D = new JMenuItem("View 3D Surface", new ImageIcon(getClass().getResource("/cicilan/skripsi/pkg1/Icon/3d.png")));
    JPopupMenu.Separator separatorFile1 = new JPopupMenu.Separator();
    JPopupMenu.Separator separatorFile2 = new JPopupMenu.Separator();
    File fileOpen, fileLoad, fileSave;
    BagiLayerX bagiLayerX;
    BagiLayerY bagiLayerY;
    boolean isFileEmpty = true;
    
    public PanelMenu(Frame frame){
        // SUB MENU UNTUK FILE (OPEN, SAVE, EXIT)
        menuFile.add(file_Load);
        file_Load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.META_MASK));
        
        menuFile.add(separatorFile1);
        
        menuFile.add(file_Exit);
        file_Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_MASK));
        
        
        // SUB MENU UNTUK FUNCTION (VIEW DATA, CLEAR DATA, GET PICTURE, INTERPOLATION)
        menuFunction.add(func_ViewData);
        func_ViewData.setEnabled(false);
        func_ViewData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_MASK));
        
        menuFunction.add(func_ViewResult);
        func_ViewResult.setEnabled(false);
        func_ViewResult.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.META_MASK));
        
        menuFunction.add(func_View3D);
        func_View3D.setEnabled(false);
        func_View3D.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.META_MASK));
        
        menuFunction.add(func_Clear);
        func_Clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.META_MASK));
        
        menuFunction.add(separatorFile2);
        
        menuFunction.add(func_Interpolation);
        func_Interpolation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.META_MASK));
        
        // PENGATURAN UMUM UNTUK MENU
        add(menuFile);
        add(menuFunction);
        
        // ACTION UNTUK MENU ITEM
        file_Load.addActionListener((e) -> {
            LoadData loadData = new LoadData();
            frame.data = loadData.getData();
            isFileEmpty = loadData.isEmpty();
            frame.namaFile = loadData.getNamaFile();
            if(!isFileEmpty){
                frame.panelButton.splineInterpolasi.setEnabled(true);
                func_ViewData.setEnabled(true);
                frame.dataAvailable = true;
            }
            else{
                frame.panelButton.splineInterpolasi.setEnabled(false);
                func_ViewData.setEnabled(false);
            }
            frame.panelGambar.repaint();
        });
        file_Exit.addActionListener((e) -> {
            System.exit(0);
        });
        func_ViewData.addActionListener((e) -> {
            ShowData showData = new ShowData(frame.data);
        });
        func_ViewResult.addActionListener((e) -> {
            ShowResult showResult = new ShowResult(frame.hasilInterpolasi);
        });
        func_Clear.addActionListener((e) -> {
            frame.canBeVisualized = false;
            frame.dataAvailable = false;
            func_ViewData.setEnabled(false);
            func_ViewResult.setEnabled(false);
            func_View3D.setEnabled(false);
            frame.panelButton.splineInterpolasi.setEnabled(false);
            frame.panelGambar.repaint();
        });
        func_View3D.addActionListener((e) -> {
            // mendapatkan GL2 Profile Capabilities
            GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            
            // membentuk canvas
            GLCanvas glcanvas = new GLCanvas(capabilities);
            Visualisasi3D visualisasi3D = new Visualisasi3D(frame);
            glcanvas.addGLEventListener(visualisasi3D);
            glcanvas.setFocusable(true);
            glcanvas.requestFocus();
            glcanvas.setSize(2000, 2000);
            
            // LISTENER KEYBOARD
            glcanvas.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent event){
                    switch(event.getKeyCode()){
                        case KeyEvent.VK_UP : visualisasi3D.transY -= 0.01f; break;
                        case KeyEvent.VK_DOWN : visualisasi3D.transY += 0.01f; break;
                        case KeyEvent.VK_LEFT : visualisasi3D.transX += 0.01f; break;
                        case KeyEvent.VK_RIGHT : visualisasi3D.transX -= 0.01f; break;
                    }
                }

                @Override
                public void keyReleased(KeyEvent event) {
                    switch(event.getKeyCode()){
                        case KeyEvent.VK_UP : break;
                        case KeyEvent.VK_DOWN : break;
                        case KeyEvent.VK_LEFT : break;
                        case KeyEvent.VK_RIGHT : break;
                    }
                }
                
            });
            
            // LISTENER MOUSE
            glcanvas.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent event){
                    visualisasi3D.x1 = visualisasi3D.x2 = event.getX() * 0.5f;
                    visualisasi3D.y1 = visualisasi3D.y2 = - event.getY() * 0.5f;
                }
                public void mouseReleased(MouseEvent event){
                    visualisasi3D.rtri += visualisasi3D.x2 - visualisasi3D.x1;
                    visualisasi3D.rtri2 += visualisasi3D.y2 - visualisasi3D.y1;
                    visualisasi3D.x1 = visualisasi3D.x2 = visualisasi3D.y1 = visualisasi3D.y2 = 0;
                }
            });
            glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent event){
                    visualisasi3D.x2 = event.getX() * 0.5f;
                    visualisasi3D.y2 = - event.getY() * 0.5f;
                }
            });
            glcanvas.addMouseWheelListener((event) -> {
                if(event.getWheelRotation() == 1){
                    visualisasi3D.scale += 0.05f;
                }
                else if(event.getWheelRotation() == -1){
                    visualisasi3D.scale -= 0.05f;
                }
            });
            // membentuk frame
            JFrame frame3D = new JFrame("Visualisasi 3D");
            frame3D.getContentPane().add(glcanvas);
            frame3D.setLocationRelativeTo(null);
            frame3D.setSize(1200, 1200);
            frame3D.setVisible(true);
            final FPSAnimator animator = new FPSAnimator(glcanvas, 500, true); 
            animator.start(); 
        });
        func_Interpolation.addActionListener((e) -> {
            double skalaDetil = frame.skalaDetil;
            frame.canBeVisualized = true;
            func_ViewResult.setEnabled(true);
            func_View3D.setEnabled(true);
            bagiLayerY = new BagiLayerY(frame.data, skalaDetil);
            bagiLayerX = new BagiLayerX(bagiLayerY.getHasil(), skalaDetil);
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
            
            // MEMBENTUK FORMAT DATA UNTUK VISUALISASI
            double minX, minY, minZ, maxX, maxY, maxZ;
            double distX, distY, rangeZ;
            minX = frame.data[0][0];
            maxX = frame.data[0][0];
            for(int i = 1; i < frame.data.length; i++){
                if(minX > frame.data[i][0]){
                    minX = frame.data[i][0];
                }
                else if(maxX < frame.data[i][0]){
                    maxX = frame.data[i][0];
                }
            }
            
            // MENENTUKAN MINMAX NILAI Z
            minZ = maxZ = frame.hasilInterpolasi[0][2];
            for(int i = 1; i < frame.hasilInterpolasi.length; i++){
                if(minZ > frame.hasilInterpolasi[i][2])
                    minZ = frame.hasilInterpolasi[i][2];
                if(maxZ < frame.hasilInterpolasi[i][2])
                    maxZ = frame.hasilInterpolasi[i][2];
            }
            rangeZ = maxZ - minZ;
            
            minY = frame.data[0][1];
            maxY = frame.data[frame.data.length - 1][1];
            frame.dataVisualisasi = new double[(int) ((maxY - minY) / skalaDetil) + 1][(int) ((maxX - minX) / skalaDetil) + 1];
            frame.warnaVisualisasi = new int[(int) ((maxY - minY) / skalaDetil) + 1][(int) ((maxX - minX) / skalaDetil) + 1][3];
            int paramID = 0;
            for(double i = minY; i <= maxY; i += skalaDetil){
                if(frame.hasilInterpolasi[paramID][0] != minX){
                    double paramX = minX;
                    while(paramX != frame.hasilInterpolasi[paramID][0]){
                        frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] = -999999.0f;
                        frame.dataVisualisasi[(int) (maxY - i)][(int) (maxX - paramX)] = -999999.0f;
                        paramX++;
                    }
                    while(paramX == frame.hasilInterpolasi[paramID][0]){
                        frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] = frame.hasilInterpolasi[paramID][2];
                        
                        // MENENTUKAN INDEKS RGB PADA ARRAY frame.warnaVisualisasi
                        if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 1 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 153;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 102;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 2 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 102;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 68;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 3 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 51;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 34;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 4 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 5 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 85;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 170;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 6 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 170;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 85;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 7 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 8 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 85;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 9 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 170;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 10 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 11 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 204;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 12 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 153;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 13 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 102;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 14 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 68;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 15 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 34;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (paramX - minX)] <= minZ + (rangeZ * 16 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][1] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (paramX - minX)][2] = 0;
                        }
                        paramID++;
                        paramX++;
                    }
                }
                else{
                    for(double j = minX; j <= maxX; j += skalaDetil){
                        frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] = frame.hasilInterpolasi[paramID][2];
                        
                        // MENENTUKAN INDEKS RGB PADA ARRAY frame.warnaVisualisasi
                        if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 1 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 153;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 102;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 2 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 102;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 68;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 3 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 51;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 34;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 4 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 255;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 5 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 85;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 170;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 6 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 170;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 85;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 7 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 8 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 85;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 9 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 170;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 10 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 11 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 204;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 12 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 153;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 13 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 102;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 14 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 68;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 15 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 34;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
                        }
                        else if(frame.dataVisualisasi[(int) (maxY - i)][(int) (j - minX)] <= minZ + (rangeZ * 16 / 16)){
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][0] = 255;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][1] = 0;
                            frame.warnaVisualisasi[(int) (maxY - i)][(int) (j - minX)][2] = 0;
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
        });
    }
}