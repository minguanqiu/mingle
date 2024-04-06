package io.github.minguanq.mingle.svc.component;

import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Ming
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
        assertThat(svcProperties.getMsg_type()).isEqualTo("test");
        assertThat(svcProperties.isLogging()).isFalse();
        assertThat(svcProperties.isBody_process()).isTrue();
        assertThat(svcProperties.getIp_secure()[0]).isEqualTo("0.0.0.0");
    }

}
