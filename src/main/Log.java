package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Log {
	private String name;
	public Log(String name) throws FileNotFoundException, UnsupportedEncodingException{
		this.name = name;
		File logtxt = new File(name);
		if (!logtxt.isFile()){
			PrintWriter writer = new PrintWriter(name, "UTF-8");
			writer.println("==================" + '\n' + "   " + name + " FILE     " + '\n' + "==================" + '\n');
			writer.close();
		}
	}
	
	public void appendLog(String line){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(name, true)))) {
		    out.println(line);
		    out.close();
		}catch (IOException e) {
		    System.out.print("Some serious bug happened");
		}
	}
}
