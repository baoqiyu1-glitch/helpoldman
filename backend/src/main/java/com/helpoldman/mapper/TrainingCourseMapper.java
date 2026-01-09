package com.helpoldman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helpoldman.entity.TrainingCourse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrainingCourseMapper extends BaseMapper<TrainingCourse> {
    // 可以添加自定义的查询方法
}