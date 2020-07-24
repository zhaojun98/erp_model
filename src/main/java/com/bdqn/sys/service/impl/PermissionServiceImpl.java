package com.bdqn.sys.service.impl;

import com.bdqn.sys.entity.Permission;
import com.bdqn.sys.dao.PermissionMapper;
import com.bdqn.sys.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-24
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public boolean removeById(Serializable id) {
        //删除第三张关系表的数据（sys_role_permission）
        //根据菜单id或权限id删除sys_role_permission权限菜单关系表数据
        permissionMapper.deleteRolePermissionByPid(id);
        return super.removeById(id);
    }

    @Override
    public List<Integer> findRolePermissionByRoleId(int roleId) {
        return permissionMapper.findRolePermissionByRoleId(roleId);
    }
}
