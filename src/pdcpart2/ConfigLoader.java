/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author Onafanewlevel
 */
public class ConfigLoader {
    
    private Properties properties;
    
    public ConfigLoader(String filename) throws IOException {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(Paths.get("src/main", filename).toFile())){properties.load(input);}
    }
    
    public String getProperties(String key){return properties.getProperty(key);}
}
