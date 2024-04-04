package io.github.amings.mingle.svc;

/**
 * Base class for all service
 *
 * @author Ming
 */
public sealed interface Service<Req extends SvcRequest, Res extends SvcResponse> permits AbstractService {

    Res doService(Req request);

}
