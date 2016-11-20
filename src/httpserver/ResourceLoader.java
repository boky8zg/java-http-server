/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author bojan
 */
public final class ResourceLoader {
    
    public static void read(HttpResponse httpResponse, String filePath) throws IOException {
        String pathString = Config.WWWROOT + filePath;
        
        if (pathString.endsWith("/")) {
            for (String defaultFile : Config.DEFAULT_FILES) {
                File file = new File(Config.WWWROOT + filePath + defaultFile);
                
                if (file.isFile()) {
                    pathString = Config.WWWROOT + filePath + defaultFile;
                    break;
                }
            }
        } else {
            File file = new File(pathString);
            
            if (!file.isFile()) {
                throw new IOException();
            }
        }
        
        Path path = Paths.get(pathString);
        byte[] fileData = Files.readAllBytes(path);
        String extension = getExtension(pathString);
        
        httpResponse.addHeader("Contet-Type", MimeType.getFromExtension(extension));
        httpResponse.addHeader("Content-Length", String.valueOf(fileData.length));
        httpResponse.setBody(fileData);
    }
    
    private static String getExtension(String path) {
        String extension = "";

        int i = path.lastIndexOf('.');
        if (i > 0) {
            extension = path.substring(i+1);
        }
        
        return extension;
    }
    
}
