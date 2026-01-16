package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.Donation;
import com.helpoldman.mapper.DonationMapper;
import com.helpoldman.service.DonationService;
import org.springframework.stereotype.Service;

@Service
public class DonationServiceImpl extends ServiceImpl<DonationMapper, Donation> implements DonationService {
}