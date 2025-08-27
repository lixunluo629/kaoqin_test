package com.moredian.onpremise.frontend.controller.account;

import com.moredian.onpremise.api.group.AppService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.AppRequest;
import com.moredian.onpremise.core.model.request.DeleteAppRequest;
import com.moredian.onpremise.core.model.request.SaveAppRequest;
import com.moredian.onpremise.core.model.response.AppRemainDayResponse;
import com.moredian.onpremise.core.model.response.AppResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-app", description = "on-premise应用相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/account/AppController.class */
public class AppController extends BaseController {

    @Autowired
    private AppService appService;

    @RequestMapping(value = {"/app/app/list"}, method = {RequestMethod.POST})
    @ApiOperation(value = "应用列表", notes = "应用列表")
    public DataResponse<List<AppResponse>> appList() {
        AppRequest request = (AppRequest) super.getBaseRequest(new AppRequest());
        DataResponse<List<AppResponse>> response = new DataResponse().success();
        response.setData(this.appService.appList(request));
        return response;
    }

    @RequestMapping(value = {"/app/app/saveApp"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "应用相关", description = "保存应用")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveAppRequest", paramType = "body")})
    @ApiOperation(value = "保存应用", notes = "保存应用")
    public CommonResponse saveApp(@RequestBody SaveAppRequest request) {
        SaveAppRequest request2 = (SaveAppRequest) super.getBaseRequest(request);
        if (request2.getAppId() != null && request2.getAppId().longValue() > 0) {
            this.appService.updateApp(request2);
        } else {
            this.appService.insertApp(request2);
        }
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/app/app/deleteApp"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "应用相关", description = "删除应用")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteAppRequest", paramType = "body")})
    @ApiOperation(value = "删除应用", notes = "删除应用")
    public CommonResponse deleteApp(@RequestBody DeleteAppRequest request) {
        this.appService.deleteApp((DeleteAppRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/app/remainDay"}, method = {RequestMethod.POST})
    @ApiOperation(value = "查询应用有效期", notes = "查询应用有效期")
    public DataResponse<List<AppRemainDayResponse>> appRemainDay() {
        DataResponse<List<AppRemainDayResponse>> response = new DataResponse().success();
        response.setData(this.appService.appRemainDay(getOrgId()));
        return response;
    }
}
