package io.github.minguanqiu.mingle.svc.action.rest;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Qiu Guan Ming
 */
@ActiveProfiles("properties-test")
@SpringBootTest
public class ActionRestPropertiesTests {

  @Autowired
  RestActionProperties properties;

  @Test
  void testProperties() {
    RestActionProperties.RestProperties.ServerProperties serverProperties = properties.getRest()
        .getServer().get("Simple");
    assertThat(serverProperties.getScheme()).isEqualTo("https");
    assertThat(serverProperties.getHost()).isEqualTo("127.0.0.1");
    assertThat(serverProperties.getPort()).isEqualTo(8080);
    assertThat(serverProperties.getPathSegments()).contains("test1", "test2");
    RestActionProperties.RestProperties.MockProperties mockProperties = properties.getRest()
        .getMock().get("SimpleAction");
    assertThat(mockProperties.getCode()).isEqualTo(400);
    assertThat(mockProperties.getHeader()).contains(Map.entry("test", "test"),
        Map.entry("test1", "test1"));
    assertThat(mockProperties.getResponseBody().getMediaType()).isEqualTo("application/xml");
    assertThat(mockProperties.getResponseBody().getContent()).isEqualTo(
        "{\"test\":\"test\",\"test1\":\"test1\"}");
    assertThat(mockProperties.getMessage()).isEqualTo("400 OK");
    assertThat(mockProperties.getDelay()).isEqualTo(5000);

  }
}
