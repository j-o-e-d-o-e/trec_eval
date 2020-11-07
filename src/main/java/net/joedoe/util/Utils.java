package net.joedoe.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {
    private static final Properties prop;

    static {
        prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.get(key).toString();
    }
}
