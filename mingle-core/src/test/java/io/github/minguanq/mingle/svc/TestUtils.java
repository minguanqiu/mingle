package io.github.minguanq.mingle.svc;

import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

/**
 * @author Ming
 */
public class TestUtils {

    public static final String X001 = "X001";
    public static final String X002 = "X002";
    public static final String X003 = "X003";
    public static final String X004 = "X004";
    public static final String X005 = "X005";

    public static String getTestContent(String action, String text1, String text2) {
        return """
                {
                    "action" : "%s",
                    "text1" : "%s",
                    "text2" : "%s"
                }
                """.formatted(action, text1, text2);
    }

    public static MockHttpServletRequestBuilder buildSvcRequest(SvcPathHandler svcPathHandler, Class<?> clazz) {
        String svcPath = svcPathHandler.getPath(clazz);
        return MockMvcRequestBuilders.post(URI.create(svcPath)).servletPath(svcPath).contentType("application/json");
    }

}



