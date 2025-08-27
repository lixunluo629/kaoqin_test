package org.aspectj.bridge;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import redis.clients.jedis.Protocol;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/IMessage.class */
public interface IMessage {
    public static final IMessage[] RA_IMessage = new IMessage[0];
    public static final Kind WEAVEINFO = new Kind("weaveinfo", 5);
    public static final Kind INFO = new Kind(Protocol.CLUSTER_INFO, 10);
    public static final Kind DEBUG = new Kind("debug", 20);
    public static final Kind TASKTAG = new Kind("task", 25);
    public static final Kind WARNING = new Kind(AsmRelationshipUtils.DECLARE_WARNING, 30);
    public static final Kind ERROR = new Kind(AsmRelationshipUtils.DECLARE_ERROR, 40);
    public static final Kind FAIL = new Kind(com.moredian.onpremise.core.common.constants.Constants.FAIL, 50);
    public static final Kind ABORT = new Kind("abort", 60);
    public static final List<Kind> KINDS = Collections.unmodifiableList(Arrays.asList(WEAVEINFO, INFO, DEBUG, TASKTAG, WARNING, ERROR, FAIL, ABORT));

    String getMessage();

    Kind getKind();

    boolean isError();

    boolean isWarning();

    boolean isDebug();

    boolean isInfo();

    boolean isAbort();

    boolean isTaskTag();

    boolean isFailed();

    boolean getDeclared();

    int getID();

    int getSourceStart();

    int getSourceEnd();

    Throwable getThrown();

    ISourceLocation getSourceLocation();

    String getDetails();

    List<ISourceLocation> getExtraSourceLocations();

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/IMessage$Kind.class */
    public static final class Kind implements Comparable<Kind> {
        public static final Comparator<Kind> COMPARATOR = new Comparator<Kind>() { // from class: org.aspectj.bridge.IMessage.Kind.1
            @Override // java.util.Comparator
            public int compare(Kind one, Kind two) {
                if (null == one) {
                    return null == two ? 0 : -1;
                }
                if (null == two) {
                    return 1;
                }
                if (one != two) {
                    return one.precedence - two.precedence;
                }
                return 0;
            }
        };
        private final int precedence;
        private final String name;

        public boolean isSameOrLessThan(Kind kind) {
            return 0 >= COMPARATOR.compare(this, kind);
        }

        @Override // java.lang.Comparable
        public int compareTo(Kind other) {
            return COMPARATOR.compare(this, other);
        }

        private Kind(String name, int precedence) {
            this.name = name;
            this.precedence = precedence;
        }

        public String toString() {
            return this.name;
        }
    }
}
