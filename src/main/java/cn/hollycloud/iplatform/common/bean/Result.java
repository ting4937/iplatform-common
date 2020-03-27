package cn.hollycloud.iplatform.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Cloud on 2018/4/10.
 */
@Data
@Accessors(chain = true)
public class Result<T> {
    public final static int FAIL = 0;
    public final static int OK = 200;
    public final static int BAD_REQUEST = 400;
    public final static int UNAUTHORIZED = 401;
    public final static int FORBIDDEN = 403;

    @ApiModelProperty(value = "状态码,200表示成功, 401表示未登录, 403表示无权限", position = 0)
    private int status;
    @ApiModelProperty(value = "成功或错误消息", position = 1)
    private String message;
    @ApiModelProperty(value = "返回数据", position = 2)
    private T data;

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
