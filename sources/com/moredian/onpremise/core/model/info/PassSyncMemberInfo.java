package com.moredian.onpremise.core.model.info;

import com.moredian.onpremise.core.model.response.TerminalSyncMemberResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/PassSyncMemberInfo.class */
public class PassSyncMemberInfo {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) PassSyncMemberInfo.class);
    public static Map<Long, TerminalSyncMemberResponse> INSERT = new ConcurrentHashMap(16);
    public static Map<Long, TerminalSyncMemberResponse> MODIFY = new ConcurrentHashMap(16);
    public static Map<Long, TerminalSyncMemberResponse> DELETE = new ConcurrentHashMap(16);
    public static Map<Long, TerminalSyncMemberResponse> INSERT_RESULT = new ConcurrentHashMap(16);
    public static Map<Long, TerminalSyncMemberResponse> MODIFY_RESULT = new ConcurrentHashMap(16);
    public static Map<Long, TerminalSyncMemberResponse> DELETE_RESULT = new ConcurrentHashMap(16);

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PassSyncMemberInfo)) {
            return false;
        }
        PassSyncMemberInfo other = (PassSyncMemberInfo) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(Object other) {
        return other instanceof PassSyncMemberInfo;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "PassSyncMemberInfo()";
    }

    public static void doPassSyncMember(TerminalSyncMemberResponse syncMember) {
        if (DELETE.get(syncMember.getMemberId()) != null && INSERT.get(syncMember.getMemberId()) != null) {
            DELETE.remove(syncMember.getMemberId());
            INSERT.remove(syncMember.getMemberId());
        }
        if (DELETE.get(syncMember.getMemberId()) != null && MODIFY.get(syncMember.getMemberId()) != null) {
            MODIFY.remove(syncMember.getMemberId());
        }
        if (INSERT.get(syncMember.getMemberId()) != null && MODIFY.get(syncMember.getMemberId()) != null) {
            INSERT.remove(syncMember.getMemberId());
        }
        if (DELETE.get(syncMember.getMemberId()) != null && INSERT_RESULT.get(syncMember.getMemberId()) != null) {
            DELETE.remove(syncMember.getMemberId());
            INSERT_RESULT.remove(syncMember.getMemberId());
            if (MODIFY_RESULT.get(syncMember.getMemberId()) == null) {
                MODIFY_RESULT.put(syncMember.getMemberId(), syncMember);
            }
        }
        if (DELETE.get(syncMember.getMemberId()) != null && MODIFY_RESULT.get(syncMember.getMemberId()) != null) {
            DELETE.remove(syncMember.getMemberId());
        }
        if (INSERT.get(syncMember.getMemberId()) != null && DELETE_RESULT.get(syncMember.getMemberId()) != null) {
            INSERT.remove(syncMember.getMemberId());
            DELETE_RESULT.remove(syncMember.getMemberId());
            if (MODIFY_RESULT.get(syncMember.getMemberId()) == null) {
                MODIFY_RESULT.put(syncMember.getMemberId(), syncMember);
            }
        }
        if (INSERT.get(syncMember.getMemberId()) != null && MODIFY_RESULT.get(syncMember.getMemberId()) != null) {
            INSERT.remove(syncMember.getMemberId());
        }
        if (MODIFY.get(syncMember.getMemberId()) != null && DELETE_RESULT.get(syncMember.getMemberId()) != null) {
            DELETE_RESULT.remove(syncMember.getMemberId());
        }
        if (MODIFY.get(syncMember.getMemberId()) != null && INSERT_RESULT.get(syncMember.getMemberId()) != null) {
            INSERT_RESULT.remove(syncMember.getMemberId());
        }
    }
}
