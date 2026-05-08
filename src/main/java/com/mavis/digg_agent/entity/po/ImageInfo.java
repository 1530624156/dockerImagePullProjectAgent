package com.mavis.digg_agent.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * ImageInfo
 *
 * @author Mavis郭逸轩
 * @since 2026/5/8 15:39
 */
@TableName(value ="image_info")
@Data
@ToString
public class ImageInfo implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String imageName;
    private String imageTag;
    private String downloadUrl;
    private String isDel;
    private Date expTime;
}
