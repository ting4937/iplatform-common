package cn.hollycloud.iplatform.common.utils;

import cn.hollycloud.iplatform.common.annotation.TreeId;
import cn.hollycloud.iplatform.common.annotation.TreeName;
import cn.hollycloud.iplatform.common.annotation.TreeParentId;
import cn.hollycloud.iplatform.common.bean.TreeBean;
import cn.hollycloud.iplatform.common.exception.ServiceFailException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author: Cloud
 * Date: 2019-10-30
 * Time: 16:02
 */
public class TreeUtils {
    public static List<TreeBean> parseTree(JSONArray flatArray, String idName, String parentIdName, String displayName) {
        List<TreeBean> tree = new ArrayList<>();
        List<TreeBean> treeBeans = handleTree(flatArray, idName, parentIdName, displayName);
        for (TreeBean treeBean : treeBeans) {
            if (treeBean.getParentId() == null) {
                tree.add(treeBean);
            }
        }
        return tree;
    }

    public static List<TreeBean> parseTree(List list, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String idName = null;
        String parentIdName = null;
        String displayName = null;
        for (Field field : fields) {
            if (field.getAnnotation(TreeId.class) != null) {
                idName = field.getName();
            } else if (field.getAnnotation(TreeName.class) != null) {
                displayName = field.getName();
            } else if (field.getAnnotation(TreeParentId.class) != null) {
                parentIdName = field.getName();
            }
        }
        if (StringUtils.isEmpty(idName)) {
            throw new ServiceFailException("没有找到TreeId注解");
        }
        if (StringUtils.isEmpty(parentIdName)) {
            throw new ServiceFailException("没有找到TreeParentId注解");
        }
        if (StringUtils.isEmpty(displayName)) {
            throw new ServiceFailException("没有找到TreeName注解");
        }
        JSONArray jsonArray = JSONArray.parseArray(JsonUtils.serialize(list));
        return parseTree(jsonArray, idName, parentIdName, displayName);
    }

    /**
     * 组装树，返回扁平树，树下节点已挂载
     *
     * @param flatArray
     * @param idName
     * @param parentIdName
     * @param displayName
     * @return
     */
    private static List<TreeBean> handleTree(JSONArray flatArray, String idName, String parentIdName, String displayName) {
        List<TreeBean> tree = new ArrayList<>();
        Map<Object, TreeBean> map = new HashMap();
        //把列表映射成id,obj形式
        for (int i = 0; i < flatArray.size(); i++) {
            JSONObject object = flatArray.getJSONObject(i);
            TreeBean treeBean = new TreeBean();
            Object id = object.get(idName);
            if (id == null) {
                continue;
            }
            treeBean.setId(id);
            Object name = object.get(displayName);
            if (name == null) {
                continue;
            }
            treeBean.setDisplayName(name);
            Object parentId = object.get(parentIdName);
            if (parentId != null) {
                treeBean.setParentId(parentId);
            }
            treeBean.setNativeObject(object);
            treeBean.setDisplayName(name);
            map.put(id, treeBean);
            tree.add(treeBean);
        }
        //
        for (int i = 0; i < tree.size(); i++) {
            TreeBean object = tree.get(i);
            Object parentId = object.getParentId();
            if (parentId != null) {
                //父节点为null表示根节点
                TreeBean parentNode = map.get(parentId);
                if (parentNode != null) {
                    List<TreeBean> children = parentNode.getChildren();
                    children.add(object);
                    parentNode.setIsLeaf(false);
                }
            }
        }
        return tree;
    }

    public static List<TreeBean> mountTree(JSONArray flatArray, String idName, String parentIdName, String displayName,
                                           JSONArray childNodes, String childIdName, String childParentIdName, String childDisplayName) {
        List<TreeBean> tree = new ArrayList<>();
        List<TreeBean> treeBeans = handleTree(flatArray, idName, parentIdName, displayName);
        Map<Object, TreeBean> map = new HashMap();
        //把列表映射成id,obj形式
        for (int i = 0; i < treeBeans.size(); i++) {
            TreeBean treeBean = treeBeans.get(i);
            treeBean.setIsLeaf(false);
            if (treeBean.getParentId() == null) {
                tree.add(treeBean);
            }
            map.put(treeBean.getId(), treeBean);
        }

        for (int i = 0; i < childNodes.size(); i++) {
            JSONObject childNode = childNodes.getJSONObject(i);
            TreeBean treeBean = new TreeBean();
            Object id = childNode.get(childIdName);
            if (id == null) {
                continue;
            }
            treeBean.setId(id);
            Object name = childNode.get(childDisplayName);
            if (name == null) {
                continue;
            }
            treeBean.setDisplayName(name);
            treeBean.setNativeObject(childNode);
            Object parentId = childNode.get(childParentIdName);
            if (parentId == null) {
                tree.add(treeBean);
            } else {
                treeBean.setParentId(parentId);
                TreeBean parentNode = map.get(parentId);
                if (parentNode == null) {
                    continue;
                }
                List<TreeBean> children = parentNode.getChildren();
                children.add(treeBean);
            }
        }
        return tree;
    }
}
