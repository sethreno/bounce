/*
 * FullscreenTest.java
 *
 * Created on August 6, 2004, 1:43 PM
 */

package Bounce;

import input.ICommandReciever;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JTextField;

public class BounceGame extends Game implements ICommandReciever {

	// screen constants
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 800;
	public static final int COLOR_DEPTH = 32;
	public static final int REFRESH_RATE = 60;

	// game object constants
	public static final int PLAYER_START_X = WIDTH - WIDTH / 4;
	public static final int PLAYER_START_Y = HEIGHT - HEIGHT / 10;
	public static final int BASE_PLAYER_HEIGHT = HEIGHT / 30;
	public static final int BASE_PLAYER_WIDTH = WIDTH / 8;
	public static final int PLAYER_WIDTH_INCREASE = WIDTH / 8;
	public static final int PLAYER_WIDTH_DECREASE = WIDTH / 16;
	public static final int MAX_PLAYER_WIDTH = WIDTH / 2;
	public static final int MIN_PLAYER_WIDTH = WIDTH / 32;
	public static final int BASE_PLAYER_SPEED = WIDTH / 2;
	public static final int PLAYER_SPEED_INCREASE = 10;
	public static final int MAX_PLAYER_SPEED = WIDTH;

	public static final int BALL_START_X = WIDTH / 2 - WIDTH / 40;
	public static final int BALL_START_Y = HEIGHT / 2;
	public static final int BASE_BALL_SIZE = WIDTH / 80;
	public static final int BASE_BALL_SPEED = WIDTH / 4;
	public static final int BALL_SPEED_INCREASE = WIDTH / 40;
	public static final int MAX_BALL_SPEED = WIDTH / 2;

	public static final int BASE_SHOT_DELAY = 500;
	public static final int SHOT_DELAY_DECREASE = 200;
	public static final int MIN_SHOT_DELAY = 100;

	public static final int POWER_UP_FREQUENCY = 10;
	public static final int SCORE_MULTIPLIER = 50;

	public static final int FONT_SIZE = WIDTH / 40;
	public static final String FONT_TYPE = "Sans Serif";

	// game state
	public ArrayList<Ball> balls = new ArrayList<Ball>();
	public ArrayList<Tile> tiles = new ArrayList<Tile>();
	public ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	public ArrayList<Shot> shots = new ArrayList<Shot>();
	public Player player = new Player(PLAYER_START_X, PLAYER_START_Y, BASE_PLAYER_WIDTH, BASE_PLAYER_HEIGHT);
	public int playerSpeed = BASE_PLAYER_SPEED;
	public int ballSpeed = BASE_BALL_SPEED;
	public int ballCount = 3;
	private int level = 1;
	private long score = 0;
	private HighScores highScores = new HighScores();
	private String playerName = "Poser";

	private boolean waitForEnter = false;
	private String message = "";
	private boolean showFPS = true;
	private boolean isFullscreen = true;

	public BounceGame() {
		super(WIDTH, HEIGHT, COLOR_DEPTH, REFRESH_RATE);
	}

