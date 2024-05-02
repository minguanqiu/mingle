package io.github.minguanq.mingle.svc.action.handler.impl;

import io.github.minguanq.mingle.svc.action.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author Ming
 */

@ActiveProfiles("interceptor-test")
@SpringBootTest
public class InterceptorTests {

    @Autowired
    SimpleAction simpleAction;

    @Test
    void testInterceptor() {
        ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(new SimpleActionReq());
        assertThat(simpleActionResActionResponse.getValues().get(ActionInterceptor.class.getSimpleName())).isEqualTo(2);
    }

}
