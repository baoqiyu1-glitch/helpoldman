package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.ProductCategory;
import com.helpoldman.mapper.ProductCategoryMapper;
import com.helpoldman.service.ProductCategoryService;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
}