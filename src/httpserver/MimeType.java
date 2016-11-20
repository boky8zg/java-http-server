/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.util.HashMap;

/**
 *
 * @author bojan
 */
public class MimeType {

    private static final HashMap<String, String> mimeTypes = new HashMap<String, String>() {{
        put("html", "text/html");
    }};
    
    public static String getFromExtension(String extension) {
        extension = extension.toLowerCase();
        
        return mimeTypes.get(extension);
    }
    
}
