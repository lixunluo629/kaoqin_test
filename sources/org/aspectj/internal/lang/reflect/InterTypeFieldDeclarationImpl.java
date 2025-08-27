package org.aspectj.internal.lang.reflect;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import org.aspectj.lang.reflect.AjType;
import org.aspectj.lang.reflect.AjTypeSystem;
import org.aspectj.lang.reflect.InterTypeFieldDeclaration;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/internal/lang/reflect/InterTypeFieldDeclarationImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/internal/lang/reflect/InterTypeFieldDeclarationImpl.class */
public class InterTypeFieldDeclarationImpl extends InterTypeDeclarationImpl implements InterTypeFieldDeclaration {
    private String name;
    private AjType<?> type;
    private Type genericType;

    public InterTypeFieldDeclarationImpl(AjType<?> decType, String target, int mods, String name, AjType<?> type, Type genericType) {
        super(decType, target, mods);
        this.name = name;
        this.type = type;
        this.genericType = genericType;
    }

    public InterTypeFieldDeclarationImpl(AjType<?> decType, AjType<?> targetType, Field base) {
        super(decType, targetType, base.getModifiers());
        this.name = base.getName();
        this.type = AjTypeSystem.getAjType(base.getType());
        Type gt = base.getGenericType();
        if (gt instanceof Class) {
            this.genericType = AjTypeSystem.getAjType((Class) gt);
        } else {
            this.genericType = gt;
        }
    }

    @Override // org.aspectj.lang.reflect.InterTypeFieldDeclaration
    public String getName() {
        return this.name;
    }

    @Override // org.aspectj.lang.reflect.InterTypeFieldDeclaration
    public AjType<?> getType() {
        return this.type;
    }

    @Override // org.aspectj.lang.reflect.InterTypeFieldDeclaration
    public Type getGenericType() {
        return this.genericType;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(Modifier.toString(getModifiers()));
        sb.append(SymbolConstants.SPACE_SYMBOL);
        sb.append(getType().toString());
        sb.append(SymbolConstants.SPACE_SYMBOL);
        sb.append(this.targetTypeName);
        sb.append(".");
        sb.append(getName());
        return sb.toString();
    }
}
