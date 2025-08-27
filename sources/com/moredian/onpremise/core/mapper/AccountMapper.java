package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.request.DeleteAccountRequest;
import com.moredian.onpremise.core.model.request.ListAccountRequest;
import com.moredian.onpremise.core.model.request.ModifyPasswordRequest;
import com.moredian.onpremise.core.model.request.SaveAccountRequest;
import com.moredian.onpremise.core.model.response.AccountListResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAccountResponse;
import com.mysql.jdbc.NonRegisteringDriver;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AccountMapper.class */
public interface AccountMapper {
    Account loginByUserNameAndPassword(@Param("userName") String str, @Param(NonRegisteringDriver.PASSWORD_PROPERTY_KEY) String str2);

    int deleteAccount(DeleteAccountRequest deleteAccountRequest);

    int modifyPassword(ModifyPasswordRequest modifyPasswordRequest);

    int saveAccount(SaveAccountRequest saveAccountRequest);

    int addAccount(Account account);

    Account getAccountInfo(@Param("accountId") Long l, @Param("orgId") Long l2);

    Account getAccountInfoIncludDelete(@Param("accountId") Long l, @Param("orgId") Long l2);

    Account getAccountInfoByName(@Param("accountName") String str, @Param("orgId") Long l);

    List<AccountListResponse> listAccount(ListAccountRequest listAccountRequest);

    Account getAccountByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    int updateFirstLoginStatus(@Param("accountId") Long l, @Param("orgId") Long l2);

    int unbindMember(@Param("memberId") Long l, @Param("orgId") Long l2);

    int updateLanguage(@Param("accountId") Long l, @Param("languageType") String str, @Param("orgId") Long l2);

    List<Account> listManagerAccount(@Param("orgId") Long l, @Param("queryDate") String str);

    List<Account> listNotMemberAccount(@Param("orgId") Long l);

    List<TerminalSyncAccountResponse> listSyncAccount(@Param("orgId") Long l, @Param("date") String str, @Param("deviceSn") String str2);

    Account getLastModify(@Param("orgId") Long l);
}
