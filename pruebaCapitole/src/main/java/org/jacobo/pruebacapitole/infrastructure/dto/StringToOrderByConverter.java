package org.jacobo.pruebacapitole.infrastructure.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToOrderByConverter implements Converter<String, OrderBy> {
    @Override
    public OrderBy convert(String source) {
        if (source == null) return null;
        try {
            return OrderBy.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid value for OrderBy: " + source);
        }
    }
}

