package com.assignment.flamingo.config;

import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Cloud OpenFeign configuration class.
 *
 * @author Kate
 * @since 28-Apr-2026
 */
@Configuration
public class FeignSupportConfig {

    @Bean
    public Encoder encoder(ObjectProvider<FeignHttpMessageConverters> converters) {
        return new SpringEncoder(converters);
    }
}
