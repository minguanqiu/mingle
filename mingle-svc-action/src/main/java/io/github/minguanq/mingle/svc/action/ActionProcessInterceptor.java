package io.github.minguanq.mingle.svc.action;

import io.github.minguanq.mingle.svc.action.concurrent.ActionAttribute;
import io.github.minguanq.mingle.svc.action.concurrent.ActionThreadLocal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Interceptor for protected process status when interceptor for infinite loop
 *
 * @author Ming
 */
public class ActionProcessInterceptor implements ActionInterceptor {

    @Override
    public void intercept(Chain chain) {
        ActionAttribute actionAttribute = ActionThreadLocal.get();
        Optional<Object> attributes = actionAttribute.getAttributes(ActionAttribute.Name.ACTION_PROCESS_STATUS);
        if(attributes.isPresent()) {
            ActionProcessStatus actionProcessStatus = (ActionProcessStatus) attributes.get();
            if(actionProcessStatus.isIntercepting()) {
                throw new IllegalStateException("action process status error");
            } else {
                actionProcessStatus.setIntercepting(true);
            }
        } else {
            actionAttribute.setAttributes(ActionAttribute.Name.ACTION_PROCESS_STATUS,new ActionProcessStatus(true));
        }
        chain.proceed();
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    protected static class ActionProcessStatus {

        private boolean intercepting;

        private ActionProcessStatus(boolean intercepting) {
            this.intercepting = intercepting;
        }

    }

}
