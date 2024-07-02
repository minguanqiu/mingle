package io.github.minguanqiu.mingle.svc.session;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.exception.handler.model.AllExceptionModel;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import io.github.minguanqiu.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanqiu.mingle.svc.session.dao.SvcSessionDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;
import io.github.minguanqiu.mingle.svc.session.exception.SessionHeaderMissingException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionNotExistException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionTokenDecryptionErrorException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionTypeIncorrectException;
import io.github.minguanqiu.mingle.svc.session.utils.SessionUtils;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author Qiu Guan Ming
 */
@SpringBootTest(properties = "mingle.svc.session.header=token")
@AutoConfigureMockMvc
public class SessionTests {

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
    assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
  }

  @Test
  void testAuthenticationErrorType() throws Exception {
    RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString())
        .build();
    SvcSessionEntity loginSession = new SvcSessionEntity(redisKey, 600, "Login1", "12345");
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
    assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
    AllExceptionModel allExceptionModel = jacksonUtils.readValue(
        handler.getResponseBody().toString(), AllExceptionModel.class).get();
    assertThat(allExceptionModel.getMsg()).isEqualTo(SessionTypeIncorrectException.MSG);
  }

  @Test
  void testAuthenticationNotFound() throws Exception {
    RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString())
        .build();
    SvcSessionEntity loginSession = new SvcSessionEntity(redisKey, 600, "Login", "12345");
    String token = sessionUtils.createSessionToken(redisKey, loginSession);
    svcSessionDao.delete(loginSession.getRedisKey());
    ResultActions perform = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .header(svcSessionProperties.getHeader(), token)
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
    AllExceptionModel allExceptionModel = jacksonUtils.readValue(
        handler.getResponseBody().toString(), AllExceptionModel.class).get();
    assertThat(allExceptionModel.getMsg()).isEqualTo(SessionNotExistException.MSG);
  }

  @Test
  void testAuthenticationWithoutToken() throws Exception {
    ResultActions perform = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
    AllExceptionModel allExceptionModel = jacksonUtils.readValue(
        handler.getResponseBody().toString(), AllExceptionModel.class).get();
    assertThat(allExceptionModel.getMsg()).isEqualTo(SessionHeaderMissingException.MSG);
  }

  @Test
  void testAuthenticationErrorToken() throws Exception {
    ResultActions perform = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .header(svcSessionProperties.getHeader(),
                "a68BpLwfc/8eUQnqZIqHC6rQbywqz+Og/quiFAbSONo7")
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
    AllExceptionModel allExceptionModel = jacksonUtils.readValue(
        handler.getResponseBody().toString(), AllExceptionModel.class).get();
    assertThat(allExceptionModel.getMsg()).isEqualTo(SessionTokenDecryptionErrorException.MSG);
  }

  @Test
  void testAuthority() throws Exception {
    RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString())
        .build();
    SvcSessionEntity loginSession = new SvcSessionEntity(redisKey, 600, "Login", "12345");
    loginSession.setAuthorities(List.of("SimpleSvcAuth"));
    String token = sessionUtils.createSessionToken(redisKey, loginSession);
    ResultActions perform = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvcAuth.class)
            .header(svcSessionProperties.getHeader(), token)
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
  }

  @Test
  void testAuthorityError() throws Exception {
    RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString())
        .build();
    SvcSessionEntity loginSession = new SvcSessionEntity(redisKey, 600, "Login", "12345");
    String token = sessionUtils.createSessionToken(redisKey, loginSession);
    ResultActions perform = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvcAuth.class)
            .header(svcSessionProperties.getHeader(), token)
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
    assertThat(handler.getMsg()).isEqualTo("Access Denied");
  }

  @Test
  void testSessionUtilsFeature() throws Exception {
    RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString())
        .build();
    SvcSessionEntity loginSession = new SvcSessionEntity(redisKey, 600, "Login", "12345");
    HashMap<String, Object> stringObjectHashMap = new HashMap<>();
    stringObjectHashMap.put("Hello","World");
    loginSession.setSessionValue(stringObjectHashMap);
    String token = sessionUtils.createSessionToken(redisKey, loginSession);
    ResultActions perform = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvcSessionFeature.class)
            .header(svcSessionProperties.getHeader(), token)
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
    ResultActions perform1 = mvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvcSessionFeature1.class)
            .header(svcSessionProperties.getHeader(), token)
            .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    MockHttpServletResponse response1 = perform1.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler1 = jacksonUtils.readValue(
        response1.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler1.isPresent()).isTrue();
    SvcResponseHandler handler1 = optionalSvcResponseHandler1.get();
    assertThat(handler1.getCode()).isEqualTo(svcProperties.getCode());
    assertThat(svcSessionDao.hasKey(redisKey)).isFalse();

  }
}
