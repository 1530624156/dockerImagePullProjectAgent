package com.mavis.digg_agent.logic;

import com.mavis.digg_agent.entity.enums.SysConfigEnum;
import com.mavis.digg_agent.utils.MyProcessUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
public class ShellLogic extends BaseLogic{

    /**
     * 重启clash
     * @return
     */
    public boolean restartClash(){
        String clashShellPath = getSysValue(SysConfigEnum.CLASH_SHELL_PATH.getValue());
        // 执行clash
        String[] cmds = {"sh",clashShellPath};
        try {
            List<String> res = MyProcessUtil.callShellCommand(cmds, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
