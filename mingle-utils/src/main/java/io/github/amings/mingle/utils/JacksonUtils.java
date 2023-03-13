package io.github.amings.mingle.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Ming
 */

@Slf4j
public class JacksonUtils {

    private final ObjectMapper defaultObjectMapper;

    public JacksonUtils(ObjectMapper objectMapper) {
        this.defaultObjectMapper = objectMapper;
    }

    public ObjectNode getObjectNode() {
        return defaultObjectMapper.createObjectNode();
    }

    public ArrayNode getArrayNode() {
        return defaultObjectMapper.createArrayNode();
    }

    public ObjectMapper getObjectMapper() {
        return defaultObjectMapper;
    }

    public static TextNode getTextNode(String text) {
        return new TextNode(text);
    }

    public static IntNode getIntNode(int text) {
        return new IntNode(text);
    }

    public <T> Optional<T> readValue(String value, Class<T> obj) {
        if(!isJson(value)){
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(defaultObjectMapper.readValue(value, obj));
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
            return Optional.ofNullable(defaultObjectMapper.readTree(defaultObjectMapper.writeValueAsString(model)));
        } catch (Exception e) {
            log.debug("", e);
            return Optional.empty();
        }
    }

    public Optional<JsonNode> readTree(String value) {
        if(value == null || !isJson(value)){
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(defaultObjectMapper.readTree(value));
        } catch (Exception e) {
            log.debug("", e);
            return Optional.empty();
        }
    }

    public boolean isJson(String str) {
        try {
            defaultObjectMapper.readTree(str);
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;
    }

}
