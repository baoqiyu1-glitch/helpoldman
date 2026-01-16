package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("barrier_free_renovation")
public class BarrierFreeRenovation {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String renovationType; // 改造类型
    private String address;
    private String phone;
    private String description;
    private String status; // PENDING, APPROVED, IN_PROGRESS, COMPLETED
    private String images; // 现场照片URL，多个用逗号分隔
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}