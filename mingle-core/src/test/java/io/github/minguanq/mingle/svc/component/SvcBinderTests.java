package io.github.minguanq.mingle.svc.component;


import io.github.minguanq.mingle.svc.SimpleSvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Ming
 */
@SpringBootTest
public class SvcBinderTests {

    @Autowired
    SvcBinderComponent svcBinderComponent;
    SvcBinderComponent.SvcBinderModel svcBinderModel;
    @Autowired
    ApplicationContext applicationContext;

    @Test
    @BeforeEach
    void testRegister() {
        svcBinderModel = svcBinderComponent.getSvcBinderModelMap().get(svcBinderComponent.getSvcProperties().getRootPath() + "/" + SimpleSvc.class.getSimpleName());
//        assertThat(svcBinderModel).isNotNull();
    }

    @Test
    void testSimpleSvcForValue() {
        assertThat(applicationContext.getBean("simpleSvcForValueTest")).isNotNull();
        assertThatThrownBy(() -> applicationContext.getBean("simpleSvcForValue")).isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Test
    void testSimpleSvcForPath() {
//        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
//        RequestMappingInfo requestMappingInfo = RequestMappingInfo
//                .methods(RequestMethod.POST)
//                .consumes(MediaType.APPLICATION_JSON_VALUE)
//                .produces(MediaType.APPLICATION_JSON_VALUE)
//                .build();
//        assertThat(requestMappingHandlerMapping.getHandlerMethods().containsKey(requestMappingInfo)).isTrue();
    }

}
