package io.github.minguanqiu.mingle.svc.action;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.action.enums.AutoBreak;
import io.github.minguanqiu.mingle.svc.action.exception.ActionAutoBreakException;
import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
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
@SpringBootTest(properties = "mingle.svc.action.auto-break=true")
@AutoConfigureMockMvc
public class AutoBreakTests {

  @Autowired
  SimpleAction simpleAction;
  @Autowired
  SimpleSvc svc;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private SvcResponseHandler svcResponseHandler;
  @Autowired
  private JacksonUtils jacksonUtils;
  @Autowired
  private SvcPathHandler svcPathHandler;
  @Autowired
  SvcProperties svcProperties;

  @Test
  void test() {
    try {
      SimpleActionReq simpleActionReq = new SimpleActionReq();
      simpleActionReq.setText1("");
      simpleActionReq.setText2("");
      ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(
          simpleActionReq);
      assertThat(simpleActionResActionResponse.getCode()).isEqualTo("X001");
    } catch (ActionAutoBreakException e) {
      assertThat(e).isNotNull();
      assertThat(e.getActionResponse().getCode()).isEqualTo("X001");
    }
  }

  @Test
  void testRequestFalse() {
    try {
      SimpleActionReq simpleActionReq = new SimpleActionReq();
      simpleActionReq.setAutoBreak(AutoBreak.FALSE);
      simpleActionReq.setText1("");
      simpleActionReq.setText2("");
      ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(
          simpleActionReq);
      assertThat(simpleActionResActionResponse.getCode()).isEqualTo("X001");
    } catch (ActionAutoBreakException e) {
      assertThat(e).isNull();
    }
  }

  @Test
  void testRequestTrue() {
    try {
      SimpleActionReq simpleActionReq = new SimpleActionReq();
      simpleActionReq.setAutoBreak(AutoBreak.TRUE);
      simpleActionReq.setText1("");
      simpleActionReq.setText2("");
      ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(
          simpleActionReq);
      assertThat(simpleActionResActionResponse.getCode()).isEqualTo("X001");
    } catch (ActionAutoBreakException e) {
      assertThat(e).isNotNull();
      assertThat(e.getActionResponse().getCode()).isEqualTo("X001");
    }
  }

  @Test
  void testSvcTrue() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("true", "", "")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo("X001");
    assertThat(handler.getMsg()).isEqualTo("Text1 is empty");
  }

  @Test
  void testSvcFalse() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("false", "Hello", "World")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
    assertThat(handler.getMsg()).isEqualTo(svcProperties.getMsg());
    assertThat(handler.getResponseBody().get("text1").asText()).isEqualTo("Hello");
    assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("World");
  }
}
