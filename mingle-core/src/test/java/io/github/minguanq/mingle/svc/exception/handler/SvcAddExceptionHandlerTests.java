package io.github.minguanq.mingle.svc.exception.handler;

import io.github.minguanq.mingle.svc.SimpleSvc;
import io.github.minguanq.mingle.svc.TestUtils;
import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
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
        ResultActions perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(TestUtils.getTestContent("throwNullException","123", "123")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(TestUtils.X004);
        assertThat(handler.getMsg()).isEqualTo("X004-fail");
        assertThat(handler.getResponseBody().get("exceptionMsg").asText()).isEqualTo("X004-fail");
    }


}








