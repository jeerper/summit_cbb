package com.summit.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类主要是给Ext treepanel发送数据时组装成一个TreeNode（一个树结构的TreeNode）发送到前台 ClassName:
 * TreeNode
 *
 * @author yt
 * @Description: TODO
 * @date 2017-1-6 上午11:34:12
 */
public class TreeNode<T> {
    private String id;
    private String text;
    /**
     * 附加参数在raw中
     */
    private T data;
    /**
     * 是否子叶子节点，默认是
     */
    private Boolean leaf = true;
    /**
     * 复选框控制（null 为不出现复选框，false 出现复选框不选中，true 出现复选框并选中）
     */
    private Boolean checked;
    /**
     * 展开
     */
    private Boolean expanded;
    private List<TreeNode<T>> children = new ArrayList<TreeNode<T>>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
