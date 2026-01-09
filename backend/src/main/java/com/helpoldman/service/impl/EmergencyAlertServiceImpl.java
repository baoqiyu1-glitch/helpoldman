package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.EmergencyAlert;
import com.helpoldman.mapper.EmergencyAlertMapper;
import com.helpoldman.service.EmergencyAlertService;
import com.helpoldman.utils.Result;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmergencyAlertServiceImpl extends ServiceImpl<EmergencyAlertMapper, EmergencyAlert> implements EmergencyAlertService {
    
    @Override
    public List<EmergencyAlert> getAlertsByStatus(String status) {
        QueryWrapper<EmergencyAlert> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status)
               .orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }
    
    @Override
    public Result handleAlert(Long alertId, Long handlerId, String result) {
        EmergencyAlert alert = getById(alertId);
        if (alert == null) {
            return Result.error("报警记录不存在");
        }
        
        alert.setHandlerId(handlerId);
        alert.setHandleTime(new Date());
        alert.setStatus("RESOLVED");
        alert.setResult(result);
        
        boolean success = updateById(alert);
        return success ? Result.success("处理成功") : Result.error("处理失败");
    }
}