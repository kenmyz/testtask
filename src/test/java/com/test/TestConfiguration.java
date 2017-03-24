package com.test;

import com.test.service.MainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Ken on 23.03.2017.
 */
@Configuration
@ComponentScan("com.test")
public class TestConfiguration {

	@Bean("MainService")
	MainService getService(){
		return null;
	}
}
