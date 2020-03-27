package cn.hollycloud.iplatform.common.bean;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2019-11-07
 * Time: 14:37
 */
@Data
@ApiModel("树结构bean")
public class TreeBean {
    @ApiModelProperty("主键")
    private Object id;
    @ApiModelProperty("父id")
    private Object parentId;
    @ApiModelProperty("名字")
    private Object displayName;
    @ApiModelProperty("是否叶子节点")
    private Boolean isLeaf = true;
    @ApiModelProperty("子节点")
    private List<TreeBean> children = new ArrayList<>();
    @ApiModelProperty("原生对象")
    private JSONObject nativeObject;
}
