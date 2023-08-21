package com.lahee.mutsasns;

import com.lahee.mutsasns.config.AuditorAwareImpl;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaAuditing
public class MutsaSnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MutsaSnsApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() { //등록자와 수정자를 처리해 주는 AuditorAware을 빈으로 등록
		return new AuditorAwareImpl();
	}

	@Bean
	public MappingJackson2HttpMessageConverter octetStreamJsonConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "octet-stream")));
		return converter;
	}

	@Bean
	public LayoutDialect layoutDialect() { //이부분을 설정해야합니다
		return new LayoutDialect();
	}
}
