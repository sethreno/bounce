package Bounce;
import java.awt.Color;

public class GameObject {
	private int x, y;
    private int width, height;
    private Color color;
    private boolean visible;
    
    public int getX(){ return x; }
    public void setX(int value){ x = value; }
    
    public int getY(){ return y; }
    public void setY(int value){ y = value; }
    
    public int getWidth(){ return width; }
    public void setWidth(int value){ width = value; }
    
    public int getHeight(){ return height; }
    public void setHeight(int value){ height = value; }
    
    public Color getColor() { return color; }    
    public void setColor(Color value) { color = value; }
        
    public void setVisible(boolean value){ visible = value; }
    public boolean getVisible() { return visible; }
}
