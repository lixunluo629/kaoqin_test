package com.moredian.onpremise.api.server;

import com.moredian.onpremise.core.model.request.DeleteCallbackServerRequest;
import com.moredian.onpremise.core.model.request.ListCallbackServersRequest;
import com.moredian.onpremise.core.model.request.QueryCllbackServerRequest;
import com.moredian.onpremise.core.model.request.SaveCallbackServerRequest;
import com.moredian.onpremise.core.model.response.CallbackServerResponse;
import com.moredian.onpremise.core.model.response.ListCallbackTagResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/server/CallbackServerService.class */
public interface CallbackServerService {
    boolean save(SaveCallbackServerRequest saveCallbackServerRequest);

    boolean delete(DeleteCallbackServerRequest deleteCallbackServerRequest);

    CallbackServerResponse getOneByTag(QueryCllbackServerRequest queryCllbackServerRequest);

    PageList<CallbackServerResponse> listCallbackServers(ListCallbackServersRequest listCallbackServersRequest);

    List<ListCallbackTagResponse> listTag();
}
