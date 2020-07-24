package com.bdqn.sys.utils;

public interface SystemConstant {
    /**
     * 当前登录用户的key
     */
    String LOGINUSER = "loginUser";


    /**
     *  登录成功
     */
    JSONResult LOGIN_SUCCESS = new JSONResult(true,"登录成功！");


    /**
     * 登录失败
     */
    JSONResult LOGIN_ERROR_PASS = new JSONResult(false,"登录失败,用户名或密码错误！");

    /**
     * 类型为菜单：用于首页左侧导航栏
     */
    String TYPE_MENU = "menu";

    /**
     * 类型为权限
     */
    String TYPE_PERMISSION ="permission" ;

    /**
     * 菜单是否展开，1展开
     */
    Integer OPEN_TRUE = 1;

    /**
     * 菜单是否展开，0不展开
     */
    Integer OPEN_FALSE = 0;

    /**
     * 角色为超级管理员
     */
    Integer SUPERUSER = 0;

    /**
     * 登录操作
     */
    String LOGIN_ACTION="登录操作";
    /**
     * 注销操作
     */
    String LOGOUT_ACTION="注销操作";
    /**
     * 查询操作
     */
    String SEARCH_ACTION="查询操作";
    /**
     * 更新操作
     */
    String UPDATE_ACTION="更新操作";
    /**
     * 添加操作
     */
    String ADD_ACTION="添加操作";
    /**
     * 删除操作
     */
    String DELETE_ACTION="删除操作";

    /**
     * 删除成功
     */
    JSONResult DELETE_SUCCESS = new JSONResult(true,"删除成功");
    /**
     * 删除失败
     */
    JSONResult DELETE_ERROR = new JSONResult(false,"删除失败");
    /**
     * 添加成功
     */
    JSONResult ADD_SUCCESS = new JSONResult(true,"添加成功");
    /**
     * 添加失败
     */
    JSONResult ADD_ERROR = new JSONResult(false,"添加失败");
    /**
     * 修改成功
     */
    JSONResult UPDATE_SUCCESS = new JSONResult(true,"修改成功");
    /**
     * 修改失败
     */
    JSONResult UPDATE_ERROR = new JSONResult(false,"修改失败");

    /**
     * 是否存在
     */
    String EXIST = "exist";
    /**
     * 验证信息
     */
    String MESSAGE = "message";

    /**
     * 权限分配成功
     */
    JSONResult DISTRIBUTE_SUCCESS = new JSONResult(true,"权限分配成功");

    /**
     * 权限分配失败
     */
    JSONResult DISTRIBUTE_ERROR = new JSONResult(false,"权限分配失败");

    /**
     * 普通用户
     */
    Integer NORMAL_USER = 1;

    /**
     * 用户默认的初始化密码
     */
    String DEFAULT_PWD = "123456";
    /**
     * 加密次数
     */
    Integer HASHITERATIONS = 2;

    /**
     * 重置成功
     */
    JSONResult RESET_SUCCESS = new JSONResult(true,"重置成功");

    /**
     * 重置失败
     */
    JSONResult RESET_ERROR = new JSONResult(false,"重置失败");
}
