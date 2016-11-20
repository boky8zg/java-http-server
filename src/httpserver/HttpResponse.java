/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bojan
 */
public class HttpResponse {
    
    private Socket clientSocket;
    private String httpVersion;
    private int httpCode;
    private String httpMessage;
    private HashMap<String, String> headers;
    private byte[] body;
    
    public HttpResponse(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.httpVersion = "HTTP/1.1";
        this.httpCode = 200;
        this.httpMessage = "OK";
        this.headers = new HashMap<>();
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(String httpMessage) {
        this.httpMessage = httpMessage;
    }
    
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
    
    public void send() throws IOException {
        String rawHeaders = httpVersion + " " + String.valueOf(httpCode) + " " + httpMessage + "\r\n";
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        
        for (Map.Entry<String, String> header : headers.entrySet()) {
            rawHeaders += header.getKey() + ": " + header.getValue() + "\r\n";
        }
        
        rawHeaders += "\r\n";
        
        dos.write(rawHeaders.getBytes());
        dos.write(body);
    }
    
}
