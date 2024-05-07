package io.github.minguanqiu.mingle.svc.action.rest;

import io.github.minguanqiu.mingle.svc.action.ActionResponse;
import io.github.minguanqiu.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanqiu.mingle.svc.exception.SvcRequestValidFailException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("simple-test")
public class RestActionTests1 {

    @Autowired
    SimpleAction simpleAction;
    @Autowired
    SimpleAction1 simpleAction1;
    @Autowired
    SimpleAction2 simpleAction2;
    @Autowired
    RestActionProperties restActionProperties;

    @Test
    void testSimple() {
        SimpleActionReq simpleActionReq = new SimpleActionReq();
        simpleActionReq.setText1("Hello");
        simpleActionReq.setText2("World");
        ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(simpleActionReq);
        assertThat(simpleActionResActionResponse.getCode()).isEqualTo(restActionProperties.getCode());
        assertThat(simpleActionResActionResponse.getMsg()).isEqualTo("successful");
        assertThat(simpleActionResActionResponse.getResponseBody().get().getText1()).isEqualTo("Hello");
        assertThat(simpleActionResActionResponse.getResponseBody().get().getText2()).isEqualTo("World");
    }

    @Test
    void testSimpleWithBuildResponseBodyHeader() {
        SimpleActionReq simpleActionReq = new SimpleActionReq();
        simpleActionReq.setText1("");
        simpleActionReq.setText2("World");
        ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(simpleActionReq);
        assertThat(simpleActionResActionResponse.getCode()).isEqualTo("error");
        assertThat(simpleActionResActionResponse.getMsg()).isEqualTo(SvcRequestValidFailException.MSG);
        assertThat(simpleActionResActionResponse.getResponseBody().get().getText1()).isEqualTo(null);
        assertThat(simpleActionResActionResponse.getResponseBody().get().getText2()).isEqualTo(null);
    }

    @Test
    void testSimple1WithoutBuildResponseBodyHeader() {
        SimpleActionReq simpleActionReq = new SimpleActionReq();
        simpleActionReq.setText1("");
        simpleActionReq.setText2("World");
        ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction1.doAction(simpleActionReq);
        assertThat(simpleActionResActionResponse.getCode()).isEqualTo(restActionProperties.getCode());
        assertThat(simpleActionResActionResponse.getMsg()).isEqualTo(restActionProperties.getMsg());
        assertThat(simpleActionResActionResponse.getResponseBody().get().getText1()).isEqualTo(null);
        assertThat(simpleActionResActionResponse.getResponseBody().get().getText2()).isEqualTo(null);
    }

    @Test
    void testSimple2WithoutBuildRestPath() {
        SimpleActionReq simpleActionReq = new SimpleActionReq();
        simpleActionReq.setText1("Hello");
        simpleActionReq.setText2("World");
        ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction2.doAction(simpleActionReq);
        assertThat(simpleActionResActionResponse.getValues()).containsKey("httpCode").containsValue(404);
        assertThat(simpleActionResActionResponse.getCode()).isEqualTo("error");
        assertThat(simpleActionResActionResponse.getMsg()).isEqualTo("client code error : " + "404");
    }

}


