package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.exception.ReqModelDeserializeFailException;
import io.github.amings.mingle.svc.exception.SvcReqModelValidFailException;
import io.github.amings.mingle.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * javax validation filter
 *
 * @author Ming
 */

public class SvcReqModelVerifyFilter extends AbstractSvcFilter {

    @Autowired
    Validator validator;

    @Autowired
    JacksonUtils jacksonUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional<?> reqModelOptional = jacksonUtils.readValue(svcInfo.getPayLoadString(), svcInfo.getSvcBinderModel().getReqModelClass());
        if (!reqModelOptional.isPresent()) {
            throw new ReqModelDeserializeFailException("Request model deserialize fail");
        }
        Object object = reqModelOptional.get();
        Set<ConstraintViolation<Object>> set = validator.validate(object);
        if (set.size() > 0) {
            svcInfo.setSvcReqModelValidFailException(new SvcReqModelValidFailException("Request model valid error", new ConstraintViolationException(set)));
        }
        svcInfo.setValidReqModel(object);
        filterChain.doFilter(request, response);
    }

}
