/*
 * Tile.java
 *
 * Created on July 28, 2004, 12:06 PM
 */

package Bounce;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Tile extends Actor implements IPaintable {
	
	private boolean isBreaking;
	private int breakingAnimationState;
	
	public void Break(){
		setVisible(false);
		isBreaking = true;
		Audio.playClip("tileHit");
	}
    
    /** Creates a new instance of Tile */
    public Tile(int newX, int newY) {
        this.setX(newX);
        this.setY(newY);
        this.setXSpeed(0);
        this.setYSpeed(0);
        this.setColor(Color.ORANGE);
        this.setHeight((int)(BounceGame.HEIGHT * .0333));
        this.setWidth((int)(BounceGame.WIDTH * .075));
        this.setVisible(true);
    }
    
    public void paint(Graphics g){
    	// if the tile is breaking show the breaking animation
    	if (isBreaking){
    		breakingAnimationState++;
    		if (breakingAnimationState < 20){
    			Random generator = new Random();
    			for (int i=0; i< 100 - breakingAnimationState; i++){
    				g.setColor(getColor());
    				int x = (int)getX() + generator.nextInt((int)getWidth());
    				int y = (int)getY() + generator.nextInt((int)getHeight()) + breakingAnimationState;
    				g.fillOval(x, y, 2, 2);
    			}
    		} else {
    			// the breaking animation has finished
    			isBreaking = false;
    			breakingAnimationState = 0;
    		}
    	}
    	
    	// display a normal tile
        if (this.getVisible()){
            g.setColor(this.getColor());
            g.fillRect((int)this.getX(),(int)this.getY(),(int)this.getWidth(), (int)this.getHeight());   
            //g.setColor(Color.white);
            //g.drawRect((int)this.getX(),(int)this.getY(),(int)this.getWidth(), (int)this.getHeight());
        }
    }
}
