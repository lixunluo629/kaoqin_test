package org.aspectj.weaver.patterns;

import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/FastMatchInfo.class */
public class FastMatchInfo {
    private Shadow.Kind kind;
    private ResolvedType type;
    public World world;

    public FastMatchInfo(ResolvedType type, Shadow.Kind kind, World world) {
        this.type = type;
        this.kind = kind;
        this.world = world;
    }

    public Shadow.Kind getKind() {
        return this.kind;
    }

    public ResolvedType getType() {
        return this.type;
    }

    public String toString() {
        return "FastMatchInfo [type=" + this.type.getName() + "] [" + (this.kind == null ? "AllKinds" : "Kind=" + this.kind) + "]";
    }
}
