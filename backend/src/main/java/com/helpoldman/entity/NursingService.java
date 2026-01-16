package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("nursing_services")
public class NursingService {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private Long elderId; // 老人ID
    private String serviceType; // 服务类型
    private Date serviceTime;
    private String address;
    private String contactPhone;
    private String requirements; // 特殊要求
    private String status; // PENDING, ASSIGNED, IN_PROGRESS, COMPLETED
    private Long nurseId; // 护工ID
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}