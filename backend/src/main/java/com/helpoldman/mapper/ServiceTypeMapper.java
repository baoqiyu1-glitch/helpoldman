package com.helpoldman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helpoldman.entity.ServiceType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceTypeMapper extends BaseMapper<ServiceType> {
    // 可以添加自定义的查询方法
}