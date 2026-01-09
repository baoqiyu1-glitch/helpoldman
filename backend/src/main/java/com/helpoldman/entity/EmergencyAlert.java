package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("emergency_alerts")
public class EmergencyAlert {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String alertType;
    private String location;
    private String description;
    private String status;
    private Long handlerId;
    private Date handleTime;
    private String result;
    private Date alertTime;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}