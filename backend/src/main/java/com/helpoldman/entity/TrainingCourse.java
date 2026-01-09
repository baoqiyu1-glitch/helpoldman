package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("training_courses")
public class TrainingCourse {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    private String description;
    private String instructor;
    private Date courseTime;
    private String location;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String imageUrl;
    private String status;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}