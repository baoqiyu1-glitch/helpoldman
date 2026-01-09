package com.helpoldman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.helpoldman.mapper")
public class HelpOldManApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelpOldManApplication.class, args);
        System.out.println("助老助残系统后端服务启动成功！");
    }
}