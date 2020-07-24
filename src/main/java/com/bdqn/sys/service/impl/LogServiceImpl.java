package com.bdqn.sys.service.impl;

import com.bdqn.sys.entity.Log;
import com.bdqn.sys.dao.LogMapper;
import com.bdqn.sys.service.LogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KazuGin
 * @since 2019-12-24
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}
