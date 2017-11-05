package pl.mwojciechowski.github.rest.client;

import feign.Logger;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mwojciechowski
 */
@Configuration
@EnableFeignClients
public class FeignConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
