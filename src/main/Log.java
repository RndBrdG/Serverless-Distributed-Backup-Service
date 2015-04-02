package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Log {
	
	public Log() throws FileNotFoundException, UnsupportedEncodingException{
		File logtxt = new File("log.txt");
		if (!logtxt.isFile()){
			PrintWriter writer = new PrintWriter("log.txt", "UTF-8");
			writer.println("==================" + '\n' + "     LOG FILE     " + '\n' + "==================" + '\n');
			writer.close();
		}
	}
	
	public void appendLog(String line){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)))) {
		    out.println(line);
		    out.close();
		}catch (IOException e) {
		    System.out.print("Some serious bug happened");
		}
	}
}
