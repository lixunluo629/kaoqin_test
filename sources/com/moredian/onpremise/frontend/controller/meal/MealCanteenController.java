package com.moredian.onpremise.frontend.controller.meal;

import com.moredian.onpremise.api.meal.MealCanteenService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.DeleteMealCanteenRequest;
import com.moredian.onpremise.core.model.request.ListCanteenRequest;
import com.moredian.onpremise.core.model.request.QueryCanteenDetailsRequest;
import com.moredian.onpremise.core.model.request.SaveMealCanteenRequest;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.ListCanteenResponse;
import com.moredian.onpremise.core.model.response.QueryCanteenDetailsResponse;
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

@Api(value = "on-premise-biz-meal", description = "on-premise团餐相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/meal/MealCanteenController.class */
public class MealCanteenController extends BaseController {

    @Autowired
    private MealCanteenService canteenService;

    @RequestMapping(value = {"/meal/canteen/listCanteen"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListCanteenRequest", paramType = "body")})
    @ApiOperation(value = "餐厅列表", notes = "餐厅列表")
    public DataResponse<PageList<ListCanteenResponse>> listCanteen(@RequestBody ListCanteenRequest request) {
        ListCanteenRequest request2 = (ListCanteenRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListCanteenResponse>> response = new DataResponse().success();
        response.setData(this.canteenService.listCanteen(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/canteen/insertCanteen"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "新增餐厅")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveMealCanteenRequest", paramType = "body")})
    @ApiOperation(value = "新增餐厅", notes = "新增餐厅")
    public DataResponse<Long> insertCanteen(@RequestBody SaveMealCanteenRequest request) {
        SaveMealCanteenRequest request2 = (SaveMealCanteenRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.canteenService.insert(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/canteen/updateCanteen"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "修改餐厅信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveMealCanteenRequest", paramType = "body")})
    @ApiOperation(value = "修改餐厅信息", notes = "修改餐厅信息")
    public DataResponse<Long> updateCanteen(@RequestBody SaveMealCanteenRequest request) {
        SaveMealCanteenRequest request2 = (SaveMealCanteenRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.canteenService.update(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/canteen/deleteById"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "删除餐厅信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteMealCanteenRequest", paramType = "body")})
    @ApiOperation(value = "删除餐厅信息", notes = "删除餐厅信息")
    public CommonResponse deleteById(@RequestBody DeleteMealCanteenRequest request) {
        this.canteenService.deleteById((DeleteMealCanteenRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/meal/canteen/deleteByName"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "删除餐厅信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteMealCanteenRequest", paramType = "body")})
    @ApiOperation(value = "删除餐厅信息", notes = "删除餐厅信息")
    public CommonResponse deleteByName(@RequestBody DeleteMealCanteenRequest request) {
        this.canteenService.deleteByName((DeleteMealCanteenRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/meal/canteen/getDetailsById"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryCanteenDetailsRequest", paramType = "body")})
    @ApiOperation(value = "查询餐厅详细信息", notes = "查询餐厅详细信息")
    public DataResponse<QueryCanteenDetailsResponse> getDetailsById(@RequestBody QueryCanteenDetailsRequest request) {
        QueryCanteenDetailsRequest request2 = (QueryCanteenDetailsRequest) super.getBaseRequest(request);
        DataResponse<QueryCanteenDetailsResponse> response = new DataResponse().success();
        response.setData(this.canteenService.getDetailsById(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/canteen/getDetailsByName"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryCanteenDetailsRequest", paramType = "body")})
    @ApiOperation(value = "查询餐厅详细信息", notes = "查询餐厅详细信息")
    public DataResponse<QueryCanteenDetailsResponse> getDetailsByName(@RequestBody QueryCanteenDetailsRequest request) {
        QueryCanteenDetailsRequest request2 = (QueryCanteenDetailsRequest) super.getBaseRequest(request);
        DataResponse<QueryCanteenDetailsResponse> response = new DataResponse().success();
        response.setData(this.canteenService.getDetailsByName(request2));
        return response;
    }
}
