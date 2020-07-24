package com.bdqn.sys.utils;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeBuilder {

    /**
     * 构建菜单树节点的层级关系
     * @param treeNodes     菜单集合
     * @param rootId        根节点编号1
     * @return
     */
    public static List<TreeNode> build(List<TreeNode> treeNodes,int rootId){
        //创建集合保存层级关系
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        //循环菜单集合
        for (TreeNode n1 : treeNodes) {
            //如果当前节点为根节点，则将当前节点添加到节点数组中
            if(n1.getPid()==rootId){
                nodes.add(n1);//将节点添加到集合
            }
            //如果当前子节点对应的节点相等，则添加到子节点集合中
            for (TreeNode n2 : treeNodes) {
                if(n1.getId()==n2.getPid()){
                    n1.getChildren().add(n2);
                }
            }
        }
        return nodes;
    }

}
