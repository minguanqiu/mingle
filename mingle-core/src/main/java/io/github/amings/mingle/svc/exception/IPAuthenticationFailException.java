package io.github.amings.mingle.svc.exception;

/**
 * Exception for ip address authentication fail
 *
 * @author Ming
 */
public class IPAuthenticationFailException extends RuntimeException{

    public IPAuthenticationFailException(String msg) {
       super(msg);
    }

}
