package io.github.amings.mingle.svc.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Ming
 */

@Getter
@Component
public class PropertyComponent {

    @Value("${mingle.svc.path:/svc}")
    public String svcPath;

}
