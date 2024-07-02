package io.github.minguanqiu.mingle.svc;

import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @author Qiu Guan Ming
 */
@OpenAPIDefinition(info = @Info(title = "testTitle", version = "v1"), servers = @Server(url = "testUrl"))
@Svc(tags = "tests", summary = "test service for summary", description = "test service for description")
public class SimpleSvcForDoc extends AbstractService<SimpleSvcReq, SimpleSvcRes> {

  public SimpleSvcForDoc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Operation(responses = @ApiResponse(responseCode = "200", description = "testResponse"))
  @Override
  public SimpleSvcRes doService(SimpleSvcReq request) {
    SimpleSvcRes simpleSvcRes = new SimpleSvcRes();
    simpleSvcRes.setText1(request.getText1());
    simpleSvcRes.setText2(request.getText2());
    return simpleSvcRes;
  }

}
