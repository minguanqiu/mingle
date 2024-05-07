package io.github.minguanqiu.mingle.svc.session;

import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.exception.handler.model.AllExceptionModel;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import io.github.minguanqiu.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanqiu.mingle.svc.session.dao.SessionDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SessionEntity;
import io.github.minguanqiu.mingle.svc.session.exception.SessionHeaderMissingException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionNotExistException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionTokenDecryptionErrorException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionTypeIncorrectException;
import io.github.minguanqiu.mingle.svc.session.utils.SessionUtils;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@SpringBootTest(properties = "mingle.svc.session.header=token")
@AutoConfigureMockMvc
public class SessionTests {

    @Autowired
    SessionUtils sessionUtils;
    @Autowired
    SessionDao sessionDao;
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
        RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString()).build();
        SessionEntity loginSession = new SessionEntity(redisKey, 600, "Login", "12345");
        String token = sessionUtils.createSessionToken(redisKey, loginSession);
        ResultActions perform = mvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).header(svcSessionProperties.getHeader(), token).content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
    }

    @Test
    void testAuthenticationErrorType() throws Exception {
        RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString()).build();
        SessionEntity loginSession = new SessionEntity(redisKey, 600, "Login1", "12345");
        String token = sessionUtils.createSessionToken(redisKey, loginSession);
        ResultActions perform = mvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).header(svcSessionProperties.getHeader(), token).content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
        AllExceptionModel allExceptionModel = jacksonUtils.readValue(handler.getResponseBody().toString(), AllExceptionModel.class).get();
        assertThat(allExceptionModel.getMsg()).isEqualTo(SessionTypeIncorrectException.MSG);
    }

    @Test
    void testAuthenticationNotFound() throws Exception {
        RedisKey redisKey = RedisKey.builder().addParam("Login").addParam(UUID.randomUUID().toString()).build();
        SessionEntity loginSession = new SessionEntity(redisKey, 600, "Login", "12345");
        String token = sessionUtils.createSessionToken(redisKey, loginSession);
        sessionDao.delete(loginSession);
        ResultActions perform = mvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).header(svcSessionProperties.getHeader(), token).content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
        AllExceptionModel allExceptionModel = jacksonUtils.readValue(handler.getResponseBody().toString(), AllExceptionModel.class).get();
        assertThat(allExceptionModel.getMsg()).isEqualTo(SessionNotExistException.MSG);
    }

    @Test
    void testAuthenticationWithoutToken() throws Exception {
        ResultActions perform = mvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
        AllExceptionModel allExceptionModel = jacksonUtils.readValue(handler.getResponseBody().toString(), AllExceptionModel.class).get();
        assertThat(allExceptionModel.getMsg()).isEqualTo(SessionHeaderMissingException.MSG);
    }

    @Test
    void testAuthenticationErrorToken() throws Exception {
        ResultActions perform = mvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).header(svcSessionProperties.getHeader(), "a68BpLwfc/8eUQnqZIqHC6rQbywqz+Og/quiFAbSONo7").content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
        AllExceptionModel allExceptionModel = jacksonUtils.readValue(handler.getResponseBody().toString(), AllExceptionModel.class).get();
        assertThat(allExceptionModel.getMsg()).isEqualTo(SessionTokenDecryptionErrorException.MSG);
    }

}
