/*
 * GameTest.java
 *
 * Created on August 18, 2004, 11:10 AM
 */

package Bounce;

import input.ICommandReciever;
import java.awt.Color;
import java.awt.Graphics;

public class GameTest extends Game implements ICommandReciever {
	// private InputManager input;

	public static void main(String[] args) {
		GameTest game = new GameTest();
//        game.init();
		game.start();
		System.out.println("exiting...");
		System.exit(0);
	}

	public void load() {

	}

	public void update(long delta) {
		input.PollInput(this);
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("hello world", screen.getWidth() / 2, screen.getHeight() / 2);
	}

	public void unload() {

	}

	public void InputEventHandler(String inputEvent) {
		if (inputEvent == "QUIT") {
			this.stop();
		}
		if (inputEvent == "UP") {
			screen.show();
			screen.close();
			this.openScreen(800, 600, 32, 60, true);
		}
		if (inputEvent == "DOWN") {
			screen.show();
			screen.close();
			this.openScreen(800, 600, 32, 60, false);
		}
	}
}
