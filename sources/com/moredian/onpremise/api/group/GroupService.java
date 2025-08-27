package com.moredian.onpremise.api.group;

import com.moredian.onpremise.core.model.request.DeleteGroupAuthRequest;
import com.moredian.onpremise.core.model.request.GetGroupAuthOneRequest;
import com.moredian.onpremise.core.model.request.GroupAuthListRequest;
import com.moredian.onpremise.core.model.request.SaveGroupAuthRequest;
import com.moredian.onpremise.core.model.request.TerminalCheckPrivilegeRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.UpdateGroupMemberRequest;
import com.moredian.onpremise.core.model.response.GroupAuthListResponse;
import com.moredian.onpremise.core.model.response.GroupAuthResponse;
import com.moredian.onpremise.core.model.response.GroupListResponse;
import com.moredian.onpremise.core.model.response.SaveGroupAuthResponse;
import com.moredian.onpremise.core.model.response.TerminalCheckPrivilegeResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncGroupResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.UpdateGroupMemberResponse;
import com.moredian.onpremise.core.utils.PageList;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/group/GroupService.class */
public interface GroupService {
    SaveGroupAuthResponse updateGroup(SaveGroupAuthRequest saveGroupAuthRequest);

    SaveGroupAuthResponse insertGroup(SaveGroupAuthRequest saveGroupAuthRequest);

    boolean deleteGroup(DeleteGroupAuthRequest deleteGroupAuthRequest);

    PageList<GroupListResponse> groupList(GroupAuthListRequest groupAuthListRequest);

    PageList<GroupAuthListResponse> groupAuthList(GroupAuthListRequest groupAuthListRequest);

    GroupAuthResponse getGroupAuthById(GetGroupAuthOneRequest getGroupAuthOneRequest);

    TerminalSyncResponse<TerminalSyncGroupResponse> syncGroup(TerminalSyncRequest terminalSyncRequest);

    TerminalCheckPrivilegeResponse checkPrivilege(TerminalCheckPrivilegeRequest terminalCheckPrivilegeRequest);

    UpdateGroupMemberResponse updateGroupMember(UpdateGroupMemberRequest updateGroupMemberRequest);
}
