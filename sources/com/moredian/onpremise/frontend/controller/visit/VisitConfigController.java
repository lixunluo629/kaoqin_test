package com.moredian.onpremise.frontend.controller.visit;

import com.moredian.onpremise.api.visit.VisitConfigService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.aop.token.annotation.AppTokenAnnotation;
import com.moredian.onpremise.core.model.request.VisitConfigDeviceRequest;
import com.moredian.onpremise.core.model.request.VisitConfigQueryRequest;
import com.moredian.onpremise.core.model.request.VisitConfigRequest;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.VisitConfigResponse;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Api(value = "on-premise-biz-visit", description = "on-premise访客相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/visit/VisitConfigController.class */
public class VisitConfigController extends BaseController {

    @Autowired
    private VisitConfigService visitConfigService;

    @RequestMapping(value = {"/visit/config/query"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VisitConfigQueryRequest", paramType = "body")})
    @ApiOperation(value = "访客配置参数查询", notes = "访客配置参数查询")
    public DataResponse<VisitConfigResponse> queryConfig(@RequestBody VisitConfigQueryRequest request) {
        VisitConfigQueryRequest request2 = (VisitConfigQueryRequest) super.getBaseRequest(request);
        request2.setId(1L);
        DataResponse<VisitConfigResponse> response = new DataResponse().success();
        response.setData(this.visitConfigService.getOneVisitConfigById(request2.getOrgId(), request2.getId()));
        return response;
    }

    @RequestMapping(value = {"/visit/config/update"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "访客配置", description = "访客配置参数修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VisitConfigRequest", paramType = "body")})
    @ApiOperation(value = "访客配置参数修改", notes = "访客配置参数修改")
    public DataResponse<Long> updateConfig(@RequestBody VisitConfigRequest request) {
        VisitConfigRequest request2 = (VisitConfigRequest) super.getBaseRequest(request);
        request2.setId(1L);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.visitConfigService.updateVisitConfig(request2));
        return response;
    }

    @RequestMapping(value = {"/visit/config/device/update"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "访客配置", description = "访客配置关联设备修改")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "VisitConfigDeviceRequest", paramType = "body")})
    @ApiOperation(value = "访客配置关联设备修改", notes = "访客配置关联设备修改")
    public DataResponse<Long> updateConfigDevice(@RequestBody VisitConfigDeviceRequest request) {
        VisitConfigDeviceRequest request2 = (VisitConfigDeviceRequest) super.getBaseRequest(request);
        request2.setVisitConfigId(1L);
        DataResponse<Long> response = new DataResponse().success();
        response.setData(this.visitConfigService.updateVisitConfigDevice(request2));
        return response;
    }

    @RequestMapping(value = {"/visit/file/save"}, method = {RequestMethod.POST})
    @AppTokenAnnotation
    @ApiOperation(value = "上传附件", notes = "访客配置")
    public DataResponse<List<String>> saveVisitFile(HttpServletRequest request) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        DataResponse<List<String>> response = new DataResponse().success();
        response.setData(this.visitConfigService.uploadConfigFile(file));
        return response;
    }
}
