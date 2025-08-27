package com.moredian.onpremise.frontend.controller.terminal;

import com.moredian.onpremise.core.aop.token.annotation.AppTokenAnnotation;
import com.moredian.onpremise.core.model.request.TerminalSyncGroupMemberV2Request;
import com.moredian.onpremise.core.model.request.TerminalSyncMemberDetailV2Request;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAccountV2Response;
import com.moredian.onpremise.core.model.response.TerminalSyncDeptV2Response;
import com.moredian.onpremise.core.model.response.TerminalSyncGroupMemberV2Response;
import com.moredian.onpremise.core.model.response.TerminalSyncGroupV2Response;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberDetailV2Response;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberIdV2Response;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncV2Response;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-terminal", description = "on-premise终端调用接口相关,V2")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/terminal/TerminalSyncV2Controller.class */
public class TerminalSyncV2Controller extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TerminalController.class);

    @RequestMapping(value = {"/terminal/sync/account/v2"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "管理员同步，设备端调用", notes = "管理员同步，v2版")
    public DataResponse<List<TerminalSyncAccountV2Response>> syncAccountV2(@RequestBody TerminalSyncRequest request) {
        return null;
    }

    @RequestMapping(value = {"/terminal/sync/dept/v2"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "部门同步，设备端调用", notes = "部门同步，v2版")
    public DataResponse<List<TerminalSyncDeptV2Response>> syncDeptV2(@RequestBody TerminalSyncRequest request) {
        return null;
    }

    @RequestMapping(value = {"/terminal/sync/memberId/v2"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "成员id列表同步，设备端调用", notes = "成员id列表同步，v2版")
    public DataResponse<TerminalSyncV2Response<TerminalSyncMemberIdV2Response>> syncMemberIdV2(@RequestBody TerminalSyncRequest request) {
        return null;
    }

    @RequestMapping(value = {"/terminal/sync/memberDetail/v2"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncMemberDetailV2Request", paramType = "body")})
    @ApiOperation(value = "成员详情同步，设备端调用", notes = "成员详情同步，v2版")
    public DataResponse<List<TerminalSyncMemberDetailV2Response>> syncMemberDetailV2(@RequestBody TerminalSyncMemberDetailV2Request request) {
        return null;
    }

    @RequestMapping(value = {"/terminal/sync/group/v2"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncRequest", paramType = "body")})
    @ApiOperation(value = "权限组同步，设备端调用", notes = "权限组同步，v2版")
    public DataResponse<TerminalSyncV2Response<TerminalSyncGroupV2Response>> syncGroupV2(@RequestBody TerminalSyncRequest request) {
        return null;
    }

    @RequestMapping(value = {"/terminal/sync/groupMember/v2"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "TerminalSyncGroupMemberV2Request", paramType = "body")})
    @ApiOperation(value = "权限组关联成员/部门同步，设备端调用", notes = "权限组关联成员/部门同步，v2版")
    public DataResponse<TerminalSyncResponse<TerminalSyncGroupMemberV2Response>> syncGroupMemberV2(@RequestBody TerminalSyncGroupMemberV2Request request) {
        return null;
    }
}
