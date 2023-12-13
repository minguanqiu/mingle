package io.github.amings.mingle.svc.exception.handler;

import io.github.amings.mingle.svc.annotation.ExceptionHandler;
import io.github.amings.mingle.svc.exception.SvcReqModelValidFailException;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.exception.handler.model.ConstraintViolationModel;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.utils.SvcCodeFiled;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

/**
 * ConstraintViolation Exception Handler for pojo bean pattern valid fail
 *
 * @author Ming
 */

@Deprecated
@ExceptionHandler
public class SvcReqModelValidFailExceptionHandler extends AbstractExceptionHandler<SvcReqModelValidFailException> {

    @Override
    public ResponseEntity<SvcResModelHandler> handle(SvcReqModelValidFailException e) {
        return prepareMessage(e.getConstraintViolationException());
    }

    private ResponseEntity<SvcResModelHandler> prepareMessage(ConstraintViolationException ex) {
        ConstraintViolationModel constraintViolationModel = new ConstraintViolationModel();
        ArrayList<ConstraintViolationModel.ConstraintViolationField> constraintViolationFields = constraintViolationModel.getConstraintViolationFields();
        for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            String fieldName = "";
            String fieldValue = "";
            if (cv.getInvalidValue() != null) {
                fieldValue = cv.getInvalidValue().toString();
            }
            for (Path.Node node : cv.getPropertyPath()) {
                fieldName = node.getName();
            }
            ConstraintViolationModel.ConstraintViolationField model = new ConstraintViolationModel.ConstraintViolationField();
            model.setField(fieldName);
            model.setReason(cv.getMessage());
            model.setValue(fieldValue);
            constraintViolationFields.add(model);
        }
        svcInfo.setSvcResModelHandler4Log(svcResUtils.build(SvcCodeFiled.MG03, svcMsgHandler.getMsg(SvcCodeFiled.MG03), constraintViolationModel));
        return build(SvcCodeFiled.MG03);
    }

}
