package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("meal_delivery")
public class MealDelivery {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private Long elderId; // 老人ID
    private String mealType; // 餐食类型
    private Date deliveryTime;
    private String address;
    private String contactPhone;
    private String specialRequirements; // 特殊要求
    private String status; // PENDING, PREPARING, DELIVERING, COMPLETED
    private Long delivererId; // 送餐员ID
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}