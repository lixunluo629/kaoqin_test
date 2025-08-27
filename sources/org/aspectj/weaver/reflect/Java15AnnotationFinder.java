package org.aspectj.weaver.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.apache.ibatis.javassist.bytecode.AnnotationDefaultAttribute;
import org.aspectj.apache.bcel.classfile.AnnotationDefault;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.aspectj.apache.bcel.util.NonCachingClassLoaderRepository;
import org.aspectj.apache.bcel.util.Repository;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelAnnotation;
import org.aspectj.weaver.bcel.BcelWeakClassLoaderReference;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/Java15AnnotationFinder.class */
public class Java15AnnotationFinder implements AnnotationFinder, ArgNameFinder {
    public static final ResolvedType[][] NO_PARAMETER_ANNOTATIONS = new ResolvedType[0];
    private Repository bcelRepository;
    private BcelWeakClassLoaderReference classLoaderRef;
    private World world;
    private static boolean useCachingClassLoaderRepository;

    /* JADX WARN: Type inference failed for: r0v1, types: [org.aspectj.weaver.ResolvedType[], org.aspectj.weaver.ResolvedType[][]] */
    static {
        try {
            useCachingClassLoaderRepository = System.getProperty("Xset:bcelRepositoryCaching", "true").equalsIgnoreCase("true");
        } catch (Throwable th) {
            useCachingClassLoaderRepository = false;
        }
    }

    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public void setClassLoader(ClassLoader aLoader) {
        this.classLoaderRef = new BcelWeakClassLoaderReference(aLoader);
        if (useCachingClassLoaderRepository) {
            this.bcelRepository = new ClassLoaderRepository(this.classLoaderRef);
        } else {
            this.bcelRepository = new NonCachingClassLoaderRepository(this.classLoaderRef);
        }
    }

    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public void setWorld(World aWorld) {
        this.world = aWorld;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public Object getAnnotation(ResolvedType annotationType, Object onObject) throws ClassNotFoundException {
        try {
            Class cls = Class.forName(annotationType.getName(), false, getClassLoader());
            if (onObject.getClass().isAnnotationPresent(cls)) {
                return onObject.getClass().getAnnotation(cls);
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public Object getAnnotationFromClass(ResolvedType annotationType, Class cls) throws ClassNotFoundException {
        try {
            Class<?> cls2 = Class.forName(annotationType.getName(), false, getClassLoader());
            if (cls.isAnnotationPresent(cls2)) {
                return cls.getAnnotation(cls2);
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public Object getAnnotationFromMember(ResolvedType annotationType, Member member) throws ClassNotFoundException {
        if (!(member instanceof AccessibleObject)) {
            return null;
        }
        AccessibleObject accessibleObject = (AccessibleObject) member;
        try {
            Class annotationClass = Class.forName(annotationType.getName(), false, getClassLoader());
            if (accessibleObject.isAnnotationPresent(annotationClass)) {
                return accessibleObject.getAnnotation(annotationClass);
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private ClassLoader getClassLoader() {
        return this.classLoaderRef.getClassLoader();
    }

    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType, Member onMember) {
        if (!(onMember instanceof AccessibleObject)) {
            return null;
        }
        try {
            JavaClass jc = this.bcelRepository.loadClass(onMember.getDeclaringClass());
            AnnotationGen[] anns = new AnnotationGen[0];
            if (onMember instanceof Method) {
                org.aspectj.apache.bcel.classfile.Method bcelMethod = jc.getMethod((Method) onMember);
                if (bcelMethod != null) {
                    anns = bcelMethod.getAnnotations();
                }
            } else if (onMember instanceof Constructor) {
                org.aspectj.apache.bcel.classfile.Method bcelCons = jc.getMethod((Constructor<?>) onMember);
                anns = bcelCons.getAnnotations();
            } else if (onMember instanceof Field) {
                org.aspectj.apache.bcel.classfile.Field bcelField = jc.getField((Field) onMember);
                anns = bcelField.getAnnotations();
            }
            this.bcelRepository.clear();
            if (anns == null) {
                anns = new AnnotationGen[0];
            }
            for (int i = 0; i < anns.length; i++) {
                if (anns[i].getTypeSignature().equals(ofType.getSignature())) {
                    return new BcelAnnotation(anns[i], this.world);
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public String getAnnotationDefaultValue(Member onMember) {
        org.aspectj.apache.bcel.classfile.Method bcelMethod;
        try {
            JavaClass jc = this.bcelRepository.loadClass(onMember.getDeclaringClass());
            if ((onMember instanceof Method) && (bcelMethod = jc.getMethod((Method) onMember)) != null) {
                Attribute[] attrs = bcelMethod.getAttributes();
                for (Attribute attribute : attrs) {
                    if (attribute.getName().equals(AnnotationDefaultAttribute.tag)) {
                        AnnotationDefault def = (AnnotationDefault) attribute;
                        return def.getElementValue().stringifyValue();
                    }
                }
                return null;
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public ResolvedType[] getAnnotations(Member member, boolean areRuntimeAnnotationsSufficient) {
        if (!(member instanceof AccessibleObject)) {
            return ResolvedType.NONE;
        }
        if (!areRuntimeAnnotationsSufficient) {
            try {
                JavaClass jc = this.bcelRepository.loadClass(member.getDeclaringClass());
                AnnotationGen[] anns = null;
                if (member instanceof Method) {
                    org.aspectj.apache.bcel.classfile.Method bcelMethod = jc.getMethod((Method) member);
                    if (bcelMethod != null) {
                        anns = bcelMethod.getAnnotations();
                    }
                } else if (member instanceof Constructor) {
                    org.aspectj.apache.bcel.classfile.Method bcelCons = jc.getMethod((Constructor<?>) member);
                    anns = bcelCons.getAnnotations();
                } else if (member instanceof Field) {
                    org.aspectj.apache.bcel.classfile.Field bcelField = jc.getField((Field) member);
                    anns = bcelField.getAnnotations();
                }
                this.bcelRepository.clear();
                if (anns == null || anns.length == 0) {
                    return ResolvedType.NONE;
                }
                ResolvedType[] annotationTypes = new ResolvedType[anns.length];
                for (int i = 0; i < anns.length; i++) {
                    annotationTypes[i] = this.world.resolve(UnresolvedType.forSignature(anns[i].getTypeSignature()));
                }
                return annotationTypes;
            } catch (ClassNotFoundException e) {
            }
        }
        AccessibleObject ao = (AccessibleObject) member;
        Annotation[] anns2 = ao.getDeclaredAnnotations();
        if (anns2.length == 0) {
            return ResolvedType.NONE;
        }
        ResolvedType[] annotationTypes2 = new ResolvedType[anns2.length];
        for (int i2 = 0; i2 < anns2.length; i2++) {
            annotationTypes2[i2] = UnresolvedType.forName(anns2[i2].annotationType().getName()).resolve(this.world);
        }
        return annotationTypes2;
    }

    public ResolvedType[] getAnnotations(Class forClass, World inWorld) {
        try {
            JavaClass jc = this.bcelRepository.loadClass(forClass);
            AnnotationGen[] anns = jc.getAnnotations();
            this.bcelRepository.clear();
            if (anns == null) {
                return ResolvedType.NONE;
            }
            ResolvedType[] ret = new ResolvedType[anns.length];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = inWorld.resolve(UnresolvedType.forSignature(anns[i].getTypeSignature()));
            }
            return ret;
        } catch (ClassNotFoundException e) {
            Annotation[] classAnnotations = forClass.getAnnotations();
            ResolvedType[] ret2 = new ResolvedType[classAnnotations.length];
            for (int i2 = 0; i2 < classAnnotations.length; i2++) {
                ret2[i2] = inWorld.resolve(classAnnotations[i2].annotationType().getName());
            }
            return ret2;
        }
    }

    @Override // org.aspectj.weaver.reflect.ArgNameFinder
    public String[] getParameterNames(Member forMember) {
        if (!(forMember instanceof AccessibleObject)) {
            return null;
        }
        try {
            JavaClass jc = this.bcelRepository.loadClass(forMember.getDeclaringClass());
            LocalVariableTable lvt = null;
            int numVars = 0;
            if (forMember instanceof Method) {
                org.aspectj.apache.bcel.classfile.Method bcelMethod = jc.getMethod((Method) forMember);
                lvt = bcelMethod.getLocalVariableTable();
                numVars = bcelMethod.getArgumentTypes().length;
            } else if (forMember instanceof Constructor) {
                org.aspectj.apache.bcel.classfile.Method bcelCons = jc.getMethod((Constructor<?>) forMember);
                lvt = bcelCons.getLocalVariableTable();
                numVars = bcelCons.getArgumentTypes().length;
            }
            return getParameterNamesFromLVT(lvt, numVars);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private String[] getParameterNamesFromLVT(LocalVariableTable lvt, int numVars) {
        if (lvt == null) {
            return null;
        }
        LocalVariable[] vars = lvt.getLocalVariableTable();
        if (vars.length < numVars) {
            return null;
        }
        String[] ret = new String[numVars];
        for (int i = 0; i < numVars; i++) {
            ret[i] = vars[i + 1].getName();
        }
        return ret;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v18, types: [org.aspectj.weaver.ResolvedType[], org.aspectj.weaver.ResolvedType[][]] */
    /* JADX WARN: Type inference failed for: r0v53, types: [org.aspectj.weaver.ResolvedType[], org.aspectj.weaver.ResolvedType[][]] */
    @Override // org.aspectj.weaver.reflect.AnnotationFinder
    public ResolvedType[][] getParameterAnnotationTypes(Member member) {
        if (!(member instanceof AccessibleObject)) {
            return NO_PARAMETER_ANNOTATIONS;
        }
        try {
            JavaClass jc = this.bcelRepository.loadClass(member.getDeclaringClass());
            AnnotationGen[][] anns = (AnnotationGen[][]) null;
            if (member instanceof Method) {
                org.aspectj.apache.bcel.classfile.Method bcelMethod = jc.getMethod((Method) member);
                if (bcelMethod != null) {
                    anns = bcelMethod.getParameterAnnotations();
                }
            } else if (member instanceof Constructor) {
                org.aspectj.apache.bcel.classfile.Method bcelCons = jc.getMethod((Constructor<?>) member);
                anns = bcelCons.getParameterAnnotations();
            } else if (member instanceof Field) {
            }
            this.bcelRepository.clear();
            if (anns == null) {
                return NO_PARAMETER_ANNOTATIONS;
            }
            ?? r0 = new ResolvedType[anns.length];
            for (int i = 0; i < anns.length; i++) {
                if (anns[i] != null) {
                    r0[i] = new ResolvedType[anns[i].length];
                    for (int j = 0; j < anns[i].length; j++) {
                        r0[i][j] = this.world.resolve(UnresolvedType.forSignature(anns[i][j].getTypeSignature()));
                    }
                }
            }
            return r0;
        } catch (ClassNotFoundException e) {
            AccessibleObject ao = (AccessibleObject) member;
            Annotation[][] anns2 = (Annotation[][]) null;
            if (member instanceof Method) {
                anns2 = ((Method) ao).getParameterAnnotations();
            } else if (member instanceof Constructor) {
                anns2 = ((Constructor) ao).getParameterAnnotations();
            } else if (member instanceof Field) {
            }
            if (anns2 == null) {
                return NO_PARAMETER_ANNOTATIONS;
            }
            ?? r02 = new ResolvedType[anns2.length];
            for (int i2 = 0; i2 < anns2.length; i2++) {
                if (anns2[i2] != null) {
                    r02[i2] = new ResolvedType[anns2[i2].length];
                    for (int j2 = 0; j2 < anns2[i2].length; j2++) {
                        r02[i2][j2] = UnresolvedType.forName(anns2[i2][j2].annotationType().getName()).resolve(this.world);
                    }
                }
            }
            return r02;
        }
    }
}
