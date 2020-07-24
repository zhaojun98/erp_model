package com.bdqn.sys.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdqn.sys.entity.Permission;
import com.bdqn.sys.entity.User;
import com.bdqn.sys.service.PermissionService;
import com.bdqn.sys.service.RoleService;
import com.bdqn.sys.service.UserService;
import com.bdqn.sys.utils.SystemConstant;
import com.bdqn.sys.vo.LoginUserVo;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义Realm
 */
public class UserRealm extends AuthorizingRealm {


    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;



    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //创建授权对象
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //1.获取当前登录主体
        LoginUserVo loginUserVo = (LoginUserVo) principalCollection.getPrimaryPrincipal();
        //2.获取当前用户拥有的权限列表
        Set<String> permissions = loginUserVo.getPermissions();
        //3.判断当前登录用户是否是超级管理员
        if(loginUserVo.getUser().getType()== SystemConstant.SUPERUSER){
            //超级管理员拥有所有操作权限
            simpleAuthorizationInfo.addStringPermission("*:*");
        }else{//普通用户
            //判断权限集合是否有数据
            if(permissions!=null && permissions.size()>0){
                //非超级管理员需要根据自己拥有的角色进行授权
                simpleAuthorizationInfo.addStringPermissions(permissions);
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 身份验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取当前登录主体
        String userName = (String) authenticationToken.getPrincipal();
        try {
            //根据用户名查询用户信息的方法
            User user = userService.findUserByName(userName);
            //对象不为空
            if(user!=null){
                //创建当前登录用户对象
                //创建登录用户对象，传入用户信息，角色列表，权限列表
                LoginUserVo loginUserVo = new LoginUserVo(user,null,null);
                /***************************关联权限代码开始***************************************/
                //创建条件构造器对象
                QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
                queryWrapper.eq("type",SystemConstant.TYPE_PERMISSION);//只查权限不查菜单

                //根据当前登录用户ID查询该用户拥有的角色列表
                Set<Integer> currentUserRoleIds = userService.findUserRoleByUserId(user.getId());
                //创建集合保存每个角色下拥有的权限菜单ID
                Set<Integer> pids = new HashSet<Integer>();
                //循环遍历当前用户拥有的角色列表
                for (Integer roleId : currentUserRoleIds) {
                    //4.根据角色ID查询每个角色下拥有的权限菜单
                    Set<Integer> permissionIds = roleService.findRolePermissionByRoleId(roleId);
                    //5.将查询出来的权限id放到集合中
                    pids.addAll(permissionIds);
                }
                //创建集合保存权限
                List<Permission> list = new ArrayList<Permission>();
                //判断pids集合是否有值
                if(pids!=null && pids.size()>0){
                    queryWrapper.in("id",pids);
                    //执行查询
                    list = permissionService.list(queryWrapper);
                }
                //查询权限编码
                Set<String> perCodes = new HashSet<String>();
                //循环每一个菜单
                for (Permission permission : list) {
                    perCodes.add(permission.getPercode());
                }
                //给权限集合赋值
                loginUserVo.setPermissions(perCodes);

                /***************************关联权限代码结束***************************************/
                //创建盐值(以用户名作为盐值)
                ByteSource salt = ByteSource.Util.bytes(user.getSalt());
                //创建身份验证对象
                //参数1：当前登录对象  参数2：密码  参数3：盐值 参数4：域名
                SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(loginUserVo,user.getLoginpwd(),salt,"");
                return info;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //验证失败
        return null;
    }


}
