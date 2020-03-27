package cn.hollycloud.iplatform.common.exception;

import cn.hollycloud.iplatform.common.bean.Result;
import cn.hollycloud.iplatform.common.enums.BaseErrorCode;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2018-08-23
 * Time: 10:38
 */
public class ServiceFailException extends RuntimeException {
    private int status = Result.FAIL;

    public ServiceFailException(String message) {
        super(message);
    }

    public ServiceFailException(String message, int status) {
        super(message);
        this.status = status;
    }

    public ServiceFailException(BaseErrorCode errorCode) {
        this(errorCode.getDesc(), errorCode.getValue());
    }

    public ServiceFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}


