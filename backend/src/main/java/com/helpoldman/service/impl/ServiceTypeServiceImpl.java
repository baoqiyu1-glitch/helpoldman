package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.ServiceType;
import com.helpoldman.mapper.ServiceTypeMapper;
import com.helpoldman.service.ServiceTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeServiceImpl extends ServiceImpl<ServiceTypeMapper, ServiceType> implements ServiceTypeService {

    @Override
    public List<ServiceType> getEnabledServiceTypes() {
        QueryWrapper<ServiceType> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "ENABLED")
               .orderByAsc("sort_order");
        return this.list(wrapper);
    }

    @Override
    public ServiceType getServiceTypeByCode(String code) {
        QueryWrapper<ServiceType> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code);
        return this.getOne(wrapper);
    }

    @Override
    public List<ServiceType> getServiceTypeTree() {
        // 这里可以返回树形结构的服务类型，如果有层级关系的话
        return this.getEnabledServiceTypes();
    }
}