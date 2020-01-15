package game.main;

/**
 * @author Ido Ben Haim 
 * ICS3U1 
 * Ms.D'angelo
 *
 *         Dino Game:
 *
 *         The Game was made with the thought that it can be played as a short
 *         break from doing something else (eg. homework, classwork, studying…).
 *         Due to a small window size it can be played while still seeing the
 *         other window in the background.
 *
 *
 *         How to play:
 *
 *         Press space to jump over the red blocks and down arrow key to duck
 *         over taller red blocks
 *
 */

public class Launcher {
	public static void main(String[] args) {
		// start the game
		Game game = new Game("Game", 1000, 1000);
		game.start();
	}
}