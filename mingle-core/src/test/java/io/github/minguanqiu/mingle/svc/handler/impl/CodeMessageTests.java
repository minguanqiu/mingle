package io.github.minguanqiu.mingle.svc.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.SimpleSvc;
import io.github.minguanqiu.mingle.svc.SvcTestUtils;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.Optional;
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
@ActiveProfiles("test-code-message")
@SpringBootTest
@AutoConfigureMockMvc
public class CodeMessageTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private SvcResponseHandler svcResponseHandler;
  @Autowired
  private JacksonUtils jacksonUtils;
  @Autowired
  private SvcPathHandler svcPathHandler;


  @Test
  void testThrowWithoutMsg() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("throwWithoutMsg", "123", "123")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(SvcTestUtils.X001);
    assertThat(handler.getMsg()).isEqualTo("convertX001 test var");
    assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("null");
  }

  @Test
  void testReturnWithoutMsg() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("returnWithoutMsg", "123", "123")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(SvcTestUtils.X002);
    assertThat(handler.getMsg()).isEqualTo("convertX002 test var");
    assertThat(handler.getResponseBody().isNull()).isTrue();
  }

  @Test
  void testUnKnowMsg() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("unknownCode", "123", "123")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(SvcTestUtils.X003);
    assertThat(handler.getMsg()).isNull();
  }

}
