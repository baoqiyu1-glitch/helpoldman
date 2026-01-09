package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.Equipment;
import com.helpoldman.entity.EquipmentApplication;
import com.helpoldman.mapper.EquipmentMapper;
import com.helpoldman.service.EquipmentApplicationService;
import com.helpoldman.service.EquipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {
    
    @Autowired
    private EquipmentApplicationService equipmentApplicationService;
    
    @Override
    public boolean applyEquipment(Long userId, Long equipmentId, String reason) {
        Equipment equipment = getById(equipmentId);
        if (equipment == null || equipment.getAvailableQuantity() <= 0) {
            return false;
        }
        
        // 创建申请记录
        EquipmentApplication application = new EquipmentApplication();
        application.setUserId(userId);
        application.setEquipmentId(equipmentId);
        application.setApplyReason(reason);
        application.setStatus("PENDING");
        application.setCreateTime(new Date());
        
        equipmentApplicationService.save(application);
        
        // 减少可用数量
        equipment.setAvailableQuantity(equipment.getAvailableQuantity() - 1);
        updateById(equipment);
        
        return true;
    }
    
    @Override
    public boolean returnEquipment(Long applicationId) {
        EquipmentApplication application = equipmentApplicationService.getById(applicationId);
        if (application == null) {
            return false;
        }
        
        Equipment equipment = getById(application.getEquipmentId());
        if (equipment != null) {
            equipment.setAvailableQuantity(equipment.getAvailableQuantity() + 1);
            updateById(equipment);
        }
        
        application.setStatus("RETURNED");
        application.setReturnTime(new Date());
        equipmentApplicationService.updateById(application);
        
        return true;
    }
}