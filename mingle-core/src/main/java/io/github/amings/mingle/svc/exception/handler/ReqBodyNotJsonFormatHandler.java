package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.ReqBodyNotJsonFormatException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcCodeFiled;
import org.springframework.http.ResponseEntity;

/**
 * @author Ming
 */

@ExceptionHandler
public class ReqBodyNotJsonFormatHandler extends AbstractExceptionHandler<ReqBodyNotJsonFormatException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(ReqBodyNotJsonFormatException ex) {
        return build(SvcCodeFiled.MG05);
    }

}
