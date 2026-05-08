package com.mavis.digg_agent.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mavis.digg_agent.entity.enums.SysConfigEnum;
import com.mavis.digg_agent.entity.param.DockerImagesParam;
import com.mavis.digg_agent.entity.po.ImageInfo;
import com.mavis.digg_agent.entity.po.TaskInfo;
import com.mavis.digg_agent.service.ImageInfoService;
import com.mavis.digg_agent.service.TaskInfoService;
import com.mavis.digg_agent.utils.MyDockerUtil;
import com.mavis.digg_agent.utils.RestResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private ImageInfoService imageInfoService;

    @Resource
    private DockerImagePullTask dockerImagePullTask;

    /**
     * 拉取Docker镜像
     * @param param 镜像入参
     * @return 拉取结果
     */
    public RestResult pullDockerImages(DockerImagesParam param) {
        if (param == null){
            return RestResult.fail("参数不能为空");
        }
        if (StringUtils.isBlank(param.getImageName())){
            return RestResult.fail("镜像名称不能为空");
        }
        if (StringUtils.isBlank(param.getImageTag())){
            param.setImageTag("latest");
        }
        // 查询是否已存在此镜像
        LambdaQueryWrapper<ImageInfo> imageInfoLqw = new LambdaQueryWrapper<>();
        imageInfoLqw.eq(ImageInfo::getImageName,param.getImageName()).eq(ImageInfo::getImageTag,param.getImageTag()).eq(ImageInfo::getIsDel,"0");
        long count = imageInfoService.count(imageInfoLqw);
        if (count > 0) {
            return RestResult.success("已存在此镜像，请前往[镜像列表]下载");
        }
        // 查询正在执行的任务
        LambdaQueryWrapper<TaskInfo> taskInfoLqw = new LambdaQueryWrapper<TaskInfo>().eq(TaskInfo::getTaskStatus, "0");
        List<TaskInfo> list = taskInfoService.list(taskInfoLqw);
        String maxTaskCount = getSysValue(SysConfigEnum.SYS_TASK_MAXCOUNT.getValue());
        if (StringUtils.isNotBlank(maxTaskCount)) {
            int maxCount = Integer.parseInt(maxTaskCount);
            if (list.size() >= maxCount) {
                return RestResult.fail("目前已到最大任务数，请稍后再试");
            }
        }
        // 创建任务记录，状态为执行中(0)
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskName("镜像拉取任务");
        taskInfo.setImageName(param.getImageName());
        taskInfo.setImageTag(param.getImageTag());
        taskInfo.setTaskStatus("0");
        taskInfo.setCreateTime(new Date());
        taskInfo.setCreateEmail(param.getCreateEmail());
        taskInfoService.save(taskInfo);

        // 异步执行拉取任务
        dockerImagePullTask.doPullDockerImage(param, taskInfo);

        return RestResult.success("任务已提交");
    }

    /**
     * 搜索镜像
     * @param param 镜像入参
     * @return 搜索结果
     */
    public RestResult searchDockerImages(DockerImagesParam param){
        if (param == null){
            return RestResult.fail("参数不能为空");
        }
        if (StringUtils.isBlank(param.getImageName())){
            return RestResult.fail("镜像名称不能为空");
        }
        // 启用Clash
        boolean clashResult = shellLogic.restartClash();
        if (!clashResult){
            return RestResult.fail("启用clash失败");
        }
        boolean searchImageResult = MyDockerUtil.searchImage(param.getImageName(), getSysValue(SysConfigEnum.DOCKER_IP.getValue()), Integer.parseInt(getSysValue(SysConfigEnum.DOCKER_PORT.getValue())));
        if (searchImageResult){
            return RestResult.success("搜索镜像成功");
        }
        return RestResult.fail("未找到此镜像");
    }

    //=================================内部方法=================================
    /**
     * 镜像打tar包
     * @param param 镜像入参
     * @return 打包结果
     */
    private boolean packImageToTar(DockerImagesParam param) {
        if (param == null) {
            return false;
        }
        if (StringUtils.isBlank(param.getImageName())) {
            return false;
        }
        if (StringUtils.isBlank(param.getImageTag())) {
            param.setImageTag("latest");
        }
        String imageName = String.format("%s:%s", param.getImageName(), param.getImageTag());
        String tarFileName = param.getImageName().replace(".", "_").replace("/","_") + "_" + param.getImageTag().replace(".", "_") + ".tar";
        String tarPath = getSysValue(SysConfigEnum.SYS_TAR_PATH.getValue()) + "/" + tarFileName;
        boolean saveResult = MyDockerUtil.saveImage(imageName, tarPath,
                getSysValue(SysConfigEnum.DOCKER_IP.getValue()),
                Integer.parseInt(getSysValue(SysConfigEnum.DOCKER_PORT.getValue())));
        if (saveResult) {
            return true;
        }
        return false;
    }
}
