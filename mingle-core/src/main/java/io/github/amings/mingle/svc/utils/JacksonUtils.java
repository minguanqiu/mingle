package io.github.amings.mingle.svc.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Ming
 */

@Slf4j
@Getter
public class JacksonUtils {

    private final ObjectMapper objectMapper;

    public JacksonUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode getObjectNode() {
        return objectMapper.createObjectNode();
    }

    public <T> Optional<T> readValue(String value, Class<T> obj) {
        if(!isJson(value)){
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.readValue(value, obj));
        } catch (Exception e) {
            log.debug("", e);
            return Optional.empty();
        }
    }

    public Optional<JsonNode> readTree(Object model) {
        if (model == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.readTree(objectMapper.writeValueAsString(model)));
        } catch (Exception e) {
            log.debug("", e);
            return Optional.empty();
        }
    }

    public Optional<JsonNode> readTree(String value) {
        if(!isJson(value)){
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.readTree(value));
        } catch (Exception e) {
            log.debug("", e);
            return Optional.empty();
        }
    }

    public boolean isJson(String str) {
        if(str == null || str.isEmpty()) {
            return false;
        }
        try {
            objectMapper.readTree(str);
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;
    }

}
