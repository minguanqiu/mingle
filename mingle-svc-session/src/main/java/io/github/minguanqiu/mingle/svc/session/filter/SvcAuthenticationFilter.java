package io.github.minguanqiu.mingle.svc.session.filter;

import io.github.minguanqiu.mingle.svc.register.SvcDefinition;
import io.github.minguanqiu.mingle.svc.register.SvcRegister;
import io.github.minguanqiu.mingle.svc.session.SessionHeader;
import io.github.minguanqiu.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanqiu.mingle.svc.session.dao.SvcSessionDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;
import io.github.minguanqiu.mingle.svc.session.exception.SessionHeaderDeserializeErrorException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionHeaderMissingException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionNotExistException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionTokenDecryptionErrorException;
import io.github.minguanqiu.mingle.svc.session.exception.SessionTypeIncorrectException;
import io.github.minguanqiu.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanqiu.mingle.svc.session.handler.model.SvcSessionFeature;
import io.github.minguanqiu.mingle.svc.session.security.SessionAuthentication;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Check and decryption token after create authentication.
 *
 * @author Qiu Guan Ming
 */
public class SvcAuthenticationFilter extends OncePerRequestFilter {

  private final SvcRegister svcRegister;
  private final SvcSessionProperties svcSessionProperties;
  private final SvcSessionDao svcSessionDao;
  private final SessionTokenHandler sessionTokenHandler;
  private final JacksonUtils jacksonUtils;

  /**
   * Create a new SvcAuthenticationFilter instance.
   *
   * @param svcRegister          the service register.
   * @param svcSessionProperties the service session properties.
   * @param svcSessionDao        the service session DAO.
   * @param sessionTokenHandler  the session token handler.
   * @param jacksonUtils         the jackson utils.
   */
  public SvcAuthenticationFilter(SvcRegister svcRegister, SvcSessionProperties svcSessionProperties,
      SvcSessionDao svcSessionDao, SessionTokenHandler sessionTokenHandler,
      JacksonUtils jacksonUtils) {
    this.svcRegister = svcRegister;
    this.svcSessionProperties = svcSessionProperties;
    this.svcSessionDao = svcSessionDao;
    this.sessionTokenHandler = sessionTokenHandler;
    this.jacksonUtils = jacksonUtils;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String cipherText = request.getHeader(svcSessionProperties.getHeader());
    if (cipherText == null) {
      throw new SessionHeaderMissingException();
    }
    String plainText;
    try {
      plainText = sessionTokenHandler.decryption(cipherText);
    } catch (Exception e) {
      throw new SessionTokenDecryptionErrorException();
    }
    SessionHeader sessionHeader = jacksonUtils.readValue(plainText, SessionHeader.class)
        .orElseThrow(SessionHeaderDeserializeErrorException::new);
    SvcSessionEntity sessionEntity = svcSessionDao.get(sessionHeader.redisKey());
    if (sessionEntity == null) {
      throw new SessionNotExistException();
    }
    sessionEntity.setTimeToLive(Long.parseLong(sessionHeader.timeToLive()));
    SvcDefinition svcDefinition = svcRegister.getSvcDefinition(request).get();
    SvcSessionFeature svcSessionFeature = svcDefinition.getFeature(SvcSessionFeature.class).get();
    String[] types = svcSessionFeature.types();
    if (!checkType(types, sessionEntity.getType())) {
      throw new SessionTypeIncorrectException();
    }
    svcSessionDao.set(sessionEntity); // refresh session with origin live time
    SessionAuthentication authentication = new SessionAuthentication(sessionEntity, null,
        AuthorityUtils.createAuthorityList(sessionEntity.getAuthorities().toArray(new String[0])));
    authentication.setAuthenticated(true);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  private boolean checkType(String[] types, String sessionType) {
    boolean isPass = false;
    for (String type : types) {
      if (type.equals(sessionType)) {
        isPass = true;
        break;
      }
    }
    return isPass;
  }

}
