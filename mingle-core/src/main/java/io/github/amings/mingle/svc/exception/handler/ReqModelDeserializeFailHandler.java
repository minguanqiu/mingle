package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.ReqModelDeserializeFailException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcCodeFiled;
import org.springframework.http.ResponseEntity;

/**
 * @author Ming
 */

@ExceptionHandler
public class ReqModelDeserializeFailHandler extends AbstractExceptionHandler<ReqModelDeserializeFailException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(ReqModelDeserializeFailException ex) {
        return build(SvcCodeFiled.MG02);
    }

}
