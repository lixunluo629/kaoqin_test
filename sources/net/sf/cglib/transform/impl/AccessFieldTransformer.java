package net.sf.cglib.transform.impl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/AccessFieldTransformer.class */
public class AccessFieldTransformer extends ClassEmitterTransformer {
    private Callback callback;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/AccessFieldTransformer$Callback.class */
    public interface Callback {
        String getPropertyName(Type type, String str);
    }

    public AccessFieldTransformer(Callback callback) {
        this.callback = callback;
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public void declare_field(int access, String name, Type type, Object value) {
        super.declare_field(access, name, type, value);
        String property = TypeUtils.upperFirst(this.callback.getPropertyName(getClassType(), name));
        if (property != null) {
            CodeEmitter e = begin_method(1, new Signature(new StringBuffer().append(BeanUtil.PREFIX_GETTER_GET).append(property).toString(), type, Constants.TYPES_EMPTY), null);
            e.load_this();
            e.getfield(name);
            e.return_value();
            e.end_method();
            CodeEmitter e2 = begin_method(1, new Signature(new StringBuffer().append("set").append(property).toString(), Type.VOID_TYPE, new Type[]{type}), null);
            e2.load_this();
            e2.load_arg(0);
            e2.putfield(name);
            e2.return_value();
            e2.end_method();
        }
    }
}
