package org.apache.xmlbeans.impl.jam.internal;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.xmlbeans.impl.jam.JAnnotation;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JConstructor;
import org.apache.xmlbeans.impl.jam.JElement;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JamClassIterator;
import org.springframework.beans.PropertyAccessor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/JamPrinter.class */
public class JamPrinter {
    private static final String INDENT = "  ";

    public static JamPrinter newInstance() {
        return new JamPrinter();
    }

    private JamPrinter() {
    }

    public void print(JElement root, PrintWriter out) {
        print(root, 0, out);
    }

    public void print(JamClassIterator iter, PrintWriter out) {
        while (iter.hasNext()) {
            JClass clazz = iter.nextClass();
            out.println("------------------------------");
            out.println(clazz.getQualifiedName());
            out.println("------------------------------");
            print(clazz, out);
            out.println();
        }
    }

    private void print(JElement a, int indent, PrintWriter out) {
        indent(indent, out);
        out.print(PropertyAccessor.PROPERTY_KEY_PREFIX);
        out.print(getTypeKey(a));
        out.print("] ");
        if (a instanceof JMethod) {
            out.print(((JMethod) a).getReturnType().getFieldDescriptor());
            out.print(SymbolConstants.SPACE_SYMBOL);
            out.println(a.getSimpleName());
        } else {
            out.println(a.getSimpleName());
        }
        int i = indent + 1;
    }

    private void print(JAnnotation[] atts, int indent, PrintWriter out) {
        for (int i = 0; i < atts.length; i++) {
            indent(indent, out);
            out.print("<");
            out.print(getTypeKey(atts[i]));
            out.print("> ");
            out.print(atts[i].getSimpleName());
        }
    }

    private void indent(int indent, PrintWriter out) {
        for (int i = 0; i < indent; i++) {
            out.print(INDENT);
        }
    }

    private String getTypeKey(Object o) {
        if (o == null) {
            return "[?UNKNOWN!]";
        }
        String type = o.getClass().getName();
        int lastDot = type.lastIndexOf(".");
        if (lastDot != -1 && lastDot + 1 < type.length()) {
            type = type.substring(lastDot + 1);
        }
        return type;
    }

    private static JElement[] getChildrenFor(JElement parent) {
        Collection list = new ArrayList();
        if (parent instanceof JClass) {
            list.addAll(Arrays.asList(((JClass) parent).getDeclaredFields()));
            list.addAll(Arrays.asList(((JClass) parent).getDeclaredMethods()));
            list.addAll(Arrays.asList(((JClass) parent).getConstructors()));
            list.addAll(Arrays.asList(((JClass) parent).getClasses()));
        } else if (parent instanceof JConstructor) {
            list.addAll(Arrays.asList(((JConstructor) parent).getParameters()));
        } else if (parent instanceof JMethod) {
            list.addAll(Arrays.asList(((JMethod) parent).getParameters()));
        }
        JElement[] out = new JElement[list.size()];
        list.toArray(out);
        return out;
    }
}
