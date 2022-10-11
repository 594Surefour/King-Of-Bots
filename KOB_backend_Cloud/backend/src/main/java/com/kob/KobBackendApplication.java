package com.kob;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.kob.mapper")
@SpringBootApplication
public class KobBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KobBackendApplication.class, args);
    }

}
