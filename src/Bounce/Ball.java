/*
 * Ball.java
 *
 * Created on July 25, 2004, 5:57 PM
 */
package Bounce;
import java.awt.Color;
import java.awt.Graphics;

public class Ball extends Actor implements IPaintable {
    
    /**
     * Moves the ball
     */
    public void act(long delta){

        // bounce off the screen border.
        if (this.getX() < 0){
            this.setX(this.getX() + this.getWidth());
            this.setXSpeed(-this.getXSpeed());
        }
        if (this.getX() + this.getWidth() > BounceGame.WIDTH){
            this.setX(this.getX() - this.getWidth());
            this.setXSpeed(-this.getXSpeed());
        }
        if ((int)this.getY() < 0){
            this.setY(this.getHeight());
            this.setYSpeed(-this.getYSpeed());            
        }
        super.act(delta);
    }
    
    /**
     * Displays the ball
     * @param g A reference to the Graphics object you want to use to paint the ball.
     */
    public void paint(Graphics g){
        g.setColor(this.getColor());
        g.fillOval((int)this.getX(), (int)this.getY(), (int)this.getWidth(),  (int)this.getHeight());
        //g.setColor(Color.white);
        //g.drawOval((int)this.getX(), (int)this.getY(), (int)this.getWidth(),  (int)this.getHeight());
    }
    
    /**
     * Constructor
     * @param newX The horizontal position of the ball.
     * @param newY The vertical position of the ball
     * @param newWidth This value sets the width & height of the ball. aka Diameter
     */
    public Ball(int newX, int newY, int newWidth) {
        this.setX(newX);
        this.setY(newY);
        this.setWidth(newWidth);
        this.setHeight(newWidth);
        this.setColor(Color.blue);
        this.setVisible(true);
    }
}
