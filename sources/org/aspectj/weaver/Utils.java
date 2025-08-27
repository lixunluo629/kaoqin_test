package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Utils.class */
public class Utils {
    public static boolean isSuppressing(AnnotationAJ[] anns, String lintkey) {
        String value;
        if (anns == null) {
            return false;
        }
        for (int i = 0; i < anns.length; i++) {
            if (UnresolvedType.SUPPRESS_AJ_WARNINGS.getSignature().equals(anns[i].getTypeSignature()) && ((value = anns[i].getStringFormOfValue("value")) == null || value.indexOf(lintkey) != -1)) {
                return true;
            }
        }
        return false;
    }
}
