package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.VersionedDataInputStream;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ModifiersPattern.class */
public class ModifiersPattern extends PatternNode {
    private int requiredModifiers;
    private int forbiddenModifiers;
    public static final ModifiersPattern ANY = new ModifiersPattern(0, 0);
    private static Map<String, Integer> modifierFlags;

    static {
        modifierFlags = null;
        modifierFlags = new HashMap();
        int i = 1;
        while (true) {
            int flag = i;
            if (flag <= 2048) {
                String flagName = Modifier.toString(flag);
                modifierFlags.put(flagName, new Integer(flag));
                i = flag << 1;
            } else {
                modifierFlags.put("synthetic", new Integer(4096));
                return;
            }
        }
    }

    public ModifiersPattern(int requiredModifiers, int forbiddenModifiers) {
        this.requiredModifiers = requiredModifiers;
        this.forbiddenModifiers = forbiddenModifiers;
    }

    public String toString() {
        if (this == ANY) {
            return "";
        }
        String ret = Modifier.toString(this.requiredModifiers);
        if (this.forbiddenModifiers == 0) {
            return ret;
        }
        return ret + " !" + Modifier.toString(this.forbiddenModifiers);
    }

    public boolean equals(Object other) {
        if (!(other instanceof ModifiersPattern)) {
            return false;
        }
        ModifiersPattern o = (ModifiersPattern) other;
        return o.requiredModifiers == this.requiredModifiers && o.forbiddenModifiers == this.forbiddenModifiers;
    }

    public int hashCode() {
        int result = (37 * 17) + this.requiredModifiers;
        return (37 * result) + this.forbiddenModifiers;
    }

    public boolean matches(int modifiers) {
        return (modifiers & this.requiredModifiers) == this.requiredModifiers && (modifiers & this.forbiddenModifiers) == 0;
    }

    public static ModifiersPattern read(VersionedDataInputStream s) throws IOException {
        int requiredModifiers = s.readShort();
        int forbiddenModifiers = s.readShort();
        if (requiredModifiers == 0 && forbiddenModifiers == 0) {
            return ANY;
        }
        return new ModifiersPattern(requiredModifiers, forbiddenModifiers);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeShort(this.requiredModifiers);
        s.writeShort(this.forbiddenModifiers);
    }

    public static int getModifierFlag(String name) {
        Integer flag = modifierFlags.get(name);
        if (flag == null) {
            return -1;
        }
        return flag.intValue();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
