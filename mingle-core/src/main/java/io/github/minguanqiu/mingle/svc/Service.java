package io.github.minguanqiu.mingle.svc;

/**
 * Base class for all service
 * @param <R1> service request
 * @param <R2> service response body
 * @author Qiu Guan Ming
 */
public sealed interface Service<R1 extends SvcRequest, R2 extends SvcResponseBody> permits
    AbstractService {

  R2 doService(R1 request);

}
