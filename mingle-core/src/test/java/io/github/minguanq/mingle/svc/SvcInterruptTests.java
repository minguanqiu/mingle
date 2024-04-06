package io.github.minguanq.mingle.svc;

import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.exception.ReqBodyNotJsonFormatException;
import io.github.minguanq.mingle.svc.exception.SvcRequestValidFailException;
import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
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
public class SvcInterruptTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SvcProperties svcProperties;
    @Autowired
    private SvcResponseHandler svcResponseHandler;
    @Autowired
    private JacksonUtils jacksonUtils;
    @Autowired
    private SvcPathHandler svcPathHandler;

    @Test
    void testReqBodyNotJsonFormatException() throws Exception {
        ResultActions perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content("{\"111\"}"));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
        assertThat(handler.getResponseBody().get("msg").asText()).isEqualTo(ReqBodyNotJsonFormatException.MSG);
    }

    @Test
    void testSvcReqBodyValidFailException() throws Exception {
        ResultActions perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(TestUtils.getTestContent("", "", "")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isNotEqualTo(svcProperties.getCode());
        assertThat(handler.getResponseBody().get("msg").asText()).isEqualTo(SvcRequestValidFailException.MSG);
    }

    @Test
    void testTrow() throws Exception {
        ResultActions perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(TestUtils.getTestContent("throw", "123", "123")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(TestUtils.X001);
        assertThat(handler.getMsg()).isEqualTo("x001-fail test var");
        assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("null");
    }

    @Test
    void testThrowWithoutMsg() throws Exception {
        ResultActions perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(TestUtils.getTestContent("throwWithoutMsg", "123", "123")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(TestUtils.X001);
        assertThat(handler.getMsg()).isEqualTo("convertX001 test var");
        assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("null");
    }

    @Test
    void testReturn() throws Exception {
        ResultActions perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(TestUtils.getTestContent("return", "123", "123")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(TestUtils.X002);
        assertThat(handler.getMsg()).isEqualTo("x002-fail test var");
        assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("null");
    }

    @Test
    void testReturnWithoutMsg() throws Exception {
        ResultActions perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(TestUtils.getTestContent("returnWithoutMsg", "123", "123")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(TestUtils.X002);
        assertThat(handler.getMsg()).isEqualTo("convertX002 test var");
        assertThat(handler.getResponseBody().isNull()).isTrue();
    }

}
