package io.github.amings.mingle.svc.session.utils;

import io.github.amings.mingle.svc.annotation.CodeDesc;
import io.github.amings.mingle.svc.utils.CodeFiled;
import org.springframework.stereotype.Component;

/**
 * @author Ming
 */

@Component
public class SessionCodeFiled extends CodeFiled {

    @CodeDesc("Authentication fail")
    public static final String MGS01 = "MGS01";

    @CodeDesc("Access denied")
    public static final String MGS20 = "MGS20";
    @CodeDesc("Missing authorization header")
    public static final String MGS21 = "MGS21";
    @CodeDesc("Decryption JWT token fail")
    public static final String MGS22 = "MGS22";
    @CodeDesc("SessionInfo deserialize fail")
    public static final String MGS23 = "MGS23";
    @CodeDesc("Session type incorrect")
    public static final String MGS24 = "MGS24";
    @CodeDesc("Session not found")
    public static final String MGS25 = "MGS25";
    @CodeDesc("Session has been logout by another session")
    public static final String MGS26 = "MGS26";

}
