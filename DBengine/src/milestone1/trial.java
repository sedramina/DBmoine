package milestone1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class trial {
	public static void main(String[] args) throws IOException{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/home/sameh/workspace/DBengine/trial.txt", true)));
	    out.println("amr");
	    out.close();
	}

}
