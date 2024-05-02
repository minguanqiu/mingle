package io.github.minguanq.mingle.svc.action.handler.impl;

import io.github.minguanq.mingle.svc.action.ActionInterceptor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Ming
 */
@Component
@Profile("interceptor-test")
public class LoggingInterceptorImpl implements ActionInterceptor {

    @Override
    public void intercept(Chain chain) {
        int i = 0;
        chain.response().getValues().put(ActionInterceptor.class.getSimpleName(), ++i);
        chain.proceed();
        chain.response().getValues().put(ActionInterceptor.class.getSimpleName(), ++i);
    }

}
