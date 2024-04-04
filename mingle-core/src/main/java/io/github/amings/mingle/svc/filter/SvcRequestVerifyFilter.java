package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.exception.ReqModelDeserializeFailException;
import io.github.amings.mingle.svc.exception.SvcReqModelValidFailException;
import io.github.amings.mingle.svc.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * Filter for service request model verify
 *
 * @author Ming
 */
public class SvcRequestVerifyFilter extends AbstractSvcFilter {

    private final Validator validator;
    private final JacksonUtils jacksonUtils;

    public SvcRequestVerifyFilter(SvcInfo svcInfo, Validator validator, JacksonUtils jacksonUtils) {
        super(svcInfo);
        this.validator = validator;
        this.jacksonUtils = jacksonUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional<?> reqModelOptional = jacksonUtils.readValue(svcInfo.getPayLoadString(), svcInfo.getSvcBinderModel().getRequestClass());
        if (reqModelOptional.isEmpty()) {
            throw new ReqModelDeserializeFailException("Request model deserialize fail");
        }
        Object object = reqModelOptional.get();
        Set<ConstraintViolation<Object>> set = validator.validate(object);
        if (!set.isEmpty()) {
            svcInfo.setSvcReqModelValidFailException(new SvcReqModelValidFailException("Request model valid error", new ConstraintViolationException(set)));
        }
        svcInfo.setSvcRequest(object);
        filterChain.doFilter(request, response);
    }

}
