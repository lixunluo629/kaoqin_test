package com.moredian.onpremise.frontend.controller.visit;

import com.moredian.onpremise.api.visit.VisitRecordService;
import com.moredian.onpremise.core.model.request.VisitRecordDetailListRequest;
import com.moredian.onpremise.core.model.request.VisitRecordListRequest;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.VisitRecordDetailResponse;
import com.moredian.onpremise.core.model.response.VisitRecordResponse;
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

@Api(value = "on-premise-biz-visit", description = "on-premise访客相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/visit/VisitorRecordController.class */
public class VisitorRecordController extends BaseController {

    @Autowired
    private VisitRecordService visitRecordService;

    @RequestMapping(value = {"/visit/record/page"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VisitRecordListRequest", paramType = "body")})
    @ApiOperation(value = "访客记录分页查询", notes = "访客记录分页查询")
    public DataResponse<PageList<VisitRecordResponse>> pageVisitRecord(@RequestBody VisitRecordListRequest request) {
        VisitRecordListRequest request2 = (VisitRecordListRequest) super.getBaseRequest(request);
        DataResponse<PageList<VisitRecordResponse>> response = new DataResponse().success();
        response.setData(this.visitRecordService.pageVisitRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/visit/record/detail/page"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VisitRecordDetailListRequest", paramType = "body")})
    @ApiOperation(value = "访客记录明细分页查询", notes = "访客记录明细分页查询")
    public DataResponse<PageList<VisitRecordDetailResponse>> pageVisitRecordDetail(@RequestBody VisitRecordDetailListRequest request) {
        VisitRecordDetailListRequest request2 = (VisitRecordDetailListRequest) super.getBaseRequest(request);
        DataResponse<PageList<VisitRecordDetailResponse>> response = new DataResponse().success();
        response.setData(this.visitRecordService.pageVisitRecordDetail(request2));
        return response;
    }
}
