package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.exception.SvcAuthenticationException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcCodeFiled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * when not authority will be caught
 *
 * @author Ming
 */

@Slf4j
@Component
public class SvcAuthenticationExceptionHandler extends AbstractExceptionHandler<SvcAuthenticationException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(SvcAuthenticationException ex) {
        log.debug(ex.getMessage());
        return build(SvcCodeFiled.MG04);
    }

}
