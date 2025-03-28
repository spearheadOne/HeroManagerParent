package org.abondar.industrial.heromanager;

import org.abondar.industrial.heromanager.properties.AuthServerProperties;
import org.abondar.industrial.heromanager.properties.RedisProperties;
import org.abondar.industrial.heromanager.properties.AuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(value = {RedisProperties.class, AuthServerProperties.class, AuthProperties.class})
public class HeroManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HeroManagerApplication.class, args);
    }
}
