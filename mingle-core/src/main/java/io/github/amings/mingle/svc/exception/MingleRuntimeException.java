package io.github.amings.mingle.svc.exception;

/**
 * for spring boot run Svc configuration fail
 *
 * @author Ming
 */

public class MingleRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param msg exception msg
	 */
	public MingleRuntimeException(String msg) {
		super(msg);
	}

}
