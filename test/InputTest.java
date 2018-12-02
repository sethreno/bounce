
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JTextField;
/**
 *
 * @author  breno
 */
public class InputTest implements KeyListener {
    private GraphicsEnvironment graphicsEnvironment;
    private GraphicsDevice graphicsDevice;
    private DisplayMode originalDisplayMode;
    private BufferStrategy bufferStrategy;
    private JFrame frame;
    private boolean running = true;
    private String playerName = "Player1";
    
    /** Creates a new instance of InputTest */
    public InputTest() {
        //get the graphics environment and store the current display mode
        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        DisplayMode displayModes[] = graphicsDevice.getDisplayModes();
        DisplayMode originalDisplayMode = graphicsDevice.getDisplayMode();
        
        //create the window
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        final JTextField text = new JTextField(20);
        text.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    playerName = text.getText();
                }
            });

        //text.addKeyListener(this);
        text.setBounds(20,20, 100, 20);
        text.setLocation(380, 280);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(text);
              
        try {
          //switch to full screen mode
          if (graphicsDevice.isFullScreenSupported()) {
                graphicsDevice.setFullScreenWindow(frame);
          }
          
          // Set the dislay mode
          DisplayMode displayMode = new DisplayMode(800,600,32,60);
          if (graphicsDevice.isDisplayChangeSupported()) {
            graphicsDevice.setDisplayMode(displayMode);
          }
          
          //crate the BufferStrategy, used for double buffering.
          frame.createBufferStrategy(2);
          bufferStrategy = frame.getBufferStrategy();
          
          //animation and movement timer
          long lastLoopTime = System.currentTimeMillis();
          long delta = System.currentTimeMillis() - lastLoopTime;
        
          // rendering loop
          while (running) {     
            Graphics g = bufferStrategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0,0, 800, 600);
            
            g.setColor(Color.white);
            g.drawString(playerName, 10, 15);
            
            g.translate(380, 280);
            text.paint(g);
            g.translate(0,0);
            
            bufferStrategy.show();
            g.dispose();
            Thread.sleep(20);
          }
        } catch (Exception e) {
        } finally {
          graphicsDevice.setDisplayMode(originalDisplayMode);
          graphicsDevice.setFullScreenWindow(null);
          //saveHighScores();
        }        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InputTest test = new InputTest();
        System.exit(0);
    }
    
    public void keyPressed(java.awt.event.KeyEvent e) {
        //exit the game
        if (e.getKeyCode() == e.VK_ESCAPE){
            running = false;
        }
    }
    
    public void keyReleased(java.awt.event.KeyEvent e) {
    }
    
    public void keyTyped(java.awt.event.KeyEvent e) {
    }    
}
