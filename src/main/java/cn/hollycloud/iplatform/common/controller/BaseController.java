package cn.hollycloud.iplatform.common.controller;

import cn.hollycloud.iplatform.common.bean.PageResult;
import cn.hollycloud.iplatform.common.bean.Result;
import cn.hollycloud.iplatform.common.exception.ServiceFailException;
import cn.hollycloud.iplatform.common.utils.ResultUtils;
import cn.hutool.core.io.IoUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2018-05-04
 * Time: 15:07
 */
public class BaseController {
    public Result resultOk() {
        return ResultUtils.resultOk();
    }

    public <T extends Result> T resultOk(Object data) {
        return ResultUtils.resultOk(data);
    }

    public <T extends Result> T resultOk(Object data, String msg) {
        return ResultUtils.resultOk(data, msg);
    }

    public Result resultFail() {
        return ResultUtils.resultFail();
    }

    public Result resultFail(String msg) {
        return ResultUtils.resultFail(msg);
    }

    public Result resultFail(int code, String msg) {
        return ResultUtils.resultFail(code, msg);
    }

    public static PageResult resultPageFail(int code, String msg) {
        return ResultUtils.resultPageFail(code, msg);
    }

    public static PageResult resultPageFail() {
        return ResultUtils.resultPageFail();
    }

    public static PageResult resultPageFail(String msg) {
        return ResultUtils.resultPageFail(msg);
    }

    public void addFileHeader(HttpServletRequest request, HttpServletResponse response, String fileName, String contentType) {
        try {
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            response.setContentType(contentType);
            String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            if (agent.contains("firefox")) {
                response.setCharacterEncoding("utf-8");
                response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
            } else {
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportFile(InputStream inputStream, HttpServletRequest request, HttpServletResponse response, String fileName, String contentType) {
        try {
            addFileHeader(request, response, fileName, contentType);
            ServletOutputStream out = response.getOutputStream();
            //输出
            IoUtil.copy(inputStream, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new ServiceFailException("导出失败,{}", e);
        }
    }

    public void exportFile(InputStream inputStream, HttpServletRequest request, HttpServletResponse response, String fileName) {
        exportFile(inputStream, request, response, fileName, "application/octet-stream");
    }
}
