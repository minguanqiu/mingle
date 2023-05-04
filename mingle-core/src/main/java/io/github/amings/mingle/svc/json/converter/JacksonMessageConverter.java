package io.github.amings.mingle.svc.json.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author Ming
 */

public class JacksonMessageConverter extends MappingJackson2HttpMessageConverter {

    public JacksonMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

}
