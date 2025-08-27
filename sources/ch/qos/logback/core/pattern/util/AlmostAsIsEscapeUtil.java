package ch.qos.logback.core.pattern.util;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/pattern/util/AlmostAsIsEscapeUtil.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/pattern/util/AlmostAsIsEscapeUtil.class */
public class AlmostAsIsEscapeUtil extends RestrictedEscapeUtil {
    @Override // ch.qos.logback.core.pattern.util.RestrictedEscapeUtil, ch.qos.logback.core.pattern.util.IEscapeUtil
    public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
        super.escape("%)", buf, next, pointer);
    }
}
