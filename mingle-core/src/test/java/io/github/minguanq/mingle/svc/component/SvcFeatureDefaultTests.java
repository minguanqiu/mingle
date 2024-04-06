package io.github.minguanq.mingle.svc.component;

import io.github.minguanq.mingle.svc.SimpleSvc;
import io.github.minguanq.mingle.svc.TestUtils;
import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SvcFeatureDefaultTests {

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
    void testSimpleSvcForIPSecure() throws Exception {
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
    }

}
