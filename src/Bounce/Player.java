/*
 * Player.java
 *
 * Created on July 27, 2004, 6:16 PM
 */

package Bounce;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author  BRENO
 */
public class Player extends Actor implements IPaintable {
    
    private long lastShotTime;
    private long shotInterval;
    private boolean gunEnabled;
    
    public long getLastShotTime(){
        return lastShotTime;
    }
    public void setLastShotTime(long value){
        lastShotTime = value;
    }
    public long getShotInterval(){
        return shotInterval;
    }
    public void setShotInterval(long value){
        shotInterval = value;
    }
    public boolean getGunEnabled(){
        return gunEnabled;
    }
    public void setGunEnabled(boolean value){
        gunEnabled = value;
    }
        
    
    /** Creates a new instance of Player */
    public Player(int newX, int newY, int newWidth, int newHeight) {
        this.setX(newX);
        this.setY(newY);
        this.setWidth(newWidth);
        this.setHeight(newHeight);
        this.setColor(Color.blue);
        this.setVisible(true);
        this.setGunEnabled(false);
        this.setShotInterval(BounceGame.BASE_SHOT_DELAY);
        this.setLastShotTime(0);
    }
    
    private boolean isFlashing;
    private int flashingAnimationState;
    public void Flash(){
    	isFlashing = true;
    	Audio.playClip("playerHit");
    }
    
     public void paint(Graphics g){
    	 g.setColor(getColor());
    	 if (isFlashing){
    		 flashingAnimationState++;
    		 if (flashingAnimationState < 10){
    			 // make the player "flash" by setting its color to white every other frame
        		 if (flashingAnimationState % 2 == 0){
        			 g.setColor(Color.white);
        		 } 
    		 }else {
    			 // the flashing animation is over
    			 isFlashing = false;
    			 flashingAnimationState = 0;
    		 }

    	 }
        g.fillRect((int)this.getX(),(int)this.getY(),(int)this.getWidth(), (int)this.getHeight());
        g.setColor(Color.white);
        g.drawRect((int)this.getX(), (int)this.getY(), (int)this.getWidth(), (int)this.getHeight());
     }
    public void act(long delta){
        super.act(delta);
        
        // stop the player on the left border
        if (this.getX() < 0){
            this.setX(0);
        }
        // stop the player on the right border
        if (this.getX() > BounceGame.WIDTH - this.getWidth()){
            this.setX(BounceGame.WIDTH - 5 - this.getWidth());
        }
        if (this.getY() > (int)(BounceGame.HEIGHT - BounceGame.HEIGHT * .03) - this.getHeight()){
            this.setY((int)(BounceGame.HEIGHT - BounceGame.HEIGHT * .03) - this.getHeight());
        }
        if (this.getY() < 0){
            this.setY(5);
        }
    }
    public void shoot(BounceGame game){
        if (System.currentTimeMillis() - this.getLastShotTime() > this.getShotInterval() && this.getGunEnabled()){
            //create a new shot.
            Shot tmpShot = new Shot((int)(this.getX() + this.getWidth() / 2), (int)(this.getY() - 10));
            game.shots.add(tmpShot);        
            this.setLastShotTime(System.currentTimeMillis());
            Audio.playClip("shot");
        }
    }
}
