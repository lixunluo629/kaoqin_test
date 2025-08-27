package com.moredian.onpremise.frontend.controller.meal;

import com.moredian.onpremise.api.meal.MealMemberService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.BatchSaveMealMemberRequest;
import com.moredian.onpremise.core.model.request.BatchUpdateCardStatusRequest;
import com.moredian.onpremise.core.model.request.BindMealCardRequest;
import com.moredian.onpremise.core.model.request.ListMealMemberRequest;
import com.moredian.onpremise.core.model.request.MemberDetailsRequest;
import com.moredian.onpremise.core.model.request.SaveMealMemberRequest;
import com.moredian.onpremise.core.model.request.UnbindMealCardRequest;
import com.moredian.onpremise.core.model.request.UpdateCardStatusRequest;
import com.moredian.onpremise.core.model.response.BatchInsertMemberResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.ListMealMemberResponse;
import com.moredian.onpremise.core.model.response.MealMemberDetailResponse;
import com.moredian.onpremise.core.model.response.SaveMemberResponse;
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
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/meal/MealMemberController.class */
public class MealMemberController extends BaseController {

    @Autowired
    private MealMemberService mealMemberService;

    @RequestMapping(value = {"/meal/member/listMealMember"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListMealMemberRequest", paramType = "body")})
    @ApiOperation(value = "就餐成员列表", notes = "就餐成员列表")
    public DataResponse<PageList<ListMealMemberResponse>> listMealMember(@RequestBody ListMealMemberRequest request) {
        ListMealMemberRequest request2 = (ListMealMemberRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListMealMemberResponse>> response = new DataResponse().success();
        response.setData(this.mealMemberService.listMealTempMember(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/member/bindCard"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "绑定就餐卡")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "BindMealCardRequest", paramType = "body")})
    @ApiOperation(value = "绑定就餐卡", notes = "绑定就餐卡")
    public CommonResponse bindCard(@RequestBody BindMealCardRequest request) {
        this.mealMemberService.bindCard((BindMealCardRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/meal/member/unbindCard"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "解绑就餐卡")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "UnbindMealCardRequest", paramType = "body")})
    @ApiOperation(value = "解绑就餐卡", notes = "解绑就餐卡")
    public CommonResponse unbindCard(@RequestBody UnbindMealCardRequest request) {
        this.mealMemberService.unbindCard((UnbindMealCardRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/meal/member/updateCardStatus"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "修改就餐卡状态")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "UpdateCardStatusRequest", paramType = "body")})
    @ApiOperation(value = "修改就餐卡状态", notes = "修改就餐卡状态")
    public CommonResponse updateCardStatus(@RequestBody UpdateCardStatusRequest request) {
        this.mealMemberService.updateCardStatus((UpdateCardStatusRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/meal/member/batchUpdateCardStatus"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "批量修改就餐卡状态")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "BatchUpdateCardStatusRequest", paramType = "body")})
    @ApiOperation(value = "批量修改就餐卡状态", notes = "批量修改就餐卡状态")
    public CommonResponse batchUpdateCardStatus(@RequestBody BatchUpdateCardStatusRequest request) {
        this.mealMemberService.batchUpdateCardStatus((BatchUpdateCardStatusRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/meal/member/saveMealTempMember"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "保存临时工信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveMealMemberRequest", paramType = "body")})
    @ApiOperation(value = "保存临时工信息", notes = "保存临时工信息")
    public DataResponse<SaveMemberResponse> saveMealTempMember(@RequestBody SaveMealMemberRequest request) {
        SaveMealMemberRequest request2 = (SaveMealMemberRequest) super.getBaseRequest(request);
        DataResponse<SaveMemberResponse> response = new DataResponse().success();
        response.setData(this.mealMemberService.saveMealTempMember(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/member/batchSaveMealTempMember"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "团餐相关", description = "批量保存临时工信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "BatchSaveMealMemberRequest", paramType = "body")})
    @ApiOperation(value = "批量保存临时工信息", notes = "批量保存临时工信息")
    public DataResponse<List<BatchInsertMemberResponse>> batchSaveMealTempMember(@RequestBody BatchSaveMealMemberRequest request) {
        BatchSaveMealMemberRequest request2 = (BatchSaveMealMemberRequest) super.getBaseRequest(request);
        DataResponse<List<BatchInsertMemberResponse>> response = new DataResponse().success();
        response.setData(this.mealMemberService.batchSaveMealTempMember(request2));
        return response;
    }

    @RequestMapping(value = {"/meal/member/getMealMemberDetail"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "MemberDetailsRequest", paramType = "body")})
    @ApiOperation(value = "获取临时工信息", notes = "获取临时工信息")
    public DataResponse<MealMemberDetailResponse> getMealMemberDetail(@RequestBody MemberDetailsRequest request) {
        MemberDetailsRequest request2 = (MemberDetailsRequest) super.getBaseRequest(request);
        DataResponse<MealMemberDetailResponse> response = new DataResponse().success();
        response.setData(this.mealMemberService.getMealMemberDetail(request2));
        return response;
    }
}
