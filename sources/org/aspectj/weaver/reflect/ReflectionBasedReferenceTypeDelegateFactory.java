package org.aspectj.weaver.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ReflectionBasedReferenceTypeDelegateFactory.class */
public class ReflectionBasedReferenceTypeDelegateFactory {
    public static ReflectionBasedReferenceTypeDelegate createDelegate(ReferenceType forReferenceType, World inWorld, ClassLoader usingClassLoader) {
        ReflectionBasedReferenceTypeDelegate rbrtd;
        try {
            Class c = Class.forName(forReferenceType.getName(), false, usingClassLoader);
            if (LangUtil.is15VMOrGreater() && (rbrtd = create15Delegate(forReferenceType, c, usingClassLoader, inWorld)) != null) {
                return rbrtd;
            }
            return new ReflectionBasedReferenceTypeDelegate(c, usingClassLoader, inWorld, forReferenceType);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static ReflectionBasedReferenceTypeDelegate createDelegate(ReferenceType forReferenceType, World inWorld, Class<?> clazz) {
        ReflectionBasedReferenceTypeDelegate rbrtd;
        if (LangUtil.is15VMOrGreater() && (rbrtd = create15Delegate(forReferenceType, clazz, clazz.getClassLoader(), inWorld)) != null) {
            return rbrtd;
        }
        return new ReflectionBasedReferenceTypeDelegate(clazz, clazz.getClassLoader(), inWorld, forReferenceType);
    }

    public static ReflectionBasedReferenceTypeDelegate create14Delegate(ReferenceType forReferenceType, World inWorld, ClassLoader usingClassLoader) {
        try {
            Class c = Class.forName(forReferenceType.getName(), false, usingClassLoader);
            return new ReflectionBasedReferenceTypeDelegate(c, usingClassLoader, inWorld, forReferenceType);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static ReflectionBasedReferenceTypeDelegate create15Delegate(ReferenceType forReferenceType, Class forClass, ClassLoader usingClassLoader, World inWorld) throws ClassNotFoundException {
        try {
            Class delegateClass = Class.forName("org.aspectj.weaver.reflect.Java15ReflectionBasedReferenceTypeDelegate");
            ReflectionBasedReferenceTypeDelegate ret = (ReflectionBasedReferenceTypeDelegate) delegateClass.newInstance();
            ret.initialize(forReferenceType, forClass, usingClassLoader, inWorld);
            return ret;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Attempted to create Java 1.5 reflection based delegate but org.aspectj.weaver.reflect.Java15ReflectionBasedReferenceTypeDelegate was not found on classpath");
        } catch (IllegalAccessException illAccEx) {
            throw new IllegalStateException("Attempted to create Java 1.5 reflection based delegate but IllegalAccessException: " + illAccEx + " occured");
        } catch (InstantiationException insEx) {
            throw new IllegalStateException("Attempted to create Java 1.5 reflection based delegate but InstantiationException: " + insEx + " occured");
        }
    }

    private static GenericSignatureInformationProvider createGenericSignatureProvider(World inWorld) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (LangUtil.is15VMOrGreater()) {
            try {
                Class providerClass = Class.forName("org.aspectj.weaver.reflect.Java15GenericSignatureInformationProvider");
                Constructor cons = providerClass.getConstructor(World.class);
                GenericSignatureInformationProvider ret = (GenericSignatureInformationProvider) cons.newInstance(inWorld);
                return ret;
            } catch (ClassNotFoundException e) {
            } catch (IllegalAccessException illAcc) {
                throw new IllegalStateException("Attempted to create Java 1.5 generic signature provider but: " + illAcc + " occured");
            } catch (InstantiationException insEx) {
                throw new IllegalStateException("Attempted to create Java 1.5 generic signature provider but: " + insEx + " occured");
            } catch (NoSuchMethodException nsmEx) {
                throw new IllegalStateException("Attempted to create Java 1.5 generic signature provider but: " + nsmEx + " occured");
            } catch (InvocationTargetException invEx) {
                throw new IllegalStateException("Attempted to create Java 1.5 generic signature provider but: " + invEx + " occured");
            }
        }
        return new Java14GenericSignatureInformationProvider();
    }

    public static ResolvedMember createResolvedMember(Member reflectMember, World inWorld) {
        if (reflectMember instanceof Method) {
            return createResolvedMethod((Method) reflectMember, inWorld);
        }
        if (reflectMember instanceof Constructor) {
            return createResolvedConstructor((Constructor) reflectMember, inWorld);
        }
        return createResolvedField((Field) reflectMember, inWorld);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ResolvedMember createResolvedMethod(Method aMethod, World world) {
        ReflectionBasedResolvedMemberImpl ret = new ReflectionBasedResolvedMemberImpl(org.aspectj.weaver.Member.METHOD, toResolvedType(aMethod.getDeclaringClass(), (IReflectionWorld) world), aMethod.getModifiers(), toResolvedType(aMethod.getReturnType(), (IReflectionWorld) world), aMethod.getName(), toResolvedTypeArray(aMethod.getParameterTypes(), world), toResolvedTypeArray(aMethod.getExceptionTypes(), world), aMethod);
        if (world instanceof IReflectionWorld) {
            ret.setAnnotationFinder(((IReflectionWorld) world).getAnnotationFinder());
        }
        ret.setGenericSignatureInformationProvider(createGenericSignatureProvider(world));
        return ret;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ResolvedMember createResolvedAdviceMember(Method aMethod, World world) {
        ReflectionBasedResolvedMemberImpl ret = new ReflectionBasedResolvedMemberImpl(org.aspectj.weaver.Member.ADVICE, toResolvedType(aMethod.getDeclaringClass(), (IReflectionWorld) world), aMethod.getModifiers(), toResolvedType(aMethod.getReturnType(), (IReflectionWorld) world), aMethod.getName(), toResolvedTypeArray(aMethod.getParameterTypes(), world), toResolvedTypeArray(aMethod.getExceptionTypes(), world), aMethod);
        if (world instanceof IReflectionWorld) {
            ret.setAnnotationFinder(((IReflectionWorld) world).getAnnotationFinder());
        }
        ret.setGenericSignatureInformationProvider(createGenericSignatureProvider(world));
        return ret;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ResolvedMember createStaticInitMember(Class forType, World world) {
        return new ResolvedMemberImpl(org.aspectj.weaver.Member.STATIC_INITIALIZATION, toResolvedType(forType, (IReflectionWorld) world), 8, UnresolvedType.VOID, "<clinit>", new UnresolvedType[0], new UnresolvedType[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ResolvedMember createResolvedConstructor(Constructor aConstructor, World world) {
        ReflectionBasedResolvedMemberImpl ret = new ReflectionBasedResolvedMemberImpl(org.aspectj.weaver.Member.CONSTRUCTOR, toResolvedType(aConstructor.getDeclaringClass(), (IReflectionWorld) world), aConstructor.getModifiers(), UnresolvedType.VOID, "<init>", toResolvedTypeArray(aConstructor.getParameterTypes(), world), toResolvedTypeArray(aConstructor.getExceptionTypes(), world), aConstructor);
        if (world instanceof IReflectionWorld) {
            ret.setAnnotationFinder(((IReflectionWorld) world).getAnnotationFinder());
        }
        ret.setGenericSignatureInformationProvider(createGenericSignatureProvider(world));
        return ret;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ResolvedMember createResolvedField(Field aField, World world) {
        ReflectionBasedResolvedMemberImpl ret = new ReflectionBasedResolvedMemberImpl(org.aspectj.weaver.Member.FIELD, toResolvedType(aField.getDeclaringClass(), (IReflectionWorld) world), aField.getModifiers(), toResolvedType(aField.getType(), (IReflectionWorld) world), aField.getName(), new UnresolvedType[0], aField);
        if (world instanceof IReflectionWorld) {
            ret.setAnnotationFinder(((IReflectionWorld) world).getAnnotationFinder());
        }
        ret.setGenericSignatureInformationProvider(createGenericSignatureProvider(world));
        return ret;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ResolvedMember createHandlerMember(Class exceptionType, Class inType, World world) {
        return new ResolvedMemberImpl(org.aspectj.weaver.Member.HANDLER, toResolvedType(inType, (IReflectionWorld) world), 8, "<catch>", "(" + world.resolve(exceptionType.getName()).getSignature() + ")V");
    }

    public static ResolvedType resolveTypeInWorld(Class aClass, World aWorld) {
        String className = aClass.getName();
        if (aClass.isArray()) {
            return aWorld.resolve(UnresolvedType.forSignature(className.replace('.', '/')));
        }
        return aWorld.resolve(className);
    }

    private static ResolvedType toResolvedType(Class aClass, IReflectionWorld aWorld) {
        return aWorld.resolve(aClass);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static ResolvedType[] toResolvedTypeArray(Class[] classes, World world) {
        ResolvedType[] ret = new ResolvedType[classes.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ((IReflectionWorld) world).resolve(classes[i]);
        }
        return ret;
    }
}
