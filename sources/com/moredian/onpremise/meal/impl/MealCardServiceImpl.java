package com.moredian.onpremise.meal.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.meal.MealCardService;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.MemberCardStatusEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.MealCardMapper;
import com.moredian.onpremise.core.mapper.MealCardMemberMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.MealCard;
import com.moredian.onpremise.core.model.domain.MealCardMember;
import com.moredian.onpremise.core.model.dto.MemberDto;
import com.moredian.onpremise.core.model.info.MealCardMemberInfo;
import com.moredian.onpremise.core.model.request.CheckMemberHasCardRequest;
import com.moredian.onpremise.core.model.request.DeleteMealCardRequest;
import com.moredian.onpremise.core.model.request.ListMealCardRequest;
import com.moredian.onpremise.core.model.request.QueryMealCardDetailRequest;
import com.moredian.onpremise.core.model.request.SaveMealCardRequest;
import com.moredian.onpremise.core.model.request.SendCardToMemberRequest;
import com.moredian.onpremise.core.model.response.CheckMemberHasCardResponse;
import com.moredian.onpremise.core.model.response.ListMealCardResponse;
import com.moredian.onpremise.core.model.response.ListSearchMealCardResponse;
import com.moredian.onpremise.core.model.response.MealCardDetailResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.JsonUtils;
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

