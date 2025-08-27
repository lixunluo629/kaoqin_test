package com.moredian.onpremise.account.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.account.AccountService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.constants.OpenApiConstants;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.HasAuthCodeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.mapper.AccountAuthMapper;
import com.moredian.onpremise.core.mapper.AccountGroupMapper;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.AppMapper;
import com.moredian.onpremise.core.mapper.AuthCodeMapper;
import com.moredian.onpremise.core.mapper.AuthModuleMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.GroupMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.mapper.OpenApiMapper;
import com.moredian.onpremise.core.mapper.OperLogMapper;
import com.moredian.onpremise.core.mapper.OrganizationMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.AccountAuth;
import com.moredian.onpremise.core.model.domain.AccountGroup;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.domain.OpenApi;
import com.moredian.onpremise.core.model.domain.Organization;
import com.moredian.onpremise.core.model.dto.FreezeAccountDto;
import com.moredian.onpremise.core.model.dto.GroupDeviceGroupDto;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.CloneAccountRequest;
import com.moredian.onpremise.core.model.request.DeleteAccountRequest;
import com.moredian.onpremise.core.model.request.ListAccountRequest;
import com.moredian.onpremise.core.model.request.ModifyPasswordRequest;
import com.moredian.onpremise.core.model.request.OpenApiAppKeyRequest;
import com.moredian.onpremise.core.model.request.OpenApiAppTokenRequest;
import com.moredian.onpremise.core.model.request.OperLogListRequest;
import com.moredian.onpremise.core.model.request.ResetPasswordRequest;
import com.moredian.onpremise.core.model.request.SaveAccountRequest;
import com.moredian.onpremise.core.model.request.TerminalLoginRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.UpdateLanguageRequest;
import com.moredian.onpremise.core.model.request.UpdateOrgInfoRequest;
import com.moredian.onpremise.core.model.request.UserLoginRequest;
import com.moredian.onpremise.core.model.request.UserLoginRequestV2;
import com.moredian.onpremise.core.model.response.AccountListResponse;
import com.moredian.onpremise.core.model.response.AccountManageDeptResponse;
import com.moredian.onpremise.core.model.response.AccountManageDeviceResponse;
import com.moredian.onpremise.core.model.response.AccountManageGroupResponse;
import com.moredian.onpremise.core.model.response.AccountOrgInfoResponse;
import com.moredian.onpremise.core.model.response.AppResponse;
import com.moredian.onpremise.core.model.response.AuthModuleResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponse;
import com.moredian.onpremise.core.model.response.OpenApiAppKeyResponse;
import com.moredian.onpremise.core.model.response.OpenApiAppTokenResponse;
import com.moredian.onpremise.core.model.response.OperLogResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAccountResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.AuthCodeUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.JwtUtils;
import com.moredian.onpremise.core.utils.MD5Utils;
import com.moredian.onpremise.core.utils.MyBase64Utils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncAccountRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-account-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/account/impl/AccountServiceImpl.class */
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private OrganizationMapper orgMapper;

    @Autowired
    private OpenApiMapper openApiMapper;

    @Autowired
    private AuthCodeMapper authCodeMapper;

    @Autowired
    private AccountGroupMapper accountGroupMapper;

    @Value("${onpremise.license.public.key}")
    private String publicKey;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private AccountAuthMapper accountAuthMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private AuthModuleMapper authModuleMapper;

    @Autowired
    private DeptService deptService;

    @Autowired
    private OperLogMapper operLogMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private AppMapper appMapper;

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public UserLoginResponse login(UserLoginRequest loginRequest) {
        AssertUtil.isNullOrEmpty(loginRequest.getUserName(), OnpremiseErrorEnum.ACCOUNT_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(loginRequest.getPassword(), OnpremiseErrorEnum.PASSWORD_MUST_NOT_NULL);
        loginRequest.setPassword(loginRequest.getPassword());
        Account account = this.accountMapper.loginByUserNameAndPassword(loginRequest.getUserName(), loginRequest.getPassword());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        if (account.getFirstLoginFlag().intValue() == 1) {
            this.accountMapper.updateFirstLoginStatus(account.getAccountId(), account.getOrgId());
        }
        UserLoginResponse response = packagingLoginResponse(account, loginRequest.getSessionId());
        return response;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public UserLoginResponse loginV2(UserLoginRequestV2 loginRequest) throws NoSuchAlgorithmException {
        int mistakeTimes;
        AssertUtil.isNullOrEmpty(loginRequest.getUserName(), OnpremiseErrorEnum.ACCOUNT_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(loginRequest.getPassword(), OnpremiseErrorEnum.PASSWORD_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(loginRequest.getTimestamp(), OnpremiseErrorEnum.PASSWORD_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(System.currentTimeMillis() - loginRequest.getTimestamp().longValue() < 300000), OnpremiseErrorEnum.LOGIN_TIMESTAMP_EXPIRES);
        Account account = this.accountMapper.getAccountInfoByName(loginRequest.getUserName(), 1L);
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        String md5Str = MD5Utils.getMD5Code(account.getAccountPassword() + loginRequest.getTimestamp());
        FreezeAccountDto freezeAccountDto = CacheAdapter.getFreezeAccount(Constants.FREEZE_ACCOUNT + account.getAccountName());
        Long now = Long.valueOf(System.currentTimeMillis());
        if (freezeAccountDto != null) {
            AssertUtil.isTrue(Boolean.valueOf(now.longValue() > freezeAccountDto.getFreezeTime().longValue()), OnpremiseErrorEnum.FREEZE_ACCOUNT.getErrorCode(), String.valueOf(freezeAccountDto.getFreezeTime().longValue() - now.longValue()));
            mistakeTimes = freezeAccountDto.getMistakeTimes().intValue();
        } else {
            freezeAccountDto = new FreezeAccountDto();
            freezeAccountDto.setFreezeTime(0L);
            freezeAccountDto.setExpireTime(Long.valueOf(now.longValue() + 300000));
            mistakeTimes = 0;
        }
        freezeAccountDto.setAccountName(account.getAccountName());
        if (!md5Str.equals(loginRequest.getPassword())) {
            if (mistakeTimes == 4) {
                freezeAccountDto.setFreezeTime(Long.valueOf(now.longValue() + 900000));
                freezeAccountDto.setMistakeTimes(0);
                CacheAdapter.cacheFreezeAccount(Constants.FREEZE_ACCOUNT + account.getAccountName(), freezeAccountDto);
                throw new BizException(OnpremiseErrorEnum.FREEZE_ACCOUNT.getErrorCode(), String.valueOf(Constants.DEFAULT_FREEZE_TIME));
            }
            int mistakeTimes2 = mistakeTimes + 1;
            freezeAccountDto.setMistakeTimes(Integer.valueOf(mistakeTimes2));
            CacheAdapter.cacheFreezeAccount(Constants.FREEZE_ACCOUNT + account.getAccountName(), freezeAccountDto);
            throw new BizException(OnpremiseErrorEnum.LOGIN_PASSWORD_WRONG_TIMES.getErrorCode(), String.valueOf(mistakeTimes2));
        }
        freezeAccountDto.setMistakeTimes(0);
        CacheAdapter.cacheFreezeAccount(Constants.FREEZE_ACCOUNT + account.getAccountName(), freezeAccountDto);
        if (account.getFirstLoginFlag().intValue() == 1) {
            this.accountMapper.updateFirstLoginStatus(account.getAccountId(), account.getOrgId());
        }
        UserLoginResponse response = packagingLoginResponse(account, loginRequest.getSessionId());
        return response;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public boolean terminalLogin(TerminalLoginRequest loginRequest) {
        AssertUtil.isNullOrEmpty(loginRequest.getUserName(), OnpremiseErrorEnum.ACCOUNT_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(loginRequest.getPassword(), OnpremiseErrorEnum.PASSWORD_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(loginRequest.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        loginRequest.setPassword(loginRequest.getPassword());
        Account account = this.accountMapper.loginByUserNameAndPassword(loginRequest.getUserName(), loginRequest.getPassword());
        boolean resp = false;
        if (account != null) {
            if (account.getAccountGrade() != null && account.getAccountGrade().intValue() == AccountGradeEnum.CHILDREN_ACCOUNT.getValue()) {
                AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(account.getAccountId(), account.getOrgId());
                if (accountAuth != null && accountAuth.getManageDeviceSn() != null && accountAuth.getManageDeviceSn().contains(loginRequest.getDeviceSn())) {
                    resp = true;
                }
            } else {
                resp = true;
            }
        }
        return resp;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public boolean logout(BaseRequest request) {
        CacheAdapter.removeLoginInfo(request.getSessionId());
        return true;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public AccountOrgInfoResponse getAccountOrgInfo(BaseRequest request) {
        Organization organization = this.orgMapper.getOneById(request.getOrgId());
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        AccountOrgInfoResponse response = new AccountOrgInfoResponse();
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        response.setAccountName(account.getAccountName());
        response.setDeptNum(Integer.valueOf(this.deptMapper.getDeptCount(request.getOrgId())));
        response.setMemberNum(this.memberMapper.countAllMembers(request.getOrgId()));
        response.setOrgName(organization.getOrgName());
        response.setOrgCode(CacheAdapter.getServerOrgCode(Constants.SERVER_ORG_CODE_KEY));
        return response;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean updateOrgInfo(UpdateOrgInfoRequest request) {
        AssertUtil.isNullOrEmpty(request.getOrgName(), OnpremiseErrorEnum.ORG_NAME_MUST_NOT_NULL);
        Organization organization = this.orgMapper.getOneById(request.getOrgId());
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        organization.getOrgName();
        AssertUtil.isTrue(Boolean.valueOf(this.orgMapper.updateOrgInfo(request) > 0), OnpremiseErrorEnum.UPDATE_ORG_INFO_FAIL);
        List<ListDeptResponse> depts = this.deptMapper.getTopDepts(request.getOrgId(), new ArrayList());
        if (MyListUtils.listIsEmpty(depts)) {
            for (ListDeptResponse response : depts) {
                List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(response.getDeptId(), request.getOrgId());
                if (MyListUtils.listIsEmpty(childDepts)) {
                    AssertUtil.isTrue(Boolean.valueOf(this.deptMapper.updateFirstDeptName(response.getDeptId(), request.getOrgName(), request.getOrgId()) > 0), OnpremiseErrorEnum.UPDATE_DEPT_FAIL);
                }
            }
        }
        AssertUtil.isTrue(Boolean.valueOf(this.deptMapper.updateTopDept(request.getOrgName(), request.getOrgId()) > 0), OnpremiseErrorEnum.UPDATE_DEPT_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public PageList<AccountListResponse> listAccount(ListAccountRequest request) throws BeansException {
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        if (account.getAccountGrade().equals(Integer.valueOf(AccountGradeEnum.SUPER_ACCOUNT.getValue())) || account.getAccountGrade().equals(Integer.valueOf(AccountGradeEnum.MAIN_ACCOUNT.getValue()))) {
            request.setLoginAccountId(null);
        }
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<AccountListResponse> listResponse = this.accountMapper.listAccount(request);
            packagingAccountList(listResponse, request.getOrgId());
            return new PageList<>(listResponse);
        }
        List<AccountListResponse> listResponse2 = this.accountMapper.listAccount(request);
        packagingAccountList(listResponse2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(listResponse2), listResponse2);
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public Long updateAccount(SaveAccountRequest request) {
        Account account;
        checkSaveAccountRequest(request);
        Integer oldAccountGrade = 0;
        if (request.getMemberId() != null && (account = this.accountMapper.getAccountByMemberId(request.getMemberId(), request.getOrgId())) != null) {
            oldAccountGrade = account.getAccountGrade();
            this.accountMapper.unbindMember(request.getMemberId(), request.getOrgId());
        }
        Account account2 = this.accountMapper.getAccountInfo(request.getAccountId(), request.getOrgId());
        if (account2.getMemberId() != null) {
            if (account2.getMemberId().equals(request.getMemberId())) {
                Member member = this.memberMapper.getMemberInfoByMemberId(account2.getMemberId(), request.getOrgId());
                if (member != null) {
                    int result = this.memberMapper.unbindAccount(member.getMemberId());
                    AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.UNBIND_MEMBER_ACCOUNT_FAIL);
                }
                if (request.getMemberId() != null) {
                    int result2 = this.memberMapper.bindAccount(request);
                    AssertUtil.isTrue(Boolean.valueOf(result2 > 0), OnpremiseErrorEnum.BIND_MEMBER_ACCOUNT_FAIL);
                }
            }
            int result3 = this.accountMapper.saveAccount(request);
            AssertUtil.isTrue(Boolean.valueOf(result3 > 0), OnpremiseErrorEnum.UPDATE_ACCOUNT_FAIL);
        } else {
            if (request.getMemberId() != null) {
                int result4 = this.memberMapper.bindAccount(request);
                AssertUtil.isTrue(Boolean.valueOf(result4 > 0), OnpremiseErrorEnum.BIND_MEMBER_ACCOUNT_FAIL);
            }
            int result5 = this.accountMapper.saveAccount(request);
            AssertUtil.isTrue(Boolean.valueOf(result5 > 0), OnpremiseErrorEnum.UPDATE_ACCOUNT_FAIL);
        }
        doSaveAccountGroup(request);
        doSaveAccountAuth(request, account2.getAccountGrade(), oldAccountGrade);
        return request.getAccountId();
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public Long insertAccount(SaveAccountRequest request) {
        Account account;
        checkSaveAccountRequest(request);
        if (request.getAccountGrade() == null) {
            request.setAccountGrade(Integer.valueOf(AccountGradeEnum.CHILDREN_ACCOUNT.getValue()));
        }
        AssertUtil.isNullOrEmpty(request.getPassword(), OnpremiseErrorEnum.PASSWORD_MUST_NOT_NULL);
        Integer oldAccountGrade = 0;
        if (request.getMemberId() != null && (account = this.accountMapper.getAccountByMemberId(request.getMemberId(), request.getOrgId())) != null) {
            oldAccountGrade = account.getAccountGrade();
            this.accountMapper.unbindMember(request.getMemberId(), request.getOrgId());
        }
        Account account2 = new Account();
        account2.setOrgId(request.getOrgId());
        account2.setAccountName(request.getAccountName());
        account2.setAccountPassword(request.getPassword());
        account2.setMemberId(request.getMemberId());
        account2.setAccountGrade(request.getAccountGrade());
        account2.setCloneAccountId(request.getCloneAccountId());
        account2.setOperatorId(request.getLoginAccountId());
        account2.setModuleManager(request.getModuleManager());
        AssertUtil.isTrue(Boolean.valueOf(this.accountMapper.addAccount(account2) > 0), OnpremiseErrorEnum.ADD_ACCOUNT_FAIL);
        request.setAccountId(account2.getAccountId());
        if (request.getMemberId() != null) {
            AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.bindAccount(request) > 0), OnpremiseErrorEnum.BIND_MEMBER_ACCOUNT_FAIL);
        }
        doSaveAccountGroup(request);
        doSaveAccountAuth(request, account2.getAccountGrade(), oldAccountGrade);
        return account2.getAccountId();
    }

    private void doSaveAccountGroup(SaveAccountRequest request) {
        this.accountGroupMapper.deleteByAccountId(request.getOrgId(), request.getAccountId());
        if (StringUtils.isEmpty(request.getManageGroupId())) {
            return;
        }
        List<AccountGroup> accountGroupList = new ArrayList<>();
        String[] groupIdStrArr = request.getManageGroupId().split(",");
        for (String groupIdStr : groupIdStrArr) {
            AccountGroup accountGroup = new AccountGroup();
            accountGroup.setOrgId(request.getOrgId());
            accountGroup.setAccountId(request.getAccountId());
            accountGroup.setGroupId(Long.valueOf(groupIdStr));
            accountGroupList.add(accountGroup);
        }
        this.accountGroupMapper.insertBatchAccountGroup(accountGroupList);
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteAccount(DeleteAccountRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getAccountId() != null && request.getAccountId().longValue() > 0), OnpremiseErrorEnum.ACCOUNT_ID_MUST_NOT_NULL);
        Account account = this.accountMapper.getAccountInfo(request.getAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(!request.getLoginAccountId().equals(request.getAccountId())), OnpremiseErrorEnum.DELETE_ACCOUNT_FAIL);
        AssertUtil.isTrue(Boolean.valueOf(this.accountMapper.deleteAccount(request) > 0), OnpremiseErrorEnum.DELETE_ACCOUNT_FAIL);
        Member member = this.memberMapper.getMemberInfoByMemberId(account.getMemberId(), account.getOrgId());
        if (member != null) {
            AssertUtil.isTrue(Boolean.valueOf(this.memberMapper.unbindAccount(member.getMemberId()) > 0), OnpremiseErrorEnum.UNBIND_MEMBER_ACCOUNT_FAIL);
        }
        String deviceSn = "";
        if (account.getAccountGrade().equals(Integer.valueOf(AccountGradeEnum.MAIN_ACCOUNT.getValue())) || account.getAccountGrade().equals(Integer.valueOf(AccountGradeEnum.SUPER_ACCOUNT.getValue()))) {
            doSendNettyMsg(request.getOrgId());
            return true;
        }
        AccountAuth auth = this.accountAuthMapper.getOneByAccountId(request.getAccountId(), request.getOrgId());
        if (auth != null) {
            this.accountAuthMapper.deleteByAccountId(request.getAccountId(), request.getOrgId());
            deviceSn = auth.getManageDeviceSn();
        }
        doSendNettyMsgByDeviceSn(request.getOrgId(), deviceSn);
        return true;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean modifyPassword(ModifyPasswordRequest request) {
        AssertUtil.isNullOrEmpty(Boolean.valueOf(request.getAccountId() != null && request.getAccountId().longValue() > 0), OnpremiseErrorEnum.ACCOUNT_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getPassword(), OnpremiseErrorEnum.PASSWORD_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getPassword(), OnpremiseErrorEnum.CONFIRM_PASSWORD_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getPassword().equals(request.getConfirmPassword())), OnpremiseErrorEnum.TWICE_PASSWORD_IS_DIFFERENT);
        AssertUtil.isTrue(Boolean.valueOf(this.accountMapper.modifyPassword(request) > 0), OnpremiseErrorEnum.MODIFY_ACCOUNT_PASSWORD_FAIL);
        doSendNettyMsg(request.getOrgId(), request.getAccountId());
        return true;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public OpenApiAppKeyResponse getOpenApiAppKey(OpenApiAppKeyRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getOrgId() != null && request.getOrgId().longValue() > 0), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        OpenApiAppKeyResponse response = new OpenApiAppKeyResponse();
        OpenApi openApi = this.openApiMapper.getLastOne(request.getOrgId());
        if (openApi == null) {
            return response;
        }
        response.setAppKey(openApi.getAppKey());
        return response;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public OpenApiAppTokenResponse getOpenApiToken(OpenApiAppTokenRequest request) {
        AssertUtil.isNullOrEmpty(request.getAppKey(), OnpremiseErrorEnum.OPENAPI_APPKEY_MUST_NOT_NULL);
        OpenApiAppTokenResponse response = new OpenApiAppTokenResponse();
        OpenApi openApi = this.openApiMapper.getByAppKey(request.getAppKey());
        if (openApi == null) {
            return response;
        }
        UserLoginResponse cacheResponse = new UserLoginResponse();
        Account account = this.accountMapper.getAccountInfoByName("open_api", openApi.getOrgId());
        cacheResponse.setOrgId(openApi.getOrgId());
        cacheResponse.setAccountId(account.getAccountId());
        cacheResponse.setAccountName(account.getAccountName());
        cacheResponse.setAccountGrade(account.getAccountGrade());
        cacheResponse.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), Constants.TOKEN_EXPIRE_TIME).getTime()));
        AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(account.getAccountId(), account.getOrgId());
        if (accountAuth != null) {
            cacheResponse.setManageAppId(accountAuth.getManageAppId());
            cacheResponse.setManageDeptId(getDeptId(accountAuth.getManageDeptId(), account.getOrgId()));
            cacheResponse.setManageDeviceSn(accountAuth.getManageDeviceSn());
        }
        CacheAdapter.cacheLoginInfo(OpenApiConstants.OPEN_API_LOGIN_ACCOUNT, cacheResponse);
        Map<String, Object> params = new HashMap<>(5);
        params.put("orgId", openApi.getOrgId());
        params.put("appKey", openApi.getAppKey());
        params.put("appId", openApi.getAppId());
        params.put(AuthConstants.AUTH_PARAM_ORG_CODE_KEY, request.getOrgCode());
        response.setAppToken(JwtUtils.tokenParams(params, request.getAppKey(), MyDateUtils.addSeconds(new Date(), Constants.TOKEN_EXPIRE_TIME)));
        response.setExpires(Integer.valueOf(Constants.TOKEN_EXPIRE_TIME));
        return response;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public OpenApiAppTokenResponse getOpenApiTokenV2(OpenApiAppTokenRequest request) {
        AssertUtil.isNullOrEmpty(request.getAppKey(), OnpremiseErrorEnum.OPENAPI_APPKEY_MUST_NOT_NULL);
        OpenApiAppTokenResponse response = new OpenApiAppTokenResponse();
        OpenApi openApi = this.openApiMapper.getByAppKey(request.getAppKey());
        AssertUtil.isNullOrEmpty(openApi, OnpremiseErrorEnum.OPENAPI_APPKEY_INVALID);
        UserLoginResponse cacheResponse = new UserLoginResponse();
        Account account = this.accountMapper.getAccountInfoByName("open_api", openApi.getOrgId());
        cacheResponse.setOrgId(openApi.getOrgId());
        cacheResponse.setAccountId(account.getAccountId());
        cacheResponse.setAccountName(account.getAccountName());
        cacheResponse.setAccountGrade(account.getAccountGrade());
        cacheResponse.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), Constants.TOKEN_EXPIRE_TIME).getTime()));
        AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(account.getAccountId(), account.getOrgId());
        if (accountAuth != null) {
            cacheResponse.setManageAppId(accountAuth.getManageAppId());
            cacheResponse.setManageDeptId(getDeptId(accountAuth.getManageDeptId(), account.getOrgId()));
            cacheResponse.setManageDeviceSn(accountAuth.getManageDeviceSn());
        }
        CacheAdapter.cacheLoginInfo(OpenApiConstants.OPEN_API_LOGIN_ACCOUNT, cacheResponse);
        Map<String, Object> params = new HashMap<>(5);
        Date expireDate = MyDateUtils.addSeconds(new Date(), Constants.TOKEN_EXPIRE_TIME);
        params.put("orgId", openApi.getOrgId());
        params.put("appKey", openApi.getAppKey());
        params.put(AuthConstants.AUTH_PARAM_ORG_CODE_KEY, request.getOrgCode());
        params.put("expires", Long.valueOf(expireDate.getTime()));
        params.put("uuid", UUID.randomUUID());
        response.setAppToken(JwtUtils.tokenParams(params, request.getAppKey(), expireDate));
        response.setExpires(Integer.valueOf(Constants.TOKEN_EXPIRE_TIME));
        return response;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public Long resetPassword(ResetPasswordRequest request) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        AssertUtil.isNullOrEmpty(request.getAccountName(), OnpremiseErrorEnum.ACCOUNT_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getAuthCode(), OnpremiseErrorEnum.SYSTEM_AUTH_CODE_MUST_NOT_NULL);
        Account account = this.accountMapper.getAccountInfoByName(request.getAccountName(), null);
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        AuthCodeUtils.decryptByPublicKey(request.getAuthCode(), this.publicKey);
        packagingLoginResponse(account, request.getSessionId());
        return account.getAccountId();
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public boolean updateLanguage(UpdateLanguageRequest request) {
        AssertUtil.isNullOrEmpty(Boolean.valueOf(request.getLoginAccountId() != null && request.getLoginAccountId().longValue() > 0), OnpremiseErrorEnum.ACCOUNT_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getLanguageType(), OnpremiseErrorEnum.ACCOUNT_LANGUAGE_TYPE_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(this.accountMapper.updateLanguage(request.getAccountId(), request.getLanguageType(), request.getOrgId()) > 0), OnpremiseErrorEnum.UPDATE_LANGUAGE_TYPE_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public List<AuthModuleResponse> listAccountManageModule(BaseRequest request) {
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(request.getLoginAccountId(), request.getOrgId());
        List<AuthModuleResponse> responses = new ArrayList<>();
        List<AppResponse> appResponses = this.appMapper.getUnOpenAppList(request.getOrgId());
        List<Integer> appTypes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appResponses)) {
            for (AppResponse appResponse : appResponses) {
                appTypes.add(appResponse.getAppType());
            }
        }
        if (accountAuth == null || account.getAccountGrade().intValue() == AccountGradeEnum.SUPER_ACCOUNT.getValue() || account.getAccountGrade().intValue() == AccountGradeEnum.MAIN_ACCOUNT.getValue()) {
            responses = this.authModuleMapper.listById(null, request.getOrgId(), appTypes);
        } else if (accountAuth.getManageModuleId() != null && accountAuth.getManageModuleId().trim().length() > 0) {
            responses = this.authModuleMapper.listById(Arrays.asList(accountAuth.getManageModuleId().split(",")), request.getOrgId(), appTypes);
        }
        return responses;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public OpenApiAppKeyResponse generatorOpenApiAppKey(OpenApiAppKeyRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getOrgId() != null && request.getOrgId().longValue() > 0), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        OpenApiAppKeyResponse response = new OpenApiAppKeyResponse();
        OpenApi openApi = this.openApiMapper.getLastOne(request.getOrgId());
        if (openApi == null) {
            openApi = doInsertOpenApi(request.getOrgId());
            doInsertOpenApiAccount(request.getOrgId());
        }
        response.setAppKey(openApi.getAppKey());
        return response;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    @Transactional(rollbackFor = {RuntimeException.class})
    public OpenApiAppKeyResponse generatorOpenApiAppKeyV2(OpenApiAppKeyRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getOrgId() != null && request.getOrgId().longValue() > 0), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        OpenApiAppKeyResponse response = new OpenApiAppKeyResponse();
        this.openApiMapper.delete(request.getOrgId());
        OpenApi openApi = doInsertOpenApi(request.getOrgId());
        Account openApiAccount = this.accountMapper.getAccountInfoByName("open_api", request.getOrgId());
        if (openApiAccount == null) {
            doInsertOpenApiAccount(request.getOrgId());
        }
        response.setAppKey(openApi.getAppKey());
        return response;
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public List<TerminalSyncAccountResponse> syncAccount(TerminalSyncRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getLastSyncTime() != null), OnpremiseErrorEnum.LAST_SYNC_TIME_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        return this.accountMapper.listSyncAccount(request.getOrgId(), MyDateUtils.getDateString(request.getLastSyncTime().longValue(), "yyyy-MM-dd HH:mm:ss.SSS"), request.getDeviceSn());
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public void resetPasswordEncode() {
        ListAccountRequest request = new ListAccountRequest();
        request.setOrgId(1L);
        List<AccountListResponse> accountList = this.accountMapper.listAccount(request);
        if (!CollectionUtils.isEmpty(accountList)) {
            for (AccountListResponse accountListResponse : accountList) {
                String password = MyBase64Utils.decodeStringForString(accountListResponse.getPassword());
                ModifyPasswordRequest modifyPasswordRequest = new ModifyPasswordRequest();
                modifyPasswordRequest.setAccountId(accountListResponse.getAccountId());
                modifyPasswordRequest.setPassword(MD5Utils.getMD5Code(password));
                AssertUtil.isTrue(Boolean.valueOf(this.accountMapper.modifyPassword(modifyPasswordRequest) > 0), OnpremiseErrorEnum.MODIFY_ACCOUNT_PASSWORD_FAIL);
            }
        }
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public PageList<OperLogResponse> operLogPageList(OperLogListRequest request) {
        if (MyDateUtils.parseDate(request.getStartTimeStr(), "yyyy-MM-dd") != null) {
            request.setStartTimeStr(request.getStartTimeStr() + MyDateUtils.TIME_OF_DAY_BEGIN);
        }
        if (MyDateUtils.parseDate(request.getEndTimeStr(), "yyyy-MM-dd") != null) {
            request.setEndTimeStr(request.getEndTimeStr() + MyDateUtils.TIME_OF_DAY_END);
        }
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            return new PageList<>(this.operLogMapper.pageList(request));
        }
        List<OperLogResponse> listResp = this.operLogMapper.pageList(request);
        return new PageList<>(Paginator.initPaginator(listResp), listResp);
    }

    @Override // com.moredian.onpremise.api.account.AccountService
    public Long cloneAccount(CloneAccountRequest request) throws BeansException {
        Account account = this.accountMapper.getAccountInfo(request.getCloneAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        Account accountClone = new Account();
        BeanUtils.copyProperties(account, accountClone);
        accountClone.setMemberId(null);
        accountClone.setFirstLoginFlag(1);
        accountClone.setAccountName(UUID.randomUUID().toString());
        accountClone.setCloneAccountId(request.getCloneAccountId());
        accountClone.setAccountPassword(MD5Utils.getMD5Code(Constants.DEFAULT_PASSWORD));
        this.accountMapper.addAccount(accountClone);
        List<AccountGroup> accountGroupList = this.accountGroupMapper.listByAccountId(request.getOrgId(), request.getCloneAccountId());
        if (CollectionUtils.isEmpty(accountGroupList)) {
            return accountClone.getAccountId();
        }
        List<AccountGroup> accountGroupCloneList = new ArrayList<>();
        for (AccountGroup accountGroup : accountGroupList) {
            AccountGroup accountGroupClone = new AccountGroup();
            BeanUtils.copyProperties(accountGroup, accountGroupClone);
            accountGroupClone.setAccountId(accountClone.getAccountId());
            accountGroupCloneList.add(accountGroupClone);
        }
        this.accountGroupMapper.insertBatchAccountGroup(accountGroupCloneList);
        return accountClone.getAccountId();
    }

    private String passwordForBase64(String password) {
        if (password != null && password.trim().length() > 0) {
            return MyBase64Utils.encodeStringForString(password);
        }
        return "";
    }

    private void passwordForBase64(SaveAccountRequest request) {
        request.setPassword(passwordForBase64(request.getPassword()));
    }

    private void passwordForBase64(ModifyPasswordRequest request) {
        request.setPassword(passwordForBase64(request.getPassword()));
    }

    private UserLoginResponse packagingLoginResponse(Account account, String sessionId) {
        Organization organization = this.orgMapper.getOneById(account.getOrgId());
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        UserLoginResponse response = new UserLoginResponse();
        response.setOrgId(organization.getOrgId());
        response.setOrgName(organization.getOrgName());
        response.setAccountId(account.getAccountId());
        response.setAccountName(account.getAccountName());
        response.setAccountGrade(account.getAccountGrade());
        response.setFirstLoginFlag(account.getFirstLoginFlag());
        response.setLanguageType(account.getLanguageType());
        Integer authCodeNum = Integer.valueOf(this.authCodeMapper.countAuthCode(account.getOrgId()));
        response.setHasAuthCodeFlag(Integer.valueOf(authCodeNum.intValue() == 0 ? HasAuthCodeEnum.NO.getValue() : HasAuthCodeEnum.YES.getValue()));
        response.setExpireTime(Long.valueOf(MyDateUtils.addMinutes(new Date(), 30).getTime()));
        AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(account.getAccountId(), account.getOrgId());
        if (accountAuth != null) {
            response.setManageAppId(accountAuth.getManageAppId());
            response.setManageDeptId(getDeptId(accountAuth.getManageDeptId(), account.getOrgId()));
            response.setManageDeviceSn(accountAuth.getManageDeviceSn());
        }
        CacheAdapter.cacheLoginInfo(sessionId, response);
        return response;
    }

    private void packagingAccountList(List<AccountListResponse> listResponse, Long orgId) throws BeansException {
        int size = listResponse.size();
        if (MyListUtils.listIsEmpty(listResponse)) {
            for (int i = 0; i < size; i++) {
                AccountListResponse response = listResponse.get(i);
                AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(response.getAccountId(), orgId);
                if (accountAuth != null) {
                    response.setManageAppId(accountAuth.getManageAppId());
                    String manageDeptId = accountAuth.getManageDeptId();
                    List<AccountManageDeptResponse> accountManageDeptResponseList = new ArrayList<>();
                    if (!StringUtils.isEmpty(manageDeptId)) {
                        List<Dept> deptList = this.deptMapper.listDeptByDeptIds(orgId, Arrays.asList(manageDeptId.split(",")));
                        for (Dept dept : deptList) {
                            AccountManageDeptResponse accountManageDeptResponse = new AccountManageDeptResponse();
                            BeanUtils.copyProperties(dept, accountManageDeptResponse);
                            accountManageDeptResponseList.add(accountManageDeptResponse);
                        }
                    }
                    response.setManageDepts(accountManageDeptResponseList);
                    List<AccountManageDeviceResponse> accountManageDeviceResponseList = new ArrayList<>();
                    if (!StringUtils.isEmpty(accountAuth.getManageDeviceSn())) {
                        List<Device> deviceList = this.deviceMapper.listDeviceByDeviceSns(orgId, Arrays.asList(accountAuth.getManageDeviceSn().split(",")));
                        for (Device device : deviceList) {
                            AccountManageDeviceResponse accountManageDeviceResponse = new AccountManageDeviceResponse();
                            BeanUtils.copyProperties(device, accountManageDeviceResponse);
                            accountManageDeviceResponseList.add(accountManageDeviceResponse);
                        }
                    }
                    response.setManageDevices(accountManageDeviceResponseList);
                    response.setManageModuleId(accountAuth.getManageModuleId());
                }
                List<AccountManageGroupResponse> accountManageGroupResponseList = new ArrayList<>();
                List<AccountGroup> accountGroupList = this.accountGroupMapper.listByAccountId(orgId, response.getAccountId());
                if (!CollectionUtils.isEmpty(accountGroupList)) {
                    List<Long> groupIdList = new ArrayList<>();
                    for (AccountGroup accountGroup : accountGroupList) {
                        groupIdList.add(accountGroup.getGroupId());
                    }
                    List<GroupDeviceGroupDto> groupList = this.groupMapper.listByGroupId(groupIdList, orgId);
                    for (GroupDeviceGroupDto groupDeviceGroupDto : groupList) {
                        AccountManageGroupResponse accountManageGroupResponse = new AccountManageGroupResponse();
                        BeanUtils.copyProperties(groupDeviceGroupDto, accountManageGroupResponse);
                        accountManageGroupResponseList.add(accountManageGroupResponse);
                    }
                }
                response.setManageGroups(accountManageGroupResponseList);
                Member member = this.memberMapper.getMemberInfoByMemberId(response.getMemberId(), orgId);
                if (member != null) {
                    response.setPassword("");
                    response.setMemberName(member.getMemberName());
                    response.setMemberMobile(member.getMemberMobile());
                }
            }
        }
    }

    private boolean checkSaveAccountRequest(SaveAccountRequest request) {
        AssertUtil.isNullOrEmpty(request.getAccountName(), OnpremiseErrorEnum.ACCOUNT_NAME_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getManageAppId() != null), OnpremiseErrorEnum.ACCOUNT_MANAGE_APP_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getManageDeptId() != null), OnpremiseErrorEnum.ACCOUNT_MANAGE_DEPT_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getManageDeviceSn() != null), OnpremiseErrorEnum.ACCOUNT_MANAGE_DEVICE_MUST_NOT_NULL);
        Account account = this.accountMapper.getAccountInfoByName(request.getAccountName(), request.getOrgId());
        if (request.getAccountId() == null || request.getAccountId().longValue() <= 0) {
            AssertUtil.isTrue(Boolean.valueOf(account == null), OnpremiseErrorEnum.ACCOUNT_NAME_ALREADY_EXIST);
            return true;
        }
        AssertUtil.isTrue(Boolean.valueOf(account == null || request.getAccountId().longValue() == account.getAccountId().longValue()), OnpremiseErrorEnum.ACCOUNT_NAME_ALREADY_EXIST);
        return true;
    }

    private OpenApi doInsertOpenApi(Long orgId) {
        OpenApi openApi = new OpenApi();
        openApi.setAppId(System.currentTimeMillis() + "" + RandomUtils.nextInt(2));
        openApi.setAppKey(JwtUtils.generateSecretKey());
        openApi.setGmtValidStart(new Date());
        openApi.setGmtValidEnd(MyDateUtils.addTime(4, new Date(), 20));
        openApi.setOrgId(orgId);
        AssertUtil.isTrue(Boolean.valueOf(this.openApiMapper.insert(openApi) > 0), OnpremiseErrorEnum.GENERATOR_OPENAPI_APPKEY_FAIL);
        return openApi;
    }

    private boolean doInsertOpenApiAccount(Long orgId) {
        Account account = new Account();
        account.setOrgId(orgId);
        account.setAccountName("open_api");
        account.setAccountPassword(MD5Utils.getMD5Code("open_api"));
        account.setAccountGrade(Integer.valueOf(AccountGradeEnum.MAIN_ACCOUNT.getValue()));
        account.setMemberId(0L);
        AssertUtil.isTrue(Boolean.valueOf(this.accountMapper.addAccount(account) > 0), OnpremiseErrorEnum.ADD_ACCOUNT_FAIL);
        return true;
    }

    private void doSendNettyMsg(Long orgId, Long accountId) {
        Account account = this.accountMapper.getAccountInfo(accountId, orgId);
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        if (account.getAccountGrade() != null && (AccountGradeEnum.MAIN_ACCOUNT.getValue() == account.getAccountGrade().intValue() || AccountGradeEnum.SUPER_ACCOUNT.getValue() == account.getAccountGrade().intValue())) {
            doSendNettyMsg(orgId);
            return;
        }
        AccountAuth auth = this.accountAuthMapper.getOneByAccountId(accountId, orgId);
        if (auth != null && auth.getManageDeviceSn() != null) {
            doSendNettyMsgByDeviceSn(orgId, auth.getManageDeviceSn());
        }
    }

    private void doSendNettyMsg(Long orgId) {
        Account account = this.accountMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncAccountRequest(Long.valueOf(account == null ? 0L : account.getGmtModify().getTime()), orgId), Integer.valueOf(SyncAccountRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private void doSendNettyMsgByDeviceSn(Long orgId, String deviceSns) {
        if (deviceSns != null) {
            String[] deviceSnArray = deviceSns.split(",");
            if (MyListUtils.arrayIsEmpty(deviceSnArray)) {
                Account account = this.accountMapper.getLastModify(orgId);
                for (String deviceSn : deviceSnArray) {
                    this.nettyMessageApi.sendMsg(new SyncAccountRequest(Long.valueOf(account == null ? 0L : account.getGmtModify().getTime()), orgId), Integer.valueOf(SyncAccountRequest.MODEL_TYPE.type()), deviceSn);
                }
            }
        }
    }

    private boolean doSaveAccountAuth(SaveAccountRequest request, Integer accountGrade, Integer oldAccountGrade) {
        AccountAuth auth = this.accountAuthMapper.getOneByAccountId(request.getAccountId(), request.getOrgId());
        String deviceSns = "";
        if (auth == null) {
            AccountAuth auth2 = new AccountAuth();
            auth2.setOrgId(request.getOrgId());
            auth2.setAccountId(request.getAccountId());
            auth2.setManageAppId(request.getManageAppId());
            auth2.setManageDeptId(request.getManageDeptId());
            auth2.setManageDeviceSn(request.getManageDeviceSn());
            auth2.setManageModuleId(request.getManageModuleId());
            AssertUtil.isTrue(Boolean.valueOf(this.accountAuthMapper.insertAccountAuth(auth2) > 0), OnpremiseErrorEnum.SAVE_ACCOUNT_AUTH_FAIL);
            deviceSns = request.getManageDeviceSn();
        } else {
            if (auth.getManageDeviceSn() != null && auth.getManageDeviceSn().trim().length() > 0) {
                deviceSns = auth.getManageDeviceSn();
            }
            if (request.getManageDeviceSn() != null && request.getManageDeviceSn().trim().length() > 0) {
                if (deviceSns.trim().length() == 0) {
                    deviceSns = request.getManageDeviceSn();
                } else {
                    deviceSns = deviceSns + "," + request.getManageDeviceSn();
                }
            }
            auth.setManageAppId(request.getManageAppId());
            auth.setManageDeptId(request.getManageDeptId());
            auth.setManageDeviceSn(request.getManageDeviceSn());
            auth.setManageModuleId(request.getManageModuleId());
            AssertUtil.isTrue(Boolean.valueOf(this.accountAuthMapper.updateAccountAuth(auth) > 0), OnpremiseErrorEnum.SAVE_ACCOUNT_AUTH_FAIL);
        }
        if (AccountGradeEnum.MAIN_ACCOUNT.getValue() == accountGrade.intValue() || AccountGradeEnum.MAIN_ACCOUNT.getValue() == oldAccountGrade.intValue() || AccountGradeEnum.SUPER_ACCOUNT.getValue() == accountGrade.intValue() || AccountGradeEnum.SUPER_ACCOUNT.getValue() == oldAccountGrade.intValue()) {
            Member member = this.memberMapper.getLastModify(request.getOrgId());
            List<String> deviceSnList = this.deviceMapper.listDeviceSn(request.getOrgId());
            if (CollectionUtils.isEmpty(deviceSnList)) {
                return true;
            }
            this.nettyMessageApi.sendMsg(new SyncAccountRequest(Long.valueOf(member == null ? 0L : member.getGmtModify().getTime()), request.getOrgId()), Integer.valueOf(SyncAccountRequest.MODEL_TYPE.type()), deviceSnList);
            return true;
        }
        doSendNettyMsgByDeviceSn(request.getOrgId(), deviceSns);
        return true;
    }

    private String getDeptId(String deptIds, Long orgId) {
        if (StringUtils.isEmpty(deptIds)) {
            return "";
        }
        List<Long> allDeptIdList = new ArrayList<>();
        List<Long> selectedDeptIdList = new ArrayList<>();
        String[] selectedDeptIdArr = deptIds.split(",");
        List<Long> childDeptIdList = new ArrayList<>();
        for (String deptId : selectedDeptIdArr) {
            if (!StringUtils.isEmpty(deptId)) {
                selectedDeptIdList.add(Long.valueOf(Long.parseLong(deptId)));
                this.deptService.packagingChildDept(Long.valueOf(deptId), orgId, childDeptIdList);
            }
        }
        allDeptIdList.addAll(selectedDeptIdList);
        if (!CollectionUtils.isEmpty(childDeptIdList)) {
            allDeptIdList.addAll(childDeptIdList);
        }
        String result = JsonUtils.toJson(allDeptIdList);
        return result.substring(1, result.length() - 1);
    }
}
