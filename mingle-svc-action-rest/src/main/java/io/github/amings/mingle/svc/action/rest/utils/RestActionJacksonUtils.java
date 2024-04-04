package io.github.amings.mingle.svc.action.rest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amings.mingle.svc.utils.JacksonUtils;

/**
 * Jackson utils
 *
 * @author Ming
 */
public class RestActionJacksonUtils extends JacksonUtils {
    public RestActionJacksonUtils(ObjectMapper objectMapper) {
        super(objectMapper);
    }

}
