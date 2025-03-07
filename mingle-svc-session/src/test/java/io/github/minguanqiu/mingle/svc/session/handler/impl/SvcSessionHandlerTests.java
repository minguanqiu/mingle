package io.github.minguanqiu.mingle.svc.session.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import io.github.minguanqiu.mingle.svc.session.SimpleSvc;
import io.github.minguanqiu.mingle.svc.session.SvcTestUtils;
import io.github.minguanqiu.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanqiu.mingle.svc.session.dao.SvcSessionDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;
import io.github.minguanqiu.mingle.svc.session.utils.SessionUtils;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author Qiu Guan Ming
 */

@ActiveProfiles("test-session-handler")
@SpringBootTest
@AutoConfigureMockMvc
public class SvcSessionHandlerTests {


  @Autowired
  SessionUtils sessionUtils;
  @Autowired
  SvcSessionDao svcSessionDao;
  @Autowired
  MockMvc mvc;
  @Autowired
  SvcPathHandler svcPathHandler;
  @Autowired
  SvcSessionProperties svcSessionProperties;
  @Autowired
  JacksonUtils jacksonUtils;
  @Autowired
  SvcProperties svcProperties;
  @Autowired
  SvcResponseHandler svcResponseHandler;

  @Test
  void testAuthentication() throws Exception {
    RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString())
        .build();
    SvcSessionEntity loginSession = new SvcSessionEntity(redisKey, 600, "Login", "12345");
    String token = sessionUtils.createSessionToken(redisKey, loginSession);
    ResultActions perform = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .header(svcSessionProperties.getHeader(), token)
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo("error");
    assertThat(handler.getMsg()).isEqualTo("Session not exist");
  }


}
