package com.moredian.onpremise.frontend.controller.temperature;

import com.moredian.onpremise.api.temperature.TemperatureConfigService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.TemperatureConfigRequest;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.TemperatureConfigResponse;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-biz-temperature", description = "on-premise测温相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/temperature/TemperatureConfigController.class */
public class TemperatureConfigController extends BaseController {

    @Autowired
    private TemperatureConfigService temperatureConfigService;

    @RequestMapping(value = {"/temperature/config/query"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TemperatureConfigRequest", paramType = "body")})
    @ApiOperation(value = "测温参数查询", notes = "测温参数查询")
    public DataResponse<TemperatureConfigResponse> queryConfig(@RequestBody TemperatureConfigRequest request) {
        TemperatureConfigRequest request2 = (TemperatureConfigRequest) super.getBaseRequest(request);
        DataResponse<TemperatureConfigResponse> response = new DataResponse().success();
        response.setData(this.temperatureConfigService.queryConfig(request2));
        return response;
    }

    @RequestMapping(value = {"/temperature/config/update"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "测温相关", description = "修改测温参数")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TemperatureConfigRequest", paramType = "body")})
    @ApiOperation(value = "测温参数更新", notes = "测温参数更新")
    public DataResponse<Long> updateConfig(@RequestBody TemperatureConfigRequest request) {
        TemperatureConfigRequest request2 = (TemperatureConfigRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.temperatureConfigService.updateConfig(request2));
        return response;
    }
}
