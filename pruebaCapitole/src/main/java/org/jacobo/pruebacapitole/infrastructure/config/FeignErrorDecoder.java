package org.jacobo.pruebacapitole.infrastructure.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeignErrorDecoder implements ErrorDecoder {
    private static final Logger logger = LoggerFactory.getLogger(FeignErrorDecoder.class);
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        logger.error("Feign error: {} - {}", response.status(), response.reason());

        switch (response.status()) {
            case 400:
                return new RuntimeException("Bad Request from remote service");
            case 404:
                return new RuntimeException("Resource not found");
            case 500:
                return new RuntimeException("Internal server error from remote service");
            default:
                return defaultDecoder.decode(methodKey, response);
        }
    }
}