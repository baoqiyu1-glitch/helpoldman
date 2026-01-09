package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.Equipment;

public interface EquipmentService extends IService<Equipment> {
    boolean applyEquipment(Long userId, Long equipmentId, String reason);
    boolean returnEquipment(Long applicationId);
}