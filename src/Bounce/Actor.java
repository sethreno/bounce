/*
 * Actor.java
 *
 * Created on July 27, 2004, 11:36 AM
 */

package Bounce;

public class Actor extends GameObject {
	private int xSpeed, ySpeed;

	public int getXSpeed() {
		return xSpeed;
	}

	public void setXSpeed(int value) {
		xSpeed = value;
	}

	public int getYSpeed() {
		return ySpeed;
	}

	public void setYSpeed(int value) {
		ySpeed = value;
	}

	public void doLogic() {
	}

	public void act(long delta) {
		// update the location of the entity based on move speeds
		setX((int) (getX() + (delta * xSpeed) / 1000));
		setY((int) (getY() + (delta * ySpeed) / 1000));
	}
}
