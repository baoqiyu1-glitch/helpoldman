package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("subsidy_applications")
public class SubsidyApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String subsidyType; // 补助类型
    private BigDecimal appliedAmount; // 申请金额
    private String applicationReason; // 申请理由
    private String supportingDocuments; // 证明材料
    private String status; // PENDING, APPROVED, REJECTED
    private String reviewComments; // 审核意见
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}