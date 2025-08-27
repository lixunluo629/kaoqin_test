package net.dongliu.apk.parser.struct.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.struct.StringPool;
import net.dongliu.apk.parser.utils.ResourceLoader;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourceTable.class */
public class ResourceTable {
    private Map<Short, ResourcePackage> packageMap = new HashMap();
    private StringPool stringPool;
    public static Map<Integer, String> sysStyle = ResourceLoader.loadSystemStyles();

    public void addPackage(ResourcePackage resourcePackage) {
        this.packageMap.put(Short.valueOf(resourcePackage.getId()), resourcePackage);
    }

    public ResourcePackage getPackage(short id) {
        return this.packageMap.get(Short.valueOf(id));
    }

    public StringPool getStringPool() {
        return this.stringPool;
    }

    public void setStringPool(StringPool stringPool) {
        this.stringPool = stringPool;
    }

    @Nonnull
    public List<Resource> getResourcesById(long resourceId) {
        ResourceValue currentResourceValue;
        short packageId = (short) ((resourceId >> 24) & 255);
        short typeId = (short) ((resourceId >> 16) & 255);
        int entryIndex = (int) (resourceId & 65535);
        ResourcePackage resourcePackage = getPackage(packageId);
        if (resourcePackage == null) {
            return Collections.emptyList();
        }
        TypeSpec typeSpec = resourcePackage.getTypeSpec(typeId);
        List<Type> types = resourcePackage.getTypes(typeId);
        if (typeSpec == null || types == null) {
            return Collections.emptyList();
        }
        if (!typeSpec.exists(entryIndex)) {
            return Collections.emptyList();
        }
        List<Resource> result = new ArrayList<>();
        for (Type type : types) {
            ResourceEntry resourceEntry = type.getResourceEntry(entryIndex);
            if (resourceEntry != null && (currentResourceValue = resourceEntry.getValue()) != null && (!(currentResourceValue instanceof ResourceValue.ReferenceResourceValue) || resourceId != ((ResourceValue.ReferenceResourceValue) currentResourceValue).getReferenceResourceId())) {
                result.add(new Resource(typeSpec, type, resourceEntry));
            }
        }
        return result;
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourceTable$Resource.class */
    public static class Resource {
        private TypeSpec typeSpec;
        private Type type;
        private ResourceEntry resourceEntry;

        public Resource(TypeSpec typeSpec, Type type, ResourceEntry resourceEntry) {
            this.typeSpec = typeSpec;
            this.type = type;
            this.resourceEntry = resourceEntry;
        }

        public TypeSpec getTypeSpec() {
            return this.typeSpec;
        }

        public Type getType() {
            return this.type;
        }

        public ResourceEntry getResourceEntry() {
            return this.resourceEntry;
        }
    }
}
