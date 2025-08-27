package com.moredian.onpremise.api.member;

import com.moredian.onpremise.core.model.request.BatchInsertMemberRequest;
import com.moredian.onpremise.core.model.request.BatchUpdateMemberDeptRequest;
import com.moredian.onpremise.core.model.request.DeleteFaceRequest;
import com.moredian.onpremise.core.model.request.DeleteMemberRequest;
import com.moredian.onpremise.core.model.request.ExcelMemberRequest;
import com.moredian.onpremise.core.model.request.ExtractNoticeRequest;
import com.moredian.onpremise.core.model.request.H5CertifyMemberRequest;
import com.moredian.onpremise.core.model.request.ImageUploadBase64Request;
import com.moredian.onpremise.core.model.request.MatchFeatureRequest;
import com.moredian.onpremise.core.model.request.MemberDetailsRequest;
import com.moredian.onpremise.core.model.request.MemberFaceExportRequest;
import com.moredian.onpremise.core.model.request.MemberGroupListRequest;
import com.moredian.onpremise.core.model.request.MemberInputFaceRecordListRequest;
import com.moredian.onpremise.core.model.request.MemberListRequest;
import com.moredian.onpremise.core.model.request.QueryDeptAndMemberRequest;
import com.moredian.onpremise.core.model.request.QueryExtractFeatureResultRequest;
import com.moredian.onpremise.core.model.request.SaveMemberFaceRequest;
import com.moredian.onpremise.core.model.request.SaveMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalFindMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalSaveMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalSearchMemberRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncEigenvalueValueRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordRequest;
import com.moredian.onpremise.core.model.response.BatchInsertMemberResponse;
import com.moredian.onpremise.core.model.response.BatchUpdateMemberDeptResponse;
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
import com.moredian.onpremise.core.model.response.SaveMemberFaceByDeviceResponse;
import com.moredian.onpremise.core.model.response.SaveMemberFaceResponse;
import com.moredian.onpremise.core.model.response.SaveMemberResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncEigenvalueValueResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/member/MemberService.class */
public interface MemberService {
    PageList<MemberListResponse> memberList(MemberListRequest memberListRequest);

    SaveMemberResponse updateMember(SaveMemberRequest saveMemberRequest);

    SaveMemberResponse insertMember(SaveMemberRequest saveMemberRequest);

    boolean deleteMember(DeleteMemberRequest deleteMemberRequest);

    boolean deleteMemberByJobNum(DeleteMemberRequest deleteMemberRequest);

    MemberDetailsResponse getMemberDetails(MemberDetailsRequest memberDetailsRequest);

    MemberDetailsResponse getMemberDetailsByJobNum(MemberDetailsRequest memberDetailsRequest);

    SaveImageResponse saveMemberFaceImage(MultipartFile multipartFile);

    SaveImageResponse saveMemberFaceImage(ImageUploadBase64Request imageUploadBase64Request);

    ExtractNoticeResponse extractNoticeDevice(ExtractNoticeRequest extractNoticeRequest);

    SaveMemberFaceByDeviceResponse saveMemberFaceByDevice(SaveMemberFaceRequest saveMemberFaceRequest);

    MemberDetailsResponse getMemberByMemberNumbers(TerminalFindMemberRequest terminalFindMemberRequest);

    TerminalSyncResponse<TerminalSyncMemberResponse> syncMember(TerminalSyncRequest terminalSyncRequest);

    List<TerminalSyncEigenvalueValueResponse> syncMemberEigenvalueValue(TerminalSyncEigenvalueValueRequest terminalSyncEigenvalueValueRequest);

    List<QueryDeptAndMemberResponse> getDeptAndMember(QueryDeptAndMemberRequest queryDeptAndMemberRequest);

    List<BatchInsertMemberResponse> batchInsertMember(BatchInsertMemberRequest batchInsertMemberRequest);

    PageList<ExcelMemberResponse> excelMember(ExcelMemberRequest excelMemberRequest);

    BatchUpdateMemberDeptResponse batchUpdateMemberDept(BatchUpdateMemberDeptRequest batchUpdateMemberDeptRequest);

    QueryExtractFeatureResultResponse queryExtractFeatureResult(QueryExtractFeatureResultRequest queryExtractFeatureResultRequest);

    boolean reloadMemberFeature();

    boolean loadAllMemberFeature(Long l);

    boolean matchFeature(MatchFeatureRequest matchFeatureRequest);

    Set<Long> getMemberListByDept(Long l, Long l2, Set<Long> set);

    List<String> listFaceUrl(Long l);

    String faceExport(MemberFaceExportRequest memberFaceExportRequest);

    Long saveMemberByDevice(TerminalSaveMemberRequest terminalSaveMemberRequest);

    boolean deleteFace(DeleteFaceRequest deleteFaceRequest);

    boolean batchDeleteFace(DeleteFaceRequest deleteFaceRequest);

    boolean deleteFaceByJobNum(DeleteFaceRequest deleteFaceRequest);

    boolean batchDeleteFaceByJobNum(DeleteFaceRequest deleteFaceRequest);

    FaceReloadProgressResponse faceReloadProgress();

    SaveMemberFaceResponse saveMemberFace(SaveMemberFaceRequest saveMemberFaceRequest);

    boolean replaceFaceByVerifyRecord(VerifyRecordRequest verifyRecordRequest);

    PageList<MemberGroupListResponse> memberGroupList(MemberGroupListRequest memberGroupListRequest);

    MemberDetailsResponse getMemberByIdentityCard(TerminalSearchMemberRequest terminalSearchMemberRequest);

    PageList<MemberInputFaceRecordListResponse> inputFaceRecordList(MemberInputFaceRecordListRequest memberInputFaceRecordListRequest);

    MemberDetailsResponse certifyMember(H5CertifyMemberRequest h5CertifyMemberRequest);
}
