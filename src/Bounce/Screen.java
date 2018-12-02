/*
 * Screen.java
 *
 * Created on August 13, 2004, 12:26 PM
 */

package Bounce;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.MemoryImageSource;

import javax.swing.JFrame;

/**
 * The Screen class manages initializing and displaying fullscreen
 * graphics applications with double buffering.
 * @author Seth Reno
 */
public class Screen {
    
    private int width;
    private int height;
    private int depth;
    private int refresh;
    private boolean isFullscreen;
    private GraphicsEnvironment graphicsEnvironment;
    private GraphicsDevice graphicsDevice;
    private DisplayMode originalDisplayMode;
    private BufferStrategy bufferStrategy;
    private JFrame frame;
    
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getDepth(){return depth;}
    public int getRefresh(){return refresh;}
    public boolean getIsFullscreen(){return isFullscreen;}
    
    /**
     * Creates a new instance of Screen with the specified width, height,
     * color depth, and refresh rate.
     * @param w width of the screen resolution
     * @param h height of the screen resolution
     * @param d color depth
     * @param r refresh rate
     */
    public Screen(int w, int h, int d, int r, boolean fullscreen) {
        //get the graphics environment and store the current display mode
        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        //DisplayMode displayModes[] = graphicsDevice.getDisplayModes();
        originalDisplayMode = graphicsDevice.getDisplayMode();
        
        //create the window
        frame = new JFrame();
        frame.setUndecorated(true);
        width = w;
        height = h;
        depth = d;
        refresh = r;
        isFullscreen = fullscreen;
    }
    
    /**
     * Opens a new fullscreen window.
     */    
    public void open(){
                
        try {
            if (isFullscreen){
                frame.setIgnoreRepaint(true);
                if (graphicsDevice.isFullScreenSupported()) {
                    graphicsDevice.setFullScreenWindow(frame);
                }
                DisplayMode displayMode = new DisplayMode(width, height, depth, refresh);
                if (graphicsDevice.isDisplayChangeSupported()) {
                  graphicsDevice.setDisplayMode(displayMode);
                }
            }else{
            	frame.setBounds(0, 0, width, height);
                frame.setVisible(true);
                frame.setResizable(false);
            }                   
          
          //crate the BufferStrategy, used for double buffering.
          frame.createBufferStrategy(2);
          bufferStrategy = frame.getBufferStrategy();
          
          hideCursor();
          frame.requestFocus();
        } catch (Exception e) {
        }
    } 

    /**
     * Closes the fullscreen window
     */    
    public void close(){
    	if (isFullscreen){
//    		restore the original display mode
            if (graphicsDevice.isDisplayChangeSupported()){
                graphicsDevice.setDisplayMode(originalDisplayMode);
            }          
            graphicsDevice.setFullScreenWindow(null);
    	}
        frame.dispose();
    }
    
    /**
     * adds a KeyListener to the fullscreen window
     * @param l KeyListener
     */    
    public void addKeyListener(KeyListener l){
        frame.addKeyListener(l);
    }
    /**
     * adds a mouseListener to the fullscreen window
     * @param l MouseListener
     */    
    public void addMouseListener(MouseListener l){
        frame.addMouseListener(l);
    }
    /**
     * hides the mouse cursor
     */    
    public void hideCursor(){
        //hide the cursor - (create a new invisible cursor)
        int[] pixels = new int[16 * 16];
        Image image = Toolkit.getDefaultToolkit().createImage(
        new MemoryImageSource(16, 16, pixels, 0, 16));
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisiblecursor");
        frame.setCursor(transparentCursor);
    }
    /**
     * shows the mouse cursor
     */    
    public void showCursor(){
        frame.setCursor(Cursor.getDefaultCursor());
    }
    /**
     * returns the current graphics context of the fullscreen window.
     * @return Graphics
     */    


    private Graphics g;
    public Graphics getGraphics(){
    	if (g == null){
    		g = bufferStrategy.getDrawGraphics();
    	}
        return g;
    }
    
    public void clear(){
    	 g = getGraphics();
         g.setColor(Color.black);
         g.fillRect(0,0, width, height);
    }
    
    /**
     * updates the screen with the current graphics context.
     */    
    public void show(){
        bufferStrategy.show();
        g.dispose();
        g = null;
    }
    
    public void AddComponent(Component comp){
    	frame.getContentPane().add(comp);
    }
    
    public void RemoveComponent(Component comp){
    	frame.getContentPane().remove(comp);
    }
}
