package cn.hollycloud.iplatform.common.utils;

import cn.hollycloud.iplatform.common.bean.PageResult;
import cn.hollycloud.iplatform.common.bean.Result;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2019-07-10
 * Time: 14:01
 */
public class ResultUtils {
    public static Result resultOk() {
        return resultOk(null);
    }

    public static <T extends Result> T resultOk(Object data) {
        return resultOk(data, "操作成功");
    }

    public static <T extends Result> T resultOk(Object data, String msg) {
        Result result = new Result();
        result.setStatus(Result.OK);
        result.setMessage(msg);
        if (data != null && data instanceof IPage) {
            //分页数据
            IPage page = (IPage) data;
            PageResult pageResult = Convert.convert(PageResult.class, result);
            pageResult.setCurrent((int)page.getCurrent());
            pageResult.setTotal((int)page.getTotal());
            pageResult.setTotalPages((int)page.getPages());
            pageResult.setData(page.getRecords());
            return (T) pageResult;
        } else {
            //普通数据
            result.setData(data);
            return (T) result;
        }
    }

    public static Result resultFail() {
        return resultFail("操作失败");
    }

    public static Result resultFail(String msg) {
        Result result = new Result();
        result.setStatus(Result.FAIL);
        result.setMessage(msg);
        return result;
    }

    public static Result resultFail(int code, String msg) {
        Result result = new Result();
        result.setStatus(code);
        result.setMessage(msg);
        return result;
    }

    public static PageResult resultPageFail(int code, String msg) {
        PageResult pageResult = new PageResult();
        pageResult.setStatus(code);
        pageResult.setMessage(msg);
        return pageResult;
    }

    public static PageResult resultPageFail() {
        return resultPageFail(Result.FAIL, "操作失败");
    }

    public static PageResult resultPageFail(String msg) {
        return resultPageFail(Result.FAIL, msg);
    }
}
