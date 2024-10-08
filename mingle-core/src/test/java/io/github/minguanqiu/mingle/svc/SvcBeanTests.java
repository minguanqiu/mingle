package io.github.minguanqiu.mingle.svc;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Qiu Guan Ming
 */
@SpringBootTest
public class SvcBeanTests {

  @Autowired
  JacksonUtils jacksonUtils;
  @Autowired
  @Qualifier("svcLogJacksonUtils")
  JacksonUtils svcLogJacksonUtils;

  @Test
  void testExcludeLogColumn() {
    SimpleSvcReq simpleSvcReq = new SimpleSvcReq();
    simpleSvcReq.setText3("123");
    assertThat(jacksonUtils.readTree(simpleSvcReq).get().has("text3")).isTrue();
    assertThat(svcLogJacksonUtils.readTree(simpleSvcReq).get().has("text3")).isFalse();
  }

}
