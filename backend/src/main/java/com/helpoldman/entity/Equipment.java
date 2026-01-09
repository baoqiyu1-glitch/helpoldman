package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("equipment")
public class Equipment {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private String type;
    private String model;
    private Integer quantity;
    private Integer availableQuantity;
    private String description;
    private String imageUrl;
    private String status;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}