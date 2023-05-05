package io.github.amings.mingle.svc.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;

/**
 * @author Ming
 */

@Component
public class SvcRegisterComponent {

    @Autowired
    private SvcBinderComponent svcBinderComponent;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @PostConstruct
    private void init() {
        svcBinderComponent.getSvcMap().forEach((k, v) -> {
            if(!v.isCustom()) {
                requestMappingHandlerMapping
                        .registerMapping(RequestMappingInfo.paths(v.getSvcPath())
                                .methods(RequestMethod.POST)
                                .consumes(MediaType.APPLICATION_JSON_VALUE)
                                .produces(MediaType.APPLICATION_JSON_VALUE)
                                .options(requestMappingHandlerMapping.getBuilderConfiguration()).build(), context.getBeanNamesForType(v.getSvcClass())[0], v.getSvcMethod());
            }
        });
    }

}
