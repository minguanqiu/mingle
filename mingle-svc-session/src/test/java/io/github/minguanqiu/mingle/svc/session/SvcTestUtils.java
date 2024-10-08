package io.github.minguanqiu.mingle.svc.session;

import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import java.net.URI;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Qiu Guan Ming
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

  public static MockHttpServletRequestBuilder buildSvcRequest(SvcPathHandler svcPathHandler,
      Class<?> clazz) {
    String svcPath = svcPathHandler.getPath(clazz);
    return MockMvcRequestBuilders.post(URI.create(svcPath)).servletPath(svcPath)
        .contentType("application/json");
  }

}



