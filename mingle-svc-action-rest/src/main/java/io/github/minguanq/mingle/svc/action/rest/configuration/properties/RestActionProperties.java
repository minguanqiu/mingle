package io.github.minguanq.mingle.svc.action.rest.configuration.properties;

import io.github.minguanq.mingle.svc.action.configuration.properties.SvcActionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ConfigurationProperties} mapping action properties
 *
 * @author Ming
 */
@Getter
@Setter
public class RestActionProperties extends SvcActionProperties {

    private RestProperties rest = new RestProperties();

    @Getter
    @Setter
    public static class RestProperties {

        private Map<String, ServerProperties> server = new HashMap<>();

        private Map<String, MockProperties> mock = new HashMap<>();

        @Getter
        @Setter
        public static class ServerProperties {

            private String scheme = "http";

            private String host;

            private int port;

            private String[] pathSegments;

        }

        @Getter
        @Setter
        public static class MockProperties {

            private int code = 200;

            private Map<String, String> header = new HashMap<>();

            private ResponseBody responseBody = new ResponseBody();

            private String message = "200 OK";

            private long delay;

            @Getter
            @Setter
            public static class ResponseBody {

                private String mediaType = "application/json";

                private String content;

            }

        }

    }

}
