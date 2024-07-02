package io.github.minguanqiu.mingle.svc.action.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.action.ActionInterceptor;
import io.github.minguanqiu.mingle.svc.action.ActionResponse;
import io.github.minguanqiu.mingle.svc.action.SimpleAction;
import io.github.minguanqiu.mingle.svc.action.SimpleActionReq;
import io.github.minguanqiu.mingle.svc.action.SimpleActionRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Qiu Guan Ming
 */

@ActiveProfiles("interceptor-test")
@SpringBootTest
public class InterceptorTests {

  @Autowired
  SimpleAction simpleAction;

  @Test
  void testAddInterceptor() {
    SimpleActionReq simpleActionReq = new SimpleActionReq();
    simpleActionReq.setText1("Hello");
    simpleActionReq.setText2("World");
    ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(
        simpleActionReq);
    assertThat(simpleActionResActionResponse.getValues()
        .get(ActionInterceptor.class.getSimpleName())).isEqualTo(2);
  }

}
