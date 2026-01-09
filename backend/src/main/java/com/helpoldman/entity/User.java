package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private Integer age;
    private String gender;
    private String healthStatus;
    private String userType;
    private String address;
    private String idCard;
    private String emergencyContact;
    private String emergencyPhone;
     @TableField("avatar") 
    private String avatarUrl;
    private Integer serviceHours;
    private String status;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    
    @TableLogic
    private Integer deleted;
}