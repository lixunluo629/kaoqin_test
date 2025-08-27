package com.moredian.onpremise.frontend.controller.member;

import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.DeleteDeptRequest;
import com.moredian.onpremise.core.model.request.DeptDetailRequest;
import com.moredian.onpremise.core.model.request.ListChildImmediateDeptRequest;
import com.moredian.onpremise.core.model.request.ListDeptRequest;
import com.moredian.onpremise.core.model.request.SaveDeptRequest;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.DeptDetailResponse;
import com.moredian.onpremise.core.model.response.ListChildImmediateDeptResponse;
import com.moredian.onpremise.core.model.response.ListDeptNoConstructureResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponseV2;
import com.moredian.onpremise.core.model.response.SaveDeptResponse;
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

@Api(value = "on-premise-dept", description = "on-premise部门相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/member/DeptController.class */
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    @RequestMapping(value = {"/dept/dept/saveDept"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "部门相关", description = "保存部门")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveDeptRequest", paramType = "body")})
    @ApiOperation(value = "保存部门", notes = "保存部门")
    public DataResponse<SaveDeptResponse> saveDept(@RequestBody SaveDeptRequest request) {
        SaveDeptResponse dataResponse;
        SaveDeptRequest request2 = (SaveDeptRequest) super.getBaseRequest(request);
        DataResponse<SaveDeptResponse> response = new DataResponse().success();
        if (request2.getDeptId() != null && request2.getDeptId().longValue() > 0) {
            dataResponse = this.deptService.update(request2);
        } else {
            dataResponse = this.deptService.insert(request2);
        }
        response.setData(dataResponse);
        return response;
    }

    @RequestMapping(value = {"/dept/dept/getDeptDetail"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeptDetailRequest", paramType = "body")})
    @ApiOperation(value = "部门详情", notes = "部门详情")
    public DataResponse<DeptDetailResponse> getDeptDetail(@RequestBody DeptDetailRequest request) {
        DeptDetailRequest request2 = (DeptDetailRequest) super.getBaseRequest(request);
        DataResponse<DeptDetailResponse> response = new DataResponse().success();
        response.setData(this.deptService.getDeptDetail(request2));
        return response;
    }

    @RequestMapping(value = {"/dept/dept/deleteDept"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "部门相关", description = "删除部门")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteDeptRequest", paramType = "body")})
    @ApiOperation(value = "删除部门", notes = "删除部门")
    public CommonResponse deleteDept(@RequestBody DeleteDeptRequest request) {
        this.deptService.deleteDept((DeleteDeptRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/dept/dept/listDept"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListDeptRequest", paramType = "body")})
    @ApiOperation(value = "部门列表", notes = "部门列表")
    public DataResponse<List<ListDeptResponse>> listDept(@RequestBody ListDeptRequest request) {
        ListDeptRequest request2 = (ListDeptRequest) super.getBaseRequest(request);
        DataResponse<List<ListDeptResponse>> response = new DataResponse().success();
        response.setData(this.deptService.listDept(request2));
        return response;
    }

    @RequestMapping(value = {"/dept/dept/listChildImmediateDept"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListChildImmediateDeptRequest", paramType = "body")})
    @ApiOperation(value = "获取直属子部门列表", notes = "获取直属子部门列表")
    public DataResponse<PageList<ListChildImmediateDeptResponse>> listChildImmediateDept(@RequestBody ListChildImmediateDeptRequest request) {
        ListChildImmediateDeptRequest request2 = (ListChildImmediateDeptRequest) super.getBaseRequest(request);
        DataResponse<PageList<ListChildImmediateDeptResponse>> response = new DataResponse().success();
        response.setData(this.deptService.listChildImmediateDept(request2));
        return response;
    }

    @RequestMapping(value = {"/dept/dept/listDeptNoConstructure"}, method = {RequestMethod.POST})
    @ApiOperation(value = "不包含组织架构部门列表", notes = "不包含组织架构部门列表")
    public DataResponse<List<ListDeptNoConstructureResponse>> listDeptNoConstructure() {
        DataResponse<List<ListDeptNoConstructureResponse>> response = new DataResponse().success();
        response.setData(this.deptService.listDeptNoConstructure(super.getBaseRequest(new BaseRequest())));
        return response;
    }

    @RequestMapping(value = {"/dept/dept/listDept/v2"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ListDeptRequest", paramType = "body")})
    @ApiOperation(value = "部门列表", notes = "部门列表")
    public DataResponse<List<ListDeptResponseV2>> listDeptV2(@RequestBody ListDeptRequest request) {
        ListDeptRequest request2 = (ListDeptRequest) super.getBaseRequest(request);
        DataResponse<List<ListDeptResponseV2>> response = new DataResponse().success();
        response.setData(this.deptService.listDeptV2(request2));
        return response;
    }
}
