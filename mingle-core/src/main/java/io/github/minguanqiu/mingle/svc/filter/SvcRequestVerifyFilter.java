package io.github.minguanqiu.mingle.svc.filter;

import io.github.minguanqiu.mingle.svc.exception.SvcRequestDeserializeFailException;
import io.github.minguanqiu.mingle.svc.exception.SvcRequestValidFailException;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
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
 * @author Qiu Guan Ming
 */
public class SvcRequestVerifyFilter extends AbstractSvcFilter {

  private final Validator validator;
  private final JacksonUtils jacksonUtils;

  /**
   * Create a new SvcRequestVerifyFilter instance.
   *
   * @param svcInfo      the service information.
   * @param validator    the validator.
   * @param jacksonUtils the jackson utils.
   */
  public SvcRequestVerifyFilter(SvcInfo svcInfo, Validator validator, JacksonUtils jacksonUtils) {
    super(svcInfo);
    this.validator = validator;
    this.jacksonUtils = jacksonUtils;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    Optional<?> reqModelOptional = jacksonUtils.readValue(svcInfo.getRequestBody(),
        svcInfo.getSvcDefinition().getRequestClass());
    if (reqModelOptional.isEmpty()) {
      throw new SvcRequestDeserializeFailException();
    }
    Object object = reqModelOptional.get();
    Set<ConstraintViolation<Object>> set = validator.validate(object);
    if (!set.isEmpty()) {
      throw new SvcRequestValidFailException(new ConstraintViolationException(set));
    }
    svcInfo.setSvcRequest(object);
    filterChain.doFilter(request, response);
  }

}
