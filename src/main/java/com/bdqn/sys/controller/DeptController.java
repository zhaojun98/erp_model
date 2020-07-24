package com.bdqn.sys.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.sys.entity.Dept;
import com.bdqn.sys.service.DeptService;
import com.bdqn.sys.utils.DataGridViewResult;
import com.bdqn.sys.utils.JSONResult;
import com.bdqn.sys.utils.SystemConstant;
import com.bdqn.sys.utils.TreeNode;
import com.bdqn.sys.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-27
 */
@RestController
@RequestMapping("/sys/dept")
public class DeptController {

    @Resource
    private DeptService deptService;


    /**
     * 加载左侧部门树
     * @return
     */
    @RequestMapping("/loadDeptTreeLeft")
    public DataGridViewResult loadDeptTreeLeft(){
        //查询所有部门
        List<Dept> deptList = deptService.list();
        //创建节点集合
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        //循环遍历所有部门
        for (Dept dept : deptList) {
            //如果状态为1，节点为展开状态
            Boolean spread = dept.getOpen()== SystemConstant.OPEN_TRUE ? true : false;
            //封装树节点
            TreeNode treeNode = new TreeNode();
            treeNode.setId(dept.getId());//部门编号
            treeNode.setPid(dept.getPid());//父级部门编号
            treeNode.setTitle(dept.getTitle());//部门名称
            treeNode.setSpread(spread);//展开状态
            //将树节点对象添加到节点集合
            treeNodes.add(treeNode);
        }
        //将树节点集合返回
        return new DataGridViewResult(treeNodes);
    }


    /**
     * 查询部门列表
     * @param deptVo
     * @return
     */
    @RequestMapping("/deptlist")
    public DataGridViewResult deptlist(DeptVo deptVo){
        //创建分页对象
        IPage<Dept> page = new Page<Dept>(deptVo.getPage(),deptVo.getLimit());
        //创建条件构造器对象
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<Dept>();
        //部门名称查询
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()),"title",deptVo.getTitle());
        //地址
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getAddress()),"address",deptVo.getAddress());
        //部门编号
        queryWrapper.eq(deptVo.getId()!=null,"id",deptVo.getId())
                .or().eq(deptVo.getId()!=null,"pid", deptVo.getId());
        //排序
        queryWrapper.orderByAsc("id");
        //调用查询的方法
        deptService.page(page,queryWrapper);
        //返回数据
        return new DataGridViewResult(page.getTotal(),page.getRecords());
    }

    /**
     * 添加部门
     * @param dept
     * @return
     */
    @RequestMapping("/addDept")
    public JSONResult addDept(Dept dept){
        dept.setCreatetime(new Date());
        if(deptService.save(dept)){
            return SystemConstant.ADD_SUCCESS;
        }
        return SystemConstant.ADD_ERROR;
    }

    /**
     * 修改部门
     * @param dept
     * @return
     */
    @RequestMapping("/updateDept")
    public JSONResult updateDept(Dept dept){
        if(deptService.updateById(dept)){
            return SystemConstant.UPDATE_SUCCESS;
        }
        return SystemConstant.UPDATE_ERROR;
    }


    /**
     * 检查当前部门下是否存在子节点
     * @param id
     * @return
     */
    @RequestMapping("/checkDeptHasChildren")
    public String checkDeptHasChildren(int id){
        Map<String,Object> map = new HashMap<String,Object>();
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<Dept>();
        //根据父节点查询
        queryWrapper.eq("pid",id);
        //统计部门数量
        int count = deptService.count(queryWrapper);
        if(count>0){
            //存在子节点
            map.put(SystemConstant.EXIST,true);
            map.put(SystemConstant.MESSAGE,"对不起，当前部门下有子节点，无法删除！");
        }else{
            //不存在子节点
            map.put(SystemConstant.EXIST,false);
        }
        //将map集合转换成json
        return JSON.toJSONString(map);
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public JSONResult deleteById(int id){
        if(deptService.removeById(id)){
            return SystemConstant.DELETE_SUCCESS;
        }else{
            return SystemConstant.DELETE_ERROR;
        }
    }
}

