/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author joseluis
 */
public class SocketServer {
    
    public static final Logger logger = LogManager.getLogger(SocketServer.class);
    private static int port;

    /**
     * @param args the command line arguments
     */
	public static void main(String[] args) throws IOException
	{
		ServerSocket server = null;
                
                //System.setProperty("log4j.configuration", "/media/myStorage/joseluis/NetBeansProjects/SocketServer/log4j2.xml");
                //System.out.println(System.getProperty("log4j.configuration"));
                //System.out.println("Logger info: "+logger.isInfoEnabled());
                //System.out.println("Logger trace: "+logger.isTraceEnabled());
                //System.out.println("Logger error: "+logger.isErrorEnabled());
                //System.out.println("Logger debug: "+logger.isDebugEnabled());
                Properties prop = new Properties();
                try {
                    prop.loadFromXML(new FileInputStream(new File("configuracion.xml")));
                    //prop.load(new FileInputStream(new File("configuracion.properties")));
                    port = Integer.parseInt(prop.getProperty("socketserver.port"));
                } catch (FileNotFoundException ex) {
                    java.util.logging.Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(-1);
                }

		try {

			// server is listening on port 1234
			server = new ServerSocket(port);
			server.setReuseAddress(true);

			// running infinite loop for getting
			// client request
			while (true) {
                            
                            logger.info(" Aguardando peticiones en puerto {}...", port);

				// socket object to receive incoming client
				// requests
				Socket client = server.accept();

				// Displaying that new client is connected
				// to server
				logger.info(" Cliente conectado "
								+ client.getInetAddress()
										.getHostAddress());

				// create a new thread object
				ClientHandler clientSock
					= new ClientHandler(client);

				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ClientHandler class
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;

		// Constructor
		public ClientHandler(Socket socket)
		{
			this.clientSocket = socket;
		}

		public void run()
		{
			PrintWriter out = null;
			BufferedReader in = null;
			try {
					
				// get the outputstream of client
				out = new PrintWriter(
					clientSocket.getOutputStream(), true);

				// get the inputstream of client
				in = new BufferedReader(
					new InputStreamReader(
						clientSocket.getInputStream()));

				String line;
				while ((line = in.readLine()) != null) {

					// writing the received message from
					// client
					logger.info(
						" Recibido del cliente: {}",
						line);
					out.println(line);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
						clientSocket.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
    
}
