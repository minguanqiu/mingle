package io.github.minguanq.mingle.svc.handler.impl;

import io.github.minguanq.mingle.svc.SimpleSvc;
import io.github.minguanq.mingle.svc.TestUtils;
import io.github.minguanq.mingle.svc.component.SvcRegisterComponent;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@ActiveProfiles("test-handler")
@SpringBootTest
@AutoConfigureMockMvc
public class SvcHandlerTests {

    @Autowired
    SvcRegisterComponent svcRegisterComponent;
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
    SvcRegisterComponent.SvcDefinition.Feature feature;
    ResultActions perform;

    @BeforeEach
    void beforeTest() throws Exception {
        perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(TestUtils.getTestContent("normal", "Hello", "World")));
        SvcRegisterComponent.SvcDefinition svcDefinition = svcRegisterComponent.getSvcDefinition(o -> o.getSvcClass().equals(SimpleSvc.class)).get(0);
        feature = svcDefinition.getFeature();
    }

    @Test
    void testSimpleSvcForLogging() {
        assertThat(perform.andReturn().getRequest().getAttribute(SvcLoggingHandler.class.getSimpleName())).isNotNull();
    }

    @Test
    void testSimpleSvcBodyProcess() {
        assertThat(perform.andReturn().getRequest().getAttribute(SvcRequestBodyProcessHandler.class.getSimpleName())).isNotNull();
    }
    
}
