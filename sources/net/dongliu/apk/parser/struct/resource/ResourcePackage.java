package net.dongliu.apk.parser.struct.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.dongliu.apk.parser.struct.StringPool;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourcePackage.class */
public class ResourcePackage {
    private String name;
    private short id;
    private StringPool typeStringPool;
    private StringPool keyStringPool;
    private Map<Short, TypeSpec> typeSpecMap = new HashMap();
    private Map<Short, List<Type>> typesMap = new HashMap();

    public ResourcePackage(PackageHeader header) {
        this.name = header.getName();
        this.id = (short) header.getId();
    }

    public void addTypeSpec(TypeSpec typeSpec) {
        this.typeSpecMap.put(Short.valueOf(typeSpec.getId()), typeSpec);
    }

    @Nullable
    public TypeSpec getTypeSpec(short id) {
        return this.typeSpecMap.get(Short.valueOf(id));
    }

    public void addType(Type type) {
        List<Type> types = this.typesMap.get(Short.valueOf(type.getId()));
        if (types == null) {
            types = new ArrayList();
            this.typesMap.put(Short.valueOf(type.getId()), types);
        }
        types.add(type);
    }

    @Nullable
    public List<Type> getTypes(short id) {
        return this.typesMap.get(Short.valueOf(id));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getId() {
        return this.id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public StringPool getTypeStringPool() {
        return this.typeStringPool;
    }

    public void setTypeStringPool(StringPool typeStringPool) {
        this.typeStringPool = typeStringPool;
    }

    public StringPool getKeyStringPool() {
        return this.keyStringPool;
    }

    public void setKeyStringPool(StringPool keyStringPool) {
        this.keyStringPool = keyStringPool;
    }

    public Map<Short, TypeSpec> getTypeSpecMap() {
        return this.typeSpecMap;
    }

    public void setTypeSpecMap(Map<Short, TypeSpec> typeSpecMap) {
        this.typeSpecMap = typeSpecMap;
    }

    public Map<Short, List<Type>> getTypesMap() {
        return this.typesMap;
    }

    public void setTypesMap(Map<Short, List<Type>> typesMap) {
        this.typesMap = typesMap;
    }
}
