package com.mavis.digg_agent.logic;

import com.mavis.digg_agent.entity.enums.SysConfigEnum;
import org.springframework.stereotype.Component;



@Component
public class ShellLogic extends BaseLogic{
    public boolean startClash(){
        String clashShellPath = getSysValue(SysConfigEnum.CLASH_SHELL_PATH.getValue());
        // TODO 执行clash
        return true;
    }
}
