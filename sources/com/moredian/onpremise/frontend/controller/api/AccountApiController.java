package com.moredian.onpremise.frontend.controller.api;

import com.moredian.onpremise.api.account.AccountService;
import com.moredian.onpremise.core.model.request.OpenApiAppTokenRequest;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.OpenApiAppTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/api"})
@Api(value = "on-premise-account-api", description = "on-premise账户相关，openApi")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/api/AccountApiController.class */
public class AccountApiController extends BaseApiController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = {"/account/getOpenApiToken"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "OpenApiAppTokenRequest", paramType = "body")})
    @ApiOperation(value = "获取openApiToken", notes = "获取openApiToken")
    public DataResponse<OpenApiAppTokenResponse> getOpenApiToken(@RequestBody OpenApiAppTokenRequest request) {
        DataResponse<OpenApiAppTokenResponse> response = new DataResponse().success();
        response.setData(this.accountService.getOpenApiToken(request));
        return response;
    }
}
