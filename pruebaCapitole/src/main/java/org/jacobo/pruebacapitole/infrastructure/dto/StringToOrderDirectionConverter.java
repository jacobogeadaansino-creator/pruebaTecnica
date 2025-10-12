package org.jacobo.pruebacapitole.infrastructure.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToOrderDirectionConverter implements Converter<String, OrderDirection> {
    @Override
    public OrderDirection convert(String source) {
        if (source == null) return null;
        try {
            return OrderDirection.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid value for OrderDirection: " + source);
        }
    }
}

