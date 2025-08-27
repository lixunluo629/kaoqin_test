package com.moredian.onpremise.api.account;

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
import com.moredian.onpremise.core.model.response.AccountOrgInfoResponse;
import com.moredian.onpremise.core.model.response.AuthModuleResponse;
import com.moredian.onpremise.core.model.response.OpenApiAppKeyResponse;
import com.moredian.onpremise.core.model.response.OpenApiAppTokenResponse;
import com.moredian.onpremise.core.model.response.OperLogResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAccountResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/account/AccountService.class */
public interface AccountService {
    UserLoginResponse login(UserLoginRequest userLoginRequest);

    UserLoginResponse loginV2(UserLoginRequestV2 userLoginRequestV2);

    boolean terminalLogin(TerminalLoginRequest terminalLoginRequest);

    boolean logout(BaseRequest baseRequest);

    AccountOrgInfoResponse getAccountOrgInfo(BaseRequest baseRequest);

    boolean updateOrgInfo(UpdateOrgInfoRequest updateOrgInfoRequest);

    PageList<AccountListResponse> listAccount(ListAccountRequest listAccountRequest);

    Long updateAccount(SaveAccountRequest saveAccountRequest);

    Long insertAccount(SaveAccountRequest saveAccountRequest);

    boolean deleteAccount(DeleteAccountRequest deleteAccountRequest);

    boolean modifyPassword(ModifyPasswordRequest modifyPasswordRequest);

    OpenApiAppKeyResponse getOpenApiAppKey(OpenApiAppKeyRequest openApiAppKeyRequest);

    OpenApiAppKeyResponse generatorOpenApiAppKey(OpenApiAppKeyRequest openApiAppKeyRequest);

    OpenApiAppKeyResponse generatorOpenApiAppKeyV2(OpenApiAppKeyRequest openApiAppKeyRequest);

    OpenApiAppTokenResponse getOpenApiToken(OpenApiAppTokenRequest openApiAppTokenRequest);

    OpenApiAppTokenResponse getOpenApiTokenV2(OpenApiAppTokenRequest openApiAppTokenRequest);

    Long resetPassword(ResetPasswordRequest resetPasswordRequest);

    boolean updateLanguage(UpdateLanguageRequest updateLanguageRequest);

    List<AuthModuleResponse> listAccountManageModule(BaseRequest baseRequest);

    List<TerminalSyncAccountResponse> syncAccount(TerminalSyncRequest terminalSyncRequest);

    void resetPasswordEncode();

    PageList<OperLogResponse> operLogPageList(OperLogListRequest operLogListRequest);

    Long cloneAccount(CloneAccountRequest cloneAccountRequest);
}
