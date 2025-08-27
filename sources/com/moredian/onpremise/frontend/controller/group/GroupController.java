package com.moredian.onpremise.frontend.controller.group;

import com.moredian.onpremise.api.group.GroupService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.DeleteGroupAuthRequest;
import com.moredian.onpremise.core.model.request.GetGroupAuthOneRequest;
import com.moredian.onpremise.core.model.request.GroupAuthListRequest;
import com.moredian.onpremise.core.model.request.SaveGroupAuthRequest;
import com.moredian.onpremise.core.model.request.UpdateGroupMemberRequest;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.GroupAuthListResponse;
import com.moredian.onpremise.core.model.response.GroupAuthResponse;
import com.moredian.onpremise.core.model.response.GroupListResponse;
import com.moredian.onpremise.core.model.response.SaveGroupAuthResponse;
import com.moredian.onpremise.core.model.response.UpdateGroupMemberResponse;
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

@Api(value = "on-premise-group", description = "on-premise权限组相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/group/GroupController.class */
public class GroupController extends BaseController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = {"/group/auth/saveGroupAuth"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "权限组相关", description = "权限组保存")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveGroupAuthRequest", paramType = "body")})
    @ApiOperation(value = "权限组保存", notes = "权限组保存")
    public DataResponse<SaveGroupAuthResponse> saveGroupAuth(@RequestBody SaveGroupAuthRequest request) {
        SaveGroupAuthRequest request2 = (SaveGroupAuthRequest) super.getBaseRequest(request);
        DataResponse<SaveGroupAuthResponse> response = new DataResponse().success();
        request2.setAllMemberFlag(0);
        if (request2.getGroupId() != null && request2.getGroupId().longValue() > 0) {
            response.setData(this.groupService.updateGroup(request2));
        } else {
            response.setData(this.groupService.insertGroup(request2));
        }
        return response;
    }

    @RequestMapping(value = {"/group/auth/deleteGroupAuth"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "权限组相关", description = "权限组删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteGroupAuthRequest", paramType = "body")})
    @ApiOperation(value = "权限组删除", notes = "权限组删除")
    public CommonResponse deleteGroupAuth(@RequestBody DeleteGroupAuthRequest request) {
        this.groupService.deleteGroup((DeleteGroupAuthRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/group/auth/groupAuthList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "GroupAuthListRequest", paramType = "body")})
    @ApiOperation(value = "权限组列表", notes = "权限组列表")
    public DataResponse<PageList<GroupAuthListResponse>> groupAuthList(@RequestBody GroupAuthListRequest request) {
        GroupAuthListRequest request2 = (GroupAuthListRequest) super.getBaseRequest(request);
        DataResponse<PageList<GroupAuthListResponse>> response = new DataResponse().success();
        response.setData(this.groupService.groupAuthList(request2));
        return response;
    }

    @RequestMapping(value = {"/group/auth/getGroupAuthById"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "GetGroupAuthOneRequest", paramType = "body")})
    @ApiOperation(value = "根据组id查询权限组详情", notes = "根据组id查询权限组详情")
    public DataResponse<GroupAuthResponse> getGroupAuthById(@RequestBody GetGroupAuthOneRequest request) {
        GetGroupAuthOneRequest request2 = (GetGroupAuthOneRequest) super.getBaseRequest(request);
        DataResponse<GroupAuthResponse> response = new DataResponse().success();
        response.setData(this.groupService.getGroupAuthById(request2));
        return response;
    }

    @RequestMapping(value = {"/group/groupList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = false, dataType = "GroupAuthListRequest", paramType = "body")})
    @ApiOperation(value = "权限组列表", notes = "权限组列表")
    public DataResponse<PageList<GroupListResponse>> groupList(@RequestBody GroupAuthListRequest request) {
        GroupAuthListRequest request2 = (GroupAuthListRequest) super.getBaseRequest(request);
        DataResponse<PageList<GroupListResponse>> response = new DataResponse().success();
        response.setData(this.groupService.groupList(request2));
        return response;
    }

    @RequestMapping(value = {"/group/member/updateMember"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "权限组相关", description = "权限组修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "UpdateGroupMemberRequest", paramType = "body")})
    @ApiOperation(value = "权限组修改成员列表", notes = "权限组修改成员列表")
    public DataResponse<UpdateGroupMemberResponse> updateMember(@RequestBody UpdateGroupMemberRequest request) {
        UpdateGroupMemberRequest request2 = (UpdateGroupMemberRequest) super.getBaseRequest(request);
        DataResponse<UpdateGroupMemberResponse> response = new DataResponse().success();
        response.setData(this.groupService.updateGroupMember(request2));
        return response;
    }
}
