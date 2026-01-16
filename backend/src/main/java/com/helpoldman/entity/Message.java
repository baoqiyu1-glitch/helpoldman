package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("messages")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String title;
    private String content;
    private String type; // 消息类型：SYSTEM, VOLUNTEER, TRAINING等
    
    @TableField("is_read") // 修复：添加数据库字段映射
    private Boolean isRead; // 是否已读
    
    private String icon; // 消息图标（前端展示用）
    
    @TableField("`description`") // 修复：添加数据库字段映射
    private String description; // 消息简短描述（前端展示用）
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    
    @TableLogic
    private Integer deleted;
}