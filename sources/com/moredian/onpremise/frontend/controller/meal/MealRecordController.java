package com.moredian.onpremise.frontend.controller.meal;

import com.moredian.onpremise.api.meal.MealRecordService;
import com.moredian.onpremise.core.model.request.CountMealCardRequest;
import com.moredian.onpremise.core.model.request.CountMealRecordRequest;
import com.moredian.onpremise.core.model.request.ListMealRecordRequest;
import com.moredian.onpremise.core.model.response.CountMealCardResponse;
import com.moredian.onpremise.core.model.response.CountMealRecordResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.ListMealRecordResponse;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-biz-meal", description = "on-premise团餐相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/meal/MealRecordController.class */
public class MealRecordController extends BaseController {

    @Autowired
    private MealRecordService mealRecordService;

    @RequestMapping(value = {"/meal/record/listSuccessRecord"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListMealRecordRequest", paramType = "body")})
    @ApiOperation(value = "就餐记录列表", notes = "就餐记录列表")
    public DataResponse<PageList<ListMealRecordResponse>> listSuccessRecord(@RequestBody ListMealRecordRequest request) {
        ListMealRecordRequest request2 = (ListMealRecordRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListMealRecordResponse>> response = new DataResponse().success();
        response.setData(this.mealRecordService.listSuccessRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/record/listErrorRecord"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListMealRecordRequest", paramType = "body")})
    @ApiOperation(value = "异常就餐记录列表", notes = "异常就餐记录列表")
    public DataResponse<PageList<ListMealRecordResponse>> listErrorRecord(@RequestBody ListMealRecordRequest request) {
        ListMealRecordRequest request2 = (ListMealRecordRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListMealRecordResponse>> response = new DataResponse().success();
        response.setData(this.mealRecordService.listErrorRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/record/countMealRecord"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountMealRecordRequest", paramType = "body")})
    @ApiOperation(value = "就餐记录统计", notes = "就餐记录统计")
    public DataResponse<PageList<CountMealRecordResponse>> countMealRecord(@RequestBody CountMealRecordRequest request) {
        CountMealRecordRequest request2 = (CountMealRecordRequest) super.getBaseRequest(request);
        DataResponse<PageList<CountMealRecordResponse>> response = new DataResponse().success();
        response.setData(this.mealRecordService.countMealRecord(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/record/countMealCard"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountMealCardRequest", paramType = "body")})
    @ApiOperation(value = "餐卡记录统计", notes = "餐卡记录统计")
    public DataResponse<PageList<CountMealCardResponse>> countMealCard(@RequestBody CountMealCardRequest request) {
        CountMealCardRequest request2 = (CountMealCardRequest) super.getBaseRequest(request);
        DataResponse<PageList<CountMealCardResponse>> response = new DataResponse().success();
        response.setData(this.mealRecordService.countMealCard(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/record/countMealRecord/pdf"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountMealRecordRequest", paramType = "body")})
    @ApiOperation(value = "就餐记录统计，导出pdf", notes = "就餐记录统计")
    public void countMealRecordExport(@RequestBody CountMealRecordRequest request, HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        this.mealRecordService.countMealRecordExportPDF((CountMealRecordRequest) super.getBaseRequest(request), response);
    }

    @RequestMapping(value = {"/meal/record/countMealCard/pdf"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountMealCardRequest", paramType = "body")})
    @ApiOperation(value = "餐卡记录统计，导出pdf", notes = "餐卡记录统计")
    public void countMealCardExport(@RequestBody CountMealCardRequest request, HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        this.mealRecordService.countMealCardExportPDF((CountMealCardRequest) super.getBaseRequest(request), response);
    }

    @RequestMapping(value = {"/meal/record/countMealTotal"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CountMealRecordRequest", paramType = "body")})
    @ApiOperation(value = "团餐汇总统计", notes = "就餐记录统计")
    public DataResponse<String> countMealTotal(@RequestBody CountMealRecordRequest request) {
        DataResponse<String> response = new DataResponse().success();
        response.setData(this.mealRecordService.countMealTotal((CountMealRecordRequest) super.getBaseRequest(request)));
        return response;
    }
}
