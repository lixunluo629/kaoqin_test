package com.moredian.onpremise.frontend.controller.temperature;

import com.moredian.onpremise.api.temperature.TemperatureRecordService;
import com.moredian.onpremise.core.model.request.TemperatureRecordRequest;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.TemperatureRecordCountResponse;
import com.moredian.onpremise.core.model.response.TemperatureRecordResponse;
import com.moredian.onpremise.core.utils.PageList;
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
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/temperature/TemperatureRecordController.class */
public class TemperatureRecordController extends BaseController {

    @Autowired
    private TemperatureRecordService temperatureRecordService;

    @RequestMapping(value = {"/temperature/record/query"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TemperatureRecordRequest", paramType = "body")})
    @ApiOperation(value = "测温记录查询", notes = "测温记录查询")
    public DataResponse<PageList<TemperatureRecordResponse>> queryRecord(@RequestBody TemperatureRecordRequest request) {
        TemperatureRecordRequest request2 = (TemperatureRecordRequest) super.getBaseRequest(request);
        DataResponse<PageList<TemperatureRecordResponse>> response = new DataResponse().success();
        response.setData(this.temperatureRecordService.queryRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/temperature/record/count"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TemperatureRecordRequest", paramType = "body")})
    @ApiOperation(value = "测温记录查询", notes = "测温记录查询")
    public DataResponse<PageList<TemperatureRecordCountResponse>> queryRecordCount(@RequestBody TemperatureRecordRequest request) {
        TemperatureRecordRequest request2 = (TemperatureRecordRequest) super.getBaseRequest(request);
        DataResponse<PageList<TemperatureRecordCountResponse>> response = new DataResponse().success();
        response.setData(this.temperatureRecordService.queryRecordCount(request2));
        return response;
    }
}
