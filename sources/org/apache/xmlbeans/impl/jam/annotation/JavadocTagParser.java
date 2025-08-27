package org.apache.xmlbeans.impl.jam.annotation;

import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Tag;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotation;
import org.apache.xmlbeans.impl.jam.mutable.MSourcePosition;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;
import org.apache.xmlbeans.impl.jam.provider.JamServiceContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/annotation/JavadocTagParser.class */
public abstract class JavadocTagParser {
    private JamServiceContext mContext = null;
    private boolean mAddSingleValueMembers = false;

    public abstract void parse(MAnnotatedElement mAnnotatedElement, Tag tag);

    public void setAddSingleValueMembers(boolean b) {
        this.mAddSingleValueMembers = b;
    }

    public void init(JamServiceContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("null logger");
        }
        if (this.mContext != null) {
            throw new IllegalStateException("JavadocTagParser.init() called twice");
        }
        this.mContext = ctx;
    }

    protected MAnnotation[] createAnnotations(MAnnotatedElement target, Tag tag) {
        String tagName = tag.name().trim().substring(1);
        MAnnotation current = target.getMutableAnnotation(tagName);
        if (current == null) {
            current = target.findOrCreateAnnotation(tagName);
            setPosition(current, tag);
        }
        MAnnotation literal = target.addLiteralAnnotation(tagName);
        setPosition(literal, tag);
        MAnnotation[] out = {literal, current};
        if (this.mAddSingleValueMembers) {
            setSingleValueText(out, tag);
        }
        return out;
    }

    protected void setValue(MAnnotation[] anns, String memberName, String value) {
        String value2 = value.trim();
        String memberName2 = memberName.trim();
        for (int i = 0; i < anns.length; i++) {
            if (anns[i].getValue(memberName2) == null) {
                anns[i].setSimpleValue(memberName2, value2, getStringType());
            }
        }
    }

    protected JamLogger getLogger() {
        return this.mContext.getLogger();
    }

    protected JClass getStringType() {
        return ((ElementContext) this.mContext).getClassLoader().loadClass("java.lang.String");
    }

    protected void setSingleValueText(MAnnotation[] targets, Tag tag) {
        String tagText = tag.text();
        for (MAnnotation mAnnotation : targets) {
            mAnnotation.setSimpleValue("value", tagText, getStringType());
        }
    }

    private void setPosition(MAnnotation target, Tag tag) {
        SourcePosition pos = tag.position();
        if (pos != null) {
            MSourcePosition mpos = target.createSourcePosition();
            mpos.setLine(pos.line());
            mpos.setColumn(pos.column());
            if (pos.file() != null) {
                mpos.setSourceURI(pos.file().toURI());
            }
        }
    }
}
