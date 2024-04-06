package io.github.minguanq.mingle.svc.component;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.github.minguanq.mingle.svc.SimpleSvc;
import io.github.minguanq.mingle.svc.SimpleSvcForDoc;
import io.github.minguanq.mingle.svc.TestUtils;
import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Ming
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SvcRegisterTests {

    @Autowired
    private SvcRegisterComponent svcRegisterComponent;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SvcPathHandler svcPathHandler;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonUtils jacksonUtils;
    @Autowired
    private SvcProperties svcProperties;
    @Autowired
    private SvcResponseHandler svcResponseHandler;

    @Test
    void testRegister() {
        SvcRegisterComponent.SvcDefinition svcDefinition = svcRegisterComponent.getSvcDefinitionMap().get(svcPathHandler.getPath(SimpleSvc.class));
        assertThat(svcDefinition).isNotNull();
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        RequestMappingInfo requestMappingInfo = RequestMappingInfo
                .paths(svcPathHandler.getPath(SimpleSvc.class))
                .methods(RequestMethod.POST)
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .build();
        assertThat(requestMappingHandlerMapping.getHandlerMethods().get(requestMappingInfo)).isNotNull();
    }

    @Test
    void testSimpleSvcForDoc() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/v3/api-docs")).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(contentAsString);
        assertThat(jsonNodeOptional).isPresent();
        JsonNode apiNode = jsonNodeOptional.get();
        assertThat(apiNode.get("info")).contains(new TextNode("testTitle"), new TextNode("v1"));
        assertThat(apiNode.get("servers").get(0)).contains(new TextNode("testUrl"));
        JsonNode paths = apiNode.get("paths");
        String svcPath = svcPathHandler.getPath(SimpleSvcForDoc.class);
        JsonNode simpleSvcForDocNode = paths.get(svcPath).get("post");
        assertThat(simpleSvcForDocNode).isNotNull();
        assertThat(simpleSvcForDocNode.get("tags").get(0).asText()).isEqualTo("tests");
        assertThat(simpleSvcForDocNode.get("summary").asText()).isEqualTo("test service for summary");
        assertThat(simpleSvcForDocNode.get("description").asText()).isEqualTo("test service for description");
        assertThat(simpleSvcForDocNode.get("operationId").asText()).isEqualTo(svcPath);
        JsonNode requestBody = simpleSvcForDocNode.get("requestBody").get("content").get("application/json").get("schema").get("properties");
        assertThat(requestBody.has("text1")).isTrue();
        assertThat(requestBody.has("text2")).isTrue();
        assertThat(simpleSvcForDocNode.get("responses").get("200").get("description").asText()).isEqualTo("testResponse");
        JsonNode responseBody = simpleSvcForDocNode.get("responses").get("200").get("content").get("application/json").get("schema").get("properties");
        assertThat(responseBody.has("code")).isTrue();
        assertThat(responseBody.has("msg")).isTrue();
        assertThat(responseBody.has("body")).isTrue();
        assertThat(responseBody.get("body").get("properties").has("text1")).isTrue();
        assertThat(responseBody.get("body").get("properties").has("text2")).isTrue();
    }

    @Test
    void testSimpleSvcForValue() {
        assertThat(applicationContext.getBean("simpleSvcForValueTest")).isNotNull();
        assertThatThrownBy(() -> applicationContext.getBean("simpleSvcForValue")).isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Test
    void testSimpleSvcForNormal() throws Exception {
        ResultActions perform = mockMvc.perform(TestUtils.buildSvcRequest(svcPathHandler, SimpleSvc.class).content(TestUtils.getTestContent("normal", "Hello", "World")));
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(response.getContentAsString(), svcResponseHandler.getClass());
        assertThat(optionalSvcResponseHandler.isPresent()).isTrue();
        SvcResponseHandler handler = optionalSvcResponseHandler.get();
        assertThat(handler.getCode()).isEqualTo(svcProperties.getCode());
        assertThat(handler.getMsg()).isEqualTo(svcProperties.getMsg());
        assertThat(handler.getResponseBody().get("text1").asText()).isEqualTo("Hello");
        assertThat(handler.getResponseBody().get("text2").asText()).isEqualTo("World");
    }


}
