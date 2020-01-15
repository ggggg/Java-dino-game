package game.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

import game.main.display.Display;
import game.main.highScore.Writefile;
import game.main.input.Keyboard;
import game.main.player.Block;
import game.main.player.Player;

public class Game implements Runnable {
	// the score
	int score;
	int highScore;

	// the player and the block
	private Player player;
	private Block block;
	
	//set the ground height
	private int groundHeight;

	// the window
	public String title;
	private Display display;
	public int width, height;

	String back = "sound/back.wav";

	// If the game is running
	public boolean running = false;

	// the thread
	private Thread thread;

	// the display loader
	private BufferStrategy bs;

	// the back colors
	private Color[] c;
	private int colorNow;

	// get keyboard
	private Keyboard key;

	// the graphic
	private Graphics g;

	// Create the game
	public Game(String title, int width, int height) {
		// The window data
		this.title = title;
		this.width = width;
		this.height = height;

		// input
		key = new Keyboard();
	}

	public void init() {
		// create the window
		display = new Display(title, width, height);
		
		//set the ground height
		groundHeight=height/2;
		
		// add the window
		display.getFrame().addKeyListener(key);

		// make the player and the block
		player = new Player(this, 100, groundHeight);
		block = new Block(this, groundHeight,width,height);

		// set the colors
		c = new Color[6];
		makeColors();
		colorNow = 0;
		
		

		// read highScore
		Writefile write = new Writefile();
		highScore = write.read();
		System.out.println(highScore);

	}

	// all the colors for the background
	public void makeColors() {
		c[0] = Color.LIGHT_GRAY;
		c[1] = Color.blue;
		c[2] = Color.cyan;
		c[3] = Color.YELLOW;
		c[4] = Color.DARK_GRAY;
		c[5] = Color.green;

	}

	// Change the color
	private void nextColor() {
		if (colorNow == c.length - 1)
			colorNow = 0;
		else
			colorNow++;
	}

	// Update the variables
	private void tick() {
		// check for input
		key.tick();

		// update player and block
		player.tick();
		block.tick();

	}

	// draw to the screen
	private void render() {
		// get the display canvas
		bs = display.getCanvas().getBufferStrategy();
		// check if there is a canvas buffer
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		// set the graphics
		g = bs.getDrawGraphics();
		// Clear Screen
		g.clearRect(0, 0, width, height);

		// set background color
		g.setColor(c[colorNow]);
		// draw background
		g.fillRect(0, 0, width, height);

		// main

		// draw player and block
		player.render(g);
		block.render(g);

		// add to the score
		score += 0.01;

		// draw score
		g.setColor(Color.black);
		g.drawString("Score: " + String.valueOf(score), 10, 40);
		g.drawString("High Score: " + String.valueOf(highScore), 100, 40);
		g.drawRect(-10, groundHeight+player.getHeight(), width+20, height/2+10);
		// last

		// show the next buffer
		bs.show();

		// clear the graphic object
		g.dispose();

	}

	// Implement runnable
	@Override
	public void run() {
		// set the start variables
		init();

		// game loop
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		// start the game loop
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				// update and draw the variable
				tick();
				render();
				postRender();
				ticks++;
				delta--;
			}

			if (timer >= 1000000000) {

				ticks = 0;
				timer = 0;
				score++;
				// change the background
				if (score % 10 == 0 && score != 0) {
					System.out.println(colorNow);
					nextColor();
				}

			}

		}

		// stop everything
		stop();
	}

	// after drawing to the screen
	private void postRender() {
		// check collision
		if (player.collision()) {
			// end the game
			gameOver();
		}

	}

	// check if the game is over
	public boolean over = false;

	// when the game is over
	public void gameOver() {
		// play sound

		player.playSound(player.getOverSound());

		// check if the score is the highest
		if (score > highScore) {
			Writefile write = new Writefile();
			// Save the score
			write.write(score);
		}
		// ask if they want to play again
		int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like Contiue?", "Warning",
				JOptionPane.YES_NO_OPTION);
		// yes
		if (dialogResult == JOptionPane.YES_OPTION) {
			running = true;
			// restart
			reset(getBlock());
			key.reset();
		} // no
		else {
			// stop the program
			System.exit(1);
			stop();
		}

	}

	// reset the game
	public void reset(Block b) {
		player.reset(player);
		block.reset();
		b.setX(width*2);
		b.setSpeed(30);
		score = 0;

		// read highScore
		Writefile write = new Writefile();
		highScore = write.read();
		System.out.println(highScore);

		colorNow = 0;
		b.setSpeed(2);

	}

	// Give the keyboard controls to another class
	public Keyboard getkey() {
		return key;
	}

	// start the game
	public synchronized void start() {
		// check is already running
		if (!running) {
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	public synchronized void stop() {
		// check if already stopped
		if (running) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// give the block to another class
	public Block getBlock() {
		return block;
	}

	public void playSound(String sound) {

		try {
			// Open an audio input stream.
			URL url = this.getClass().getClassLoader().getResource(sound);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