	/**
	 * The entry point for the game.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		BounceGame game = new BounceGame();
		game.start();
		System.exit(0);
	}

	public void load() {
		highScores.loadHighScores();
		Audio.loadClips();
		doIntro();
		initGame();
	}

	public void update(long delta) {
		move(delta);
		doLogic();
	}

	public void unload() {
		highScores.saveHighScores();
	}

	/** move all the objects & check for collision */
	private void move(long delta) {
		// move the player
		player.act(delta);

		// check for player / powerUp collisions
		for (PowerUp p : powerUps) {
			// move the power ups
			p.act(delta);

			if (Physics.checkCollision(player, p)) {
				p.activatePowerUp(this);
				Audio.playClip("playerHit");
				player.Flash();
				p.setVisible(false);
				score += (SCORE_MULTIPLIER * 2) * level;
			}
		}

		// move the shots
		for (Shot s : shots) {
			s.act(delta);

			// check for shot / tile collisions
			for (Tile t : tiles) {
				if (Physics.checkCollision(s, t)) {
					t.Break();
					score = score + level * SCORE_MULTIPLIER;
					s.setVisible(false);
				}
			}
		}

		// move the balls
		for (Ball b : balls) {
			b.act(delta);

			// check for ball / player collisions
			if (Physics.checkCollision(b, player)) {
				// set the bottom of the ball to the top of the player to prevent the ball
				// from passing through the player
				b.setY(player.getY() - b.getHeight());

				// reverse the absolute value to make sure it always bounces up
				b.setYSpeed(-Math.abs(b.getYSpeed()));

				// get the x coordinate of the middle of the player
				int middleX = player.getX() + player.getWidth() / 2;

				// get the difference between where the ball hit and the middle
				int distanceFromMiddle = b.getX() - middleX;

				// if the within middle (+ or - 20%) dont change the x speed
				if (!(Math.abs(distanceFromMiddle) <= 20)) {

					// not in the middle so calculate the ange based on the distance from the middle
					float angleMultiplyer = ((float) distanceFromMiddle / (float) player.getWidth())
							+ Math.signum(distanceFromMiddle);

					// set the new x speed
					int newXSpeed = (int) (ballSpeed * angleMultiplyer);

					b.setXSpeed(newXSpeed);
				}

				// make the player flash
				player.Flash();
			}

			// check for ball / tile collisions
			for (Tile t : tiles) {
				if (Physics.checkCollision(b, t)) {
					// bounce the ball off the tile
					Physics.bounceFirst(b, t);

					// break the tile
					t.Break();

					score += SCORE_MULTIPLIER * level;

					// maybe create a new power up
					Random generator = new Random();
					if (generator.nextInt(POWER_UP_FREQUENCY) == 1) {
						powerUps.add(new PowerUp(t.getX(), t.getY()));
					}
				}
			}
		}
	}

	/**
	 * Display the game world
	 * 
	 * @param g The graphics context of the main window.
	 */
	public void paint(Graphics g) {
		// draw the player
		player.paint(g);
		String gameInfo;
		gameInfo = "Balls Remaining: " + String.valueOf(ballCount);
		g.drawString(gameInfo, WIDTH / 80, HEIGHT / 60);
		gameInfo = "Score: " + String.valueOf(score);
		g.drawString(gameInfo, (WIDTH - g.getFontMetrics().stringWidth(gameInfo)) / 2, HEIGHT / 60);
		gameInfo = "Level: " + String.valueOf(level);
		g.drawString(gameInfo, WIDTH - WIDTH / 10, HEIGHT / 60);
		if (showFPS) {
			String strFPS = "FPS: " + this.getFramesPerSecond();
			g.drawString(strFPS, WIDTH / 80, HEIGHT - HEIGHT / 60);
		}
		PowerUp tmpPowerUp;
		for (int i = 0; i < powerUps.size(); i++) {
			tmpPowerUp = (PowerUp) powerUps.get(i);
			tmpPowerUp.paint(g);
		}

		// draw the shots
		Shot tmpShot;
		for (int i = 0; i < shots.size(); i++) {
			tmpShot = (Shot) shots.get(i);
			tmpShot.paint(g);
		}

		// draw the balls
		Ball tmpBall;
		for (int i = 0; i < balls.size(); i++) {
			tmpBall = (Ball) balls.get(i);
			tmpBall.paint(g);
		}

		// draw the tiles
		Tile tmpTile;
		for (int j = 0; j < tiles.size(); j++) {
			tmpTile = (Tile) tiles.get(j);
			tmpTile.paint(g);
		}
		if (this.getPaused()) {
			g.setColor(Color.white);
			g.drawString(message, (WIDTH - g.getFontMetrics().stringWidth(message)) / 2, HEIGHT / 2 - HEIGHT / 10);
			g.drawString("Press start to continue.",
					(WIDTH - g.getFontMetrics().stringWidth("Press start to continue.")) / 2, HEIGHT / 2 - HEIGHT / 20);
		}
	}

