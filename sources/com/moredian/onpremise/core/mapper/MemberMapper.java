package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.GroupMember;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.request.ListMealMemberRequest;
import com.moredian.onpremise.core.model.request.MemberFaceExportRequest;
import com.moredian.onpremise.core.model.request.MemberGroupListRequest;
import com.moredian.onpremise.core.model.request.MemberListRequest;
import com.moredian.onpremise.core.model.request.SaveAccountRequest;
import com.moredian.onpremise.core.model.request.SaveMemberFaceRequest;
import com.moredian.onpremise.core.model.response.DeptMemberListResponse;
import com.moredian.onpremise.core.model.response.ExcelMemberResponse;
import com.moredian.onpremise.core.model.response.ListMealMemberResponse;
import com.moredian.onpremise.core.model.response.MemberGroupListResponse;
import com.moredian.onpremise.core.model.response.MemberListResponse;
import com.moredian.onpremise.core.model.response.QueryDeptAndMemberResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncEigenvalueValueResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MemberMapper.class */
public interface MemberMapper {
    int unbindAccount(@Param("memberId") Long l);

    Member getMemberInfoByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    int deleteMember(@Param("memberId") Long l, @Param("orgId") Long l2);

    int saveMember(Member member);

    int addMember(Member member);

    int bindAccount(SaveAccountRequest saveAccountRequest);

    Member getMemberInfoByMobile(@Param("mobile") String str, @Param("orgId") Long l);

    Member getMemberInfoByJobNum(@Param("memberJobNum") String str, @Param("orgId") Long l);

    Member getMemberInfoByMd5(@Param("faceMd5") String str, @Param("orgId") Long l);

    int saveMemberFace(SaveMemberFaceRequest saveMemberFaceRequest);

    List<MemberListResponse> memberList(MemberListRequest memberListRequest);

    int updateDept(@Param("memberId") Long l, @Param("oldDeptId") Long l2, @Param("newDeptIds") String str, @Param("orgId") Long l3);

    List<TerminalSyncMemberResponse> listSyncMember(@Param("orgId") Long l, @Param("date") String str, @Param("deviceSn") String str2);

    List<TerminalSyncEigenvalueValueResponse> listSyncMemberEigenvalueValue(@Param("orgId") Long l, @Param("memberIds") List<Long> list);

    List<TerminalSyncMemberResponse> listRelatedSyncMember(@Param("orgId") Long l);

    TerminalSyncMemberResponse getSyncMember(@Param("orgId") Long l, @Param("memberId") Long l2);

    List<QueryDeptAndMemberResponse> getMemberByName(@Param("orgId") Long l, @Param("name") String str, @Param("manageDeptIds") List<String> list);

    List<QueryDeptAndMemberResponse> getMemberByNameOrJobNum(@Param("orgId") Long l, @Param("keywords") String str, @Param("manageDeptIds") List<String> list);

    int countByDeptId(@Param("orgId") Long l, @Param("deptId") Long l2);

    int countByDeptIdAndDeptIds(@Param("orgId") Long l, @Param("deptId") Long l2, @Param("deptIds") List<Long> list);

    List<DeptMemberListResponse> listMemberByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<Long> listMemberIdByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    Integer countMemberByDepts(@Param("deptIds") List<Long> list, @Param("memberIds") List<Long> list2, @Param("orgId") Long l);

    List<ExcelMemberResponse> excelMember(@Param("orgId") Long l, @Param("manageDeptIds") List<String> list);

    Integer countAllMembers(@Param("orgId") Long l);

    List<Member> getAllMembers(@Param("orgId") Long l);

    Member getLastModify(@Param("orgId") Long l);

    List<TerminalSyncMemberResponse> getSyncMembers(@Param("members") List<GroupMember> list, @Param("orgId") Long l);

    List<TerminalSyncMemberResponse> getSyncManagerMember(@Param("accounts") List<Account> list, @Param("orgId") Long l);

    List<Member> listHasFeatureMember(@Param("notMemberId") Long l);

    List<Member> listMemberByMemberIds(@Param("memberIds") List<Long> list, @Param("orgId") Long l);

    List<Long> listOtherMemberIdByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("memberId") Long l3);

    List<ListMealMemberResponse> listMealMember(ListMealMemberRequest listMealMemberRequest);

    Member getMemberByManageDeptId(@Param("manageDeptId") List<String> list, @Param("memberId") Long l, @Param("orgId") Long l2);

    List<String> listFaceUrl(@Param("orgId") Long l);

    int updateModifyTime(@Param("orgId") Long l, @Param("memberIdList") List<Long> list, @Param("deptIdList") List<Long> list2);

    List<Member> faceExport(MemberFaceExportRequest memberFaceExportRequest);

    List<Member> listMember(@Param("orgId") Long l, @Param("memberIds") List<Long> list, @Param("keywords") String str);

    List<Member> listMemberByJobNum(@Param("orgId") Long l, @Param("memberJobNums") List<String> list);

    int deleteFace(@Param("orgId") Long l, @Param("memberId") Long l2, @Param("memberIds") List<Long> list);

    int deleteFaceByJobNum(@Param("orgId") Long l, @Param("memberJobNum") String str, @Param("memberJobNums") List<String> list);

    Member getMemberInfoByName(@Param("memberName") String str, @Param("orgId") Long l);

    List<MemberGroupListResponse> memberGroupList(MemberGroupListRequest memberGroupListRequest);

    Member getMemberInfoByIdentityCard(@Param("identityCard") String str, @Param("orgId") Long l);

    Member getMemberInfoByRemark(@Param("remark") String str, @Param("orgId") Long l);
}
