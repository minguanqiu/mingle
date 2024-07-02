package io.github.minguanqiu.mingle.svc.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.SimpleSvc;
import io.github.minguanqiu.mingle.svc.SvcTestUtils;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author Qiu Gaun Ming
 */

@ActiveProfiles("test-svcPath-handler")
@SpringBootTest
@AutoConfigureMockMvc
public class SvcPathTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private SvcResponseHandler svcResponseHandler;
  @Autowired
  private JacksonUtils jacksonUtils;
  @Autowired
  private SvcPathHandler svcPathHandler;
  @Autowired
  private SvcProperties svcProperties;


  @Test
  void testSvcPath() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class)
            .content(SvcTestUtils.getTestContent("normal", "123", "123")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler).isPresent();
    SvcResponseHandler svcResponseHandler = optionalSvcResponseHandler.get();
    assertThat(svcResponseHandler.getCode()).isEqualTo(svcProperties.getCode());
  }
}