	/**
	 * Performs all the Game Logic.
	 */
	public void doLogic() {
		// check to see if the ball is past the player
		Ball tmpBall;
		for (int i = 0; i < balls.size(); i++) {
			tmpBall = (Ball) balls.get(i);
			if (tmpBall.getY() > HEIGHT) {
				balls.remove(i);
				Audio.playClip("playerHit");
				if (balls.size() == 0) {
					notifyDeath();
					return;
				}
			}
		}

		// check to see if all the tiles are gone
		Tile tmpTile;
		int tileCount = 0;
		for (int i = 0; i < tiles.size(); i++) {
			tmpTile = (Tile) tiles.get(i);
			if (tmpTile.getVisible()) {
				tileCount++;
			}
		}
		if (tileCount == 0) {
			// TODO play a sound
			notifyLevelOver();
		}

		// remove shots that go off the screen.
		Shot tmpShot;
		for (int i = 0; i < shots.size(); i++) {
			tmpShot = (Shot) shots.get(i);
			if (tmpShot.getY() < 0) {
				shots.remove(i);
			}
		}

		// remove power ups that are off the screen
		PowerUp p;
		for (int i = 0; i < powerUps.size(); i++) {
			p = powerUps.get(i);
			if (p.getY() > HEIGHT) {
				powerUps.remove(i);
			}
		}
	}

	/**
	 * Notifies the Game that the Player has died.
	 */
	public void notifyDeath() {
		// create a new ball and wait for a key press
		ballCount--;
		if (ballCount < 0) {
			notifyGameOver();
		}
		initActors();
		message = "Try again";
		this.setPaused(true);
	}

	/**
	 * Notifies the Game that the game is over.
	 */
	public void notifyGameOver() {
		doGameOver();
	}

	/**
	 * Notifies the Game that the current Level is complete.
	 */
	public void notifyLevelOver() {
		level++;
		message = "Level " + level;
		initLevel();
		this.setPaused(true);
	}

	/**
	 * Itiitializes all the Actors
	 */
	public void initActors() {
		player.setX(PLAYER_START_X);
		player.setY(PLAYER_START_Y);
		player.setGunEnabled(false);
		if (playerSpeed < MAX_PLAYER_SPEED) {
			playerSpeed = BASE_PLAYER_SPEED + PLAYER_SPEED_INCREASE * level;
		}
		player.setWidth(BASE_PLAYER_WIDTH);
		shots.clear();
		balls.clear();
		powerUps.clear();

		// create some balls
		Ball ball = new Ball(BALL_START_X, BALL_START_Y, BASE_BALL_SIZE);
		ballSpeed = BASE_BALL_SPEED + level * BALL_SPEED_INCREASE;
		ball.setYSpeed(ballSpeed);
		ball.setXSpeed(ballSpeed);
		balls.add(ball);
	}

	/**
	 * Initializes the Game
	 */
	public void initGame() {
		this.setPaused(true);
		ballCount = 3;
		level = 1;
		score = 0;
		initActors();
		initLevel();

	}

	/**
	 * Initialize the current Level
	 */
	public void initLevel() {
		initActors();

		// load the level from file
		tiles = LevelManager.getTiles(level);
	}

	/**
	 * Display the title screen and init the game.
	 */
	public void doIntro() {
		// frame.requestFocus();
		initGame();

		String title = "BOUNCE";
		String producedBy = "A Seth Reno production";
		String pressAnyKey = "Press start to begin";
		this.setPaused(true);
		try {
			while (this.getPaused()) {
				screen.clear();

				Graphics g = screen.getGraphics();

				g.setColor(Color.white);

				Font bigFont = new Font(FONT_TYPE, Font.ITALIC, WIDTH / FONT_SIZE * 3);
				g.setFont(bigFont);
				g.drawString(title, (WIDTH - g.getFontMetrics().stringWidth(title)) / 2, HEIGHT / 2 - HEIGHT / 10);

				Font medFont = new Font(FONT_TYPE, Font.ITALIC, WIDTH / (int) (FONT_SIZE * 1.5));
				g.setFont(medFont);
				g.drawString(producedBy, (WIDTH - g.getFontMetrics().stringWidth(producedBy)) / 2, HEIGHT / 2);

				Font smallFont = new Font(FONT_TYPE, Font.PLAIN, WIDTH / FONT_SIZE);
				g.setFont(smallFont);
				g.drawString((pressAnyKey), (WIDTH - g.getFontMetrics().stringWidth(pressAnyKey)) / 2,
						HEIGHT / 2 + HEIGHT / 10);

				input.PollInput(this);

				screen.show();

				// do nothing
				Thread.sleep(20);
			}
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	/**
	 * display a the game over screen.
	 */
	public void doGameOver() {
		String gameOver = "Game Over Man";
		String congrads = "Congratulations you set a high score";
		String prompt = "Enter you name";
		boolean promptForName = false;
		waitForEnter = true;

		// add a text field to allow the user to enter their name.
		JTextField text;
		text = new JTextField(20);
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField text = (JTextField) e.getSource();
				playerName = text.getText();
				highScores.addNewScore(playerName, score, level);
				waitForEnter = false;
			}
		});
		text.setBounds(0, 0, WIDTH / 8, HEIGHT / 30);
		text.setLocation((WIDTH - text.getWidth()) / 2, (int) (HEIGHT * .7));
		text.setVisible(false);
		screen.AddComponent(text);

