package com.helpoldman.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ServiceApplyDTO {
    private Long serviceTypeId;
    private String serviceContent;  // 改为serviceContent匹配前端
    private Date appointmentTime;   // 改为appointmentTime匹配前端
    private String address;
    private String contactPhone;
}