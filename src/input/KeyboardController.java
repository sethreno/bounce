package input;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component;
import java.util.ArrayList;

public class KeyboardController {
	
	public static final int ESC = 13;
	public static final int UP = 21;
	public static final int DOWN = 23;
	public static final int LEFT = 20;
	public static final int RIGHT = 22;
	public static final int ENTER = 2;
	public static final int SPACE = 15;
	public static final int F1 = 83;

	private Controller keyboard;
	private boolean enabled = true;
	private Component comps[];
	private ArrayList<Integer> keys;

	public KeyboardController() {
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] cs = ce.getControllers();
		keyboard = findKeyboard(cs);
		comps = keyboard.getComponents();
		loadKeys(comps);		
	}
	
	public void poll(){
		if (enabled){
			keyboard.poll();
		}		
	}
	
	public boolean isKeyPressed(int key){
		if (key >= keys.size()){ return false;}
		return comps[keys.get(key)].getPollData() != 0.0f;		
	}

	private Controller findKeyboard(Controller[] cs) {
		int i = 0;
		while (i < cs.length) {
			if (cs[i].getType() == Controller.Type.KEYBOARD) {
				break;
			}
			i++;
		}

		if (i == cs.length) {
			System.out.println("No keyboard found");
			enabled = false;
			return null;
		} else {
			System.out.println("keybaord index: " + i);
		}
		return cs[i];
	}
	
	  private void loadKeys(Component[] comps)
	  {
		  keys = new ArrayList<Integer>();
		  Component c;
		  for (int i=0; i<comps.length; i++){
			  c = comps[i];
			  if (isKey(c)){
				  System.out.println(i + " " + c.getName());
				  keys.add(i);
			  }
		  }
	  }
		  	
	  private boolean isKey(Component c)
	  {
	    if (!c.isAnalog() && !c.isRelative()) {    // digital and absolute
	      String className = c.getIdentifier().getClass().getName();
	      if (className.endsWith("Key"))
	        return true;
	    }
	    return false;
	  }
}
