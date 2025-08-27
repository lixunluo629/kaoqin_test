package org.apache.xmlbeans.impl.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlIDREF;
import org.apache.xmlbeans.XmlIDREFS;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.ValidatorListener;
import org.apache.xmlbeans.impl.common.XPath;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IdentityConstraint.class */
public class IdentityConstraint {
    private ConstraintState _constraintStack;
    private ElementState _elementStack;
    private Collection _errorListener;
    private boolean _invalid;
    private boolean _trackIdrefs;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !IdentityConstraint.class.desiredAssertionStatus();
    }

    public IdentityConstraint(Collection errorListener, boolean trackIdrefs) {
        this._errorListener = errorListener;
        this._trackIdrefs = trackIdrefs;
    }

    public void element(ValidatorListener.Event e, SchemaType st, SchemaIdentityConstraint[] ics) {
        newState();
        ConstraintState constraintState = this._constraintStack;
        while (true) {
            ConstraintState cs = constraintState;
            if (cs == null) {
                break;
            }
            cs.element(e, st);
            constraintState = cs._next;
        }
        for (int i = 0; ics != null && i < ics.length; i++) {
            newConstraintState(ics[i], e, st);
        }
    }

    public void endElement(ValidatorListener.Event e) {
        if (this._elementStack._hasConstraints) {
            ConstraintState constraintState = this._constraintStack;
            while (true) {
                ConstraintState cs = constraintState;
                if (cs == null || cs == this._elementStack._savePoint) {
                    break;
                }
                cs.remove(e);
                constraintState = cs._next;
            }
            this._constraintStack = this._elementStack._savePoint;
        }
        this._elementStack = this._elementStack._next;
        ConstraintState constraintState2 = this._constraintStack;
        while (true) {
            ConstraintState cs2 = constraintState2;
            if (cs2 != null) {
                cs2.endElement(e);
                constraintState2 = cs2._next;
            } else {
                return;
            }
        }
    }

    public void attr(ValidatorListener.Event e, QName name, SchemaType st, String value) {
        ConstraintState constraintState = this._constraintStack;
        while (true) {
            ConstraintState cs = constraintState;
            if (cs != null) {
                cs.attr(e, name, st, value);
                constraintState = cs._next;
            } else {
                return;
            }
        }
    }

    public void text(ValidatorListener.Event e, SchemaType st, String value, boolean emptyContent) {
        ConstraintState constraintState = this._constraintStack;
        while (true) {
            ConstraintState cs = constraintState;
            if (cs != null) {
                cs.text(e, st, value, emptyContent);
                constraintState = cs._next;
            } else {
                return;
            }
        }
    }

    public boolean isValid() {
        return !this._invalid;
    }

    private void newConstraintState(SchemaIdentityConstraint ic, ValidatorListener.Event e, SchemaType st) {
        if (ic.getConstraintCategory() == 2) {
            new KeyrefState(ic, e, st);
        } else {
            new SelectorState(ic, e, st);
        }
    }

    private void buildIdStates() {
        IdState ids = new IdState();
        if (this._trackIdrefs) {
            new IdRefState(ids);
        }
    }

    private void newState() {
        boolean firstTime = this._elementStack == null;
        ElementState st = new ElementState();
        st._next = this._elementStack;
        this._elementStack = st;
        if (firstTime) {
            buildIdStates();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void emitError(ValidatorListener.Event event, String code, Object[] args) {
        this._invalid = true;
        if (this._errorListener != null) {
            if (!$assertionsDisabled && event == null) {
                throw new AssertionError();
            }
            this._errorListener.add(errorForEvent(code, args, 0, event));
        }
    }

    public static XmlError errorForEvent(String code, Object[] args, int severity, ValidatorListener.Event event) {
        XmlError error;
        XmlCursor loc = event.getLocationAsCursor();
        if (loc != null) {
            error = XmlError.forCursor(code, args, severity, loc);
        } else {
            Location location = event.getLocation();
            if (location != null) {
                error = XmlError.forLocation(code, args, severity, location.getSystemId(), location.getLineNumber(), location.getColumnNumber(), location.getCharacterOffset());
            } else {
                error = XmlError.forMessage(code, args, severity);
            }
        }
        return error;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void emitError(ValidatorListener.Event event, String msg) {
        this._invalid = true;
        if (this._errorListener != null) {
            if (!$assertionsDisabled && event == null) {
                throw new AssertionError();
            }
            this._errorListener.add(errorForEvent(msg, 0, event));
        }
    }

    public static XmlError errorForEvent(String msg, int severity, ValidatorListener.Event event) {
        XmlError error;
        XmlCursor loc = event.getLocationAsCursor();
        if (loc != null) {
            error = XmlError.forCursor(msg, severity, loc);
        } else {
            Location location = event.getLocation();
            if (location != null) {
                error = XmlError.forLocation(msg, severity, location.getSystemId(), location.getLineNumber(), location.getColumnNumber(), location.getCharacterOffset());
            } else {
                error = XmlError.forMessage(msg, severity);
            }
        }
        return error;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSavePoint(ConstraintState cs) {
        if (!this._elementStack._hasConstraints) {
            this._elementStack._savePoint = cs;
        }
        this._elementStack._hasConstraints = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static XmlObject newValue(SchemaType st, String value) {
        try {
            return st.newValue(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    static SchemaType getSimpleType(SchemaType st) {
        if (!$assertionsDisabled && !st.isSimpleType() && st.getContentType() != 2) {
            throw new AssertionError(st + " does not have simple content.");
        }
        while (!st.isSimpleType()) {
            st = st.getBaseType();
        }
        return st;
    }

    static boolean hasSimpleContent(SchemaType st) {
        return st.isSimpleType() || st.getContentType() == 2;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IdentityConstraint$ConstraintState.class */
    public abstract class ConstraintState {
        ConstraintState _next;

        abstract void element(ValidatorListener.Event event, SchemaType schemaType);

        abstract void endElement(ValidatorListener.Event event);

        abstract void attr(ValidatorListener.Event event, QName qName, SchemaType schemaType, String str);

        abstract void text(ValidatorListener.Event event, SchemaType schemaType, String str, boolean z);

        abstract void remove(ValidatorListener.Event event);

        ConstraintState() {
            IdentityConstraint.this.setSavePoint(IdentityConstraint.this._constraintStack);
            this._next = IdentityConstraint.this._constraintStack;
            IdentityConstraint.this._constraintStack = this;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IdentityConstraint$SelectorState.class */
    public class SelectorState extends ConstraintState {
        SchemaIdentityConstraint _constraint;
        Set _values;
        XPath.ExecutionContext _context;

        SelectorState(SchemaIdentityConstraint constraint, ValidatorListener.Event e, SchemaType st) {
            super();
            this._values = new LinkedHashSet();
            this._constraint = constraint;
            this._context = new XPath.ExecutionContext();
            this._context.init((XPath) this._constraint.getSelectorPath());
            if ((this._context.start() & 1) != 0) {
                createFieldState(e, st);
            }
        }

        void addFields(XmlObjectList fields, ValidatorListener.Event e) {
            if (this._constraint.getConstraintCategory() == 2) {
                this._values.add(fields);
                return;
            }
            if (this._values.contains(fields)) {
                if (this._constraint.getConstraintCategory() == 3) {
                    IdentityConstraint.this.emitError(e, XmlErrorCodes.IDENTITY_CONSTRAINT_VALID$DUPLICATE_UNIQUE, new Object[]{fields, QNameHelper.pretty(this._constraint.getName())});
                    return;
                } else {
                    IdentityConstraint.this.emitError(e, XmlErrorCodes.IDENTITY_CONSTRAINT_VALID$DUPLICATE_KEY, new Object[]{fields, QNameHelper.pretty(this._constraint.getName())});
                    return;
                }
            }
            this._values.add(fields);
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void element(ValidatorListener.Event e, SchemaType st) {
            if ((this._context.element(e.getName()) & 1) != 0) {
                createFieldState(e, st);
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void endElement(ValidatorListener.Event e) {
            this._context.end();
        }

        void createFieldState(ValidatorListener.Event e, SchemaType st) {
            IdentityConstraint.this.new FieldState(this, e, st);
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void remove(ValidatorListener.Event e) {
            ConstraintState constraintState = this._next;
            while (true) {
                ConstraintState cs = constraintState;
                if (cs != null) {
                    if (cs instanceof KeyrefState) {
                        KeyrefState kr = (KeyrefState) cs;
                        if (kr._constraint.getReferencedKey() == this._constraint) {
                            kr.addKeyValues(this._values, true);
                        }
                    }
                    constraintState = cs._next;
                } else {
                    return;
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void attr(ValidatorListener.Event e, QName name, SchemaType st, String value) {
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void text(ValidatorListener.Event e, SchemaType st, String value, boolean emptyContent) {
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IdentityConstraint$KeyrefState.class */
    public class KeyrefState extends SelectorState {
        Map _keyValues;
        private Object CHILD_ADDED;
        private Object CHILD_REMOVED;
        private Object SELF_ADDED;

        KeyrefState(SchemaIdentityConstraint constraint, ValidatorListener.Event e, SchemaType st) {
            super(constraint, e, st);
            this._keyValues = new HashMap();
            this.CHILD_ADDED = new Object();
            this.CHILD_REMOVED = new Object();
            this.SELF_ADDED = new Object();
        }

        void addKeyValues(Set values, boolean child) {
            for (Object key : values) {
                Object value = this._keyValues.get(key);
                if (value == null) {
                    this._keyValues.put(key, child ? this.CHILD_ADDED : this.SELF_ADDED);
                } else if (value == this.CHILD_ADDED) {
                    if (child) {
                        this._keyValues.put(key, this.CHILD_REMOVED);
                    } else {
                        this._keyValues.put(key, this.SELF_ADDED);
                    }
                } else if (value == this.CHILD_REMOVED && !child) {
                    this._keyValues.put(key, this.SELF_ADDED);
                }
            }
        }

        private boolean hasKeyValue(Object key) {
            Object value = this._keyValues.get(key);
            return (value == null || value == this.CHILD_REMOVED) ? false : true;
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.SelectorState, org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void remove(ValidatorListener.Event e) {
            ConstraintState constraintState = this._next;
            while (true) {
                ConstraintState cs = constraintState;
                if (cs == null || cs == IdentityConstraint.this._elementStack._savePoint) {
                    break;
                }
                if (cs instanceof SelectorState) {
                    SelectorState sel = (SelectorState) cs;
                    if (sel._constraint == this._constraint.getReferencedKey()) {
                        addKeyValues(sel._values, false);
                    }
                }
                constraintState = cs._next;
            }
            for (XmlObjectList fields : this._values) {
                if (fields.unfilled() < 0 && !hasKeyValue(fields)) {
                    IdentityConstraint.this.emitError(e, XmlErrorCodes.IDENTITY_CONSTRAINT_VALID$KEYREF_KEY_NOT_FOUND, new Object[]{fields, QNameHelper.pretty(this._constraint.getName())});
                    return;
                }
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IdentityConstraint$FieldState.class */
    public class FieldState extends ConstraintState {
        SelectorState _selector;
        XPath.ExecutionContext[] _contexts;
        boolean[] _needsValue;
        XmlObjectList _value;

        FieldState(SelectorState selector, ValidatorListener.Event e, SchemaType st) {
            super();
            this._selector = selector;
            SchemaIdentityConstraint ic = selector._constraint;
            int fieldCount = ic.getFields().length;
            this._contexts = new XPath.ExecutionContext[fieldCount];
            this._needsValue = new boolean[fieldCount];
            this._value = new XmlObjectList(fieldCount);
            for (int i = 0; i < fieldCount; i++) {
                this._contexts[i] = new XPath.ExecutionContext();
                this._contexts[i].init((XPath) ic.getFieldPath(i));
                if ((this._contexts[i].start() & 1) != 0) {
                    if (!IdentityConstraint.hasSimpleContent(st)) {
                        IdentityConstraint.this.emitError(e, "Identity constraint field must have simple content");
                    } else {
                        this._needsValue[i] = true;
                    }
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void element(ValidatorListener.Event e, SchemaType st) {
            for (int i = 0; i < this._contexts.length; i++) {
                if (this._needsValue[i]) {
                    IdentityConstraint.this.emitError(e, "Identity constraint field must have simple content");
                    this._needsValue[i] = false;
                }
            }
            for (int i2 = 0; i2 < this._contexts.length; i2++) {
                if ((this._contexts[i2].element(e.getName()) & 1) != 0) {
                    if (!IdentityConstraint.hasSimpleContent(st)) {
                        IdentityConstraint.this.emitError(e, "Identity constraint field must have simple content");
                    } else {
                        this._needsValue[i2] = true;
                    }
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void attr(ValidatorListener.Event e, QName name, SchemaType st, String value) {
            if (value == null) {
                return;
            }
            for (int i = 0; i < this._contexts.length; i++) {
                if (this._contexts[i].attr(name)) {
                    XmlObject o = IdentityConstraint.newValue(st, value);
                    if (o == null) {
                        return;
                    }
                    boolean set = this._value.set(o, i);
                    if (!set) {
                        IdentityConstraint.this.emitError(e, "Multiple instances of field with xpath: '" + this._selector._constraint.getFields()[i] + "' for a selector");
                    }
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void text(ValidatorListener.Event e, SchemaType st, String value, boolean emptyContent) {
            if (value != null || emptyContent) {
                for (int i = 0; i < this._contexts.length; i++) {
                    if (this._needsValue[i]) {
                        if (emptyContent || !IdentityConstraint.hasSimpleContent(st)) {
                            IdentityConstraint.this.emitError(e, "Identity constraint field must have simple content");
                            return;
                        }
                        SchemaType simpleType = IdentityConstraint.getSimpleType(st);
                        XmlObject o = IdentityConstraint.newValue(simpleType, value);
                        if (o == null) {
                            return;
                        }
                        boolean set = this._value.set(o, i);
                        if (!set) {
                            IdentityConstraint.this.emitError(e, "Multiple instances of field with xpath: '" + this._selector._constraint.getFields()[i] + "' for a selector");
                        }
                    }
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void endElement(ValidatorListener.Event e) {
            for (int i = 0; i < this._needsValue.length; i++) {
                this._contexts[i].end();
                this._needsValue[i] = false;
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void remove(ValidatorListener.Event e) {
            if (this._selector._constraint.getConstraintCategory() == 1 && this._value.unfilled() >= 0) {
                IdentityConstraint.this.emitError(e, "Key " + QNameHelper.pretty(this._selector._constraint.getName()) + " is missing field with xpath: '" + this._selector._constraint.getFields()[this._value.unfilled()] + "'");
            } else {
                this._selector.addFields(this._value, e);
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IdentityConstraint$IdState.class */
    public class IdState extends ConstraintState {
        Set _values;

        IdState() {
            super();
            this._values = new LinkedHashSet();
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void attr(ValidatorListener.Event e, QName name, SchemaType st, String value) {
            handleValue(e, st, value);
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void text(ValidatorListener.Event e, SchemaType st, String value, boolean emptyContent) {
            if (emptyContent) {
                return;
            }
            handleValue(e, st, value);
        }

        private void handleValue(ValidatorListener.Event e, SchemaType st, String value) {
            if (value != null && st != null && !st.isNoType() && XmlID.type.isAssignableFrom(st)) {
                XmlObjectList xmlValue = new XmlObjectList(1);
                XmlObject o = IdentityConstraint.newValue(XmlID.type, value);
                if (o == null) {
                    return;
                }
                xmlValue.set(o, 0);
                if (this._values.contains(xmlValue)) {
                    IdentityConstraint.this.emitError(e, XmlErrorCodes.ID_VALID$DUPLICATE, new Object[]{value});
                } else {
                    this._values.add(xmlValue);
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void element(ValidatorListener.Event e, SchemaType st) {
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void endElement(ValidatorListener.Event e) {
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void remove(ValidatorListener.Event e) {
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IdentityConstraint$IdRefState.class */
    public class IdRefState extends ConstraintState {
        IdState _ids;
        List _values;

        IdRefState(IdState ids) {
            super();
            this._ids = ids;
            this._values = new ArrayList();
        }

        private void handleValue(ValidatorListener.Event e, SchemaType st, String value) {
            if (value == null || st == null || st.isNoType()) {
                return;
            }
            if (XmlIDREFS.type.isAssignableFrom(st)) {
                XmlIDREFS lv = (XmlIDREFS) IdentityConstraint.newValue(XmlIDREFS.type, value);
                if (lv == null) {
                    return;
                }
                List l = lv.xgetListValue();
                for (int i = 0; i < l.size(); i++) {
                    XmlObjectList xmlValue = new XmlObjectList(1);
                    xmlValue.set((XmlIDREF) l.get(i), 0);
                    this._values.add(xmlValue);
                }
                return;
            }
            if (XmlIDREF.type.isAssignableFrom(st)) {
                XmlObjectList xmlValue2 = new XmlObjectList(1);
                XmlIDREF idref = (XmlIDREF) st.newValue(value);
                if (idref == null) {
                    return;
                }
                xmlValue2.set(idref, 0);
                this._values.add(xmlValue2);
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void attr(ValidatorListener.Event e, QName name, SchemaType st, String value) {
            handleValue(e, st, value);
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void text(ValidatorListener.Event e, SchemaType st, String value, boolean emptyContent) {
            if (emptyContent) {
                return;
            }
            handleValue(e, st, value);
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void remove(ValidatorListener.Event e) {
            for (Object o : this._values) {
                if (!this._ids._values.contains(o)) {
                    IdentityConstraint.this.emitError(e, "ID not found for IDRef value '" + o + "'");
                }
            }
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void element(ValidatorListener.Event e, SchemaType st) {
        }

        @Override // org.apache.xmlbeans.impl.common.IdentityConstraint.ConstraintState
        void endElement(ValidatorListener.Event e) {
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/IdentityConstraint$ElementState.class */
    private static class ElementState {
        ElementState _next;
        boolean _hasConstraints;
        ConstraintState _savePoint;

        private ElementState() {
        }
    }
}
