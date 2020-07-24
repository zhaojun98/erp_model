package com.bdqn.sys.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.sys.entity.Log;
import com.bdqn.sys.entity.User;
import com.bdqn.sys.service.LogService;
import com.bdqn.sys.service.RoleService;
import com.bdqn.sys.service.UserService;
import com.bdqn.sys.utils.*;
import com.bdqn.sys.vo.LoginUserVo;
import com.bdqn.sys.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-23
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;


    /**
     * 登录请求
     *
     * @param loginname
     * @param pwd
     * @param request
     * @return
     */
    @PostMapping("/login")
    public JSONResult login(String loginname, String pwd, HttpServletRequest request) {
        //获取当前主体对象
        Subject subject = SecurityUtils.getSubject();
        //创建令牌对象
        UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd);
        try {
            //执行登录
            subject.login(token);
            //获取当前登录用户
            LoginUserVo loginUserVo = (LoginUserVo) subject.getPrincipal();
            //保存当前登录用户
            request.getSession().setAttribute(SystemConstant.LOGINUSER, loginUserVo.getUser());

            //记录日志
            //内容，操作类型，登录人，登录人id，登录人ip地址，操作时间
            Log log = new Log("用户登录", SystemConstant.LOGIN_ACTION,
                    loginname + "-" + loginUserVo.getUser().getName(),
                    loginUserVo.getUser().getId(), request.getRemoteAddr(), new Date());
            logService.save(log);

            //返回结果
            //登录成功
            return SystemConstant.LOGIN_SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        //登录失败
        return SystemConstant.LOGIN_ERROR_PASS;
    }


    @RequestMapping("/userlist")
    public DataGridViewResult userlist(UserVo userVo){
        try {
            //创建分页对象
            IPage<User> page = new Page<User>(userVo.getPage(),userVo.getLimit());
            //调用分页查询的方法
            IPage<User> userIPage = userService.findUserListByPage(page,userVo);
            return new DataGridViewResult(userIPage.getTotal(),userIPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据部门编号查询该部门下的员工信息
     * @param deptId
     * @return
     */
    @RequestMapping("/loadUserByDeptId")
    public DataGridViewResult loadUserByDeptId(Integer deptId){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        //只查询普通用户
        queryWrapper.eq("type",SystemConstant.NORMAL_USER);
        //根据部门编号查询
        queryWrapper.eq(deptId!=null,"deptid",deptId);
        //调用查询的方法
        List<User> users = userService.list(queryWrapper);
        //将数据返回
        return new DataGridViewResult(users);
    }

    /**
     * 验证用户名是否存在
     * @param loginName
     * @return
     */
    @RequestMapping("/checkLoginName")
    public String checkLoginName(String loginName){
        Map<String,Object> map = new HashMap<String,Object>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("loginname",loginName);
        //获取验证的数量
        if(userService.count(queryWrapper)>0){
            map.put(SystemConstant.EXIST,true);
            map.put(SystemConstant.MESSAGE,"登录名称已存在,请重新输入！");
        }else{
            map.put(SystemConstant.EXIST,false);
            map.put(SystemConstant.MESSAGE,"登录名称可以使用！");
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("/addUser")
    public JSONResult addUser(User user){
        //入职日期
        user.setHiredate(new Date());
        //使用随机数作为密码盐值
        String salt = UUIDUtil.randomUUID();
        //默认密码
        user.setLoginpwd(PasswordUtil.md5(SystemConstant.DEFAULT_PWD,salt,SystemConstant.HASHITERATIONS));
        //盐值
        user.setSalt(salt);
        //用户类型
        user.setType(SystemConstant.NORMAL_USER);//普通用户
        //默认头像
        user.setImgpath("defaultImage.jpg");
        //调用新增用户的方法
        if(userService.save(user)){
            return SystemConstant.ADD_SUCCESS;
        }
        return SystemConstant.ADD_ERROR;
    }
    @RequestMapping("/updateUser")
    public JSONResult updateUser(User user){
        //调用修改用户的方法
        if(userService.updateById(user)){
            return SystemConstant.UPDATE_SUCCESS;
        }
        return SystemConstant.UPDATE_ERROR;
    }

    /**
     * 根据用户ID查询用户详细信息
     * @param id
     * @return
     */
    @RequestMapping("/loadUserById")
    public DataGridViewResult loadUserById(Integer id){
        return new DataGridViewResult(userService.getById(id));
    }


    @RequestMapping("/resetPwd")
    public JSONResult resetPwd(int id){
        //重新生成UUID
        String salt = UUIDUtil.randomUUID();
        //创建用户对象
        User user = new User();
        user.setId(id);//主键
        user.setSalt(salt);//新的salt盐值
        user.setLoginpwd(PasswordUtil.md5(SystemConstant.DEFAULT_PWD,salt,SystemConstant.HASHITERATIONS));
        //调用修改用户的方法
        if(userService.updateById(user)){
            return SystemConstant.RESET_SUCCESS;
        }
        return SystemConstant.RESET_ERROR;
    }


    /**
     * 初始化角色列表表格数据
     * @param id    用户编号
     * @return
     */
    @RequestMapping("/initRoleByUserId")
    public DataGridViewResult initRoleByUserId(int id){
        //查询所有的角色列表
        List<Map<String,Object>> mapList = roleService.listMaps();
        try {
            //调用查询当前用户已经拥有的角色列表的方法(查询当前用户ID自己已经有的角色列表)
            Set<Integer> roleIdsList = userService.findUserRoleByUserId(id);
            //循环比较当前用户已经拥有的角色，已经拥有的角色要选中
            for (Map<String, Object> objectMap : mapList) {
                //定义变量，标记是否选中，false表示不选中
                boolean flag = false;
                //取出所有角色的ID
                int roleId =(int)objectMap.get("id");
                //内层循环：循环用户已经有的角色列表
                for (Integer rid : roleIdsList) {
                    //比较rid与roleId是否相等，相等意味着该角色是有的，要选中
                    if(rid==roleId){
                        flag = true;//选中
                        break;
                    }
                }
                //将选中的状态放到集合中
                objectMap.put("LAY_CHECKED",flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new DataGridViewResult(Long.valueOf(mapList.size()),mapList);
    }


    @RequestMapping("/saveUserRole")
    public JSONResult saveUserRole(int userId,String roleIds){
        try {
            if(userService.saveUserRole(userId,roleIds)){
                return SystemConstant.DISTRIBUTE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SystemConstant.DISTRIBUTE_ERROR;

    }

}

