package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.exception.ReqModelDeserializeFailException;
import io.github.amings.mingle.svc.exception.SvcReqModelValidFailException;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
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
