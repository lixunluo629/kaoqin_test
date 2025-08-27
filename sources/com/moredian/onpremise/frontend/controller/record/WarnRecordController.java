package com.moredian.onpremise.frontend.controller.record;

import com.moredian.onpremise.api.record.WarnRecordService;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.WarnRecordListRequest;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.WarnRecordResponse;
import com.moredian.onpremise.core.utils.PageList;
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

@Api(value = "on-premise-warn-record", description = "on-premise报警记录相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/record/WarnRecordController.class */
public class WarnRecordController extends BaseController {

    @Autowired
    private WarnRecordService warnRecordService;

    @RequestMapping(value = {"/record/warn/recordList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "WarnRecordListRequest", paramType = "body")})
    @ApiOperation(value = "报警记录列表", notes = "报警记录列表")
    public DataResponse<PageList<WarnRecordResponse>> recordList(@RequestBody WarnRecordListRequest request) {
        WarnRecordListRequest request2 = (WarnRecordListRequest) super.getBaseRequest(request);
        DataResponse<PageList<WarnRecordResponse>> response = new DataResponse().success();
        response.setData(this.warnRecordService.recordList(request2));
        return response;
    }

    @RequestMapping(value = {"/record/warn/excelRecord"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "WarnRecordListRequest", paramType = "body")})
    @ApiOperation(value = "导出报警记录", notes = "导出报警记录")
    public DataResponse<String> excelRecord(@RequestBody WarnRecordListRequest request) {
        WarnRecordListRequest request2 = (WarnRecordListRequest) super.getBaseRequest(request);
        DataResponse<String> response = new DataResponse().success();
        response.setData(this.warnRecordService.excelRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/record/warn/getWarnTypes"}, method = {RequestMethod.POST})
    @ApiOperation(value = "获取报警类型", notes = "获取报警类型")
    public DataResponse<List<String>> getWarnTypes() {
        BaseRequest request = super.getBaseRequest(new BaseRequest());
        DataResponse<List<String>> response = new DataResponse().success();
        response.setData(this.warnRecordService.getWarnTypes(request));
        return response;
    }
}
