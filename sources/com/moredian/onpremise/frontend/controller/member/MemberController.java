package com.moredian.onpremise.frontend.controller.member;

import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.model.request.BatchInsertMemberRequest;
import com.moredian.onpremise.core.model.request.BatchUpdateMemberDeptRequest;
import com.moredian.onpremise.core.model.request.DeleteFaceRequest;
import com.moredian.onpremise.core.model.request.DeleteMemberRequest;
import com.moredian.onpremise.core.model.request.ExcelMemberRequest;
import com.moredian.onpremise.core.model.request.ExtractNoticeRequest;
import com.moredian.onpremise.core.model.request.ImageUploadBase64Request;
import com.moredian.onpremise.core.model.request.MemberDetailsRequest;
import com.moredian.onpremise.core.model.request.MemberFaceExportRequest;
import com.moredian.onpremise.core.model.request.MemberGroupListRequest;
import com.moredian.onpremise.core.model.request.MemberInputFaceRecordListRequest;
import com.moredian.onpremise.core.model.request.MemberListRequest;
import com.moredian.onpremise.core.model.request.QueryDeptAndMemberRequest;
import com.moredian.onpremise.core.model.request.QueryExtractFeatureResultRequest;
import com.moredian.onpremise.core.model.request.SaveMemberRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordRequest;
import com.moredian.onpremise.core.model.response.BatchInsertMemberResponse;
import com.moredian.onpremise.core.model.response.BatchUpdateMemberDeptResponse;
import com.moredian.onpremise.core.model.response.CommonResponse;
import com.moredian.onpremise.core.model.response.DataResponse;
import com.moredian.onpremise.core.model.response.ExcelMemberResponse;
import com.moredian.onpremise.core.model.response.ExtractNoticeResponse;
import com.moredian.onpremise.core.model.response.FaceReloadProgressResponse;
import com.moredian.onpremise.core.model.response.MemberDetailsResponse;
import com.moredian.onpremise.core.model.response.MemberGroupListResponse;
import com.moredian.onpremise.core.model.response.MemberInputFaceRecordListResponse;
import com.moredian.onpremise.core.model.response.MemberListResponse;
import com.moredian.onpremise.core.model.response.QueryDeptAndMemberResponse;
import com.moredian.onpremise.core.model.response.QueryExtractFeatureResultResponse;
import com.moredian.onpremise.core.model.response.SaveImageResponse;
import com.moredian.onpremise.core.model.response.SaveMemberResponse;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.frontend.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "on-premise-member", description = "on-premise成员相关")
@RestController
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/member/MemberController.class */
public class MemberController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MemberController.class);

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = {"/member/member/memberList"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "MemberListRequest", paramType = "body")})
    @ApiOperation(value = "成员列表", notes = "成员列表")
    public DataResponse<PageList<MemberListResponse>> memberList(@RequestBody MemberListRequest request) {
        MemberListRequest request2 = (MemberListRequest) super.getBaseRequest(request);
        DataResponse<PageList<MemberListResponse>> response = new DataResponse().success();
        response.setData(this.memberService.memberList(request2));
        return response;
    }

    @RequestMapping(value = {"/member/member/saveMember"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "保存成员")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "SaveMemberRequest", paramType = "body")})
    @ApiOperation(value = "保存成员", notes = "保存成员")
    public DataResponse<SaveMemberResponse> saveMember(@RequestBody SaveMemberRequest request) {
        SaveMemberRequest request2 = (SaveMemberRequest) super.getBaseRequest(request);
        DataResponse<SaveMemberResponse> response = new DataResponse().success();
        if (request2.getMemberId() != null && request2.getMemberId().longValue() > 0) {
            response.setData(this.memberService.updateMember(request2));
        } else {
            response.setData(this.memberService.insertMember(request2));
        }
        return response;
    }

    @RequestMapping(value = {"/member/member/deleteMember"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "删除成员")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteMemberRequest", paramType = "body")})
    @ApiOperation(value = "删除成员", notes = "删除成员")
    public CommonResponse deleteMember(@RequestBody DeleteMemberRequest request) {
        this.memberService.deleteMember((DeleteMemberRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/member/member/deleteMemberByJobNum"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "删除成员")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "DeleteMemberRequest", paramType = "body")})
    @ApiOperation(value = "按工号删除成员", notes = "按工号删除成员")
    public CommonResponse deleteMemberByJobNum(@RequestBody DeleteMemberRequest request) {
        this.memberService.deleteMemberByJobNum((DeleteMemberRequest) super.getBaseRequest(request));
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/member/member/getMemberDetails"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "MemberDetailsRequest", paramType = "body")})
    @ApiOperation(value = "获取成员详情", notes = "获取成员详情")
    public DataResponse<MemberDetailsResponse> getMemberDetails(@RequestBody MemberDetailsRequest request) {
        MemberDetailsRequest request2 = (MemberDetailsRequest) super.getBaseRequest(request);
        DataResponse<MemberDetailsResponse> response = new DataResponse().success();
        response.setData(this.memberService.getMemberDetails(request2));
        return response;
    }

    @RequestMapping(value = {"/member/member/getMemberDetailsByJobNum"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "MemberDetailsRequest", paramType = "body")})
    @ApiOperation(value = "根据工号获取成员详情", notes = "根据工号获取成员详情")
    public DataResponse<MemberDetailsResponse> getMemberDetailsByJobNum(@RequestBody MemberDetailsRequest request) {
        MemberDetailsRequest request2 = (MemberDetailsRequest) super.getBaseRequest(request);
        DataResponse<MemberDetailsResponse> response = new DataResponse().success();
        response.setData(this.memberService.getMemberDetailsByJobNum(request2));
        return response;
    }

    @RequestMapping(value = {"/member/dept/getDeptAndMember"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryDeptAndMemberRequest", paramType = "body")})
    @ApiOperation(value = "模糊搜索成员或部门", notes = "模糊搜索成员或部门")
    public DataResponse<List<QueryDeptAndMemberResponse>> getDeptAndMember(@RequestBody QueryDeptAndMemberRequest request) {
        QueryDeptAndMemberRequest request2 = (QueryDeptAndMemberRequest) super.getBaseRequest(request);
        DataResponse<List<QueryDeptAndMemberResponse>> response = new DataResponse().success();
        response.setData(this.memberService.getDeptAndMember(request2));
        return response;
    }

    @RequestMapping(value = {"/member/member/insertMemberBatch"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "批量添加成员")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "BatchInsertMemberRequest", paramType = "body")})
    @ApiOperation(value = "批量添加成员", notes = "批量添加成员")
    public DataResponse<List<BatchInsertMemberResponse>> batchInsertMember(@RequestBody BatchInsertMemberRequest request) {
        BatchInsertMemberRequest request2 = (BatchInsertMemberRequest) super.getBaseRequest(request);
        DataResponse<List<BatchInsertMemberResponse>> response = new DataResponse().success();
        response.setData(this.memberService.batchInsertMember(request2));
        return response;
    }

    @RequestMapping(value = {"/member/member/excelMember"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ExcelMemberRequest", paramType = "body")})
    @ApiOperation(value = "导出成员", notes = "导出成员")
    public DataResponse<PageList<ExcelMemberResponse>> excelMember(@RequestBody ExcelMemberRequest request) {
        ExcelMemberRequest request2 = (ExcelMemberRequest) super.getBaseRequest(request);
        DataResponse<PageList<ExcelMemberResponse>> response = new DataResponse().success();
        response.setData(this.memberService.excelMember(request2));
        return response;
    }

    @RequestMapping(value = {"/member/file/saveImage"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "上传人脸")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ImageUploadBase64Request", paramType = "body")})
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public DataResponse<SaveImageResponse> saveImage(@RequestBody ImageUploadBase64Request request) {
        logger.info("saveImage request :{} ,{} ,{},{},{}", request.getEndFlag(), request.getJobNum(), request.getMemberJobNum(), request.getNeedExtractFeature(), request.getNeedAutoSave());
        ImageUploadBase64Request request2 = (ImageUploadBase64Request) super.getBaseRequest(request);
        DataResponse<SaveImageResponse> response = new DataResponse().success();
        response.setData(this.memberService.saveMemberFaceImage(request2));
        return response;
    }

    @RequestMapping(value = {"/member/member/batchUpdateMemberDept"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "批量修改成员部门")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "BatchUpdateMemberDeptRequest", paramType = "body")})
    @ApiOperation(value = "批量修改成员部门", notes = "批量修改成员部门")
    public DataResponse<BatchUpdateMemberDeptResponse> batchUpdateMemberDept(@RequestBody BatchUpdateMemberDeptRequest request) {
        BatchUpdateMemberDeptRequest request2 = (BatchUpdateMemberDeptRequest) super.getBaseRequest(request);
        DataResponse<BatchUpdateMemberDeptResponse> response = new DataResponse().success();
        response.setData(this.memberService.batchUpdateMemberDept(request2));
        return response;
    }

    @RequestMapping(value = {"/member/member/queryExtractFeatureResult"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "QueryExtractFeatureResultRequest", paramType = "body")})
    @ApiOperation(value = "抽取特征值结果查询", notes = "抽取特征值结果查询")
    public DataResponse<QueryExtractFeatureResultResponse> queryExtractFeatureResult(@RequestBody QueryExtractFeatureResultRequest request) {
        QueryExtractFeatureResultRequest request2 = (QueryExtractFeatureResultRequest) super.getBaseRequest(request);
        DataResponse<QueryExtractFeatureResultResponse> response = new DataResponse().success();
        response.setData(this.memberService.queryExtractFeatureResult(request2));
        return response;
    }

    @RequestMapping(value = {"/member/member/extractNotice"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "通知到设备准备录脸")
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "ExtractNoticeRequest", paramType = "body")})
    @ApiOperation(value = "通知到设备准备录脸", notes = "通知到设备准备录脸")
    public DataResponse<ExtractNoticeResponse> extractNoticeDevice(@RequestBody ExtractNoticeRequest request) {
        ExtractNoticeRequest request2 = (ExtractNoticeRequest) super.getBaseRequest(request);
        DataResponse<ExtractNoticeResponse> response = new DataResponse().success();
        response.setData(this.memberService.extractNoticeDevice(request2));
        return response;
    }

    @RequestMapping(value = {"/member/face/export"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "MemberFaceExportRequest", paramType = "body")})
    @ApiOperation(value = "导出成员人脸", notes = "导出成员人脸")
    public DataResponse<String> faceExport(@RequestBody MemberFaceExportRequest request) {
        MemberFaceExportRequest request2 = (MemberFaceExportRequest) super.getBaseRequest(request);
        DataResponse<String> response = new DataResponse().success();
        response.setData(this.memberService.faceExport(request2));
        return response;
    }

    @RequestMapping(value = {"/member/face/reload"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "人脸重抽特征值")
    @ApiOperation(value = "人脸重抽特征值", notes = "人脸重抽特征值")
    public CommonResponse faceReload() {
        this.memberService.reloadMemberFeature();
        return new CommonResponse().success();
    }

    @RequestMapping(value = {"/member/face/reloadProgress"}, method = {RequestMethod.POST})
    @ApiOperation(value = "人脸重抽特征值进度", notes = "人脸重抽特征值进度")
    public DataResponse<FaceReloadProgressResponse> faceReloadProgress() {
        DataResponse<FaceReloadProgressResponse> response = new DataResponse().success();
        response.setData(this.memberService.faceReloadProgress());
        return response;
    }

    @RequestMapping(value = {"/member/face/delete"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "根据人员id删除人脸")
    @ApiOperation(value = "根据人员id删除人脸", notes = "删除人脸")
    public DataResponse<Boolean> deleteFace(@RequestBody DeleteFaceRequest request) {
        DeleteFaceRequest request2 = (DeleteFaceRequest) super.getBaseRequest(request);
        DataResponse<Boolean> response = new DataResponse().success();
        response.setData(Boolean.valueOf(this.memberService.deleteFace(request2)));
        return response;
    }

    @RequestMapping(value = {"/member/face/batchDelete"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "根据人员id批量删除人脸")
    @ApiOperation(value = "根据人员id删除人脸", notes = "批量删除人脸")
    public DataResponse<Boolean> batchDeleteFace(@RequestBody DeleteFaceRequest request) {
        DeleteFaceRequest request2 = (DeleteFaceRequest) super.getBaseRequest(request);
        DataResponse<Boolean> response = new DataResponse().success();
        response.setData(Boolean.valueOf(this.memberService.batchDeleteFace(request2)));
        return response;
    }

    @RequestMapping(value = {"/member/face/deleteByJobNum"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "根据人员工号删除人脸")
    @ApiOperation(value = "根据人员工号删除人脸", notes = "删除人脸")
    public DataResponse<Boolean> deleteFaceByJobNum(@RequestBody DeleteFaceRequest request) {
        DeleteFaceRequest request2 = (DeleteFaceRequest) super.getBaseRequest(request);
        DataResponse<Boolean> response = new DataResponse().success();
        response.setData(Boolean.valueOf(this.memberService.deleteFaceByJobNum(request2)));
        return response;
    }

    @RequestMapping(value = {"/member/face/batchDeleteByJobNum"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "根据人员工号批量删除人脸")
    @ApiOperation(value = "根据人员工号删除人脸", notes = "批量删除人脸")
    public DataResponse<Boolean> batchDeleteFaceByJobNum(@RequestBody DeleteFaceRequest request) {
        DeleteFaceRequest request2 = (DeleteFaceRequest) super.getBaseRequest(request);
        DataResponse<Boolean> response = new DataResponse().success();
        response.setData(Boolean.valueOf(this.memberService.batchDeleteFaceByJobNum(request2)));
        return response;
    }

    @RequestMapping(value = {"/member/face/replaceFaceByVerifyRecord"}, method = {RequestMethod.POST})
    @LogAnnotation(module = "成员相关", description = "根据识别记录抓拍照换人脸底库")
    @ApiOperation(value = "根据识别记录抓拍照换人脸底库", notes = "更换人脸底库")
    public DataResponse<Boolean> replaceFaceByVerifyRecord(@RequestBody VerifyRecordRequest request) {
        VerifyRecordRequest request2 = (VerifyRecordRequest) super.getBaseRequest(request);
        DataResponse<Boolean> response = new DataResponse().success();
        response.setData(Boolean.valueOf(this.memberService.replaceFaceByVerifyRecord(request2)));
        return response;
    }

    @RequestMapping(value = {"/member/group/list"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "MemberGroupListRequest", paramType = "body")})
    @ApiOperation(value = "成员权限列表", notes = "成员权限列表")
    public DataResponse<PageList<MemberGroupListResponse>> memberGroupList(@RequestBody MemberGroupListRequest request) {
        MemberGroupListRequest request2 = (MemberGroupListRequest) super.getBaseRequest(request);
        DataResponse<PageList<MemberGroupListResponse>> response = new DataResponse().success();
        response.setData(this.memberService.memberGroupList(request2));
        return response;
    }

    @RequestMapping(value = {"/member/inputFaceRecord/list"}, method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(name = "request", value = "请求实体", required = true, dataType = "MemberInputFaceRecordListRequest", paramType = "body")})
    @ApiOperation(value = "成员更改人脸记录列表", notes = "成员更改人脸记录列表")
    public DataResponse<PageList<MemberInputFaceRecordListResponse>> inputFaceRecordList(@RequestBody MemberInputFaceRecordListRequest request) {
        MemberInputFaceRecordListRequest request2 = (MemberInputFaceRecordListRequest) super.getBaseRequest(request);
        DataResponse<PageList<MemberInputFaceRecordListResponse>> response = new DataResponse().success();
        response.setData(this.memberService.inputFaceRecordList(request2));
        return response;
    }
}
