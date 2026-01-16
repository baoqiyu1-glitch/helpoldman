package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.NursingService;
import com.helpoldman.mapper.NursingServiceMapper;
import com.helpoldman.service.NursingServiceService;
import org.springframework.stereotype.Service;

@Service
public class NursingServiceServiceImpl extends ServiceImpl<NursingServiceMapper, NursingService> implements NursingServiceService {
}