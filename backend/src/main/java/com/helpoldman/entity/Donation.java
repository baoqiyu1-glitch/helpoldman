package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("donations")
public class Donation {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long donorId;
    private String donationType; // MONEY, GOODS, SERVICE
    private BigDecimal amount;
    private String goodsName;
    private Integer goodsQuantity;
    private String description;
    private String status; // PENDING, RECEIVED, DISTRIBUTED
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}