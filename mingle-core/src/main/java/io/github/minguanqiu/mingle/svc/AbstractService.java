package io.github.minguanqiu.mingle.svc;

import com.google.common.reflect.TypeToken;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.exception.BreakSvcProcessException;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import lombok.Getter;

/**
 * This class provide service register must extend and add {@link Svc} on target class
 *
 * @author Qiu Guan Ming
 */
public abstract non-sealed class AbstractService<R1 extends SvcRequest, R2 extends SvcResponseBody>
    implements Service<R1, R2> {

  protected final SvcInfo svcInfo;

  @Getter
  protected final Class<R1> reqClass;

  @Getter
  protected final Class<R2> resClass;


  @SuppressWarnings("unchecked")
  public AbstractService(SvcInfo svcInfo) {
    this.svcInfo = svcInfo;
    reqClass = (Class<R1>) new TypeToken<R1>(getClass()) {
    }.getRawType();
    resClass = (Class<R2>) new TypeToken<R2>(getClass()) {
    }.getRawType();
  }

  /**
   * Interrupt service logic.
   *
   * @param svcResponseHeader service response header.
   * @throws BreakSvcProcessException will throw.
   **/
  protected void throwLogic(SvcResponseHeader svcResponseHeader) throws BreakSvcProcessException {
    throwLogic(svcResponseHeader, null);
  }

  /**
   * Interrupt service logic.
   *
   * @param svcResponseHeader service response header
   * @param svcResponse       service response
   * @throws BreakSvcProcessException will throw
   **/
  protected void throwLogic(SvcResponseHeader svcResponseHeader, R2 svcResponse)
      throws BreakSvcProcessException {
    throw new BreakSvcProcessException(svcResponseHeader, svcResponse);
  }

  /**
   * Return service logic.
   *
   * @param svcResponseHeader service response header
   **/
  protected R2 returnLogic(SvcResponseHeader svcResponseHeader) {
    return returnLogic(svcResponseHeader, null);
  }

  /**
   * Return service logic.
   *
   * @param svcResponseHeader service response header
   * @param svcResponse       service response
   **/
  protected R2 returnLogic(SvcResponseHeader svcResponseHeader, R2 svcResponse) {
    svcInfo.setSvcResponseHeader(svcResponseHeader);
    return svcResponse;
  }


}
