package cn.hollycloud.iplatform.common.exception;

import cn.hollycloud.iplatform.common.bean.Result;
import cn.hollycloud.iplatform.common.enums.BaseErrorCode;
import lombok.Data;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2018-08-23
 * Time: 10:38
 */
@Data
public class ServiceFailException extends RuntimeException {
    private int status = Result.FAIL;
    private boolean print = false;

    public ServiceFailException(String message) {
        this(message, false);
    }

    public ServiceFailException(String message, int status) {
        this(message, status, false);
    }


    public ServiceFailException(String message, boolean print) {
        this(message, Result.FAIL, print);
    }

    public ServiceFailException(BaseErrorCode errorCode) {
        this(errorCode, false);
    }

    public ServiceFailException(BaseErrorCode errorCode, boolean print) {
        this(errorCode.getDesc(), errorCode.getValue(), print);
    }

    public ServiceFailException(String message, int status, boolean print) {
        super(message);
        this.status = status;
        this.print = print;
    }

    public ServiceFailException(String message, int status, boolean print, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.print = print;
    }

    public ServiceFailException(String message, int status, Throwable cause) {
        this(message, status, false, cause);
    }

    public ServiceFailException(String message, Throwable cause) {
        this(message, Result.FAIL, cause);
    }

    public ServiceFailException(String message, boolean print, Throwable cause) {
        this(message, Result.FAIL, print, cause);
    }

    public ServiceFailException(BaseErrorCode errorCode, boolean print, Throwable cause) {
        this(errorCode.getDesc(), errorCode.getValue(), print, cause);
    }

    public ServiceFailException(BaseErrorCode errorCode, Throwable cause) {
        this(errorCode.getDesc(), errorCode.getValue(), false, cause);
    }
}


