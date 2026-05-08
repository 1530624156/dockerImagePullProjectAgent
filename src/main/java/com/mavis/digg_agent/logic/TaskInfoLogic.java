package com.mavis.digg_agent.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mavis.digg_agent.entity.po.TaskInfo;
import com.mavis.digg_agent.service.TaskInfoService;
import com.mavis.digg_agent.utils.RestResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * TaskInfoLogic
 *
 * @author Mavis郭逸轩
 */
@Component
public class TaskInfoLogic extends BaseLogic {

    @Resource
    private TaskInfoService taskInfoService;

    public RestResult getAllRunningTask() {
        LambdaQueryWrapper<TaskInfo> taskInfoLqw = new LambdaQueryWrapper<>();
        taskInfoLqw.orderByDesc(TaskInfo::getCreateTime);
        return RestResult.success(taskInfoService.list(taskInfoLqw));
    }
}
