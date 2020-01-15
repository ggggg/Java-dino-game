package game.main.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import game.main.Game;
import game.main.gfx.ImageLoader;

public class Player {

	String jump = "sound/download.wav", overSound = "sound/over.wav";

	// is the game over
	public static boolean over;

	// size
	int width, height;

	// the player image
	private BufferedImage img;

	// is jumping
	private boolean jumping = false;

	// the jump power
	float power;

	// the poison
	private float x, y, startingY;

	// the game class
	Game game;
	
	//set the ground height
	int groundHeight;

	// the contractor
	public Player(Game game, float x, float y) {
		// save the x and y to the class
		this.x = x;
		this.y = y;
		startingY = y;

		// save the game object
		this.game = game;
		power = 0;

		
		// starting size
		width = 32;
		height = 32;

		// the image
		img = ImageLoader.loadImage("/textures/player.png");

	}

	public int getHeight() {
		return height;
	}

	// check if the sound was already played
	private boolean soundplayed = false;

	// change the variables
	public void tick() {

		// if not on ground
		if (y <= startingY) {
			// go back slowly to the ground
			y += power;
			power += 0.1;

		} else {
			// make sure that the player stays on the ground
			y = startingY;
			power = 0;
			jumping = false;
		}

		// if the space is pressed
		if (game.getkey().space && !jumping) {
			jumping = true;

			// set the jump speed
			power = (float) -3.230;
			y += power;

			// play the sound
			playSound(jump);

		}

		// if the down key is pressed
		if (game.getkey().down && !jumping) {

			// set the y
			y = startingY + 20;

			// play the sound
			if (!soundplayed)
				playSound(jump);

		}
		// check if the sound was played
		if (game.getkey().down) {
			soundplayed = true;
		} else if (!jumping) {
			y = startingY;
			soundplayed = false;
		}

	}

	// draw the player
	public void render(Graphics g) {
//		 g.setColor(Color.blue);
//		g.fillRect((int) x+5, (int) y, width - 5, height);
		g.drawImage(img, (int) x, (int) y, null);
	}

	// get the collision box
	public Rectangle getBounds() {
		return new Rectangle((int) x+5, (int) y, width - 5, height);
	}

	// check for collision
	public boolean collision() {
		return game.getBlock().getBounds().intersects(getBounds());
	}

	// reset the player
	public void reset(Player p) {
		jumping = false;
		p.y = p.startingY;
		game.getkey().down = false;
	}

	// play the sound
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

//get the sound
	public String getOverSound() {
		return overSound;
	}
}
