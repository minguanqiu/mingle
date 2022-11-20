package io.github.amings.mingle.svc.utils;

import io.github.amings.mingle.svc.annotation.CodeDesc;
import org.springframework.stereotype.Component;

/**
 * @author Ming
 */

@Component
public class SvcCodeFiled extends CodeFiled {

    @CodeDesc("Unknown exception")
    public static final String MG01 = "MG01";
    @CodeDesc("Request deserialize fail")
    public static final String MG02 = "MG02";
    @CodeDesc("Request body valid error")
    public static final String MG03 = "MG03";
    @CodeDesc("Access denied")
    public static final String MG04 = "MG04";
    @CodeDesc("Request body not json format")
    public static final String MG05 = "MG05";

}
