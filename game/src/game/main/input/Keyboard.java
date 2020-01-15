package game.main.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private boolean[] keys;
	public boolean space;
	public boolean down;
	
	//create an array with all the keys
	public Keyboard() {
		keys = new boolean[256];
	}

	//reset the keyboard
	public void reset() {
		space = false;
		down = false;
	}
	
	//check if the keys are down
	public void tick() {
		space = keys[KeyEvent.VK_SPACE];
		down = keys[KeyEvent.VK_DOWN];
	}

	//when a key is pressed
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	//when a key is released
	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	//not in use (forced by the KeyListener class)
	@Override
	public void keyTyped(KeyEvent e) {

	}

}
