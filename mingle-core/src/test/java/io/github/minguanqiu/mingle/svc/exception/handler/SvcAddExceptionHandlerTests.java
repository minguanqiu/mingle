package io.github.minguanqiu.mingle.svc.exception.handler;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.SimpleSvc;
import io.github.minguanqiu.mingle.svc.SvcTestUtils;
import io.github.minguanqiu.mingle.svc.exception.handler.model.TestExceptionModel;
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
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("add-exception-handler")
public class SvcAddExceptionHandlerTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private SvcResponseHandler svcResponseHandler;
  @Autowired
  private JacksonUtils jacksonUtils;
  @Autowired
  private SvcPathHandler svcPathHandler;

  @Test
  void testOverrideAllExceptionHandler() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("throwNullException", "123", "123")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
    SvcResponseHandler handler = optionalSvcResponseHandler.get();
    assertThat(handler.getCode()).isEqualTo(SvcTestUtils.X004);
    assertThat(handler.getMsg()).isEqualTo("X004-fail");
    TestExceptionModel testExceptionModel = jacksonUtils.readValue(
        handler.getResponseBody().toString(), TestExceptionModel.class).get();
    assertThat(testExceptionModel.getExceptionMsg()).isEqualTo("X004-fail");
  }


}








