package io.github.minguanqiu.mingle.svc;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.exception.ReqBodyNotJsonFormatException;
import io.github.minguanqiu.mingle.svc.exception.SvcRequestValidFailException;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.Optional;
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
@SpringBootTest
@AutoConfigureMockMvc
public class SvcInterruptTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private SvcProperties svcProperties;
  @Autowired
  private SvcResponseHandler svcResponseHandler;
  @Autowired
  private JacksonUtils jacksonUtils;
  @Autowired
  private SvcPathHandler svcPathHandler;

  @Test
  void testReqBodyNotJsonFormatException() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content("{\"111\"}"));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
    assertThat(handler.getResponseBody().get("msg").asText()).isEqualTo(
        ReqBodyNotJsonFormatException.MSG);
  }

  @Test
  void testSvcReqBodyValidFailException() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("", "", "")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
    assertThat(handler.getResponseBody().get("msg").asText()).isEqualTo(
        SvcRequestValidFailException.MSG);
  }

  @Test
  void testTrow() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("throw", "123", "123")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(SvcTestUtils.X001);
    assertThat(handler.getMsg()).isEqualTo("x001-fail test var");
    assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("null");
  }

  @Test
  void testReturn() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("return", "123", "123")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(SvcTestUtils.X002);
    assertThat(handler.getMsg()).isEqualTo("x002-fail test var");
    assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("null");
  }

}
