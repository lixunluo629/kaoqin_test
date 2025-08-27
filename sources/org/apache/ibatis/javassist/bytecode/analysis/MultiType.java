package org.apache.ibatis.javassist.bytecode.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.ibatis.javassist.CtClass;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/analysis/MultiType.class */
public class MultiType extends Type {
    private Map interfaces;
    private Type resolved;
    private Type potentialClass;
    private MultiType mergeSource;
    private boolean changed;

    public MultiType(Map interfaces) {
        this(interfaces, null);
    }

    public MultiType(Map interfaces, Type potentialClass) {
        super(null);
        this.changed = false;
        this.interfaces = interfaces;
        this.potentialClass = potentialClass;
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public CtClass getCtClass() {
        if (this.resolved != null) {
            return this.resolved.getCtClass();
        }
        return Type.OBJECT.getCtClass();
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public Type getComponent() {
        return null;
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public int getSize() {
        return 1;
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public boolean isArray() {
        return false;
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    boolean popChanged() {
        boolean changed = this.changed;
        this.changed = false;
        return changed;
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public boolean isAssignableFrom(Type type) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isAssignableTo(Type type) {
        if (this.resolved != null) {
            return type.isAssignableFrom(this.resolved);
        }
        if (Type.OBJECT.equals(type)) {
            return true;
        }
        if (this.potentialClass != null && !type.isAssignableFrom(this.potentialClass)) {
            this.potentialClass = null;
        }
        Map map = mergeMultiAndSingle(this, type);
        if (map.size() == 1 && this.potentialClass == null) {
            this.resolved = Type.get((CtClass) map.values().iterator().next());
            propogateResolved();
            return true;
        }
        if (map.size() >= 1) {
            this.interfaces = map;
            propogateState();
            return true;
        }
        if (this.potentialClass != null) {
            this.resolved = this.potentialClass;
            propogateResolved();
            return true;
        }
        return false;
    }

    private void propogateState() {
        MultiType multiType = this.mergeSource;
        while (true) {
            MultiType source = multiType;
            if (source != null) {
                source.interfaces = this.interfaces;
                source.potentialClass = this.potentialClass;
                multiType = source.mergeSource;
            } else {
                return;
            }
        }
    }

    private void propogateResolved() {
        MultiType multiType = this.mergeSource;
        while (true) {
            MultiType source = multiType;
            if (source != null) {
                source.resolved = this.resolved;
                multiType = source.mergeSource;
            } else {
                return;
            }
        }
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public boolean isReference() {
        return true;
    }

    private Map getAllMultiInterfaces(MultiType type) {
        Map map = new HashMap();
        for (CtClass intf : type.interfaces.values()) {
            map.put(intf.getName(), intf);
            getAllInterfaces(intf, map);
        }
        return map;
    }

    private Map mergeMultiInterfaces(MultiType type1, MultiType type2) {
        Map map1 = getAllMultiInterfaces(type1);
        Map map2 = getAllMultiInterfaces(type2);
        return findCommonInterfaces(map1, map2);
    }

    private Map mergeMultiAndSingle(MultiType multi, Type single) {
        Map map1 = getAllMultiInterfaces(multi);
        Map map2 = getAllInterfaces(single.getCtClass(), null);
        return findCommonInterfaces(map1, map2);
    }

    private boolean inMergeSource(MultiType source) {
        while (source != null) {
            if (source == this) {
                return true;
            }
            source = source.mergeSource;
        }
        return false;
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public Type merge(Type type) {
        Map merged;
        if (this == type) {
            return this;
        }
        if (type == UNINIT) {
            return this;
        }
        if (type == BOGUS) {
            return BOGUS;
        }
        if (type == null) {
            return this;
        }
        if (this.resolved != null) {
            return this.resolved.merge(type);
        }
        if (this.potentialClass != null) {
            Type mergePotential = this.potentialClass.merge(type);
            if (!mergePotential.equals(this.potentialClass) || mergePotential.popChanged()) {
                this.potentialClass = Type.OBJECT.equals(mergePotential) ? null : mergePotential;
                this.changed = true;
            }
        }
        if (type instanceof MultiType) {
            MultiType multi = (MultiType) type;
            if (multi.resolved != null) {
                merged = mergeMultiAndSingle(this, multi.resolved);
            } else {
                merged = mergeMultiInterfaces(multi, this);
                if (!inMergeSource(multi)) {
                    this.mergeSource = multi;
                }
            }
        } else {
            merged = mergeMultiAndSingle(this, type);
        }
        if (merged.size() > 1 || (merged.size() == 1 && this.potentialClass != null)) {
            if (merged.size() != this.interfaces.size()) {
                this.changed = true;
            } else if (!this.changed) {
                Iterator iter = merged.keySet().iterator();
                while (iter.hasNext()) {
                    if (!this.interfaces.containsKey(iter.next())) {
                        this.changed = true;
                    }
                }
            }
            this.interfaces = merged;
            propogateState();
            return this;
        }
        if (merged.size() == 1) {
            this.resolved = Type.get((CtClass) merged.values().iterator().next());
        } else if (this.potentialClass != null) {
            this.resolved = this.potentialClass;
        } else {
            this.resolved = OBJECT;
        }
        propogateResolved();
        return this.resolved;
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public boolean equals(Object o) {
        if (!(o instanceof MultiType)) {
            return false;
        }
        MultiType multi = (MultiType) o;
        if (this.resolved != null) {
            return this.resolved.equals(multi.resolved);
        }
        if (multi.resolved != null) {
            return false;
        }
        return this.interfaces.keySet().equals(multi.interfaces.keySet());
    }

    @Override // org.apache.ibatis.javassist.bytecode.analysis.Type
    public String toString() {
        if (this.resolved != null) {
            return this.resolved.toString();
        }
        StringBuffer buffer = new StringBuffer("{");
        Iterator iter = this.interfaces.keySet().iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            buffer.append(", ");
        }
        buffer.setLength(buffer.length() - 2);
        if (this.potentialClass != null) {
            buffer.append(", *").append(this.potentialClass.toString());
        }
        buffer.append("}");
        return buffer.toString();
    }
}
