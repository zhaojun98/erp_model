package com.bdqn.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.sys.entity.Permission;
import com.bdqn.sys.service.PermissionService;
import com.bdqn.sys.utils.DataGridViewResult;
import com.bdqn.sys.utils.JSONResult;
import com.bdqn.sys.utils.SystemConstant;
import com.bdqn.sys.utils.TreeNode;
import com.bdqn.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-24
 */
@RestController
@RequestMapping("/sys/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 查询菜单列表
     * @param permissionVo
     * @return
     */
    @RequestMapping("/permissionlist")
    public DataGridViewResult permissionlist(PermissionVo permissionVo){
        //创建分页对象
        IPage<Permission> page = new Page<Permission>(permissionVo.getPage(),permissionVo.getLimit());
        //创建条件构造器对象
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
        //只查询权限，不查询菜单
        queryWrapper.eq("type", SystemConstant.TYPE_PERMISSION);
        //权限名称查询
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()),"title",permissionVo.getTitle());
        //权限编码
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getPercode()),"percode",permissionVo.getPercode());
        //菜单编号
        queryWrapper.eq(permissionVo.getId()!=null,"id",permissionVo.getId())
                .or().eq(permissionVo.getId()!=null,"pid", permissionVo.getId());

        //排序
        queryWrapper.orderByAsc("id");
        //调用查询的方法
        permissionService.page(page,queryWrapper);
        //返回数据
        return new DataGridViewResult(page.getTotal(),page.getRecords());
    }


    /**
     * 添加权限
     * @param permission
     * @return
     */
    @RequestMapping("/addPermission")
    public JSONResult addPermission(Permission permission){
        //指定type为permission权限类型
        permission.setType(SystemConstant.TYPE_PERMISSION);
        if(permissionService.save(permission)){
            return SystemConstant.ADD_SUCCESS;
        }
        return SystemConstant.ADD_ERROR;
    }

    /**
     * 修改权限
     * @param permission
     * @return
     */
    @RequestMapping("/updatePermission")
    public JSONResult updatePermission(Permission permission){
        if(permissionService.updateById(permission)){
            return SystemConstant.UPDATE_SUCCESS;
        }
        return SystemConstant.UPDATE_ERROR;
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public JSONResult deleteById(int id){
        if(permissionService.removeById(id)){
            return SystemConstant.DELETE_SUCCESS;
        }else{
            return SystemConstant.DELETE_ERROR;
        }
    }

}

