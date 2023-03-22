package com.github.maly7.configrefreshdemo;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableConfigurationProperties({Menu.class, Coffee.class})
public class ConfigRefreshDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigRefreshDemoApplication.class, args);
	}

}
