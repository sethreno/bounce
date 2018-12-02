package input;

public class InputManager {
	private KeyboardController keyboard;
	private GamePadController controller;
	
	public void Init(){
		keyboard = new KeyboardController();
		controller = new GamePadController();
	}	
	
	// START & SELECT commands can only be issued once every 700 milliseconds
	private static final long commandDelay = 700;
	private long lastStartTime;
	private long lastSelectTime;
	
	public void PollInput(ICommandReciever target){
		keyboard.poll();		
		boolean xStopped = true;
		boolean yStopped = true;
		if (keyboard.isKeyPressed(KeyboardController.ESC)){
			target.InputEventHandler("QUIT");
		}
		if (keyboard.isKeyPressed(KeyboardController.UP)){
			target.InputEventHandler("UP");
			yStopped = false;
		}else if (keyboard.isKeyPressed(KeyboardController.DOWN)){
			target.InputEventHandler("DOWN");
			yStopped = false;
		}
		if (keyboard.isKeyPressed(KeyboardController.LEFT)){
			target.InputEventHandler("LEFT");
			xStopped = false;
		}else if (keyboard.isKeyPressed(KeyboardController.RIGHT)){
			target.InputEventHandler("RIGHT");
			xStopped = false;
		}
		if (keyboard.isKeyPressed(KeyboardController.SPACE)){
			target.InputEventHandler("SHOOT");
		}
		if (keyboard.isKeyPressed(KeyboardController.ENTER)){
			issueStartCommand(target);
		}
		if (keyboard.isKeyPressed(KeyboardController.F1)){
			issueSelectCommand(target);
		}
		if (yStopped){
			target.InputEventHandler("STOPY");
		}
		if (xStopped){
			target.InputEventHandler("STOPX");
		}
		
		if (controller.getEnabled()){
			controller.poll();
			int direction = controller.getHatDir();
			if (direction == input.GamePadController.WEST ||
					direction == GamePadController.NW ||
					direction == GamePadController.SW){
				target.InputEventHandler("LEFT");
			} else if (direction == GamePadController.EAST ||
					direction == GamePadController.NE ||
					direction == GamePadController.SE){
				target.InputEventHandler("RIGHT");
			}
			if (direction == GamePadController.NORTH ||
					direction == GamePadController.NE ||
					direction == GamePadController.NW){
				target.InputEventHandler("UP");
			} else if (direction == GamePadController.SOUTH ||
					direction == GamePadController.SE ||
					direction == GamePadController.SW){
				target.InputEventHandler("DOWN");
			}
			
			boolean[] buttons = controller.getButtons();
			if (buttons[0] || buttons[1] || buttons[2] || buttons[3] || buttons[4] || buttons[5]){
				target.InputEventHandler("SHOOT");
			}
			
			if (buttons[8] && buttons[9]){
				target.InputEventHandler("QUIT");
			}else if (buttons[9]){
				issueStartCommand(target);
			}else if (buttons[8]){
				issueSelectCommand(target);
			}
		}
	}
	
	private void issueStartCommand(ICommandReciever target){
		if ((System.currentTimeMillis() - lastStartTime) > commandDelay){
			target.InputEventHandler("START");
			lastStartTime = System.currentTimeMillis();
		}
	}
	private void issueSelectCommand(ICommandReciever target){
		if ((System.currentTimeMillis() - lastSelectTime) > commandDelay){
			target.InputEventHandler("SELECT");
			lastSelectTime = System.currentTimeMillis();
		}
	}
}
