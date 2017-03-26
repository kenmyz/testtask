package com.test;

import com.test.user.MarketUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ken on 23.03.2017.
 */
@Component
@Scope("prototype")
public class ConnectionManager extends Thread {

	final private Socket socket;

	@Autowired
	private CommandProcessor commandProcessor;

	public ConnectionManager(Socket clientSocket) {
		this.socket = clientSocket;
	}

	@Override
	public void run() {

		String line;
		try {
			try (PrintWriter out =
					     new PrintWriter(socket.getOutputStream(), true)) {
				try (BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()))) {
					while ((line = in.readLine()) != null) {
						System.out.println("Command: " + line);
						StringBuilder stringBuilder = commandProcessor.processCommand
								(line);
						System.out.println("Result: " + stringBuilder.toString());
						out.println(stringBuilder.toString());
					}
				}
			}
		} catch (Exception e) {
			commandProcessor.finishProcessing();
			e.printStackTrace();
		} finally {
			try {
				this.socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
