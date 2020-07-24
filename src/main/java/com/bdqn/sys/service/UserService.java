package com.bdqn.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdqn.sys.entity.User;
import com.bdqn.sys.vo.UserVo;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-23
 */
public interface UserService extends IService<User> {


    /**
     * 根据用户名查询用户信息的方法
     * @param userName
     * @return
     * @throws Exception
     */
    User findUserByName(String userName) throws Exception;
    /**
     * 分页查询用户列表
     * @param page      分页信息
     * @param userVo    查询条件对象
     * @return
     * @throws Exception
     */
    IPage<User> findUserListByPage(IPage<User> page, UserVo userVo) throws Exception;

    /**
     * 根据用户ID查询该用户拥有的角色列表
     * @param id    用户id
     * @return
     * @throws Exception
     */
    Set<Integer> findUserRoleByUserId(int id) throws Exception;

    /**
     * 保存用户与角色的关系
     * @param userId
     * @param roleIds
     * @return
     * @throws Exception
     */
    boolean saveUserRole(int userId, String roleIds) throws Exception;
}
