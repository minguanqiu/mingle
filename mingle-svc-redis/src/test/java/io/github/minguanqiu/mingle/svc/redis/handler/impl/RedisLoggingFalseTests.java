package io.github.minguanqiu.mingle.svc.redis.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.redis.SimpleSvc;
import io.github.minguanqiu.mingle.svc.redis.SvcTestUtils;
import io.github.minguanqiu.mingle.svc.redis.handler.RedisLogHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author Qiu Guan Ming
 */
@SpringBootTest(properties = {"mingle.svc.redis.logging=false",
    "mingle.svc.properties.logging=true"})
@AutoConfigureMockMvc
public class RedisLoggingFalseTests {

  @Autowired
  MockMvc mvc;
  @Autowired
  SvcPathHandler svcPathHandler;
  @Autowired
  JacksonUtils jacksonUtils;
  @Autowired
  SvcProperties svcProperties;
  @Autowired
  SvcResponseHandler svcResponseHandler;

  @Test
  void testLogging() throws Exception {
    ResultActions perform = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletRequest request = perform.andReturn().getRequest();
    assertThat(request.getAttribute(RedisLogHandler.class.getSimpleName())).isNull();
  }
}
