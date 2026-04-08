package com.kakarot.mall.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kakarot.mall")
@MapperScan("com.kakarot.mall.**.mapper")
public class MallAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallAppApplication.class, args);
    }

}
