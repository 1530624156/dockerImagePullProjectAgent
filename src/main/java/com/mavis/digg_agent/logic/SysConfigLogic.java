package com.mavis.digg_agent.logic;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mavis.digg_agent.entity.enums.SysConfigEnum;
import com.mavis.digg_agent.entity.enums.YesOrNoEnum;
import com.mavis.digg_agent.entity.param.SysConfigParam;
import com.mavis.digg_agent.entity.po.SysConfig;
import com.mavis.digg_agent.service.SysConfigService;
import com.mavis.digg_agent.utils.RestResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SysConfigLogic extends BaseLogic{

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 获取所有配置项{key : value}
     * @return
     */
    public Map<String,String> getAllConfigToMap(){
        List<SysConfig> sysConfigList = sysConfigService.list();
        Map<String, String> sysConfigMap = sysConfigList.stream().collect(Collectors.toMap(SysConfig::getConfigKey, SysConfig::getConfigValue));
        return sysConfigMap;
    }

    /**
     * 测试系统
     * @param param 测试参数
     * @return 测试结果
     */
    public RestResult testSystem(SysConfigParam param) {
        if (StringUtils.isBlank(param.getUsername())){
            return RestResult.fail("用户名不能为空");
        }
        if (StringUtils.isBlank(param.getPassword())){
            return RestResult.fail("密码不能为空");
        }
        // 获取系统状态
        String configStatus = getSysValue(SysConfigEnum.STATUS.getValue());
        if (!YesOrNoEnum.YES.getValue().equals(configStatus)){
            return RestResult.fail("系统未启用");
        }
        String userName = getSysValue(SysConfigEnum.USERNAME.getValue());
        if (StringUtils.isBlank(userName)){
            return RestResult.fail("系统用户名未配置");
        }
        String passWord = getSysValue(SysConfigEnum.PASSWORD.getValue());
        if (StringUtils.isBlank(passWord)){
            return RestResult.fail("系统密码未配置");
        }
        // 校验账号密码
        if (!userName.equals(param.getUsername()) || !passWord.equals(param.getPassword())){
            return RestResult.fail("用户名或密码错误");
        }
        return RestResult.success("测试通过");
    }
}
