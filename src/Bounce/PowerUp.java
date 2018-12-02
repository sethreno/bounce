/*
 * PowerUp.java
 *
 * Created on August 7, 2004, 7:04 PM
 */

package Bounce;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author  BRENO
 */
public class PowerUp extends Actor implements IPaintable {
    public static final int GROW = 0;
    public static final int SHRINK = 1;
    //public static final int SPEED = 2;
    //public static final int SLOW = 3;
    public static final int SHOOT = 3;
    public static final int SPLIT = 2;
    public static final int MAX = 3;
    
    private int type;
    
    private boolean activated = false;
    
    /** Creates a new instance of PowerUp */
    public PowerUp(int newX, int newY) {
    	Random generator = new Random();
        type = generator.nextInt(MAX);
        this.setColor(Color.red);
        this.setWidth(20);
        this.setHeight(20);
        this.setX(newX);
        this.setY(newY);
        this.setXSpeed(0);
        this.setYSpeed(200);
        this.setVisible(true);
    }

    public void paint(Graphics g){
        if (this.getVisible()){
            switch(type){
                case GROW:
                    g.setColor(Color.green);                    
                    break;
                case SHRINK:
                    g.setColor(Color.red);
                    break;
                case SHOOT:
                    g.setColor(Color.YELLOW);
                    break;
                case SPLIT:
                    g.setColor(Color.CYAN);
                    break;
            }
            g.fillOval((int)this.getX(),(int)this.getY(),(int)this.getWidth(), (int)this.getHeight());
        }
    }
    
     public void act(long delta){
        super.act(delta);
    }

    public void activatePowerUp(BounceGame game){
    	//make sure the power up only gets activated once
    	if (activated){
    		return;
    	}
    	activated = true;
    	
        Ball tmpBall;
        Ball newBall;
        game.player.Flash();
        switch(type){
            case GROW:
                game.player.setWidth(BounceGame.BASE_PLAYER_WIDTH + BounceGame.BASE_PLAYER_WIDTH );
                break;
            case SHRINK:
                game.player.setWidth(BounceGame.BASE_PLAYER_WIDTH / 2);
                break;
            case SHOOT:
                if (game.player.getGunEnabled()){
                    game.player.setShotInterval(game.player.getShotInterval() - 50);
                }
                game.player.setGunEnabled(true);
                System.out.println(game.player.getShotInterval());
                break;
            case SPLIT:
                tmpBall = (Ball)game.balls.get(0);
                newBall = new Ball(tmpBall.getX(), tmpBall.getY(), tmpBall.getWidth());
                newBall.setXSpeed(tmpBall.getXSpeed() + 10);
                newBall.setYSpeed(tmpBall.getYSpeed());
                game.balls.add(newBall);
                break;
        }
    }
}
