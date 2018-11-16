/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cicilan.skripsi.pkg1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import Perhitungan_Error.PerhitunganError;

/**
 *
 * @author DavidJourdanManalu
 */
public class PanelGambar extends JPanel{
    BufferedImage indeskWarna;
    Frame frame;
    PerhitunganError perhitunganError;
    double skala = 4;
    int titikPojokGambarX, titikPojokGambarY;
    int paramPojokX, paramPojokY;
    protected int x1 = 0, y1 = 0, x2 = 0, y2 = 0, xSave = 0, ySave = 0;
    public PanelGambar(Frame frame) {
        this.frame = frame;
        paramPojokX = (int) (0.1 * getWidth());
        paramPojokY = (int) (0.1 * getHeight());
        setBackground(new Color(0, 0, 0));
        try {
            indeskWarna = ImageIO.read(getClass().getResource("/cicilan/skripsi"
                    + "/pkg1/Icon/IndeksWarna.png"));
        } 
        catch (IOException e) {
            // DITAMBAH UNTUK ERROR HANDLING
        }
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                x1 = e.getX();
                y1 = e.getY();
                x2 = e.getX();
                y2 = e.getY();
            }
            public void mouseReleased(MouseEvent e){
                xSave += x2 - x1;
                ySave += y2 - y1;
                x1 = 0; y1 = 0; x2 =0; y2 = 0;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e){
                x2 = e.getX();
                y2 = e.getY();
                repaint();
            }
        });
        addMouseWheelListener((e) -> {
            if(e.getWheelRotation() == 1){
                zoomIn();
                frame.panelButton.labelSkala.setText("Skala : " + 
                        frame.panelGambar.skala);
            }
            else if(e.getWheelRotation() == -1){
                zoomOut();
                frame.panelButton.labelSkala.setText("Skala : " + 
                        frame.panelGambar.skala);
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        titikPojokGambarX = paramPojokX + xSave + (x2 - x1);
        titikPojokGambarY = paramPojokY + ySave + (y2 - y1);

        // GAMBAR GARIS INDEKS X DAN Y
        g.setColor(new Color(150, 150, 150));
        for(int i = (int) (titikPojokGambarX % (skala * 10)); i <= getWidth(); 
                i += (skala * 10)){
            g.drawLine(i, 0, i, getHeight());
        }
        for(int i = (int) (titikPojokGambarY % (skala * 10)); i <= getHeight(); 
                i += (skala * 10)){
            g.drawLine(0, i, getWidth(), i);
        }
        g.setColor(Color.WHITE);
        for(int i = (int) (titikPojokGambarX % (skala * 50)); i <= getWidth(); 
                i += (50 * skala)){
            g.fillRect(i - 1, 0, 3, getHeight());
        }
        for(int i = (int) (titikPojokGambarY % (skala * 50)); i <= getHeight(); 
                i += (50 * skala)){
            g.fillRect(0, i - 1, getWidth(), 3);
        }
        
        // GAMBAR INTERPOLASI                                             
        if(frame.canBeVisualized){                                            
            int awalX = 0, akhirX = frame.dataVisualisasi[0].length;
            int awalY = 0, akhirY = frame.dataVisualisasi.length;
            if(titikPojokGambarX <= 0){
                awalX = 0 - (int) (titikPojokGambarX / skala / 
                        frame.skalaDetil);
            }
            if(((frame.dataVisualisasi[0].length * frame.skalaDetil) - 
                    awalX > getWidth())){
                akhirX = (int) (awalX + (getWidth() / skala / 
                        frame.skalaDetil));
            }
            if(titikPojokGambarY <= 0){
                awalY = 0 - (int) (titikPojokGambarY / skala / 
                        frame.skalaDetil);
            }
            if(((frame.dataVisualisasi.length * frame.skalaDetil) - 
                    awalY > getHeight())){
                akhirY = (int) (awalY + (getHeight() / skala / 
                        frame.skalaDetil));
            }
            for(int i = awalY; i < akhirY; i++){
                for(int j = awalX; j < akhirX; j++){
                    if(frame.dataVisualisasi[i][j] != -999999.0f){
                        // TAMBAH PENGATURAN WARNA DISINI
                        g.setColor(new Color(
                            frame.warnaVisualisasi[i][j][0], 
                            frame.warnaVisualisasi[i][j][1], 
                            frame.warnaVisualisasi[i][j][2]
                        ));
                        g.fillRect(
                            titikPojokGambarX + (int) (j * skala * frame.skalaDetil), 
                            titikPojokGambarY + (int) (i * skala * frame.skalaDetil), 
                            (int) (skala * (j + 1) * frame.skalaDetil) - 
                                    (int) (skala * j * frame.skalaDetil), 
                            (int) (skala * (i + 1) * frame.skalaDetil) - 
                                    (int) (skala * i * frame.skalaDetil)
                        );
                    }
                }
            }
        }
        
        // LOKASI UNTUK RMSE DAN MAE
        if(!frame.panelMenu.isFileEmpty){
            perhitunganError = new PerhitunganError(frame.data, frame.namaFile);
            g.setColor(new Color(235, 235, 235));
            g.fillRoundRect(getWidth() - 500, getHeight() - 92, 250, 50, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString("RMSE = " + perhitunganError.rmse.getRMSE(), getWidth() - 490, getHeight() - 72);
            g.drawString("MAE =  " + perhitunganError.mae.getMAE(), getWidth() - 490, getHeight() - 52);
        }
        // LOKASI INDEKS 
        if(frame.canBeVisualized){
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), 20);
            g.fillRect(0, getHeight() - 20, getWidth(), 20);
            g.setColor(Color.BLACK);
            for(int i = (int) (titikPojokGambarX % (skala * 50)); 
                    i <= getWidth(); i += (50 * skala)){
                g.fillRect(i - 1, 15, 3, 5);
                g.drawString(
                        "" + (int) ((i - titikPojokGambarX) / 
                                skala + frame.minX), 
                        (i - g.getFontMetrics().stringWidth("" + (int) ((i - 
                                titikPojokGambarX) / skala + frame.minX)) / 2), 
                        11
                );
                g.fillRect(i - 1, getHeight()- 20, 3, 5);
                g.drawString(
                        "" + (int) ((i - titikPojokGambarX) / 
                                skala + frame.minX), 
                        (i - g.getFontMetrics().stringWidth("" + (int) ((i - 
                                titikPojokGambarX) / skala + frame.minX)) / 2), 
                        getHeight() - 2
                );
            }
            for(int i = (int) (titikPojokGambarX % (skala * 10)); 
                    i <= getWidth(); i += (10 * skala)){
                if((i - titikPojokGambarX) % (50 * skala) != 0){
                    g.drawLine(i, 10, i, 20);
                    g.drawLine(i, getHeight() - 10, i, getHeight() - 20);
                }
            }
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 45, getHeight());
            g.fillRect(getWidth() - 45, 0, 45, getHeight());
            g.setColor(Color.BLACK);
            for(int i = (int) (titikPojokGambarY % (skala * 50)); 
                    i <= getHeight(); i += (50 * skala)){
                g.fillRect(40, i - 1, 5, 3);
                g.drawString("" + (int) (- (i - titikPojokGambarY) / 
                        skala + frame.maxY), 2, i + 4);
                g.fillRect(getWidth() - 45, i - 1, 5, 3);
                g.drawString("" + (int) (- (i - titikPojokGambarY) / 
                        skala + frame.maxY), getWidth() - 38, i + 4);
            }
            for(int i = (int) (titikPojokGambarY % (skala * 10)); 
                    i <= getHeight(); i += (10 * skala)){
                if((i - titikPojokGambarY) % (50 * skala) != 0){
                    g.drawLine(45, i, 35, i);
                    g.drawLine(getWidth() - 35, i, getWidth() - 45, i);
                }
            }
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 45, 20);
            g.fillRect(getWidth() - 45, 0, 45, 20);
            g.fillRect(0, getHeight() - 20, 45, 20);
            g.fillRect(getWidth() - 45, getHeight() - 20, 45, 20);
        }
        else{
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), 20);
            g.fillRect(0, getHeight() - 20, getWidth(), 20);
            g.setColor(Color.BLACK);
            for(int i = (int) (titikPojokGambarX % (skala * 50)); i <= 
                    getWidth(); i += (50 * skala)){
                g.fillRect(i - 1, 15, 3, 5);
                g.fillRect(i - 1, getHeight()- 20, 3, 5);
            }
            for(int i = (int) (titikPojokGambarX % (skala * 10)); i <= 
                    getWidth(); i += (10 * skala)){
                if((i - titikPojokGambarX) % (50 * skala) != 0){
                    g.drawLine(i, 10, i, 20);
                    g.drawLine(i, getHeight() - 10, i, getHeight() - 20);
                }
            }
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 45, getHeight());
            g.fillRect(getWidth() - 45, 0, 45, getHeight());
            g.setColor(Color.BLACK);
            for(int i = (int) (titikPojokGambarY % (skala * 50)); i <= 
                    getWidth(); i += (50 * skala)){
                g.fillRect(40, i - 1, 5, 3);
                g.fillRect(getWidth() - 45, i - 1, 5, 3);
            }
            for(int i = (int) (titikPojokGambarY % (skala * 10)); i <= 
                    getHeight(); i += (10 * skala)){
                if((i - titikPojokGambarY) % (50 * skala) != 0){
                    g.drawLine(45, i, 35, i);
                    g.drawLine(getWidth() - 35, i, getWidth() - 45, i);
                }
            }
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 45, 20);
            g.fillRect(getWidth() - 45, 0, 45, 20);
            g.fillRect(0, getHeight() - 20, 45, 20);
            g.fillRect(getWidth() - 45, getHeight() - 20, 45, 20);
        }
        
        // PETUNJUK ADA DATA ATAU TIDAK
        g.setColor(new Color(235,235,235));
        g.fillRoundRect(getWidth() -  260, 40, 190, 30, 30, 30);
        if(frame.dataAvailable){
            g.setColor(Color.GREEN);
            g.fillOval(getWidth() - 253, 47, 16, 16);
            g.setColor(Color.BLACK);
            g.drawString("DATA TERSEDIA", getWidth() - 230, 60);
        }
        else{
            g.setColor(Color.RED);
            g.fillOval(getWidth() - 253, 47, 16, 16);
            g.setColor(Color.BLACK);
            g.drawString("DATA TIDAK TERSEDIA", getWidth() - 230, 60);
        }
        
        if(frame.canBeVisualized){
            
            // BUAT BAGIAN PETUNJUK INDEKS WARNA PADA POJOK KANAN BAWAH
            g.setColor(new Color(230, 230, 230));
            g.fillRoundRect(
                    (int) (getWidth() - 240), 
                    (int) (0.50 * getHeight() - 36), 
                    160, 
                    (int) (0.50 * getHeight() - 6), 
                    10, 
                    10
            );
            g.setColor(Color.BLACK);
            g.setFont(new Font("Lucida Grande", Font.BOLD, 15));
            g.drawString("Nilai Z: ", (int) (getWidth() - 221),(int) (0.50 * getHeight() - 22));
            g.drawImage(
                    indeskWarna, 
                    (int) (getWidth() - 226), 
                    (int) (0.50 * getHeight() - 11), 
                    (int) (getWidth() - 196), 
                    (int) (getHeight() - 56), 
                    0, 
                    0, 
                    236, 
                    776, 
                    null
            );
            g.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
            double rangeAntarSkala = (0.5 * getHeight() - 46) / 5;
            double rangeAntarIndeks = (frame.maxZ - frame.minZ) / 5;
            for(int i = 0; i < 6; i++){
                g.drawLine(
                        (int) (getWidth() - 196), 
                        (int) (0.5 * getHeight() - 10 + i * rangeAntarSkala), 
                        (int) (getWidth() - 186), 
                        (int) (0.5 * getHeight() - 10 + i *rangeAntarSkala)
                );
                g.drawString(String.format("%.6g%n", frame.maxZ - rangeAntarIndeks * i), (int) (getWidth() - 181), (int) (0.5 * getHeight() - 6 + i * rangeAntarSkala));
            }
        }
    }
    
    public void zoomIn(){
        paramPojokX = (int) (0.5 * getWidth() - ((skala + 0.5) / skala) * (0.5 * getWidth() - titikPojokGambarX));
        paramPojokY = (int) (0.5 * getHeight() - ((skala + 0.5) / skala) * (0.5 * getHeight()- titikPojokGambarY));
        xSave = 0;
        ySave = 0;
        skala += 0.5;
        repaint();
    }
    
    public void zoomOut(){
        if(skala > 1){
            paramPojokX = (int) (0.5 * getWidth() - ((skala - 0.5) / skala) * (0.5 * getWidth() - titikPojokGambarX));
            paramPojokY = (int) (0.5 * getHeight() - ((skala - 0.5) / skala) * (0.5 * getHeight() - titikPojokGambarY));
            xSave = 0;
            ySave = 0;
            skala -= 0.5;
            repaint();
        }
    }
}