package org.aspectj.asm;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;
import org.aspectj.weaver.model.AsmRelationshipUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IRelationship.class */
public interface IRelationship extends Serializable {
    String getName();

    Kind getKind();

    void addTarget(String str);

    List<String> getTargets();

    String getSourceHandle();

    boolean hasRuntimeTest();

    boolean isAffects();

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/asm/IRelationship$Kind.class */
    public static class Kind implements Serializable {
        private static final long serialVersionUID = -2691351740214705220L;
        private final String name;
        private final int ordinal;
        public static final Kind DECLARE_WARNING = new Kind("declare warning");
        public static final Kind DECLARE_ERROR = new Kind("declare error");
        public static final Kind ADVICE_AROUND = new Kind("around advice");
        public static final Kind ADVICE_AFTERRETURNING = new Kind("after returning advice");
        public static final Kind ADVICE_AFTERTHROWING = new Kind("after throwing advice");
        public static final Kind ADVICE_AFTER = new Kind("after advice");
        public static final Kind ADVICE_BEFORE = new Kind("before advice");
        public static final Kind ADVICE = new Kind("advice");
        public static final Kind DECLARE = new Kind(AsmRelationshipUtils.DEC_LABEL);
        public static final Kind DECLARE_INTER_TYPE = new Kind("inter-type declaration");
        public static final Kind USES_POINTCUT = new Kind("uses pointcut");
        public static final Kind DECLARE_SOFT = new Kind("declare soft");
        public static final Kind[] ALL = {DECLARE_WARNING, DECLARE_ERROR, ADVICE_AROUND, ADVICE_AFTERRETURNING, ADVICE_AFTERTHROWING, ADVICE_AFTER, ADVICE_BEFORE, ADVICE, DECLARE, DECLARE_INTER_TYPE, USES_POINTCUT, DECLARE_SOFT};
        private static int nextOrdinal = 0;

        public boolean isDeclareKind() {
            return this == DECLARE_WARNING || this == DECLARE_ERROR || this == DECLARE || this == DECLARE_INTER_TYPE || this == DECLARE_SOFT;
        }

        public String getName() {
            return this.name;
        }

        public static Kind getKindFor(String stringFormOfRelationshipKind) {
            for (int i = 0; i < ALL.length; i++) {
                if (ALL[i].name.equals(stringFormOfRelationshipKind)) {
                    return ALL[i];
                }
            }
            return null;
        }

        private Kind(String name) {
            int i = nextOrdinal;
            nextOrdinal = i + 1;
            this.ordinal = i;
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        private Object readResolve() throws ObjectStreamException {
            return ALL[this.ordinal];
        }
    }
}
