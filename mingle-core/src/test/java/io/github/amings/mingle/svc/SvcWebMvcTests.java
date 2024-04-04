package io.github.amings.mingle.svc;

import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.handler.SvcResponseHandler;
import io.github.amings.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Ming
 */
@WebMvcTest
@ComponentScan
public class SvcWebMvcTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    SvcProperties svcProperties;
    @Autowired
    SvcResponseHandler svcResponseHandler;
    @Autowired
    JacksonUtils jacksonUtils;

    MockHttpServletRequestBuilder buildRequest(Class<?> clazz) {
        String svcPath = buildPath(clazz);
        return MockMvcRequestBuilders.post(URI.create(svcPath)).servletPath(svcPath).contentType("application/json");
    }

    String buildPath(Class<?> clazz) {
        return svcProperties.getRootPath() + "/" + clazz.getSimpleName();
    }

    String getTestContent() {
        return """
                {
                    "text1" : "Hello",
                    "text2" : "World"
                }
                """;
    }

    @Test
    void testSimpleSvcForNormal() throws Exception {
        ResultActions perform = mockMvc.perform(buildRequest(SimpleSvc.class).content(getTestContent()));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(svcProperties.getSuccessCode());
        assertThat(handler.getMsg()).isEqualTo(svcProperties.getSuccessMsg());
        assertThat(handler.getResponseBody().get("text1").asText()).isEqualTo("Hello");
        assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("World");
    }

    @Test
    void testSimpleSvcForPath() {
        assertThat(applicationContext.getBean("simpleSvcForValueTest")).isNotNull();
        assertThatThrownBy(() -> applicationContext.getBean("simpleSvcForValue")).isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Test
    void testSimpleSvcForDoc() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/v3/api-docs")).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }


}

