package game.main.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import game.main.Game;

public class Block {

	// the size of the block
	int width, height;

	// speed
	float speed;

	// the position
	private float x, y, startingX, startingY;

	// the main game object
	private Game game;

	// the random
	private Random rnd;
	private int random;

	//is the block flying
	private boolean flying;

	//Contractor
	public Block(Game game, float y,int screenW,int screenH) {
		
		//set the starting position
		startingX = screenW+10;
		this.setX(startingX);
		startingY = y;
		this.y = y;
		
		//set the staring size
		width = 16;
		height = width * 2;
		
		//set the game code
		this.game = game;
		
		//set staring speed
		speed = 2;
		
		//create the random
		rnd = new Random();
		
		//not flying
		flying = false;
		
	}

	
	//change the size
	public void changeSize() {
		
		//get a random number to change if its flying or not
		random = rnd.nextInt(10);
		System.out.println(random);
		
		//if flying
		if (random % 3 == 0) {
			//set the y
			y = startingY - rnd.nextInt(20);
			flying = true;
			System.out.println("c");
		}
		//if not flying
		else {
			flying = false;
			y = startingY;
			height = rnd.nextInt(20) + 20;
			width =rnd.nextInt(20) + 20;
			System.out.println("y");
		}
	}

	//update the variables
	public void tick() {
		
		
		//if not flying
		if (!flying) {
			//change the speed
			speed += 0.0005;
			
			//change the x
			setX(x - speed);
			
			//get the bottom of the block on the ground
			while (y + height != startingY+32) {
				if (y + height >startingY+32)
					y--;
				else if (y + height < startingY+32)
					y++;
			}
			
			//if out of the screen
			if (x < -60) {

				//reset the x
				setX(startingX);
				System.out.println(y + height);
				
				//change the size
				changeSize();
				}
			
		} else {
			speed += 0.001;
			setX(x - speed);
			width = 20;
			height = width;

			if (x < -60) {

				changeSize();
				setX(startingX);

			}
			if (game.over) {
				setX(startingX);
			}
		}
		//if the game is over
		if (game.over) {
			setX(startingX);
		}
	}

	//draw block the the screen
	public void render(Graphics g) {
		g.setColor(Color.red);

		g.fillRect((int) x, (int) y, width, height);
	}

	//get the coalition box
	public Rectangle getBounds() {

		return new Rectangle((int) x, (int) y, width, height);

	}

	//set the x
	public void setX(float x) {
		this.x = x;
	}

	//get the speed
	public float getSpeed() {
		return speed;
	}

	//set the speed
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	//reset
	public void reset() {
		changeSize();
	}
}
