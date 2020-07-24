package com.bdqn.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.sys.entity.Notice;
import com.bdqn.sys.entity.User;
import com.bdqn.sys.service.NoticeService;
import com.bdqn.sys.utils.DataGridViewResult;
import com.bdqn.sys.utils.JSONResult;
import com.bdqn.sys.utils.SystemConstant;
import com.bdqn.sys.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-26
 */
@RestController
@RequestMapping("/sys/notice")
public class NoticeController {


    @Resource
    private NoticeService noticeService;

    /**
     * 查询公告列表
     * @param noticeVo
     * @return
     */
    @RequestMapping("/noticelist")
    public DataGridViewResult noticelist(NoticeVo noticeVo){
        //创建分页信息，参数1：当前页码，参数2：每页显示数量
        IPage<Notice> page = new Page<Notice>(noticeVo.getPage(),noticeVo.getLimit());
        //创建条件构造器
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>();
        //标题
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()),"title",noticeVo.getTitle());
        //发布人
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()),"opername",noticeVo.getOpername());
        //开始时间
        queryWrapper.ge(noticeVo.getStartTime()!=null,"createtime",noticeVo.getStartTime());
        //结束时间
        queryWrapper.le(noticeVo.getEndTime()!=null,"createtime",noticeVo.getEndTime());
        //根据创建时间排序
        queryWrapper.orderByDesc("createtime");
        //调用查询日志列表的方法
        noticeService.page(page,queryWrapper);
        //返回数据
        return new DataGridViewResult(page.getTotal(),page.getRecords());
    }

    @PostMapping("/addNotice")
    public JSONResult addNotice(Notice notice, HttpSession session){
        //获取当前登录用户
        User user = (User) session.getAttribute(SystemConstant.LOGINUSER);
        notice.setCreatetime(new Date());//创建时间
        notice.setOpername(user.getName());//发布人
        //判断是否成功
        if(noticeService.save(notice)){
            //添加成功
            return SystemConstant.ADD_SUCCESS;
        }
        //添加失败
        return SystemConstant.ADD_ERROR;
    }

    /**
     * 修改公告
     * @param notice
     * @return
     */
    @PostMapping("/updateNotice")
    public JSONResult updateNotice(Notice notice){
        notice.setModifytime(new Date());//修改时间
        //判断是否成功
        if(noticeService.updateById(notice)){
            //修改成功
            return SystemConstant.UPDATE_SUCCESS;
        }
        //修改失败
        return SystemConstant.UPDATE_ERROR;
    }

    @RequestMapping("/deleteById")
    public JSONResult deleteById(int id){
        if(noticeService.removeById(id)){
            //成功
            return SystemConstant.DELETE_SUCCESS;
        }
        //失败
        return SystemConstant.DELETE_ERROR;
    }
}

