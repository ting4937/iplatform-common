package cn.hollycloud.iplatform.common.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2018-08-11
 * Time: 14:01
 */
public class PropertyUtils {
    private static PropertyUtils propertyUtils = new PropertyUtils();
    private static Properties properties;
    private String propertyPath = "properties";

    private PropertyUtils() {
        try {
            properties = new Properties();
            URL url = PropertyUtils.class.getClassLoader().getResource(propertyPath);
            if(url == null) {
                return;
            }
            String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
            File dir = new File(filePath);
            if (dir.exists() && dir.isDirectory()) {
                File[] dirFiles = dir.listFiles();
                BufferedReader br = null;
                for (File file : dirFiles) {
                    try {
                        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                        properties.load(br);
                    } finally {
                        IOUtils.closeQuietly(br);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }
}
