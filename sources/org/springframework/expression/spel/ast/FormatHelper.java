package org.springframework.expression.spel.ast;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ClassUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/FormatHelper.class */
public class FormatHelper {
    public static String formatMethodForMessage(String name, List<TypeDescriptor> argumentTypes) {
        StringBuilder sb = new StringBuilder(name);
        sb.append("(");
        for (int i = 0; i < argumentTypes.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            TypeDescriptor typeDescriptor = argumentTypes.get(i);
            if (typeDescriptor != null) {
                sb.append(formatClassNameForMessage(typeDescriptor.getType()));
            } else {
                sb.append(formatClassNameForMessage(null));
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static String formatClassNameForMessage(Class<?> clazz) {
        return clazz != null ? ClassUtils.getQualifiedName(clazz) : "null";
    }
}
