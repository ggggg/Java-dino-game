package game.main.display;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {

	//create the window
	private JFrame frame;
	private Canvas canvas;

	//window info
	private String title;
	private int width, height;

	//Contractor
	public Display(String title, int width, int height) {
		
		//get the variables
		this.title = title;
		this.width = width;
		this.height = height;

		//create the display
		createDisplay();
	}

	//get the canvas (allows other classes to draw
	public Canvas getCanvas() {
		return canvas;
	}
	
	//get the frame
	public JFrame getFrame() {
		return frame;
	}

	//create the display
	private void createDisplay() {
		
		//new window
		frame = new JFrame(title);
		//set the size
		frame.setSize(width, height);
		//close the program when the user clicks on x
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//not resizable
		frame.setResizable(false);
		//open in the middle of the screen
		frame.setLocationRelativeTo(null);
		//show the frame
		frame.setVisible(true);

		//create the canvas
		canvas = new Canvas();
		//the size of the canvas
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		
		//add the canvas to the window
		frame.add(canvas);
		frame.pack();
	}

}
