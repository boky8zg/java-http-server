/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bojan
 */
public class HttpServer {
    
    private int port;

    public HttpServer(int port) {
        this.port = port;
    }
    
    public void listen() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            
            System.out.println("Server listening on port " + port);
            
            while (true) {                
                Socket clientSocket = serverSocket.accept();
                HttpRequest httpRequest = new HttpRequest(clientSocket);
            }
        } catch (IOException ex) {
            System.out.println("Cannot bind server to port " + port);
        }
    }
}
