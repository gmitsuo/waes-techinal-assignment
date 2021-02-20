package com.waes.challenge.configuration;

import com.waes.challenge.domain.DiffSide;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration added to allow request parameters and path variables
 * to be used in lower case and automatically converted to their
 * respective enum references
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EnumConverter());
    }

    static class EnumConverter implements Converter<String, DiffSide> {
        @Override
        public DiffSide convert(String source) {
            return DiffSide.valueOf(source.toUpperCase());
        }
    }
}
