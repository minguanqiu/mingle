package io.github.amings.mingle.svc.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * System message
 *
 * @author Ming
 */

@Component
public class SvcSuccessField {

    private static String SUCCESS_CODE;
    private static String SUCCESS_DESC;

    @Value("${mingle.svc.successCode:0}")
    public void setSuccessCode(String successCode) {
        SUCCESS_CODE = successCode;
    }

    @Value("${mingle.svc.successDesc:successful}")
    public void setSuccessDesc(String successDesc) {
        SUCCESS_DESC = successDesc;
    }

    public static String getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static String getSuccessDesc() {
        return SUCCESS_DESC;
    }

}
