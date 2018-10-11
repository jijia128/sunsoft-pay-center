package com.cemtral.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.internal.EnableZipkinServer;


@EnableEurekaClient
@SpringBootApplication
@EnableZipkinServer        //默认采用HTTP通信方式启动ZipkinServer
public class ZipkinCenterMysqlServer {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinCenterMysqlServer.class, args);
	}

}
