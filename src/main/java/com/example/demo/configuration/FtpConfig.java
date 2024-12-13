package com.example.demo.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

/**
 * Configuration class for setting up and managing an embedded FTP server.
 */
@Service
public class FtpConfig {

	private static final Logger log = LoggerFactory.getLogger(FtpConfig.class);

	private FtpServer ftpServer;

	/**
	 * Path to the filesystem that serves as the home directory for the FTP user.
	 */
	@Value("${filesystem.path}")
	private String path;

	/**
	 * Initializes and starts the FTP server.
	 *
	 * @throws FtpException if an error occurs while starting the server.
	 */
	@PostConstruct
	public void startServer() throws FtpException {
		log.info("Starting FTP Server...");

		// Create the FTP server factory
		FtpServerFactory serverFactory = new FtpServerFactory();

		// Configure the listener
		ListenerFactory listenerFactory = new ListenerFactory();
		listenerFactory.setPort(2221); // Set the port for the FTP server
		serverFactory.addListener("default", listenerFactory.createListener());

		// Configure the user manager with clear text password encryption
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());

		// Create and configure a user
		BaseUser user = new BaseUser();
		user.setName("testuser");
		user.setPassword("password");
		user.setHomeDirectory(path); // Set the user's home directory

		// Save the user to the user manager
		serverFactory.getUserManager().save(user);

		// Create and start the server
		ftpServer = serverFactory.createServer();
		ftpServer.start();
		log.info("FTP server started on port: {}", 2221);
	}

	/**
	 * Stops the FTP server gracefully when the application context is closed.
	 */
	@PreDestroy
	public void stopServer() {
		log.info("Stopping FTP server...");
		if (ftpServer != null) {
			ftpServer.stop();
			log.info("FTP server stopped.");
		}
	}

	/**
	 * Checks the connection to the FTP server by attempting to log in with test credentials.
	 *
	 * @return {@code true} if the connection is successful; {@code false} otherwise.
	 */
	public static boolean checkConnection() {
		try {
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect("localhost", 2221); // Connect to the FTP server

			boolean login = ftpClient.login("testuser", "password"); // Attempt login
			if (login) {
				ftpClient.logout(); // Logout if login is successful
			}
			ftpClient.disconnect(); // Disconnect from the server
			return login;
		} catch (Exception e) {
			log.error("Failed to connect to FTP server: {}", e.getMessage());
			return false;
		}
	}
}
