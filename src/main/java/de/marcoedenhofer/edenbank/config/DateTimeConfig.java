package de.marcoedenhofer.edenbank.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class DateTimeConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("dd.MM.yyyy"));
    }
}
