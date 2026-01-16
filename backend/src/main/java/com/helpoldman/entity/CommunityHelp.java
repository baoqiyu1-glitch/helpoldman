package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("community_help")
public class CommunityHelp {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String title;
    private String description;
    private String helpType; // REQUEST, OFFER
    private String skill; // 技能类型
    private String availableTime; // 可提供帮助时间
    private String status; // PENDING, IN_PROGRESS, COMPLETED
    private Long helperId; // 帮助者ID
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}