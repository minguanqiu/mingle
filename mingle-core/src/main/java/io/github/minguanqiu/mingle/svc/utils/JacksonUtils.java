package io.github.minguanqiu.mingle.svc.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Utils for jackson process.
 *
 * @author Qiu Guan Ming
 */

@Slf4j
@Getter
public class JacksonUtils {

  /**
   * Jackson object mapper.
   *
   * @return return the jackson object mapper.
   */
  private final ObjectMapper objectMapper;

  /**
   * Create a new JacksonUtils instance.
   *
   * @param objectMapper the jackson object mapper.
   */
  public JacksonUtils(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Get new object node instance.
   *
   * @return return the new object node instance.
   */
  public ObjectNode getObjectNode() {
    return objectMapper.createObjectNode();
  }

  /**
   * Deserialize json to object.
   *
   * @param value the json string.
   * @param obj   the deserialize object class.
   * @param <T>   the deserialize object class.
   * @return return the optional deserialize object.
   */
  public <T> Optional<T> readValue(String value, Class<T> obj) {
    if (!isJson(value)) {
      return Optional.empty();
    }
    try {
      return Optional.ofNullable(objectMapper.readValue(value, obj));
    } catch (Exception e) {
      log.debug("", e);
      return Optional.empty();
    }
  }

  /**
   * Serialize object to json node.
   *
   * @param model the object.
   * @return return the optional json node.
   */
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

  /**
   * Serialize string to json node.
   *
   * @param value the json string.
   * @return return the optional json node.
   */
  public Optional<JsonNode> readTree(String value) {
    if (!isJson(value)) {
      return Optional.empty();
    }
    try {
      return Optional.ofNullable(objectMapper.readTree(value));
    } catch (Exception e) {
      log.debug("", e);
      return Optional.empty();
    }
  }

  /**
   * Valid json format.
   *
   * @param str any string.
   * @return return the ture or false.
   */
  public boolean isJson(String str) {
    if (str == null || str.isEmpty()) {
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
