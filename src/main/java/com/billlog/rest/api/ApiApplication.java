package com.billlog.rest.api;

import com.billlog.rest.api.model.file.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({
		FileUploadProperties.class
})

public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	//스프링 시큐리티 패스워드 인코드관련 추가설정 값.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	//Kakao와 통신이 필요하므로 SpringRestApiApplication에 RestTemplate Bean을 추가합니다.
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
