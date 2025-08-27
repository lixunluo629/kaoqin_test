package com.moredian.onpremise.frontend.controller.h5;

import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.model.request.H5CertifyMemberRequest;
import com.moredian.onpremise.core.model.request.ImageUploadBase64Request;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.MemberDetailsResponse;
import com.moredian.onpremise.core.model.response.SaveImageResponse;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-h5-member", description = "on-premise-h5页面成员相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/h5/MemberH5Controller.class */
public class MemberH5Controller extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MemberH5Controller.class);

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = {"/h5/certify/member"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "H5CertifyMemberRequest", paramType = "body")})
    @ApiOperation(value = "实名认证员工，h5端调用", notes = "实名认证员工")
    public DataResponse<MemberDetailsResponse> certifyMember(@RequestBody H5CertifyMemberRequest request) {
        logger.info("certifyMember request :{}", JsonUtils.toJson(request));
        DataResponse<MemberDetailsResponse> response = new DataResponse<>();
        response.setData(this.memberService.certifyMember(request));
        return response;
    }

    @RequestMapping(value = {"/h5/saveImage"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ImageUploadBase64Request", paramType = "body")})
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public DataResponse<SaveImageResponse> saveImage(@RequestBody ImageUploadBase64Request request) {
        logger.info("saveImage request :{} ,{} ,{},{},{}", request.getEndFlag(), request.getJobNum(), request.getMemberJobNum(), request.getNeedExtractFeature(), request.getNeedAutoSave());
        DataResponse<SaveImageResponse> response = new DataResponse().success();
        request.setOrgId(1L);
        response.setData(this.memberService.saveMemberFaceImage(request));
        return response;
    }
}