		if (highScores.isHighScore(score)) {
			text.setVisible(true);
			text.requestFocus();
			promptForName = true;
		} else {
			waitForEnter = false;
		}

		try {
			while (waitForEnter) {
				screen.clear();
				Graphics g = screen.getGraphics();

				g.setColor(Color.white);
				Font bigFont = new Font(FONT_TYPE, Font.BOLD, FONT_SIZE * 2);
				g.setFont(bigFont);
				g.drawString(gameOver, (WIDTH - g.getFontMetrics().stringWidth(gameOver)) / 2, (int) (HEIGHT * .5));
				Font medFont = new Font(FONT_TYPE, Font.BOLD, FONT_SIZE);
				g.setFont(medFont);
				if (promptForName) {
					g.drawString(congrads, (WIDTH - g.getFontMetrics().stringWidth(congrads)) / 2, (int) (HEIGHT * .6));
					g.drawString(prompt, (WIDTH - g.getFontMetrics().stringWidth(prompt)) / 2, (int) (HEIGHT * .65));
					g.translate(text.getLocationOnScreen().x, text.getLocationOnScreen().y);
					text.paint(g);
				}

				screen.show();
				Thread.sleep(20);
			}

			// remove the text field
			screen.RemoveComponent(text);
			text = null;

			// display the high scores
			displayScores();
			Thread.sleep(5000);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		doIntro();
	}

	private void displayScores() {

		screen.clear();
		Graphics g = screen.getGraphics();

		String hof = "Hall Of Fame";
		g.setColor(Color.white);
		Font bigFont = new Font(FONT_TYPE, Font.BOLD, FONT_SIZE * 2);
		g.setFont(bigFont);
		g.drawString(hof, (WIDTH - g.getFontMetrics().stringWidth(hof)) / 2, (int) (HEIGHT * .25));
		Font medFont = new Font(FONT_TYPE, Font.BOLD, FONT_SIZE);
		g.setFont(medFont);

		String strRank, strScore, strLevel, strName;
		int y = (int) (HEIGHT * .3);

		for (int i = 0; i < highScores.size(); i++) {
			strRank = String.valueOf(i + 1);
			strScore = String.valueOf(highScores.getScore(i).getScore());
			strLevel = "Level " + String.valueOf(highScores.getScore(i).getLevel());
			strName = highScores.getScore(i).getName();
			y += (int) (HEIGHT * .05);

			g.drawString(strRank, (int) (WIDTH * .2), y);
			g.drawString(strScore, (int) (WIDTH * .3), y);
			g.drawString(strLevel, (int) (WIDTH * .42), y);
			g.drawString(strName, (int) (WIDTH * .65), y);
		}
		screen.show();
	}

	public void InputEventHandler(String inputEvent) {
		if (inputEvent == "QUIT") {
			this.stop();
		} else if (inputEvent == "UP") {
			player.setYSpeed(-playerSpeed);
		} else if (inputEvent == "DOWN") {
			player.setYSpeed(playerSpeed);
		} else if (inputEvent == "LEFT") {
			player.setXSpeed(-playerSpeed);
		} else if (inputEvent == "RIGHT") {
			player.setXSpeed(playerSpeed);
		} else if (inputEvent == "SHOOT") {
			player.shoot(this);
		} else if (inputEvent == "START") {
			this.togglePause();
		} else if (inputEvent == "SELECT") {
			isFullscreen = !isFullscreen;
			screen.show();
			screen.close();
			this.openScreen(WIDTH, HEIGHT, COLOR_DEPTH, REFRESH_RATE, isFullscreen);
		} else if (inputEvent == "STOPX") {
			player.setXSpeed(0);
		} else if (inputEvent == "STOPY") {
			player.setYSpeed(0);
		}
	}
}
