package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.EmergencyAlert;
import com.helpoldman.utils.Result;

import java.util.List;

public interface EmergencyAlertService extends IService<EmergencyAlert> {
    List<EmergencyAlert> getAlertsByStatus(String status);
    Result handleAlert(Long alertId, Long handlerId, String result);
}