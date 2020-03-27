package cn.hollycloud.iplatform.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by Administrator on 2017/5/31 0031.
 */
public class FileUtils {
    public static String getRandomFilename(String originalName) {
        return UUID.randomUUID().toString() + "." + originalName.replace(".", "-").split("-")[originalName.replace(".", "-").split("-").length - 1];
    }

    /**
     * 获取文件后缀名，不包含点
     *
     * @param name
     * @return
     */
    public static String getExtention(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        int index = name.lastIndexOf(".");
        if (index == -1 || index == name.length() - 1) {
            return "";
        }
        return name.substring(index + 1);
    }

    /**
     * 根据文件路径获取文件名
     *
     * @param path
     * @return
     */
    public static String getNameFromPath(String path) {
        String[] paths = path.split("/");
        return paths[paths.length - 1];
    }

    /**
     * 获取文件名，不包含后缀
     *
     * @param fullname
     * @return
     */
    public static String getFileName(String fullname) {
        if (StringUtils.isEmpty(fullname)) {
            return "";
        }
        int index = fullname.lastIndexOf(".");
        if (index == -1) {
            return fullname;
        }
        return fullname.substring(0, index);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String formatSize(long bytes) {
        return humanReadableByteCount(bytes, false);
    }

    /**
     * 格式化目录，把\\或\转成/,并在最后加上/
     *
     * @param dir
     * @return
     */
    public static String formatDir(String dir) {
        if (StringUtils.isEmpty(dir)) {
            return dir;
        }
        dir = dir.replace("\\\\", "/").replace("\\", "/");
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }
}
