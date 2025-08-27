package com.moredian.onpremise.api.member;

import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.DeleteDeptRequest;
import com.moredian.onpremise.core.model.request.DeptDetailRequest;
import com.moredian.onpremise.core.model.request.ListChildImmediateDeptRequest;
import com.moredian.onpremise.core.model.request.ListDeptRequest;
import com.moredian.onpremise.core.model.request.SaveDeptRequest;
import com.moredian.onpremise.core.model.request.TerminalDeptRequest;
import com.moredian.onpremise.core.model.response.DeptDetailResponse;
import com.moredian.onpremise.core.model.response.ListChildImmediateDeptResponse;
import com.moredian.onpremise.core.model.response.ListDeptNoConstructureResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponseV2;
import com.moredian.onpremise.core.model.response.SaveDeptResponse;
import com.moredian.onpremise.core.model.response.TerminalDeptResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/member/DeptService.class */
public interface DeptService {
    SaveDeptResponse update(SaveDeptRequest saveDeptRequest);

    SaveDeptResponse insert(SaveDeptRequest saveDeptRequest);

    boolean insertSuperDeptGroup(Dept dept);

    boolean insertSuperDeptAttendanceGroup(Dept dept);

    boolean insertSuperDeptCheckIn(Dept dept);

    boolean insertSuperDeptMealCanteen(Dept dept);

    DeptDetailResponse getDeptDetail(DeptDetailRequest deptDetailRequest);

    boolean deleteDept(DeleteDeptRequest deleteDeptRequest);

    List<ListDeptResponse> listDept(ListDeptRequest listDeptRequest);

    List<ListDeptNoConstructureResponse> listDeptNoConstructure(BaseRequest baseRequest);

    PageList<ListChildImmediateDeptResponse> listChildImmediateDept(ListChildImmediateDeptRequest listChildImmediateDeptRequest);

    List<Long> packagingChildDept(Long l, Long l2, List<Long> list);

    String packagingDeptName(String str, boolean z);

    TerminalDeptResponse listDeptForDevice(TerminalDeptRequest terminalDeptRequest);

    List<ListDeptResponseV2> listDeptV2(ListDeptRequest listDeptRequest);

    String findSuperId(Long l, Integer num);
}
