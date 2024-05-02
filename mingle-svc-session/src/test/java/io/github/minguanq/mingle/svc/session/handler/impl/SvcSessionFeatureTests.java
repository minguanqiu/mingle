package io.github.minguanq.mingle.svc.session.handler.impl;

import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanq.mingle.svc.session.SimpleSvc;
import io.github.minguanq.mingle.svc.session.SvcTestUtils;
import io.github.minguanq.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanq.mingle.svc.session.dao.SessionDao;
import io.github.minguanq.mingle.svc.session.utils.SessionUtils;
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
@ActiveProfiles("test-session-feature")
@SpringBootTest
@AutoConfigureMockMvc
public class SvcSessionFeatureTests {

    @Autowired
    SessionUtils sessionUtils;
    @Autowired
    SessionDao sessionDao;
    @Autowired
    MockMvc mvc;
    @Autowired
    SvcPathHandler svcPathHandler;
    @Autowired
    SvcSessionProperties svcSessionProperties;
    @Autowired
    JacksonUtils jacksonUtils;
    @Autowired
    SvcProperties svcProperties;
    @Autowired
    SvcResponseHandler svcResponseHandler;

    @Test
    void testSessionFeature() throws Exception {
        ResultActions perform = mvc.perform(SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(SvcTestUtils.getTestContent("normal", "Hello", "World")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
    }

}
