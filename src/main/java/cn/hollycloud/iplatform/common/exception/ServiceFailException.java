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
}


