package com.chat.library;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {
	
	public void send(String message, String host, int port){
		Socket s;
		try {
			s = new Socket(host,port);
			OutputStream out = s.getOutputStream();
			PrintWriter pw = new PrintWriter(out, false);
			pw.println(message);
			pw.flush();
			pw.close();
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
