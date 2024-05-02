package io.github.minguanq.mingle.svc.data.handler.impl;

import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.data.SimpleSvc;
import io.github.minguanq.mingle.svc.data.SvcTestUtils;
import io.github.minguanq.mingle.svc.data.handler.DaoLoggingHandler;
import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@SpringBootTest(properties = {"mingle.svc.dao.logging=true","mingle.svc.properties.feature.logging=true"})
@AutoConfigureMockMvc
public class DaoLoggingTests {

    @Autowired
    MockMvc mvc;
    @Autowired
    SvcPathHandler svcPathHandler;
    @Autowired
    JacksonUtils jacksonUtils;
    @Autowired
    SvcProperties svcProperties;
    @Autowired
    SvcResponseHandler svcResponseHandler;

    @Test
    void testLogging() throws Exception {
        ResultActions perform = mvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
        MockHttpServletRequest request = perform.andReturn().getRequest();
        assertThat(request.getAttribute(DaoLoggingHandler.class.getSimpleName())).isNotNull();
    }
}
