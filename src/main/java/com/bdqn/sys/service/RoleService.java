package com.bdqn.sys.service;

import com.bdqn.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-31
 */
public interface RoleService extends IService<Role> {

    /**
     * 保存分配权限关系
     * @param rid
     * @param ids
     * @return
     * @throws Exception
     */
    boolean saveRolePermission(int rid, String ids) throws Exception;

    /**
     * 根据角色ID查询该角色拥有的权限菜单id
     * @param roleId
     * @return
     * @throws Exception
     */
    Set<Integer> findRolePermissionByRoleId(Integer roleId) throws Exception;
}
