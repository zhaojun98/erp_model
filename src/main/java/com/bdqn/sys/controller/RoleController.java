package com.bdqn.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.sys.entity.Permission;
import com.bdqn.sys.entity.Role;
import com.bdqn.sys.service.PermissionService;
import com.bdqn.sys.service.RoleService;
import com.bdqn.sys.utils.DataGridViewResult;
import com.bdqn.sys.utils.JSONResult;
import com.bdqn.sys.utils.SystemConstant;
import com.bdqn.sys.utils.TreeNode;
import com.bdqn.sys.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-31
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;


    @RequestMapping("/rolelist")
    public DataGridViewResult roleList(RoleVo roleVo){
        //创建分页对象
        IPage<Role> page = new Page<Role>(roleVo.getPage(),roleVo.getLimit());
        //创建条件构造器对象
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        //角色编码
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRolecode()),"rolecode",roleVo.getRolecode());
        //角色名称
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRolename()),"rolename",roleVo.getRolename());
        //排序
        queryWrapper.orderByAsc("id");
        //调用分页查询的方法
        roleService.page(page,queryWrapper);
        //将数据返回
        return new DataGridViewResult(page.getTotal(),page.getRecords());
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @RequestMapping("/addRole")
    public JSONResult addRole(Role role){
        role.setCreatetime(new Date());//添加时间
        if(roleService.save(role)){
            return SystemConstant.ADD_SUCCESS;
        }
        return SystemConstant.ADD_ERROR;
    }

    @RequestMapping("/updateRole")
    public JSONResult updateRole(Role role){
        if(roleService.updateById(role)){
            return SystemConstant.UPDATE_SUCCESS;
        }
        return SystemConstant.UPDATE_ERROR;
    }

    @RequestMapping("/deleteById")
    public JSONResult deleteById(int id){
        if(roleService.removeById(id)){
            return SystemConstant.DELETE_SUCCESS;
        }
        return SystemConstant.DELETE_ERROR;
    }

    /**
     * 初始化权限菜单树
     * @param roleId
     * @return
     */
    @RequestMapping("/initPermissionByRoleId")
    public DataGridViewResult initPermissionByRoleId(int roleId){
        //创建条件构造器对象
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
        //查询所有的权限数据（sys_permission表）的所有数据
        List<Permission> permissions = permissionService.list(queryWrapper);
        //创建集合，保存该角色拥有的菜单
        List<Permission> currentPermissions = new ArrayList<Permission>();
        //查询当前角色已经拥有哪些权限
        //根据角色id查询该角色下所有的权限菜单id
        List<Integer> currentPermissionIds = permissionService.findRolePermissionByRoleId(roleId);
        //判断当前角色是否有权限菜单id
        if(currentPermissionIds!=null && currentPermissionIds.size()>0){
            //进行条件查询
            queryWrapper.in("id",currentPermissionIds);
            currentPermissions = permissionService.list(queryWrapper);
        }
        //创建树节点TreeNode集合
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        //外层循环：循环所有的权限数据
        for (Permission p1 : permissions) {
            //定义变量，标记是否选中
            String checkArr = "0";//不选中
            //内层循环：遍历当前角色拥有的权限菜单集合
            for (Permission p2 : currentPermissions) {
                //比较权限id是否 相等，相等意味着角色拥有的，默认选中
                if(p1.getId()==p2.getId()){
                    checkArr = "1";//选中
                    break;
                }
            }
            //组装树节点对象
            Boolean spread =(p1.getOpen()==null || p1.getOpen()==1)?true:false;//是否展开
            //将节点对象添加到节点集合
            treeNodes.add(new TreeNode(p1.getId(),p1.getPid(),p1.getTitle(),spread,checkArr));
        }
        return new DataGridViewResult(treeNodes);
    }

    /**
     * 保存分配权限
     * @param rid   当前角色ID
     * @param ids   权限id
     * @return
     */
    @RequestMapping("/saveRolePermission")
    public JSONResult saveRolePermission(int rid,String ids){
        try {
            //调用保存分配权限关系的方法
            if(roleService.saveRolePermission(rid,ids)){
                return SystemConstant.DISTRIBUTE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SystemConstant.DISTRIBUTE_ERROR;
    }

}

