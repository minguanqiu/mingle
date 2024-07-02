package io.github.minguanqiu.mingle.svc.action.handler.impl;

import io.github.minguanqiu.mingle.svc.action.ActionInterceptor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Qiu Guan Ming
 */
@Component
@Profile("interceptor-test")
public class InterceptorImpl implements ActionInterceptor {

  @Override
  public void intercept(Chain chain) {
    int i = 0;
    chain.actionInfo().getValues().put(ActionInterceptor.class.getSimpleName(), ++i);
    chain.proceed();
    chain.actionInfo().getValues().put(ActionInterceptor.class.getSimpleName(), ++i);
  }

}
