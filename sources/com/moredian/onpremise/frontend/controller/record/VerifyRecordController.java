package com.moredian.onpremise.frontend.controller.record;

import com.moredian.onpremise.api.record.VerifyRecordService;
import com.moredian.onpremise.core.model.request.StatisticsVerifyScoreRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordExcelRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordListRequest;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.StatisticsVerifyScoreResponse;
import com.moredian.onpremise.core.model.response.StatisticsVerifyScoreV2Response;
import com.moredian.onpremise.core.model.response.VerifyRecordExcelResponse;
import com.moredian.onpremise.core.model.response.VerifyRecordResponse;
import com.moredian.onpremise.core.utils.MyDateUtils;
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

@Api(value = "on-premise-verify-record", description = "on-premise识别记录相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/record/VerifyRecordController.class */
public class VerifyRecordController extends BaseController {

    @Autowired
    private VerifyRecordService recordService;

    @RequestMapping(value = {"/record/verify/recordList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VerifyRecordListRequest", paramType = "body")})
    @ApiOperation(value = "识别记录列表", notes = "识别记录列表")
    public DataResponse<PageList<VerifyRecordResponse>> recordList(@RequestBody VerifyRecordListRequest request) {
        VerifyRecordListRequest request2 = (VerifyRecordListRequest) super.getBaseRequest(request);
        DataResponse<PageList<VerifyRecordResponse>> response = new DataResponse().success();
        if (request2.getStartTimeStr() != null && MyDateUtils.parseDate(request2.getStartTimeStr(), "yyyy-MM-dd") != null) {
            request2.setStartTimestamp(Long.valueOf(MyDateUtils.parseDate(request2.getStartTimeStr(), "yyyy-MM-dd").getTime()));
        }
        if (request2.getEndTimeStr() != null && MyDateUtils.parseDate(request2.getEndTimeStr(), "yyyy-MM-dd") != null) {
            request2.setEndTimestamp(Long.valueOf(MyDateUtils.parseDate(request2.getEndTimeStr(), "yyyy-MM-dd").getTime()));
        }
        response.setData(this.recordService.recordList(request2));
        return response;
    }

    @RequestMapping(value = {"/record/verify/excelRecord"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VerifyRecordExcelRequest", paramType = "body")})
    @ApiOperation(value = "导出识别记录", notes = "导出识别记录")
    public DataResponse<List<VerifyRecordExcelResponse>> excelRecord(@RequestBody VerifyRecordExcelRequest request) {
        VerifyRecordExcelRequest request2 = (VerifyRecordExcelRequest) super.getBaseRequest(request);
        DataResponse<List<VerifyRecordExcelResponse>> response = new DataResponse().success();
        if (request2.getStartTimeStr() != null && MyDateUtils.parseDate(request2.getStartTimeStr(), "yyyy-MM-dd") != null) {
            request2.setStartTimestamp(Long.valueOf(MyDateUtils.parseDate(request2.getStartTimeStr(), "yyyy-MM-dd").getTime()));
        }
        if (request2.getEndTimeStr() != null && MyDateUtils.parseDate(request2.getEndTimeStr(), "yyyy-MM-dd") != null) {
            request2.setEndTimestamp(Long.valueOf(MyDateUtils.parseDate(request2.getEndTimeStr(), "yyyy-MM-dd").getTime()));
        }
        response.setData(this.recordService.excelRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/record/verify/statisticsVerifyScore"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "StatisticsVerifyScoreRequest", paramType = "body")})
    @ApiOperation(value = "识别记录分析", notes = "识别记录分析")
    public DataResponse<StatisticsVerifyScoreResponse> statisticsVerifyScore(@RequestBody StatisticsVerifyScoreRequest request) {
        StatisticsVerifyScoreRequest request2 = (StatisticsVerifyScoreRequest) super.getBaseRequest(request);
        DataResponse<StatisticsVerifyScoreResponse> response = new DataResponse().success();
        response.setData(this.recordService.statisticsVerifyScore(request2));
        return response;
    }

    @RequestMapping(value = {"/record/verify/statisticsVerifyScore/v2"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "StatisticsVerifyScoreRequest", paramType = "body")})
    @ApiOperation(value = "识别记录分析", notes = "识别记录分析")
    public DataResponse<StatisticsVerifyScoreV2Response> statisticsVerifyScoreV2(@RequestBody StatisticsVerifyScoreRequest request) {
        StatisticsVerifyScoreRequest request2 = (StatisticsVerifyScoreRequest) super.getBaseRequest(request);
        DataResponse<StatisticsVerifyScoreV2Response> response = new DataResponse().success();
        if (request2.getStartTimeStr() != null && MyDateUtils.parseDate(request2.getStartTimeStr(), "yyyy-MM-dd") != null) {
            request2.setStartTimestamp(Long.valueOf(MyDateUtils.parseDate(request2.getStartTimeStr(), "yyyy-MM-dd").getTime()));
        }
        if (request2.getEndTimeStr() != null && MyDateUtils.parseDate(request2.getEndTimeStr(), "yyyy-MM-dd") != null) {
            request2.setEndTimestamp(Long.valueOf(MyDateUtils.parseDate(request2.getEndTimeStr(), "yyyy-MM-dd").getTime()));
        }
        response.setData(this.recordService.statisticsVerifyScoreV2(request2));
        return response;
    }
}
