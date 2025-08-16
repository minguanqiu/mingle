package io.github.minguanqiu.mingle.svc.data;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.data.dao.TestDao;
import io.github.minguanqiu.mingle.svc.data.dao.entity.TestEntity;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author Qiu Guan Ming
 */
@SpringBootTest
@AutoConfigureMockMvc
public class DataTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  TestDao testDao;
  @Autowired
  private JacksonUtils jacksonUtils;
  @Autowired
  private SvcPathHandler svcPathHandler;
  @Autowired
  private SvcProperties svcProperties;
  @Autowired
  private SvcResponseHandler svcResponseHandler;


//  @Test
  void testJPADao() {
    TestEntity testEntity = new TestEntity();
    testEntity.setSerial("1");
    testEntity.setText1("Hello");
    testEntity.setText2("World");
    testDao.save(testEntity);
    Optional<TestEntity> id = testDao.findById("1");
    TestEntity testEntity1 = id.get();
    assertThat(testEntity.getText1()).isEqualTo("Hello");
    assertThat(testEntity.getText2()).isEqualTo("World");
    testDao.delete(testEntity1);
    assertThat(testDao.findById("1")).isNotPresent();
  }

//  @Test
  void testTransactional() throws Exception {
    ResultActions perform = mockMvc.perform(
        SvcTestUtils.buildSvcRequest(svcPathHandler, SimpleSvcWithTransactional.class)
            .content(SvcTestUtils.getTestContent("normal", "123", "123")));
    MockHttpServletResponse response = perform.andReturn().getResponse();
    Optional<? extends SvcResponseHandler> optionalSvcResponseHandler = jacksonUtils.readValue(
        response.getContentAsString(), svcResponseHandler.getClass());
    assertThat(optionalSvcResponseHandler).isPresent();
    SvcResponseHandler svcResponseHandler = optionalSvcResponseHandler.get();
    assertThat(svcResponseHandler.getCode()).isEqualTo(svcProperties.getCode());
  }

}
