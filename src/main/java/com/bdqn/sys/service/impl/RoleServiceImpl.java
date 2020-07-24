package com.bdqn.sys.service.impl;

import com.bdqn.sys.entity.Role;
import com.bdqn.sys.dao.RoleMapper;
import com.bdqn.sys.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-31
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public boolean removeById(Serializable id) {
        //根据角色id删除两张关系表的数据,分别是：sys_role_user(角色用户关系表)，sys_role_permission(角色权限关系表)
        //调用删除角色用户关系表的方法
        roleMapper.deleteRoleUserByRoleId(id);
        //调用删除角色权限关系表的方法
        roleMapper.deleteRolePermissionByRoleId(id);
        return super.removeById(id);
    }

    @Override
    public boolean saveRolePermission(int rid, String ids) throws Exception {
        try {
            //先删除原有的数据
            roleMapper.deleteRolePermissionByRoleId(rid);
            //再保存新的关系数据
            //拆分权限id字符串
            String [] pids = ids.split(",");
            for (int i = 0; i < pids.length; i++) {
                //调用保存角色权限关系的方法
                roleMapper.insertRolePermission(rid,pids[i]);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Set<Integer> findRolePermissionByRoleId(Integer roleId) throws Exception {
        return roleMapper.findRolePermissionByRoleId(roleId);
    }
}
