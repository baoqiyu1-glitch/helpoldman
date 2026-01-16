package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.CommunityHelp;
import com.helpoldman.mapper.CommunityHelpMapper;
import com.helpoldman.service.CommunityHelpService;
import org.springframework.stereotype.Service;

@Service
public class CommunityHelpServiceImpl extends ServiceImpl<CommunityHelpMapper, CommunityHelp> implements CommunityHelpService {
}