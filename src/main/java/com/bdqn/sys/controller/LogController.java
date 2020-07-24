package com.bdqn.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.sys.entity.Log;
import com.bdqn.sys.service.LogService;
import com.bdqn.sys.utils.DataGridViewResult;
import com.bdqn.sys.utils.JSONResult;
import com.bdqn.sys.utils.SystemConstant;
import com.bdqn.sys.vo.LogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-24
 */
@RestController
@RequestMapping("/sys/log")
public class LogController {

    @Resource
    private LogService logService;

    @RequestMapping("/loglist")
    public DataGridViewResult loglist(LogVo logVo){
        //创建分页信息，参数1：当前页码，参数2：每页显示数量
        IPage<Log> page = new Page<Log>(logVo.getPage(),logVo.getLimit());
        //创建条件构造器
        QueryWrapper<Log> queryWrapper = new QueryWrapper<Log>();
        //操作类型
        queryWrapper.eq(StringUtils.isNotBlank(logVo.getType()),"type",logVo.getType());
        //登录名称
        queryWrapper.like(StringUtils.isNotBlank(logVo.getLoginname()),"loginname",logVo.getLoginname());
        //开始时间
        queryWrapper.ge(logVo.getStartTime()!=null,"createtime",logVo.getStartTime());
        //结束时间
        queryWrapper.le(logVo.getEndTime()!=null,"createtime",logVo.getEndTime());
        //根据创建时间排序
        queryWrapper.orderByDesc("createtime");
        //调用查询日志列表的方法
        logService.page(page,queryWrapper);
        //返回数据
        return new DataGridViewResult(page.getTotal(),page.getRecords());
    }

    @RequestMapping("/delete")
    public JSONResult delete(String ids){
        //将字符串拆分成数组
        String [] idsStr  = ids.split(",");
        //判断是否删除成功
        if(logService.removeByIds(Arrays.asList(idsStr))){
            return SystemConstant.DELETE_SUCCESS;//删除成功
        }
        //删除失败
        return SystemConstant.DELETE_ERROR;
    }

}

