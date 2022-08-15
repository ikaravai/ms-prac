package com.solvd.ikaravai.mybatisservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.solvd.ikaravai.mybatisservice.mapper")
public class MybatisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybatisServiceApplication.class, args);
	}

}