package io.github.minguanq.mingle.svc.action.rest.configuration.properties;

import io.github.minguanq.mingle.svc.action.configuration.properties.ActionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} mapping action properties
 *
 * @author Ming
 */
@Getter
@Setter
@ConfigurationProperties("mingle.svc.action.rest")
public class RestActionProperties {

    private final ActionProperties actionProperties;

    private String mockPath = "";

    public RestActionProperties(ActionProperties actionProperties) {
        this.actionProperties = actionProperties;
    }

}
