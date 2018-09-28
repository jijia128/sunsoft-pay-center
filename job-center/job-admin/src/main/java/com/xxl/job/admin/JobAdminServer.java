package com.xxl.job.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by ccb on 2017/1/20.
 */


@SpringBootApplication
@ComponentScan(basePackages={"com.xxl.job.admin","com.central"})
@PropertySource(value = "classpath:xxl-job-admin.properties")
public class JobAdminServer {
    public static void main(String[] args){
        SpringApplication.run(JobAdminServer.class,args);
    }
}