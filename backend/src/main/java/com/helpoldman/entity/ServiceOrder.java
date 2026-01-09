package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("service_orders")
public class ServiceOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    private Long userId;
    private Long serviceTypeId;
    private String title;
    private String content;
    private String address;
    private String contactPhone;
    private Date serviceTime;
    private String status;
    private String priority;
    private String remarks;
    private Long volunteerId;
    private Date assignTime;
    private Date acceptTime;
    private Date completeTime;
    private Integer rating;
    private String feedback;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}