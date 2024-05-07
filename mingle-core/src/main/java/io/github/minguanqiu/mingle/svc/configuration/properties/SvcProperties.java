package io.github.minguanqiu.mingle.svc.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping service properties
 *
 * @author Ming
 */
@Getter
@Setter
public class SvcProperties {

    private String msg_type = "svc";

    private String code = "0";

    private String msg = "successful";

    private Feature feature = new Feature();

    @Getter
    @Setter
    public static class Feature {

        boolean logging;

        boolean bodyProcess;

        String[] ipSecure = new String[]{};
    }

}
