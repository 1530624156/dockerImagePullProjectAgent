package com.mavis.digg_agent.controller;

import com.mavis.digg_agent.logic.TaskInfoLogic;
import com.mavis.digg_agent.utils.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * TaskInfoController
 *
 * @author Mavis郭逸轩
 * @since 2026/5/8 16:54
 */
@RestController
@RequestMapping("task")
public class TaskInfoController {

    @Resource
    private TaskInfoLogic taskInfoLogic;

    @RequestMapping("list")
    public RestResult getAllRunningTask(){
        return taskInfoLogic.getAllRunningTask();
    }
}
