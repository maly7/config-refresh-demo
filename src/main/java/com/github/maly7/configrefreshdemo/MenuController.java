package com.github.maly7.configrefreshdemo;

import reactor.core.publisher.Mono;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class MenuController {

	private final Menu menu;
	private final Coffee coffee;

	public MenuController(Menu menu, Coffee coffee) {
		this.menu = menu;
		this.coffee = coffee;
	}

	@GetMapping("/menu/special")
	public Mono<String> menuSpecial() {
		return Mono.just(menu.getSpecial());
	}

	@GetMapping("/coffee/special")
	public Mono<String> coffeeSpecial() {
		return Mono.just(coffee.getSpecial());
	}
}