@Service
/* loaded from: onpremise-meal-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/meal/impl/MealCardServiceImpl.class */
public class MealCardServiceImpl implements MealCardService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MealCanteenServiceImpl.class);

    @Autowired
    private MealCardMapper cardMapper;

    @Autowired
    private MealCardMemberMapper cardMemberMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override // com.moredian.onpremise.api.meal.MealCardService
    @Transactional(rollbackFor = {RuntimeException.class})
    public Long insert(SaveMealCardRequest request) throws BeansException {
        checkSaveMealCardRequest(request);
        deleteHasCardMember(0L, request.getCardMembers(), request.getOrgId());
        MealCard card = requestTransToCard(request);
        AssertUtil.isTrue(Boolean.valueOf(this.cardMapper.insert(card) > 0), OnpremiseErrorEnum.INSERT_CARD_FAIL);
        doInsertCardMember(request.getCardMembers(), request.getOrgId(), card.getMealCardId());
        return card.getMealCardId();
    }

    @Override // com.moredian.onpremise.api.meal.MealCardService
    @Transactional(rollbackFor = {RuntimeException.class})
    public Long update(SaveMealCardRequest request) throws BeansException {
        checkSaveMealCardRequest(request);
        deleteHasCardMember(request.getMealCardId(), request.getCardMembers(), request.getOrgId());
        getById(request.getMealCardId(), request.getOrgId());
        MealCard card = requestTransToCard(request);
        AssertUtil.isTrue(Boolean.valueOf(this.cardMapper.update(card) > 0), OnpremiseErrorEnum.UPDATE_CARD_FAIL);
        updateCardMember(request);
        return card.getMealCardId();
    }

    @Override // com.moredian.onpremise.api.meal.MealCardService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean delete(DeleteMealCardRequest request) {
        getById(request.getMealCardId(), request.getOrgId());
        AssertUtil.isTrue(Boolean.valueOf(this.cardMapper.delete(request.getMealCardId(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_CARD_FAIL);
        this.cardMemberMapper.deleteCardByCardId(request.getOrgId(), request.getMealCardId());
        return false;
    }

    @Override // com.moredian.onpremise.api.meal.MealCardService
    public PageList<ListMealCardResponse> listMealCard(ListMealCardRequest request) {
        Paginator paginator = request.getPaginator();
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        if (account.getAccountGrade().intValue() == AccountGradeEnum.SUPER_ACCOUNT.getValue() || account.getAccountGrade().intValue() == AccountGradeEnum.MAIN_ACCOUNT.getValue() || account.getModuleManager().intValue() == 1) {
            request.setLoginAccountId(null);
        } else {
            request.setLoginAccountId(account.getAccountId());
        }
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<ListMealCardResponse> responses = this.cardMapper.listMealCard(request);
            packagingCardList(responses, request.getOrgId());
            return new PageList<>(responses);
        }
        List<ListMealCardResponse> responses2 = this.cardMapper.listMealCard(request);
        packagingCardList(responses2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.meal.MealCardService
    public PageList<ListSearchMealCardResponse> listSearchMealCard(ListMealCardRequest request) {
        Paginator paginator = request.getPaginator();
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        request.setLoginAccountId(null);
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.cardMapper.listSearchMealCard(request));
        }
        List<ListSearchMealCardResponse> responses = this.cardMapper.listSearchMealCard(request);
        return new PageList<>(Paginator.initPaginator(responses), responses);
    }

    @Override // com.moredian.onpremise.api.meal.MealCardService
    public MealCardDetailResponse getDetailById(QueryMealCardDetailRequest request) throws BeansException {
        MealCard card = getById(request.getMealCardId(), request.getOrgId());
        MealCardDetailResponse response = new MealCardDetailResponse();
        BeanUtils.copyProperties(card, response);
        response.setCardMembers(this.cardMemberMapper.listMemberByCardId(request.getMealCardId(), request.getOrgId()));
        return response;
    }

    @Override // com.moredian.onpremise.api.meal.MealCardService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean sendCardToMember(SendCardToMemberRequest request) {
        getById(request.getMealCardId(), request.getOrgId());
        doInsertCardMember(request.getCardMembers(), request.getOrgId(), request.getMealCardId());
        return true;
    }

    @Override // com.moredian.onpremise.api.meal.MealCardService
    public List<CheckMemberHasCardResponse> checkMemberHasCard(CheckMemberHasCardRequest request) throws BeansException {
        List<CheckMemberHasCardResponse> responses = new ArrayList();
        logger.info("============request :{}", JsonUtils.toJson(request));
        if (MyListUtils.listIsEmpty(request.getMemberIds())) {
            responses = doCheckCardMember(request.getMemberIds(), request.getOrgId(), request.getMealCardId());
        }
        return responses;
    }

    private MealCard getById(Long mealCardId, Long orgId) {
        AssertUtil.checkId(mealCardId, OnpremiseErrorEnum.CARD_ID_MUST_NOT_NULL);
        MealCard card = this.cardMapper.getOneById(mealCardId, orgId);
        AssertUtil.isNullOrEmpty(card, OnpremiseErrorEnum.CARD_NOT_FIND);
        return card;
    }

    private boolean checkSaveMealCardRequest(SaveMealCardRequest request) {
        AssertUtil.isNullOrEmpty(request.getCardName(), OnpremiseErrorEnum.CARD_NAME_MUST_NOT_NULL);
        if (MyListUtils.listIsEmpty(request.getCardMembers())) {
            for (MemberDto member : request.getCardMembers()) {
                AssertUtil.checkId(member.getMemberId(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
            }
        }
        MealCard card = this.cardMapper.getOneByCardName(request.getCardName(), request.getOrgId());
        if (request.getMealCardId() == null || request.getMealCardId().longValue() <= 0) {
            AssertUtil.isTrue(Boolean.valueOf(card == null), OnpremiseErrorEnum.CANTEEN_NAME_ALREADY_EXIST);
            return true;
        }
        AssertUtil.isTrue(Boolean.valueOf(card == null || request.getMealCardId().longValue() == card.getMealCardId().longValue()), OnpremiseErrorEnum.CANTEEN_NAME_ALREADY_EXIST);
        return true;
    }

    private MealCard requestTransToCard(SaveMealCardRequest request) throws BeansException {
        MealCard canteen = new MealCard();
        BeanUtils.copyProperties(request, canteen);
        canteen.setAccountId(request.getLoginAccountId());
        canteen.setOrgId(request.getOrgId());
        return canteen;
    }

    private boolean updateCardMember(SaveMealCardRequest request) {
        List<MemberDto> newMembers = request.getCardMembers();
        List<MemberDto> oldMembers = this.cardMemberMapper.listMemberByCardId(request.getMealCardId(), request.getOrgId());
        logger.info("newMembers :{}", JsonUtils.toJson(newMembers));
        logger.info("oldMembers :{}", JsonUtils.toJson(oldMembers));
        MyListUtils<MemberDto> memberUtils = new MyListUtils<>();
        List<MemberDto> insertMembers = memberUtils.difference(newMembers, oldMembers);
        List<MemberDto> deleteMembers = memberUtils.difference(oldMembers, newMembers);
        logger.info("insertMembers size :{} , deleteMembers size :{}", Integer.valueOf(insertMembers.size()), Integer.valueOf(deleteMembers.size()));
        if (deleteMembers != null && deleteMembers.size() > 0) {
            for (MemberDto member : deleteMembers) {
                this.cardMemberMapper.deleteCardByCardIdAndMemberId(member.getMemberId(), request.getOrgId(), request.getMealCardId());
            }
        }
        doInsertCardMember(insertMembers, request.getOrgId(), request.getMealCardId());
        return true;
    }

    private boolean doInsertCardMember(List<MemberDto> members, Long orgId, Long mealCardId) {
        if (members != null && members.size() > 0) {
            MealCardMember cardMember = new MealCardMember();
            for (MemberDto member : members) {
                cardMember.setMemberId(member.getMemberId());
                cardMember.setOrgId(orgId);
                cardMember.setCardId(mealCardId);
                cardMember.setConfirmFlag(1);
                cardMember.setCardStatus(Integer.valueOf(MemberCardStatusEnum.USED.getValue()));
                cardMember.setCardNo(System.currentTimeMillis() + "" + new Random().nextInt(1000));
                AssertUtil.isTrue(Boolean.valueOf(this.cardMemberMapper.insert(cardMember) > 0), OnpremiseErrorEnum.INSERT_CARD_MEMBER_FAIL);
            }
            return true;
        }
        return true;
    }

    private void checkAccount(Long accountId, Long orgId, Long loginAccountId) {
        Account account = this.accountMapper.getAccountInfo(loginAccountId, orgId);
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(account.getAccountGrade().intValue() == AccountGradeEnum.SUPER_ACCOUNT.getValue() || account.getAccountGrade().intValue() == AccountGradeEnum.MAIN_ACCOUNT.getValue() || loginAccountId.longValue() == accountId.longValue()), OnpremiseErrorEnum.NOT_ALLOW_OPERATE_OTHERS_CANTEEN);
    }

    private void packagingCardList(List<ListMealCardResponse> responses, Long orgId) {
        if (responses != null && responses.size() > 0) {
            for (ListMealCardResponse response : responses) {
                doPackagingCanteenList(response, orgId);
            }
        }
    }

    private void doPackagingCanteenList(ListMealCardResponse response, Long orgId) {
        response.setMemberNum(this.cardMemberMapper.countMemberByCardId(response.getMealCardId(), orgId));
        Account account = this.accountMapper.getAccountInfoIncludDelete(response.getAccountId(), orgId);
        response.setAccountName(account == null ? "" : account.getAccountName());
    }

    private List<CheckMemberHasCardResponse> doCheckCardMember(List<MemberDto> members, Long orgId, Long mealCardId) throws BeansException {
        List<CheckMemberHasCardResponse> responses = new ArrayList<>();
        if (MyListUtils.listIsEmpty(members)) {
            for (MemberDto member : members) {
                CheckMemberHasCardResponse response = new CheckMemberHasCardResponse();
                MealCardMemberInfo cardMemberInfo = this.cardMemberMapper.getOneByMemberId(member.getMemberId(), orgId);
                if (mealCardId == null || mealCardId.longValue() == 0) {
                    if (cardMemberInfo != null) {
                        BeanUtils.copyProperties(cardMemberInfo, response);
                        responses.add(response);
                    }
                } else if (cardMemberInfo != null && cardMemberInfo.getMealCardId().longValue() != mealCardId.longValue()) {
                    BeanUtils.copyProperties(cardMemberInfo, response);
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    private void deleteHasCardMember(Long notMealCardId, List<MemberDto> members, Long orgId) throws BeansException {
        List<CheckMemberHasCardResponse> responses = doCheckCardMember(members, orgId, notMealCardId);
        if (MyListUtils.listIsEmpty(responses)) {
            for (CheckMemberHasCardResponse response : responses) {
                this.cardMemberMapper.deleteCardByMember(response.getMemberId(), orgId, notMealCardId);
            }
        }
    }
}
