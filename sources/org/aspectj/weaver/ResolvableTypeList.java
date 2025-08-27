package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvableTypeList.class */
public class ResolvableTypeList {
    public int length;
    private World world;
    private UnresolvedType[] types;

    public ResolvableTypeList(World world, UnresolvedType[] unresolvedTypes) {
        this.length = unresolvedTypes.length;
        this.types = unresolvedTypes;
        this.world = world;
    }

    public ResolvedType getResolved(int nameIndex) {
        UnresolvedType ut = this.types[nameIndex];
        if (!(ut instanceof ResolvedType)) {
            this.types[nameIndex] = this.world.resolve(ut);
            return (ResolvedType) this.types[nameIndex];
        }
        return (ResolvedType) ut;
    }
}
