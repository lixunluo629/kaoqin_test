package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AccountGroup;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AccountGroupMapper.class */
public interface AccountGroupMapper extends Mapper<AccountGroup> {
    int insertAccountGroup(AccountGroup accountGroup);

    int insertBatchAccountGroup(@Param("list") List<AccountGroup> list);

    int deleteByAccountId(@Param("orgId") Long l, @Param("accountId") Long l2);

    int deleteByGroupId(@Param("orgId") Long l, @Param("groupId") Long l2);

    List<AccountGroup> listByAccountId(@Param("orgId") Long l, @Param("accountId") Long l2);

    AccountGroup findByAccountIdAndGroupId(@Param("orgId") Long l, @Param("accountId") Long l2, @Param("groupId") Long l3);
}
