package io.github.minguanqiu.mingle.svc.redis;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Getter
@Setter
public class Simple {

    private String text1;

    private String text2;

    private Sub sub = new Sub();

    @Data
    public static class Sub {

        private String text1 = "Text1";

        private String text2 = "Text2";

    }

}
