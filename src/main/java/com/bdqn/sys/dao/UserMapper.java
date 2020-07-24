package com.bdqn.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bdqn.sys.entity.User;
import com.bdqn.sys.vo.UserVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-23
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询用户列表
     * @param page      分页信息
     * @param userVo    查询条件对象
     * @return
     * @throws Exception
     */
    IPage<User> findUserListByPage(@Param("page") IPage<User> page,@Param("user") UserVo userVo) throws Exception;

    /**
     * 根据用户ID查询该用户拥有的角色列表
     * @param id    用户id
     * @return
     * @throws Exception
     */
    @Select("SELECT rid FROM sys_role_user WHERE uid = #{uid}")
    Set<Integer> findUserRoleByUserId(int id) throws Exception;

    /**
     * 根据用户编号删除用户角色的数据
     * @param userId
     * @throws Exception
     */
    @Delete("delete from sys_role_user where uid=#{userid}")
    void deleteUserRoleByUserId(int userId) throws Exception;

    @Insert("INSERT INTO sys_role_user (rid,uid) VALUES(#{rid},#{uid})")
    void insertUserRole(@Param("uid") int userId, @Param("rid") String rid) throws Exception;
}
