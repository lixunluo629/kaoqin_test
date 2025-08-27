package com.moredian.onpremise.meal.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.meal.MealMemberService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.MealCardStatusEnum;
import com.moredian.onpremise.core.common.enums.MemberCardStatusEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.MealCardMapper;
import com.moredian.onpremise.core.mapper.MealCardMemberMapper;
import com.moredian.onpremise.core.mapper.MealMemberMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.MealCardMember;
import com.moredian.onpremise.core.model.domain.MealMember;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.info.MealCardMemberInfo;
import com.moredian.onpremise.core.model.request.BatchSaveMealMemberRequest;
import com.moredian.onpremise.core.model.request.BatchUpdateCardStatusRequest;
import com.moredian.onpremise.core.model.request.BindMealCardRequest;
import com.moredian.onpremise.core.model.request.ListMealMemberRequest;
import com.moredian.onpremise.core.model.request.MemberDetailsRequest;
import com.moredian.onpremise.core.model.request.SaveMealMemberRequest;
import com.moredian.onpremise.core.model.request.SaveMemberRequest;
import com.moredian.onpremise.core.model.request.UnbindMealCardRequest;
import com.moredian.onpremise.core.model.request.UpdateCardStatusRequest;
import com.moredian.onpremise.core.model.response.BatchInsertMemberResponse;
import com.moredian.onpremise.core.model.response.ListMealMemberResponse;
import com.moredian.onpremise.core.model.response.MealMemberDetailResponse;
import com.moredian.onpremise.core.model.response.MemberDetailsDeptResponse;
import com.moredian.onpremise.core.model.response.SaveMemberResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
/* loaded from: onpremise-meal-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/meal/impl/MealMemberServiceImpl.class */
public class MealMemberServiceImpl implements MealMemberService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MealMemberServiceImpl.class);

    @Autowired
    private DeptService deptService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MealCardMemberMapper cardMemberMapper;

    @Autowired
    private MealCardMapper cardMapper;

    @Autowired
    private MealMemberMapper mealMemberMapper;

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    public PageList<ListMealMemberResponse> listMealMember(ListMealMemberRequest request) {
        request.setManagerDeptId(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<ListMealMemberResponse> listResp = this.memberMapper.listMealMember(request);
            packageMealMemberResponse(listResp, request.getOrgId());
            return new PageList<>(listResp);
        }
        List<ListMealMemberResponse> listResp2 = this.memberMapper.listMealMember(request);
        packageMealMemberResponse(listResp2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(listResp2), listResp2);
    }

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    @Transactional(rollbackFor = {Exception.class})
    public boolean unbindCard(UnbindMealCardRequest request) {
        checkAccountIsAllow(request.getLoginAccountId(), request.getOrgId(), request.getMemberId(), request.getSessionId());
        AssertUtil.isTrue(Boolean.valueOf(this.cardMemberMapper.deleteCardByMember(request.getMemberId(), request.getOrgId(), null) > 0), OnpremiseErrorEnum.DELETE_CARD_MEMBER_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    @Transactional(rollbackFor = {Exception.class})
    public boolean bindCard(BindMealCardRequest request) {
        checkAccountIsAllow(request.getLoginAccountId(), request.getOrgId(), request.getMemberId(), request.getSessionId());
        this.cardMemberMapper.deleteCardByMember(request.getMemberId(), request.getOrgId(), null);
        AssertUtil.isTrue(Boolean.valueOf(this.cardMemberMapper.insert(getMealCardMember(request.getMemberId(), request.getOrgId(), request.getMealCardId())) > 0), OnpremiseErrorEnum.INSERT_CARD_MEMBER_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateCardStatus(UpdateCardStatusRequest request) {
        AssertUtil.isNullOrEmpty(request.getCardStatus(), OnpremiseErrorEnum.MEMBER_CARD_STATUS_MUST_NOT_NULL);
        MemberCardStatusEnum cardStatusEnum = MemberCardStatusEnum.getByValue(request.getCardStatus().intValue());
        AssertUtil.isNullOrEmpty(cardStatusEnum, OnpremiseErrorEnum.MEMBER_CARD_STATUS_INVALID);
        checkAccountIsAllow(request.getLoginAccountId(), request.getOrgId(), request.getMemberId(), request.getSessionId());
        MealCardMemberInfo cardMemberInfo = this.cardMemberMapper.getOneByMemberId(request.getMemberId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(cardMemberInfo, OnpremiseErrorEnum.MEMBER_CARD_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(this.cardMemberMapper.updateCardStatus(request.getMemberId(), request.getOrgId(), request.getCardStatus()) > 0), OnpremiseErrorEnum.UPDATE_MEMBER_CARD_STATUS_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    @Transactional(rollbackFor = {Exception.class})
    public boolean batchUpdateCardStatus(BatchUpdateCardStatusRequest request) throws BeansException {
        if (request.getMealCardId() != null && request.getMealCardId().longValue() == 0) {
            for (Long memberId : request.getMemberIds()) {
                UnbindMealCardRequest eachUnbindMealCardRequest = new UnbindMealCardRequest();
                BeanUtils.copyProperties(request, eachUnbindMealCardRequest);
                eachUnbindMealCardRequest.setMemberId(memberId);
                unbindCard(eachUnbindMealCardRequest);
            }
            return true;
        }
        for (Long memberId2 : request.getMemberIds()) {
            if (request.getMealCardId() != null) {
                BindMealCardRequest eachBindMealCardRequest = new BindMealCardRequest();
                BeanUtils.copyProperties(request, eachBindMealCardRequest);
                eachBindMealCardRequest.setMemberId(memberId2);
                bindCard(eachBindMealCardRequest);
            }
            UpdateCardStatusRequest eachUpdateCardStatusRequest = new UpdateCardStatusRequest();
            BeanUtils.copyProperties(request, eachUpdateCardStatusRequest);
            eachUpdateCardStatusRequest.setMemberId(memberId2);
            updateCardStatus(eachUpdateCardStatusRequest);
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    @Transactional(rollbackFor = {Exception.class})
    public SaveMemberResponse saveMealTempMember(SaveMealMemberRequest request) throws BeansException {
        SaveMemberResponse memberResp;
        SaveMemberRequest saveMember = new SaveMemberRequest();
        if (request.getMemberType() == null) {
            request.setMemberType(1);
        }
        if (StringUtils.isEmpty(request.getDeptId())) {
            List<String> managerDepts = UserLoginResponse.getAccountManageDeptId(request.getSessionId());
            request.setDeptId(CollectionUtils.isEmpty(managerDepts) ? this.deptMapper.getTopDept(Constants.ONE_LONG).getDeptId().toString() : managerDepts.get(0));
        }
        BeanUtils.copyProperties(request, saveMember);
        if (StringUtils.isEmpty(request.getMemberWorkNum())) {
            request.setMemberWorkNum(Constants.PRE_JOB_NUM + System.currentTimeMillis());
        }
        saveMember.setRemark(request.getMemberWorkNum());
        if (StringUtils.isEmpty(request.getMemberJobNum())) {
            saveMember.setMemberJobNum(request.getMemberWorkNum());
            memberResp = this.memberService.insertMember(saveMember);
            request.setMemberJobNum(saveMember.getMemberJobNum());
        } else {
            Member member = this.memberMapper.getMemberInfoByJobNum(request.getMemberJobNum(), request.getOrgId());
            saveMember.setMemberId(member.getMemberId());
            memberResp = this.memberService.updateMember(saveMember);
        }
        MealMember mealMember = this.mealMemberMapper.getMealMemberByMemberId(memberResp.getMemberId());
        if (mealMember == null) {
            MealMember mealMember2 = new MealMember();
            BeanUtils.copyProperties(request, mealMember2);
            mealMember2.setMemberId(memberResp.getMemberId());
            this.mealMemberMapper.insert(mealMember2);
        } else {
            BeanUtils.copyProperties(request, mealMember);
            this.mealMemberMapper.update(mealMember);
        }
        if (request.getCardId() != null && request.getCardId().longValue() != 0) {
            BindMealCardRequest cardMemberRequest = new BindMealCardRequest();
            BeanUtils.copyProperties(request, cardMemberRequest);
            cardMemberRequest.setMemberId(memberResp.getMemberId());
            cardMemberRequest.setMealCardId(request.getCardId());
            if (this.cardMapper.getOneById(request.getCardId(), request.getOrgId()) != null) {
                bindCard(cardMemberRequest);
            }
        }
        return memberResp;
    }

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    public MealMemberDetailResponse getMealMemberDetail(MemberDetailsRequest request) throws BeansException {
        MealMemberDetailResponse response = new MealMemberDetailResponse();
        Member member = this.memberMapper.getMemberInfoByMemberId(request.getMemberId(), request.getOrgId());
        MealMember mealMember = this.mealMemberMapper.getMealMemberByMemberId(request.getMemberId());
        if (mealMember != null) {
            BeanUtils.copyProperties(mealMember, response);
        } else {
            new MealMember();
        }
        if (member != null) {
            BeanUtils.copyProperties(member, response);
            response.setDeptName(this.deptService.packagingDeptName(response.getDeptId(), false));
            MealCardMemberInfo cardMemberInfo = this.cardMemberMapper.getOneByMemberId(response.getMemberId(), request.getOrgId());
            if (cardMemberInfo != null) {
                response.setCardId(cardMemberInfo.getMealCardId());
            }
            List<MemberDetailsDeptResponse> deptList = new ArrayList<>();
            if (!StringUtils.isEmpty(member.getDeptId())) {
                String[] deptIds = member.getDeptId().split(",");
                for (String deptId : deptIds) {
                    Dept dept = this.deptMapper.getDeptById(Long.valueOf(deptId));
                    AssertUtil.isNullOrEmpty(dept, OnpremiseErrorEnum.DEPT_NOT_FIND);
                    MemberDetailsDeptResponse tempDept = new MemberDetailsDeptResponse();
                    tempDept.setDeptId(dept.getDeptId());
                    tempDept.setDeptName(dept.getDeptName());
                    deptList.add(tempDept);
                }
            }
            response.setDeptList(deptList);
            return response;
        }
        return null;
    }

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    public List<BatchInsertMemberResponse> batchSaveMealTempMember(BatchSaveMealMemberRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getBatchList(), OnpremiseErrorEnum.BATCH_INSERT_MEMBER_MUST_NOT_NULL);
        List<BatchInsertMemberResponse> responses = new ArrayList<>();
        for (int i = 0; i < request.getBatchList().size(); i++) {
            try {
                SaveMealMemberRequest eachMember = request.getBatchList().get(i);
                BeanUtils.copyProperties(request, eachMember);
                saveMealTempMember(eachMember);
            } catch (BizException e) {
                logger.error("error:{}", (Throwable) e);
                BatchInsertMemberResponse errorResponse = new BatchInsertMemberResponse();
                errorResponse.setRowNum(Integer.valueOf(i + 1));
                errorResponse.setMessage(e.getErrorCode());
                responses.add(errorResponse);
            }
        }
        return responses;
    }

    @Override // com.moredian.onpremise.api.meal.MealMemberService
    public PageList<ListMealMemberResponse> listMealTempMember(ListMealMemberRequest request) {
        request.setManagerDeptId(UserLoginResponse.getAccountManageDeptId(request.getSessionId()));
        request.setMemberType(request.getMemberType().equals(2) ? request.getMemberType() : null);
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<ListMealMemberResponse> listResp = this.mealMemberMapper.listMealMember(request);
            packageMealMemberResponse(listResp, request.getOrgId());
            return new PageList<>(listResp);
        }
        List<ListMealMemberResponse> listResp2 = this.mealMemberMapper.listMealMember(request);
        packageMealMemberResponse(listResp2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(listResp2), listResp2);
    }

    private void packageMealMemberResponse(List<ListMealMemberResponse> responses, Long orgId) {
        if (MyListUtils.listIsEmpty(responses)) {
            for (ListMealMemberResponse response : responses) {
                response.setDeptName(this.deptService.packagingDeptName(response.getDeptId(), false));
                MealCardMemberInfo cardMemberInfo = this.cardMemberMapper.getOneByMemberId(response.getMemberId(), orgId);
                if (cardMemberInfo != null) {
                    response.setCardStatus(cardMemberInfo.getCardStatus());
                    response.setCardName(cardMemberInfo.getCardName());
                    response.setMealCardId(cardMemberInfo.getMealCardId());
                } else {
                    response.setCardStatus(Integer.valueOf(MealCardStatusEnum.NOT_BOUND.getValue()));
                    response.setCardName("");
                    response.setMealCardId(0L);
                }
                response.setMemberType(Integer.valueOf(response.getMemberType() != null ? response.getMemberType().intValue() : 1));
                response.setMemberJobNum(response.getMemberWorkNum() != null ? response.getMemberWorkNum() : response.getMemberJobNum());
            }
        }
    }

    private boolean checkAccountIsAllow(Long accountId, Long orgId, Long memberId, String sessionId) {
        AssertUtil.checkId(memberId, OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
        Account account = this.accountMapper.getAccountInfo(accountId, orgId);
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        if (account.getAccountGrade().intValue() == AccountGradeEnum.CHILDREN_ACCOUNT.getValue() && !"open_api".equals(account.getAccountName())) {
            checkMember(orgId, memberId, sessionId);
            return true;
        }
        return true;
    }

    private boolean checkMember(Long orgId, Long memberId, String sessionId) {
        Member member = this.memberMapper.getMemberByManageDeptId(UserLoginResponse.getAccountManageDeptId(sessionId), memberId, orgId);
        AssertUtil.isNullOrEmpty(member, OnpremiseErrorEnum.NOT_ALLOW_OPERATE_OTHERS_MEMBER);
        return true;
    }

    private boolean checkCardMember(Long accountId, Long orgId, Long memberId) {
        MealCardMember cardMember = this.cardMemberMapper.getOneByMemberIdAndAccountId(memberId, orgId, accountId);
        AssertUtil.isNullOrEmpty(cardMember, OnpremiseErrorEnum.NOT_ALLOW_OPERATE_OTHERS_CARD);
        return true;
    }

    private MealCardMember getMealCardMember(Long memberId, Long orgId, Long mealCardId) {
        MealCardMember cardMember = new MealCardMember();
        cardMember.setMemberId(memberId);
        cardMember.setOrgId(orgId);
        cardMember.setCardId(mealCardId);
        cardMember.setConfirmFlag(1);
        cardMember.setCardStatus(Integer.valueOf(MemberCardStatusEnum.USED.getValue()));
        cardMember.setCardNo(System.currentTimeMillis() + "" + new Random().nextInt(1000));
        return cardMember;
    }
}
