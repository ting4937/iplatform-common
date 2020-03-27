package cn.hollycloud.iplatform.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PageResult<T> extends Result {
    @ApiModelProperty(value = "总记录数", position = 3)
    private Integer total;     // 总条数
    @ApiModelProperty(value = "总页数", position = 4)
    private Integer totalPages; //总页数
    @ApiModelProperty(value = "当前页", position = 5)
    private Integer current; //当前页
    @ApiModelProperty(value = "分页数据", position = 2)
    private List<T> data;

    @Override
    public String toString() {
        return "PageResult{" +
                "status=" + getStatus() +
                ", message=" + getMessage() +
                ", total=" + total +
                ", totalPages=" + totalPages +
                ", current=" + current +
                '}';
    }
}
