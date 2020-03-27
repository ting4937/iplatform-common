package cn.hollycloud.iplatform.common.utils;

import cn.hollycloud.iplatform.common.bean.Result;
import cn.hollycloud.iplatform.common.constant.ValueConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/5/27 0027.
 */
public class AjaxUtils {
    private static final Logger logger = LoggerFactory.getLogger(AjaxUtils.class);

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        String token = request.getHeader("token");
        if (StringUtils.isNotEmpty(requestType) || StringUtils.isNotEmpty(token)) {
            return true;
        }
        return false;
    }

    public static void printMsg(int code, HttpServletResponse response) {
        printMsg(code, response, null);
    }

    public static void printMsg(int code, HttpServletResponse response, String msg) {
        printMsg(code, response, msg, null);
    }

    public static void printMsg(int code, HttpServletResponse response, String msg, Object data) {
        response.setStatus(ValueConstant.HTTP_STATUS_OK);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        Result result = new Result();
        result.setStatus(code);
        result.setMessage(msg);
        result.setData(data);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String jsonStr = JsonUtils.serialize(result);
            logger.info("返回json数据{}", jsonStr);
            out.write(jsonStr);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
