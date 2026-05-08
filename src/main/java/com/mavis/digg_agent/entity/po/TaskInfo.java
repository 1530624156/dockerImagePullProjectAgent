package com.mavis.digg_agent.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * 任务信息
 * @TableName task_info
 */
@TableName(value = "task_info")
@Data
@ToString
public class TaskInfo implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 镜像名称
     */
    private String imageName;

    /**
     * 镜像标签
     */
    private String imageTag;

    /**
     * 任务状态 0-执行中 1-成功 -1-失败
     */
    private String taskStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建邮箱
     */
    private String createEmail;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
