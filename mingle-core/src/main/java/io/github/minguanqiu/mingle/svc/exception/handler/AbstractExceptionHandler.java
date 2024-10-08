package io.github.minguanqiu.mingle.svc.exception.handler;

import com.google.common.reflect.TypeToken;
import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * This abstract class defines a framework for handling exceptions and generating response entities
 * containing service response bodies. Subclasses need to implement the abstract handle method to
 * provide specific exception handling logic.
 *
 * <pre>
 * {@code
 * public class MyExceptionHandler extends AbstractExceptionHandler<MyException> {
 *   @Override
 *   public ResponseEntity<SvcResponseBody> handle(MyException ex) {
 *     // Custom exception handling logic
 *   }
 * }
 * }
 * </pre>
 *
 * @param <E> the type of exception to be handled
 * @author Qiu Guan Ming
 * @see ResponseEntity
 * @see SvcResponseBody
 * @see SvcInfo
 * @see Exception
 */
public abstract class AbstractExceptionHandler<E extends Exception> {

  /**
   * Service information.
   */
  protected final SvcInfo svcInfo;

  /**
   * Exception class.
   *
   * @return return a class for exception.
   */
  @Getter
  protected final Class<E> eClass;

  /**
   * Create a new AbstractExceptionHandler instance.
   *
   * @param svcInfo the service information.
   */
  @SuppressWarnings("unchecked")
  public AbstractExceptionHandler(SvcInfo svcInfo) {
    this.svcInfo = svcInfo;
    eClass = (Class<E>) new TypeToken<E>(getClass()) {
    }.getRawType();
  }

  /**
   * Handles the given exception and returns an appropriate response entity.
   *
   * @param ex the exception to be handled.
   * @return containing the service response body, which represents the result of handling the
   * exception.
   */
  public abstract ResponseEntity<SvcResponseBody> handle(E ex);

  /**
   * Returns a ResponseEntity for the service response.
   *
   * @param svcResponseHeader the service response header.
   * @return containing the service response body.
   */
  protected ResponseEntity<SvcResponseBody> build(SvcResponseHeader svcResponseHeader) {
    return build(svcResponseHeader, null);
  }

  /**
   * Build a ResponseEntity for the service response.
   *
   * @param svcResponseHeader the service response header.
   * @param svcResponseBody   the service response body.
   * @return containing the service response body.
   */
  protected ResponseEntity<SvcResponseBody> build(SvcResponseHeader svcResponseHeader,
      SvcResponseBody svcResponseBody) {
    svcInfo.setSvcResponseHeader(svcResponseHeader);
    return ResponseEntity.ok(svcResponseBody);
  }

}

