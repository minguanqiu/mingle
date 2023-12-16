package io.github.amings.mingle.svc.component;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Ming
 */

@Component
public class SvcRegisterComponent {

    private final SvcBinderComponent svcBinderComponent;
    private final ApplicationContext context;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public SvcRegisterComponent(SvcBinderComponent svcBinderComponent, ApplicationContext context, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.svcBinderComponent = svcBinderComponent;
        this.context = context;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @PostConstruct
    private void init() {
        svcBinderComponent.getSvcBinderModelMap().forEach((k, v) -> {
            if(!v.isCustom()) {
                registerServiceMapping(v);
            }
        });
    }

    private void registerServiceMapping(SvcBinderComponent.SvcBinderModel v) {
        requestMappingHandlerMapping
                .registerMapping(RequestMappingInfo.paths(v.getSvcPath())
                        .methods(RequestMethod.POST)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .options(requestMappingHandlerMapping.getBuilderConfiguration()).build(), context.getBeanNamesForType(v.getSvcClass())[0], v.getSvcMethod());
    }

}
