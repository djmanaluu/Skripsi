/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cicilan.skripsi.pkg1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author DavidJourdanManalu
 */
public class Visualisasi3D implements GLEventListener{
    
    double[][] dataVisualisasi; 
    int[][][] warnaVisualisasi;
    double skalaXY, skalaZ, minX, maxX, minY, maxY, minZ, maxZ, rangeZ;
    float startX, startY, bottomZ;
    float rtri = 30.0f;
    float rtri2 = 50.0f;
    float saveRtri = 0, saveRtri2 = 0, x1 = 0, x2 = 0, y1 = 0, y2 = 0;
    double rangeGridX, rangeGridY, rangeGridZ;
    float transX = 0.0f, transY = 0.0f;
    float scale = 1.0f;
    TextRenderer t = new TextRenderer(new Font("SansSerif", Font.PLAIN, 18));
    GLU glu;
    
    public Visualisasi3D(Frame frame){
        dataVisualisasi = frame.dataVisualisasi;
        warnaVisualisasi = frame.warnaVisualisasi;
        minX = frame.minX;
        minY = frame.minY;
        maxX = frame.maxX;
        maxY = frame.maxY;
        minZ = frame.panelButton.minZ;
        maxZ = frame.panelButton.maxZ;
        rangeZ = frame.panelButton.rangeZ;
        skalaXY = 1.6 / dataVisualisasi[0].length;
        skalaZ = 1.0 / rangeZ;
        startX = -0.8f;
        startY = (float) ((dataVisualisasi.length / 2) * skalaXY);
        rangeGridX = (maxX - minX) / 10;
        rangeGridY = (maxY - minY) / 10;
        rangeGridZ = rangeZ / 10;
    }
    @Override
    public void init(GLAutoDrawable drawable) {
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();
//        gl.glDepthRangef(0.0f, 10.0f);
    }
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if(height == 0) height = 1;
//        float aspect = (float) (width / height);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
//        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
//        gl.glOrthof(-1f, 1f, -1f, 1f, -10f, 10f);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2(); 
        gl.glClear (GL2.GL_COLOR_BUFFER_BIT |  GL2.GL_DEPTH_BUFFER_BIT );  
//        gl.glDepthRangef(0.0f, 10.0f);
        
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL2.GL_LEQUAL);
//        gl.glDepthRange(0.0f, 1.0f);
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(-1f, 1f, -1f, 1f, 5, -5);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        gl.glClearColor(0.992f, 0.956f, 0.875f, 1.0f);
//        gl.glClearColor(1, 1, 1, 1);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        
        // MATRIX TRANSLASI
        gl.glTranslatef(transX, transY, 0.0f);
        
        // MATRIX SCALING
        gl.glScalef(scale, scale, scale);
        
        // MATRIX ROTASI
        gl.glRotatef(rtri2 + saveRtri2 + (y2 - y1), 1.0f, 0, 0);
        gl.glRotatef(rtri + saveRtri + (x2 - x1), 0, 0, 1.0f);
        
        gl.glLineWidth(1.0f);
        
        gl.glColor3ub((byte) 0, (byte) 0, (byte) 0);
        // VISUALISASI INTERPOLASI
        for(int i = 0; i < dataVisualisasi.length - 1; i++){
            for(int j = 0; j < dataVisualisasi[0].length - 1; j++){
                if(dataVisualisasi[i][j] != -999999.0f){
                    if(dataVisualisasi[i + 1][j] != -999999.0f && 
                            dataVisualisasi[i][j + 1] != -999999.0f && 
                            dataVisualisasi[i + 1][j + 1] != -999999.0f){
//                        gl.glColor3ub((byte)warnaVisualisasi[i][j][0],
//                                (byte)warnaVisualisasi[i][j][1],
//                                (byte)warnaVisualisasi[i][j][2]);
                        gl.glBegin(GL2.GL_LINES);
                        gl.glVertex3f(startX + (float) (j * skalaXY), 
                                startY - (float) (i * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i][j] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) ((j + 1) * skalaXY), 
                                startY - (float) (i * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i][j] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) ((j + 1) * skalaXY), 
                                startY - (float) (i * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i][j] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) ((j + 1) * skalaXY), 
                                startY - (float) ((i + 1) * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i + 1][j + 1] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) ((j + 1) * skalaXY), 
                                startY - (float) ((i + 1) * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i + 1][j + 1] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) (j * skalaXY), 
                                startY - (float) (i * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i][j] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) (j * skalaXY), 
                                startY - (float) (i * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i][j] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) ((j) * skalaXY), 
                                startY - (float) ((i + 1) * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i + 1][j] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) ((j) * skalaXY), 
                                startY - (float) ((i + 1) * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i + 1][j] - minZ) * skalaZ)));
                        gl.glVertex3f(startX + (float) ((j + 1) * skalaXY), 
                                startY - (float) ((i + 1) * skalaXY), 
                                -1 * (0.6f - (float) ((dataVisualisasi[i + 1][j + 1] - minZ) * skalaZ)));
                        gl.glEnd();
                    }
                }
            }
        }
        
