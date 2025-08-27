package com.mavis.digg_agent.logic;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mavis.digg_agent.entity.enums.SysConfigEnum;
import com.mavis.digg_agent.entity.param.DockerImagesParam;
import com.mavis.digg_agent.utils.MyDockerUtil;
import com.mavis.digg_agent.utils.RestResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * DockerImagesLogic
 *
 * @author Mavis郭逸轩
 * @since 2025/8/27 9:57
 */
@Component
public class DockerImagesLogic extends BaseLogic{

    @Resource
    private ShellLogic shellLogic;

    /**
     * 拉取Docker镜像
     * @param param
     * @return
     */
    public RestResult pullDockerImages(DockerImagesParam param) {
       if (param == null){
           return RestResult.fail("参数不能为空");
       }
       if (StringUtils.isBlank(param.getImageName())){
           return RestResult.fail("镜像名称不能为空");
       }
       if (StringUtils.isBlank(param.getImageTag())){
           return RestResult.fail("镜像标签不能为空");
       }
       // 开始拉取镜像
        boolean clashResult = shellLogic.restartClash();
       if (!clashResult){
           return RestResult.fail("启用clash失败");
       }
       if (StringUtils.isBlank(param.getImageTag())){
           param.setImageTag("latest");
       }
       String imageName = String.format("%s:%s", param.getImageName(), param.getImageTag());
        boolean pullImageResult = MyDockerUtil.pullImage(imageName, getSysValue(SysConfigEnum.DOCKER_IP.getValue()), Integer.parseInt(getSysValue(SysConfigEnum.DOCKER_PORT.getValue())), null, null);
        if (pullImageResult){
            return RestResult.success("拉取镜像成功");
        }
        return RestResult.fail("拉取镜像失败");
    }
}
