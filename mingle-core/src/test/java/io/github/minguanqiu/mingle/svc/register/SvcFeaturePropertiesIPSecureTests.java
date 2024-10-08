package io.github.minguanqiu.mingle.svc.register;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.SimpleSvc;
import io.github.minguanqiu.mingle.svc.SvcTestUtils;
import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.exception.IPAuthenticationFailException;
import io.github.minguanqiu.mingle.svc.exception.handler.model.AllExceptionModel;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


/**
 * @author Qiu Guan Ming
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "mingle.svc.properties.feature.ip-secure=0.0.0.0"})
public class SvcFeaturePropertiesIPSecureTests {

  @Autowired
  SvcRegister svcRegister;
  @Autowired
  MockMvc mockMvc;
  @Autowired
  SvcPathHandler svcPathHandler;
  @Autowired
  JacksonUtils jacksonUtils;
  @Autowired
  SvcProperties svcProperties;
  @Autowired
  SvcResponseHandler svcResponseHandler;
  ResultActions perform;

  @BeforeEach
  void beforeTest() throws Exception {
    perform = mockMvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
        .content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
  }

  @Test
  void testSimpleSvcForIPSecure() throws Exception {
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
    AllExceptionModel allExceptionModel = jacksonUtils.readValue(
        handler.getResponseBody().toString(), AllExceptionModel.class).get();
    assertThat(allExceptionModel.getMsg()).isEqualTo(IPAuthenticationFailException.MSG);
  }

}
