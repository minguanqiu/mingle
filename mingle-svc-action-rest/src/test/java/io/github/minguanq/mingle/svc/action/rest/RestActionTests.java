package io.github.minguanq.mingle.svc.action.rest;

import io.github.minguanq.mingle.svc.action.ActionResponse;
import io.github.minguanq.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@SpringBootTest
public class RestActionTests {

    @Autowired
    SimpleAction simpleAction;
    @Autowired
    RestActionProperties restActionProperties;

    @Test
    void testAction() {
        SimpleActionReq simpleActionReq = new SimpleActionReq();
        simpleActionReq.setText1("Hello");
        simpleActionReq.setText2("World");
        ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(simpleActionReq);
        assertThat(simpleActionResActionResponse.getCode()).isNotEqualTo(restActionProperties.getCode());
        assertThat(simpleActionResActionResponse.getMsg()).isEqualTo("Server not exist with name: Simple");
    }

}