//        // GAMBAR UNTUK GARIS 
//        gl.glLineWidth(12.0f);
//        
//        gl.glBegin(GL2.GL_LINES);
//        gl.glColor3ub((byte) 255, (byte) 0, (byte) 0);
//        gl.glVertex3f(startX, -startY, -0.4f);
//        gl.glVertex3f(startX, -startY, 0.6f);
//        gl.glColor3ub((byte) 0, (byte) 0, (byte) 0);
//        gl.glVertex3f(startX, startY, -0.4f);
//        gl.glVertex3f(startX, startY, 0.6f);
//        gl.glEnd();
//        
//        gl.glColor3ub((byte) 0, (byte) 255, (byte) 0);
//        gl.glBegin(GL2.GL_LINES);
//        gl.glVertex3f(startX, startY, -0.4f);
//        gl.glVertex3f(startX, -startY, -0.4f);
//        gl.glColor3ub((byte) 0, (byte) 0, (byte) 0);
//        gl.glVertex3f(startX, startY, 0.6f);
//        gl.glVertex3f(startX, -startY, .6f);
//        gl.glEnd();
//        
//        gl.glColor3ub((byte) 0, (byte) 0, (byte) 255);
//        gl.glBegin(GL2.GL_LINES);
//        gl.glVertex3f(startX, startY, -0.4f);
//        gl.glVertex3f(-startX, startY, -0.4f);
//        gl.glColor3ub((byte) 0, (byte) 0, (byte) 0);
//        gl.glVertex3f(startX, startY, 0.6f);
//        gl.glVertex3f(-startX, startY, 0.6f);
//        gl.glEnd();
//
//        gl.glLineWidth(1.0f);
//        
//        // GAMBAR UNTUK INDEKS
//        t.setColor(Color.BLACK);
//        for(int i = 0; i <= 10; i++){
//            t.begin3DRendering();
//            t.draw3D("" + (minX + i * rangeGridX), (float) (startX + (i * 0.16) - 0.02f), startY + 0.03f, -0.4f, 0.001f);
//            t.end3DRendering();
//            gl.glBegin(GL2.GL_LINES);
//            gl.glVertex3f((float) (startX + i * 0.16), startY, 0.6f);
//            gl.glVertex3f((float) (startX + i * 0.16), -startY, 0.6f);
//            gl.glVertex3f((float) (startX + i * 0.16), startY, 0.6f);
//            gl.glVertex3f((float) (startX + i * 0.16), startY, -0.4f);
//            gl.glEnd();
//        }
//        for(int i = 0; i <= 10; i++){
//            t.begin3DRendering();
//            t.draw3D("" + (minY + i * rangeGridY), startX - 0.08f, (float) (-startY + i * (2 * startY) / 10) - 0.01f, -0.4f, 0.001f);
//            t.end3DRendering();
//            gl.glBegin(GL2.GL_LINES);
//            gl.glVertex3f(startX, (float) (-startY + i  * (2 * startY) / 10), 0.6f);
//            gl.glVertex3f(-startX, (float) (-startY + i * (2 * startY) / 10), 0.6f);
//            gl.glVertex3f(startX, (float) (-startY + i  * (2 * startY) / 10), 0.6f);
//            gl.glVertex3f(startX, (float) (-startY + i  * (2 * startY) / 10), -0.4f);
//            gl.glEnd();
//        }
//        for(int i = 0; i <= 10; i++){
//            t.begin3DRendering();
//            t.draw3D("" + (minZ + i * rangeGridZ), startX + 0.01f, -startY - 0.03f, 0.6f - (float) (i * 0.1), 0.001f);
//            t.end3DRendering();
//            gl.glBegin(GL2.GL_LINES);
//            gl.glVertex3f(startX, startY, 0.6f - (float) (i * 0.1));
//            gl.glVertex3f(-startX, startY, 0.6f - (float) (i * 0.1));
//            gl.glVertex3f(startX, startY, 0.6f - (float) (i * 0.1));
//            gl.glVertex3f(startX, -startY, 0.6f - (float) (i * 0.1));
//            gl.glEnd();
//        }
        gl.glPopMatrix();
        gl.glLineWidth(12.0f);
        
//        // GAMBAR BUAT KETERANGAN
//        gl.glBegin(GL2.GL_LINES);
//        gl.glColor3f(0, 0, 1);
//        gl.glVertex3f(-0.97f, -0.9f, 0);
//        gl.glVertex3f(-0.87f, -0.9f, 0);
//        gl.glEnd();
//        t.begin3DRendering();
//        t.setColor(new Color(0, 0, 255));
//        t.draw3D("X", -0.86f, -0.905f, 0, 0.001f);
//        t.end3DRendering();
//        
//        gl.glBegin(GL2.GL_LINES);
//        gl.glColor3f(0, 1, 0);
//        gl.glVertex3f(-0.77f, -0.9f, 0);
//        gl.glVertex3f(-0.67f, -0.9f, 0);
//        gl.glEnd();
//        t.begin3DRendering();
//        t.setColor(new Color(0, 255, 0));
//        t.draw3D("Y", -0.66f, -0.905f, 0, 0.001f);
//        t.end3DRendering();
//        
//        gl.glBegin(GL2.GL_LINES);
//        gl.glColor3f(1, 0, 0);
//        gl.glVertex3f(-0.57f, -0.9f, 0);
//        gl.glVertex3f(-0.47f, -0.9f, 0);
//        gl.glEnd();
//        t.begin3DRendering();
//        t.setColor(new Color(255, 0, 0));
//        t.draw3D("Z", -0.46f, -0.905f, 0, 0.001f);
//        t.end3DRendering();
    }
}
