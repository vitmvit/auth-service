package ru.clevertec.news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Конфигурационный класс для настройки Protobuf.
 */
@Configuration
public class ProtobufConfig {

    /**
     * Создает и настраивает конвертер ProtobufHttpMessageConverter.
     *
     * @return экземпляр ProtobufHttpMessageConverter
     */
    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        List<MediaType> supportedMediaTypes = new ArrayList();
        supportedMediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        var converter = new ProtobufHttpMessageConverter();
        converter.setSupportedMediaTypes(supportedMediaTypes);
        return converter;
    }
}
