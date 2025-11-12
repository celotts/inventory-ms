package com.celotts.taxservice.infrastructure.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
public class SpringMessageSourceHolder {

    private static MessageSource messageSource;

    public SpringMessageSourceHolder(MessageSource source) {
        SpringMessageSourceHolder.messageSource = source;
    }

    public static MessageSource get() {
        return messageSource;
    }

    public static String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}