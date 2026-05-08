package com.mavis.digg_agent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.mavis.digg_agent.mapper")
@EnableAsync
public class DockerImagePullProjectAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerImagePullProjectAgentApplication.class, args);
    }

}
