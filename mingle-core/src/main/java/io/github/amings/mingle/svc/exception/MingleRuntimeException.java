package io.github.amings.mingle.svc.exception;

/**
 * for spring boot run Svc configuration fail
 *
 * @author Ming
 */

public class MingleRuntimeException extends RuntimeException {

	/**
	 * @param msg exception msg
	 */
	public MingleRuntimeException(String msg) {
		super(msg);
	}

}
