package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.request.CountInfoForDayRequest;
import com.moredian.onpremise.core.model.response.DeptDetailResponse;
import com.moredian.onpremise.core.model.response.ListChildImmediateDeptResponse;
import com.moredian.onpremise.core.model.response.ListDeptNoConstructureResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponse;
import com.moredian.onpremise.core.model.response.ListInfoForDayResponse;
import com.moredian.onpremise.core.model.response.QueryDeptAndMemberResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/DeptMapper.class */
public interface DeptMapper {
    int updateDept(Dept dept);

    int updateChildDept(Dept dept);

    int insertDept(Dept dept);

    int deleteDept(@Param("deptId") Long l);

    DeptDetailResponse getDeptDetail(@Param("deptId") Long l);

    List<ListDeptResponse> getTopDepts(@Param("orgId") Long l, @Param("deptIds") List<String> list);

    Dept getTopDept(@Param("orgId") Long l);

    List<ListDeptResponse> listChildDept(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<Dept> listChildDeptToReturnDept(@Param("deptId") Long l, @Param("orgId") Long l2);

    int getDeptCount(@Param("orgId") Long l);

    List<QueryDeptAndMemberResponse> getDeptByName(@Param("orgId") Long l, @Param("name") String str, @Param("manageDeptIds") List<String> list);

    List<ListDeptNoConstructureResponse> listDeptNoConstructure(@Param("orgId") Long l, @Param("deptIds") List<String> list);

    Dept getDeptByDeptName(@Param("orgId") Long l, @Param("deptName") String str, @Param("superDeptId") Long l2, @Param("deptGrade") Integer num);

    Dept getDeptById(@Param("deptId") Long l);

    List<ListChildImmediateDeptResponse> listChildImmediateDept(@Param("orgId") Long l, @Param("deptId") Long l2);

    List<Dept> listByDeptIds(@Param("orgId") Long l, @Param("deptIds") List<String> list);

    int updateTopDept(@Param("orgName") String str, @Param("orgId") Long l);

    int updateFirstDeptName(@Param("deptId") Long l, @Param("deptName") String str, @Param("orgId") Long l2);

    List<String> listDeptNameByDeptIds(@Param("orgId") Long l, @Param("deptIds") List<String> list);

    List<Dept> listDeptByDeptIds(@Param("orgId") Long l, @Param("deptIds") List<String> list);

    List<ListInfoForDayResponse> listCountForDayDept(CountInfoForDayRequest countInfoForDayRequest);

    List<Long> listChildDeptIds(@Param("superDeptId") Long l, @Param("orgId") Long l2);

    List<Long> listOtherChildDeptIds(@Param("superDeptId") Long l, @Param("orgId") Long l2, @Param("deptId") Long l3);

    int countBySuperDeptId(@Param("orgId") Long l, @Param("superDeptId") Long l2);
}
