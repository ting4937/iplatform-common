package cn.hollycloud.iplatform.common.utils;

import java.util.UUID;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2018-06-07
 * Time: 17:34
 */
public class UUIDUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
