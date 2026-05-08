package com.mavis.digg_agent.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mavis.digg_agent.entity.enums.SysConfigEnum;
import com.mavis.digg_agent.entity.po.ImageInfo;
import com.mavis.digg_agent.mapper.ImageInfoMapper;
import com.mavis.digg_agent.service.ImageInfoService;
import com.mavis.digg_agent.utils.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * ImageInfoLogic
 *
 * @author Mavis郭逸轩
 * @since 2026/5/8 15:43
 */
@Component
public class ImageInfoLogic extends BaseLogic{
    private static final Logger log = LoggerFactory.getLogger(ImageInfoLogic.class);
    @Resource
    private ImageInfoService imageInfoService;
    @Resource
    private ShellLogic shellLogic;


    public RestResult getAllImageInfo() {
        LambdaQueryWrapper<ImageInfo> imageInfoLqw = new LambdaQueryWrapper<>();
        imageInfoLqw.eq(ImageInfo::getIsDel, "0");
        return RestResult.success(imageInfoService.list(imageInfoLqw));
    }

    public RestResult removeExpiredImage() {
        // 扫描已过期镜像
        LambdaQueryWrapper<ImageInfo> imageInfoLqw = new LambdaQueryWrapper<>();
        imageInfoLqw.eq(ImageInfo::getIsDel, "0")
                .le(ImageInfo::getExpTime, new Date());
        List<ImageInfo> expiredImageList = imageInfoService.list(imageInfoLqw);
        expiredImageList.forEach(imageinfo -> {
            imageinfo.setIsDel("1");
        });
        // 提取所有的downloadUrl
        expiredImageList.stream().map(ImageInfo::getDownloadUrl).forEach(downloadUrl -> {
            // 删除文件
            String[] split = downloadUrl.split("/");
            String fileName = getSysValue(SysConfigEnum.SYS_TAR_PATH.getValue()) + "/" + split[split.length - 1];
            shellLogic.deleteFile(fileName);
        });
        boolean result = imageInfoService.updateBatchById(expiredImageList);
        if (result) {
            int count = expiredImageList.size();
            log.info("已删除" + count + "个已过期镜像");
            return RestResult.success(count);
        }
        return RestResult.fail("删除失败");
    }
}
