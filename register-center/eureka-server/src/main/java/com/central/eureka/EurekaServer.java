package com.central.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.ConfigurableApplicationContext;

/** 
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月28日 下午22:50:29 
* 类说明 
* eureka高可用三台机器
*/
@EnableEurekaServer
@SpringCloudApplication
@EnableHystrixDashboard
@EnableTurbine
public class EurekaServer {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer.class, args);
	}
}