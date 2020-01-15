package game.main.highScore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Writefile {
	public void write(int score) {

		// Writing
		Writer wr = null;
		try {
			// start the file
			wr = new FileWriter("highScore.txt");
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		try {

			// write in the file
			wr.write(String.valueOf(score));

			wr.flush();
			wr.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	// read the file
	public int read() {
		// the file
		File file = new File("highScore.txt");

		//read the file
		try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8.name())) {
			while (sc.hasNextLine()) {
				return Integer.decode(sc.nextLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;

	}
}
