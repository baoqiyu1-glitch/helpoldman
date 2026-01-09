package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("training_registrations")
public class TrainingRegistration {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private Long courseId;
    private String status;
    private Date registrationTime;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}