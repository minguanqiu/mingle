package io.github.minguanq.mingle.svc;

/**
 * Base class for all service
 *
 * @author Ming
 */
public sealed interface Service<Req extends SvcRequest, Res extends SvcResponseBody> permits AbstractService {

    Res doService(Req request);

}
