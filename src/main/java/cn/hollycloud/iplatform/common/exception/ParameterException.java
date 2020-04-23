package cn.hollycloud.iplatform.common.exception;

import cn.hollycloud.iplatform.common.bean.Result;
import cn.hollycloud.iplatform.common.enums.BaseErrorCode;
import lombok.Data;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2020-04-23
 * Time: 10:59
 */
@Data
public class ParameterException extends RuntimeException {
    private int status = Result.FAIL;

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, int status) {
        super(message);
        this.status = status;
    }

    public ParameterException(BaseErrorCode errorCode) {
        this(errorCode.getDesc(), errorCode.getValue());
    }

}
