package com.sign;

import com.sign.config.SpringContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author MM
 * @description
 * @create 2020-04-22 14:36
 **/
@MapperScan("com.sign")
@EnableScheduling
@SpringBootApplication
public class SignApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignApplication.class, args);
    }

    @Bean
    public SpringContextHolder springContextHolder(ApplicationContext applicationContext) {
        SpringContextHolder springContextHolder = new SpringContextHolder();
        springContextHolder.setApplicationContext(applicationContext);
        return springContextHolder;
    }
}
