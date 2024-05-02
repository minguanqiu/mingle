package io.github.minguanq.mingle.svc.data;

import io.github.minguanq.mingle.svc.handler.SvcPathHandler;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

/**
 * @author Ming
 */
public class SvcTestUtils {

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



