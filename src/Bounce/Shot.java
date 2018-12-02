/*
 * Shot.java
 *
 * Created on August 9, 2004, 3:50 PM
 */

package Bounce;
import java.awt.Color;
import java.awt.Graphics;

public class Shot extends Actor implements IPaintable {
       
    /** Creates a new instance of Shot */
    public Shot(int newX, int newY) {
        this.setX(newX);
        this.setY(newY);
        this.setXSpeed(1);
        this.setYSpeed(-400);
        this.setColor(Color.YELLOW);
        this.setWidth(10);
        this.setHeight(10);
        this.setVisible(true);
    }

    public void paint(Graphics g){
        if (this.getVisible()){
            g.setColor(this.getColor());
            g.fillOval((int)this.getX(),(int)this.getY(),(int)this.getWidth(), (int)this.getHeight());
        }    
    }
}
