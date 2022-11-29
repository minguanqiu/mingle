package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.exception.handler.model.ExceptionModel;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcCodeFiled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

/**
 * Exception or unknown Exception will be caught
 *
 * @author Ming
 */

@Slf4j
@ExceptionHandler
public class AllExceptionHandler extends AbstractExceptionHandler<Exception> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(Exception e) {
        log.error("", e);
        ExceptionModel model = new ExceptionModel();
        model.setException(e.getCause() != null ? e.getCause().toString() : e.toString());
        svcInfo.setSvcResModelHandler4Log(svcResUtils.build(SvcCodeFiled.MG01, svcMsgHandler.getMsg(SvcCodeFiled.MG01), model));
        return build(SvcCodeFiled.MG01);
    }

}
