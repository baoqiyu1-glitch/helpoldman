package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.MealDelivery;
import com.helpoldman.mapper.MealDeliveryMapper;
import com.helpoldman.service.MealDeliveryService;
import org.springframework.stereotype.Service;

@Service
public class MealDeliveryServiceImpl extends ServiceImpl<MealDeliveryMapper, MealDelivery> implements MealDeliveryService {
}