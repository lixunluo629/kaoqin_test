package com.moredian.onpremise.frontend.controller.meal;

import com.moredian.onpremise.api.meal.MealCardService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.CheckMemberHasCardRequest;
import com.moredian.onpremise.core.model.request.DeleteMealCardRequest;
import com.moredian.onpremise.core.model.request.ListMealCardRequest;
import com.moredian.onpremise.core.model.request.QueryMealCardDetailRequest;
import com.moredian.onpremise.core.model.request.SaveMealCardRequest;
import com.moredian.onpremise.core.model.request.SendCardToMemberRequest;
import com.moredian.onpremise.core.model.response.CheckMemberHasCardResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.ListMealCardResponse;
import com.moredian.onpremise.core.model.response.ListSearchMealCardResponse;
import com.moredian.onpremise.core.model.response.MealCardDetailResponse;
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

@Api(value = "on-premise-biz-meal", description = "on-premise团餐相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/meal/MealCardController.class */
public class MealCardController extends BaseController {

    @Autowired
    private MealCardService mealCardService;

    @RequestMapping(value = {"/meal/card/listMealCard"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListMealCardRequest", paramType = "body")})
    @ApiOperation(value = "就餐卡列表", notes = "就餐卡列表")
    public DataResponse<PageList<ListMealCardResponse>> listMealCard(@RequestBody ListMealCardRequest request) {
        ListMealCardRequest request2 = (ListMealCardRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListMealCardResponse>> response = new DataResponse().success();
        response.setData(this.mealCardService.listMealCard(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/card/listSearchMealCard"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListMealCardRequest", paramType = "body")})
    @ApiOperation(value = "就餐卡简单信息列表", notes = "就餐卡简单信息列表")
    public DataResponse<PageList<ListSearchMealCardResponse>> listSearchMealCard(@RequestBody ListMealCardRequest request) {
        ListMealCardRequest request2 = (ListMealCardRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListSearchMealCardResponse>> response = new DataResponse().success();
        response.setData(this.mealCardService.listSearchMealCard(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/card/insertMealCard"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "新增就餐卡")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveMealCardRequest", paramType = "body")})
    @ApiOperation(value = "新增就餐卡", notes = "新增就餐卡")
    public DataResponse<Long> insertMealCard(@RequestBody SaveMealCardRequest request) {
        SaveMealCardRequest request2 = (SaveMealCardRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.mealCardService.insert(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/card/updateMealCard"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "修改就餐卡信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveMealCardRequest", paramType = "body")})
    @ApiOperation(value = "修改就餐卡信息", notes = "修改就餐卡信息")
    public DataResponse<Long> updateMealCard(@RequestBody SaveMealCardRequest request) {
        SaveMealCardRequest request2 = (SaveMealCardRequest) super.getBaseRequest(request);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.mealCardService.update(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/card/deleteMealCard"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "删除就餐卡信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteMealCardRequest", paramType = "body")})
    @ApiOperation(value = "删除就餐卡信息", notes = "删除就餐卡信息")
    public CommonResponse deleteMealCard(@RequestBody DeleteMealCardRequest request) {
        this.mealCardService.delete((DeleteMealCardRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/meal/card/getDetailById"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryMealCardDetailRequest", paramType = "body")})
    @ApiOperation(value = "查询就餐卡详细信息", notes = "查询就餐卡详细信息")
    public DataResponse<MealCardDetailResponse> getDetailById(@RequestBody QueryMealCardDetailRequest request) {
        QueryMealCardDetailRequest request2 = (QueryMealCardDetailRequest) super.getBaseRequest(request);
        DataResponse<MealCardDetailResponse> response = new DataResponse().success();
        response.setData(this.mealCardService.getDetailById(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/card/sendCardToMember"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "给成员发放就餐卡")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SendCardToMemberRequest", paramType = "body")})
    @ApiOperation(value = "给成员发放就餐卡", notes = "给成员发放就餐卡")
    public CommonResponse sendCardToMember(@RequestBody SendCardToMemberRequest request) {
        this.mealCardService.sendCardToMember((SendCardToMemberRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/meal/card/checkMemberHasCard"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "CheckMemberHasCardRequest", paramType = "body")})
    @ApiOperation(value = "检查人员是否已经分配就餐卡", notes = "检查人员是否已经分配就餐卡")
    public DataResponse<List<CheckMemberHasCardResponse>> checkMemberHasCard(@RequestBody CheckMemberHasCardRequest request) {
        CheckMemberHasCardRequest request2 = (CheckMemberHasCardRequest) super.getBaseRequest(request);
        DataResponse<List<CheckMemberHasCardResponse>> response = new DataResponse().success();
        response.setData(this.mealCardService.checkMemberHasCard(request2));
        return response;
    }
}
