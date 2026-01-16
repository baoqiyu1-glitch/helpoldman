package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.Product;
import com.helpoldman.entity.ProductCategory;
import com.helpoldman.service.ProductCategoryService;
import com.helpoldman.service.ProductService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    
    @Autowired
    private ProductCategoryService productCategoryService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 获取所有商品分类
     */
    @GetMapping("/product-categories")
    public Result getProductCategories() {
        List<ProductCategory> categories = productCategoryService.list(
            new QueryWrapper<ProductCategory>().eq("status", "ACTIVE")
        );
        return Result.success(categories);
    }
    
    /**
     * 获取所有商品
     */
    @GetMapping("/products")
    public Result getProducts() {
        List<Product> products = productService.list(
            new QueryWrapper<Product>().eq("status", "ACTIVE")
        );
        return Result.success(products);
    }
    
    /**
     * 根据分类获取商品
     */
    @GetMapping("/products/category/{categoryId}")
    public Result getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.list(
            new QueryWrapper<Product>()
                .eq("category_id", categoryId)
                .eq("status", "ACTIVE")
        );
        return Result.success(products);
    }
    
    /**
     * 搜索商品
     */
    @GetMapping("/products/search")
    public Result searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.list(
            new QueryWrapper<Product>()
                .like("name", keyword)
                .or()
                .like("description", keyword)
                .eq("status", "ACTIVE")
        );
        return Result.success(products);
    }
    
    /**
     * 获取商品详情
     */
    @GetMapping("/products/{id}")
    public Result getProductDetail(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }
    
    /**
     * 创建商品分类
     */
    @PostMapping("/product-categories")
    public Result createProductCategory(@RequestBody ProductCategory category) {
        category.setStatus("ACTIVE");
        productCategoryService.save(category);
        return Result.success("创建商品分类成功");
    }
    
    /**
     * 创建商品
     */
    @PostMapping("/products")
    public Result createProduct(@RequestBody Product product) {
        product.setStatus("ACTIVE");
        productService.save(product);
        return Result.success("创建商品成功");
    }
    
    /**
     * 私有方法：从token中获取用户ID
     */
    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}