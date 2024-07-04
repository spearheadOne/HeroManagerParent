package org.abondar.industrial.heromanager;

import org.abondar.industrial.heromanager.redis.RedisProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(RedisProperties.class)
public class HeroManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HeroManagerApplication.class, args);
    }
}
