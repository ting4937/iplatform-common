package cn.hollycloud.iplatform.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2019-11-16
 * Time: 9:33
 */
public class SwaggerUtils {
    public static File generate(String packageName, String outputFolder) throws Exception {
        String folderStr = "/brotherStrongsShowTime";
        File folder = new File(outputFolder + folderStr);
        FileUtil.del(folder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // List<String> classNames = getClassName(packageName);
        List<String> classNames = getClassName(packageName, false);
        if (classNames != null) {
            for (String className : classNames) {
                Class clazz = Class.forName(className);
                ApiModel apiModel = (ApiModel) clazz.getAnnotation(ApiModel.class);
                String fileName = clazz.getSimpleName();
                if (apiModel != null) {
                    fileName = apiModel.value();
                }
                File file = new File(folder, fileName + ".vue");
                FileWriter fw = new FileWriter(file);
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    ApiModelProperty apiModelProperty = (ApiModelProperty) field.getAnnotation(ApiModelProperty.class);
                    if (apiModelProperty != null) {
                        String tag = "<" + getUiTag(field.getType()) + " v-model=\"data." + field.getName() + "\" label=\"" + apiModelProperty.value() + "\" " + (apiModelProperty.required() ? "required" : "") + getUiType(field.getType()) + "/>\n";
                        fw.write(tag);
                    } else {
                        String tag = "<" + getUiTag(field.getType()) + " v-model=\"data." + field.getName() + "\" label=\"" + "" + "\" " + getUiType(field.getType()) + "/>\n";
                        fw.write(tag);
                    }
                }
                fw.close();
            }
        }
        File file = ZipUtil.zip(folder);
        FileUtil.del(folder);
        return file;
    }

    private static String getUiTag(Class clazz) {
        if (clazz.getSimpleName().equals("LocalDateTime") || clazz.getSimpleName().equals("LocalDate")) {
            return "ui-date";
        } else if (clazz.getSimpleName().equals("Integer") || clazz.getSimpleName().equals("Long")) {
            return "ui-number";
        }
        return "ui-text";
    }

    private static String getUiType(Class clazz) {
        if (clazz.getSimpleName().equals("LocalDateTime")) {
            return " type=\"datetime\"";
        } else if (clazz.getSimpleName().equals("LocalDate")) {
            return " type=\"date\"";
        }
        return "";
    }

    /**
     * 获取某包下（包括该包的所有子包）所有类
     *
     */
    public static List<String> getClassName(String packageName) {
        return getClassName(packageName, true);
    }

    /**
     * 获取某包下所有类
     *
     */
    public static List<String> getClassName(String packageName, boolean childPackage) {
        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                fileNames = getClassNameByFile(url.getPath(), null, childPackage);
            } else if (type.equals("jar")) {
                fileNames = getClassNameByJar(url.getPath(), childPackage);
            }
        } else {
            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);
        }
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     */
    private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
                }
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
                            childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     */
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myClassName;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     */
    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes/")) {
                    continue;
                }
                String jarPath = urlPath + "!/" + packagePath;
                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
            }
        }
        return myClassName;
    }
}
