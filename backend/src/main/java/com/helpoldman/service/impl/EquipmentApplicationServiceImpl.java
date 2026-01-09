package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.EquipmentApplication;
import com.helpoldman.mapper.EquipmentApplicationMapper;
import com.helpoldman.service.EquipmentApplicationService;
import org.springframework.stereotype.Service;

@Service
public class EquipmentApplicationServiceImpl extends ServiceImpl<EquipmentApplicationMapper, EquipmentApplication> 
    implements EquipmentApplicationService {
}
