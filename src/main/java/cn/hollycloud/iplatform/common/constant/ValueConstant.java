package cn.hollycloud.iplatform.common.constant;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2018-05-04
 * Time: 16:03
 */
public class ValueConstant {
    public final static String CHARSET = "UTF-8";
    public final static String NULL = "null";
    /*************时间常量*******************/
    public final static long SECOND_TIME = 1000;
    public final static long MINUTE_TIME = 60 * SECOND_TIME;
    public final static long HOUR_TIME = 60 * MINUTE_TIME;
    public final static long DAY_TIME = 24 * HOUR_TIME;
    public final static int MINUTE_TIME_SECOND = 60;
    public final static int HOUR_TIME_SECOND = 60 * MINUTE_TIME_SECOND;
    public final static int DAY_TIME_SECOND = 24 * HOUR_TIME_SECOND;

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /*************HTTP response code常量*******************/
    public final static int HTTP_STATUS_OK = 200;
    public final static int HTTP_STATUS_UNAUTHORIZED = 401;
    public final static int HTTP_STATUS_SERVER_ERROR = 500;
}
