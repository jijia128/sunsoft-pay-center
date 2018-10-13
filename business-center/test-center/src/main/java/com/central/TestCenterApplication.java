package com.central;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.central.annotation.EnableLogging;
import com.central.autoconfigure.port.PortApplicationEnvironmentPreparedEventListener;

/** 
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2018年4月5日 下午19:52:21
* 类说明 
*/
 
@Configuration
@EnableLogging
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class TestCenterApplication {
	
	public static void main(String[] args) {
		//	固定端口启动
//			SpringApplication.run(TestCenterApplication.class, args);
		//随机端口启动
		SpringApplication app = new SpringApplication(TestCenterApplication.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);
	}
	//@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
		return tomcat;
	}
	// 配置http
	private Connector createStandardConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(8446);
		return connector;
	}
}
