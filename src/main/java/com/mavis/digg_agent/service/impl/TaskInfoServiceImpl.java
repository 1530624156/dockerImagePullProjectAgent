package com.mavis.digg_agent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mavis.digg_agent.entity.po.TaskInfo;
import com.mavis.digg_agent.mapper.TaskInfoMapper;
import com.mavis.digg_agent.service.TaskInfoService;
import org.springframework.stereotype.Service;

/**
* @author Mavis郭逸轩
* @description 针对表【task_info】的数据库操作Service实现
*/
@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo>
    implements TaskInfoService {

}
