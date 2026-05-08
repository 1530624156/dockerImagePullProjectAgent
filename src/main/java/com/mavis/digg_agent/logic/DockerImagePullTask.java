package com.mavis.digg_agent.logic;

import com.mavis.digg_agent.entity.enums.SysConfigEnum;
import com.mavis.digg_agent.entity.param.DockerImagesParam;
import com.mavis.digg_agent.entity.po.ImageInfo;
import com.mavis.digg_agent.entity.po.TaskInfo;
import com.mavis.digg_agent.service.ImageInfoService;
import com.mavis.digg_agent.service.TaskInfoService;
import com.mavis.digg_agent.utils.MailTemplate;
import com.mavis.digg_agent.utils.MailUtils;
import com.mavis.digg_agent.utils.MyDockerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * Docker镜像拉取异步任务
 *
 * @author Mavis郭逸轩
 */
@Component
public class DockerImagePullTask extends BaseLogic {

    private static final Logger log = LoggerFactory.getLogger(DockerImagePullTask.class);
    @Resource
    private ShellLogic shellLogic;

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private ImageInfoService imageInfoService;

    /**
     * 异步执行拉取Docker镜像
     */
    @Async
    public void doPullDockerImage(DockerImagesParam param, TaskInfo taskInfo) {
        // 启用Clash
        boolean clashResult = shellLogic.restartClash();
        if (!clashResult) {
            taskInfo.setTaskStatus("-1");
            taskInfoService.updateById(taskInfo);
            return;
        }
        // 开始拉取镜像
        String imageName = String.format("%s:%s", param.getImageName(), param.getImageTag());
        boolean pullImageResult = MyDockerUtil.pullImage(imageName,
                getSysValue(SysConfigEnum.DOCKER_IP.getValue()),
                Integer.parseInt(getSysValue(SysConfigEnum.DOCKER_PORT.getValue())), null, null);
        if (pullImageResult) {
            // 打tar包
            String tarFileName = param.getImageName().replace(".", "_").replace("/", "_")
                    + "_" + param.getImageTag().replace(".", "_") + ".tar";
            String tarPath = getSysValue(SysConfigEnum.SYS_TAR_PATH.getValue()) + "/" + tarFileName;
            boolean packedImageResult = MyDockerUtil.saveImage(imageName, tarPath,
                    getSysValue(SysConfigEnum.DOCKER_IP.getValue()),
                    Integer.parseInt(getSysValue(SysConfigEnum.DOCKER_PORT.getValue())));
            if (packedImageResult) {
                // 删除镜像
                boolean removeImage = MyDockerUtil.removeImage(imageName, getSysValue(SysConfigEnum.DOCKER_IP.getValue()), Integer.valueOf(getSysValue(SysConfigEnum.DOCKER_PORT.getValue())));
                log.info("删除镜像结果：" + removeImage + " " +imageName);
                // 保存镜像信息记录
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setImageName(param.getImageName());
                imageInfo.setImageTag(param.getImageTag());
                imageInfo.setDownloadUrl(getSysValue(SysConfigEnum.SYS_DOWNLOAD_PATH.getValue()) + "/" + tarFileName);
                imageInfo.setIsDel("0");
                Date expTime = calendar.getTime();
                imageInfo.setExpTime(expTime);
                imageInfoService.save(imageInfo);

                taskInfo.setTaskStatus("1");
                taskInfoService.updateById(taskInfo);

                // 发送邮件通知
                if (param.getCreateEmail() != null && !param.getCreateEmail().isEmpty()) {
                    String html = MailTemplate.imageReadyTemplate(
                            param.getImageName(), param.getImageTag(),
                            imageInfo.getDownloadUrl(), expTime);
                    MailUtils.sendHtmlMail(param.getCreateEmail(), "Docker镜像拉取完成通知", html);
                    log.info("已发送邮件通知至：{}", param.getCreateEmail());
                }
                return;
            }
        }
        taskInfo.setTaskStatus("-1");
        taskInfoService.updateById(taskInfo);
    }
}
