package Bounce;

import java.awt.Rectangle;

public class Physics {
	public static boolean checkCollision(GameObject obj1, GameObject obj2){
		//invisible objects cannot collide
		if (!(obj1.getVisible() && obj2.getVisible())){
			return false;
		}
		
		Rectangle r1 = new Rectangle(obj1.getX(), obj1.getY(), obj1.getWidth(), obj1.getHeight());
		Rectangle r2 = new Rectangle(obj2.getX(), obj2.getY(), obj2.getWidth(), obj2.getHeight());
		
		return r1.intersects(r2);
	}
	
	/***
	 * Changes the specified Actor's direction to make it appear to bounce
	 * @param act the Actor to bounce
	 * @param obj the GameObject to bounce off of
	 */
	public static void bounceFirst(Actor act, GameObject obj){
		//we need to determine which part of the actor hit the object
		//if it was the bottom the object should bounce up
		//if it was the top it should bounce down, etc...
		
		Rectangle r = new Rectangle(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
		
		// check for collisions w/ the top of the actor
		Rectangle top = getSubSection(act, .4f, 0f, .2f, .3f);
		if (r.intersects(top)){
			// bounce down
			act.setYSpeed(Math.abs(act.getYSpeed()));
			return;
		}
		
		// check for collisions w/ the bottom of the actor
		Rectangle bottom = getSubSection(act, .4f, .9f, .2f, .3f);
		if (r.intersects(bottom)){
			// bounce up
			act.setYSpeed(-Math.abs(act.getYSpeed()));
			return;
		}
		
		// check for collisions w/ the left of the actor
		Rectangle left = getSubSection(act, 0f, .4f, .3f, .2f);
		if (r.intersects(left)){
			// bounce right
			act.setXSpeed(Math.abs(act.getXSpeed()));
			return;
		}
		
		// check for collisions w/ the right of the actor
		Rectangle right = getSubSection(act, .9f, .4f, .3f, .2f);
		if (r.intersects(right)){
			// bounce left
			act.setXSpeed(-Math.abs(act.getXSpeed()));
			return;
		}
	}
	
	private static Rectangle getSubSection(GameObject obj, float leftMarginMultiplier, float topMarginMultiplier, float widthMultiplier, float heightMultiplier){
		float x = obj.getX() + obj.getWidth() * leftMarginMultiplier;
		float y = obj.getY() + obj.getHeight() * topMarginMultiplier;
		float width = obj.getWidth() * widthMultiplier;
		float height = obj.getHeight() * heightMultiplier;
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
}
