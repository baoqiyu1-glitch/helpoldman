package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.SubsidyApplication;
import com.helpoldman.mapper.SubsidyApplicationMapper;
import com.helpoldman.service.SubsidyApplicationService;
import org.springframework.stereotype.Service;

@Service
public class SubsidyApplicationServiceImpl extends ServiceImpl<SubsidyApplicationMapper, SubsidyApplication> implements SubsidyApplicationService {
}