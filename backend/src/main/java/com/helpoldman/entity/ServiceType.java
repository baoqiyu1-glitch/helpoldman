package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("service_types")
public class ServiceType {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private String code;
    private String description;
    private Integer sortOrder;
    private String status;
    private Long parentId;
    private String icon;
    private String color;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    
    // 手动添加setter方法（如果Lombok无法正常工作）
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getColor() {
        return color;
    }
}