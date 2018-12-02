package Bounce;

import input.ICommandReciever;
import input.InputManager;

import java.awt.Graphics;

public abstract class Game implements ICommandReciever {
	protected InputManager input;
	protected Screen screen;
	private boolean gameRunning = true;

	private boolean paused = false;

	public boolean getPaused() {
		return paused;
	}

	public void setPaused(boolean value) {
		paused = value;
	}

	private long lastLoopTime;
	private long delta;
	private long framesPerSecond;
	private long millisecondsToSleep;
	private static final long targetMilliseconds = 1000 / 60; // 60 FPS

	public long getFramesPerSecond() {
		return framesPerSecond;
	}

	public Game() {
		// the default for a game is 800 x 600, 32bit, 60hz, windowed
		screen = new Screen(800, 600, 32, 60, false);
	}

	/** Creates a new instance of Game */
	public Game(int width, int height, int depth, int refresh) {
		screen = new Screen(width, height, depth, refresh, false);
	}

	protected void openScreen(int width, int height, int depth, int refresh, boolean fullscreen) {
		screen = new Screen(width, height, depth, refresh, fullscreen);
		screen.open();
	}

	public void start() {
		input = new InputManager();
		input.Init();
		screen.open();
		load();
		lastLoopTime = System.currentTimeMillis();
		while (gameRunning) {
			// update the timer
			delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			screen.clear();
			input.PollInput(this);
			if (!paused) {
				update(delta);
			}

			paint(screen.getGraphics());

			screen.show();
			try {
				millisecondsToSleep = delta % targetMilliseconds;
				Thread.sleep(millisecondsToSleep);
				if (delta > 0) {
					framesPerSecond = 1000 / delta;
				}
			} catch (InterruptedException e) {
				// calling sleep may thorow an exception
				// we can safely ignore
			}
		}
		unload();
		screen.close();
	}

	public abstract void load();

	/** move all the objects & check for collision */
	public abstract void update(long delta);

	public abstract void paint(Graphics g);

	public abstract void unload();

	public void togglePause() {
		paused = !paused;
	}

	public void stop() {
		gameRunning = false;
	}
}
