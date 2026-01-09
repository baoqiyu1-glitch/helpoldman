package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.ServiceType;

import java.util.List;

public interface ServiceTypeService extends IService<ServiceType> {
    
    /**
     * 获取所有启用的服务类型
     */
    List<ServiceType> getEnabledServiceTypes();
    
    /**
     * 根据代码获取服务类型
     */
    ServiceType getServiceTypeByCode(String code);
    
    /**
     * 获取服务类型树形结构
     */
    List<ServiceType> getServiceTypeTree();
}