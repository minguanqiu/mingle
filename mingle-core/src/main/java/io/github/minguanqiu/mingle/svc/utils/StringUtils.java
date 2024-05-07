package io.github.minguanqiu.mingle.svc.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

/**
 * @author Ming
 */

public class StringUtils {

    public static String templateConvert(String template, Map<String, String> valueMap, String prefix, String suffix) {
        return StringSubstitutor.replace(template, valueMap, prefix, suffix);
    }

}
