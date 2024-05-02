package io.github.minguanq.mingle.svc.register;

import io.github.minguanq.mingle.svc.SimpleSvcWithFeature1;
import io.github.minguanq.mingle.svc.SvcTestUtils;
import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.handler.SvcLoggingHandler;
import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import io.github.minguanq.mingle.svc.handler.SvcRequestBodyProcessHandler;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@ActiveProfiles("test-feature-handler")
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "mingle.svc.properties.logging=false",
        "mingle.svc.properties.body-process=false",
        "mingle.svc.properties.ip-secure=0.0.0.0"})
public class SvcFeatureHandlerTests {

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
        perform = mockMvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvcWithFeature1.class).content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
    }

    @Test
    void testSimpleSvcForLogging() {
        assertThat(perform.andReturn().getRequest().getAttribute(SvcLoggingHandler.class.getSimpleName())).isNotNull();
    }

    @Test
    void testSimpleSvcBodyProcess() {
        assertThat(perform.andReturn().getRequest().getAttribute(SvcRequestBodyProcessHandler.class.getSimpleName())).isNotNull();
    }

    @Test
    void testSimpleSvcForIPSecure() throws Exception {
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
    }

}
