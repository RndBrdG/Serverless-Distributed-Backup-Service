package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
			writer.println("==================" + '\n' + "   " + name + " FILE     " + '\n' + "==================");
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
	
	public void readLog() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(name));
	    try {
	        // PRIMEIRAS TRÊS LINHAS NÂO CONTÊM INFORMAÇÃO RELEVANTE
	        String line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        while (line != null) {
	            String[] msg = line.split(" ");
	            Main.files.put(msg[0].toLowerCase(), msg[1] + " " + msg[2]);
	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	}
	
	public void clearFile() throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(name);
		writer.print("");
		writer.close();
		writer = new PrintWriter(name);
		writer.println("==================" + '\n' + "   " + name + " FILE     " + '\n' + "==================");
		writer.close();
	}
}
