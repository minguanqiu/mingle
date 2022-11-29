package io.github.amings.mingle.svc.session.exception.handler;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.session.exception.JwtHeaderMissingException;
import io.github.amings.mingle.svc.session.utils.SessionCodeFiled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Ming
 */

@Slf4j
@ExceptionHandler
public class JwtHeaderMissingExceptionHandler extends AbstractExceptionHandler<JwtHeaderMissingException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(JwtHeaderMissingException ex) {
        return build(SessionCodeFiled.MGS21);
    }

}
