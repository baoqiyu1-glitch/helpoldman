package com.helpoldman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helpoldman.entity.TrainingRegistration;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrainingRegistrationMapper extends BaseMapper<TrainingRegistration> {
    // 可以添加自定义的查询方法
}