package io.github.minguanqiu.mingle.svc.register;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


/**
 * @author Qiu Guan Ming
 */
@ActiveProfiles("test-properties")
@SpringBootTest
@AutoConfigureMockMvc
public class SvcPropertiesTests {

  @Autowired
  SvcProperties svcProperties;

  @Test
  void testPropertiesMapping() {
    assertThat(svcProperties.getCode()).isEqualTo("0000");
    assertThat(svcProperties.getMsg()).isEqualTo("success");
    assertThat(svcProperties.getMsgType()).isEqualTo("test");
    assertThat(svcProperties.getFeature().isLogging()).isFalse();
    assertThat(svcProperties.getFeature().isBodyProcess()).isTrue();
    assertThat(svcProperties.getFeature().getIpSecure()[1]).isEqualTo("1.1.1.1");
  }

}
