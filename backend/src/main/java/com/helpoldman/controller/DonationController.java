package com.helpoldman.controller;

import com.helpoldman.utils.Result;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/donation")
public class DonationController {
    
    /**
     * 获取捐赠信息
     */
    @GetMapping("/info")
    public Result getDonationInfo(@RequestHeader("Authorization") String token) {
        Map<String, Object> info = new HashMap<>();
        info.put("title", "爱心捐赠");
        info.put("description", "感谢您的爱心捐赠，您的每一份贡献都将帮助到需要帮助的老人。");
        info.put("amountOptions", Arrays.asList("50", "100", "200", "500", "custom"));
        info.put("elderId", 1); // 关联的老人ID
        
        return Result.success("获取成功", info);
    }
    
    /**
     * 提交捐赠
     */
    @PostMapping("/submit")
    public Result submitDonation(@RequestBody Map<String, Object> donationData, 
                                @RequestHeader("Authorization") String token) {
        // 实现捐赠提交逻辑
        return Result.success("捐赠成功", donationData);
    }
    
    /**
     * 获取捐赠记录
     */
    @GetMapping("/records")
    public Result getDonationRecords(@RequestHeader("Authorization") String token) {
        // 实现获取捐赠记录的逻辑
        return Result.success("获取成功", new ArrayList<>());
    }
}