package com.github.maly7.configrefreshdemo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties("coffee")
public record Coffee(String special) {
}
