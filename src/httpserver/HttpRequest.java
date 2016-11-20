/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bojan
 */
public class HttpRequest extends Thread {
    
    public static final int BUFFER_LENGTH = 1024;
    private Socket clientSocket;
    private String raw;
    private String[] rawLines;
    private String method;
    private String path;
    private String httpVersion;
    private HashMap<String, String> headers;
    private String body;

    public HttpRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.headers = new HashMap<>();
        this.start();
    }

    @Override
    public void run() {
        getRawRequest();
        parseRequest();
    }
    
    private void getRawRequest() {
        try {
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            byte[] buffer = new byte[BUFFER_LENGTH];
            int bytesRead;
            boolean endReached = false;
            
            raw = "";
            
            while (!endReached && (bytesRead = dis.read(buffer)) != -1) {
                String bufferString = new String(buffer, 0, bytesRead, "UTF-8");
                raw += bufferString;
                
                if (raw.endsWith("\r\n\r\n")) {
                    endReached = true;
                }
            }
            
            rawLines = raw.split("\r\n");
        } catch (IOException ex) {
            System.out.println("Error getting data from client");
        }
    }

    private void parseRequest() {
        parseMethod();
        parseHeaders();
        sendResponse();
        
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.out.println("Error closing socket");
        }
    }
    
    private void parseMethod() {
        String[] methodLine = rawLines[0].split(" ");
        
        method = methodLine[0];
        path = methodLine[1];
        httpVersion = methodLine[2];
        
        System.out.println(method + " " + path);
    }
    
    private void parseHeaders() {
        for (int i = 1; i < rawLines.length; i++) {
            String header[] = rawLines[i].split(": ");
            
            headers.put(header[0], header[1]);
        }
    }

    private void sendResponse() {
        HttpResponse httpResponse = new HttpResponse(clientSocket);
        
        try {
            ResourceLoader.read(httpResponse, path);
        } catch (IOException ex) {
            httpResponse.setHttpCode(404);
            httpResponse.setHttpMessage("NOT FOUND");
            httpResponse.setBody(new byte[0]);
        }
        
        try {
            httpResponse.send();
        } catch (IOException ex) {
            System.out.println("Error sending response");
        }
    }
}
