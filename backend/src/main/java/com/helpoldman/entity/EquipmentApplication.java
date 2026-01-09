package com.helpoldman.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("equipment_applications")
public class EquipmentApplication {
    private Long id;
    private Long userId;
    private Long equipmentId;
    private String applyReason;
    private Integer expectedDuration;
    private String status;
    private Date approveTime;
    private Date returnTime;
    private String remarks;
    private Long approverId;
    private Date createTime;
}
