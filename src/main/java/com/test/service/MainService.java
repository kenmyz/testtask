package com.test.service;

import com.test.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Ken on 23.03.2017.
 */
@Service
public class MainService implements ApplicationRunner {

	@Autowired
	private ApplicationContext context;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			while (true) {
				ConnectionManager conn = context.getBean(ConnectionManager
						.class, serverSocket.accept());
				conn.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
