package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AccountAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AccountAuthMapper.class */
public interface AccountAuthMapper {
    int insertAccountAuth(AccountAuth accountAuth);

    AccountAuth getOneByAccountId(@Param("accountId") Long l, @Param("orgId") Long l2);

    int updateAccountAuth(AccountAuth accountAuth);

    int deleteByAccountId(@Param("accountId") Long l, @Param("orgId") Long l2);
}
