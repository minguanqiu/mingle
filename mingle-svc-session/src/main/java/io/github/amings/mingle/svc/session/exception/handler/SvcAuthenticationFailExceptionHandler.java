package io.github.amings.mingle.svc.session.exception.handler;

import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.session.exception.SvcAuthenticationFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Authentication handler
 *
 * @author Ming
 */

@Slf4j
@Component
public class SvcAuthenticationFailExceptionHandler extends AbstractExceptionHandler<SvcAuthenticationFailException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(SvcAuthenticationFailException ex) {
        return build(ex.getMessage());
    }

}
