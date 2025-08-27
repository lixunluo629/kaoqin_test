package org.aspectj.apache.bcel.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.WeakHashMap;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.ClassParser;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/SyntheticRepository.class */
public class SyntheticRepository implements Repository {
    private static final String DEFAULT_PATH = ClassPath.getClassPath();
    private static HashMap<ClassPath, SyntheticRepository> _instances = new HashMap<>();
    private ClassPath _path;
    private WeakHashMap<String, JavaClass> _loadedClasses = new WeakHashMap<>();

    private SyntheticRepository(ClassPath classPath) {
        this._path = null;
        this._path = classPath;
    }

    public static SyntheticRepository getInstance() {
        return getInstance(ClassPath.getSystemClassPath());
    }

    public static SyntheticRepository getInstance(ClassPath classPath) {
        SyntheticRepository syntheticRepository = _instances.get(classPath);
        if (syntheticRepository == null) {
            syntheticRepository = new SyntheticRepository(classPath);
            _instances.put(classPath, syntheticRepository);
        }
        return syntheticRepository;
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void storeClass(JavaClass javaClass) {
        this._loadedClasses.put(javaClass.getClassName(), javaClass);
        javaClass.setRepository(this);
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void removeClass(JavaClass javaClass) {
        this._loadedClasses.remove(javaClass.getClassName());
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass findClass(String str) {
        return this._loadedClasses.get(str);
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass loadClass(String str) throws ClassNotFoundException {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("Invalid class name " + str);
        }
        String strReplace = str.replace('/', '.');
        try {
            return loadClass(this._path.getInputStream(strReplace), strReplace);
        } catch (IOException e) {
            throw new ClassNotFoundException("Exception while looking for class " + strReplace + ": " + e.toString());
        }
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass loadClass(Class cls) throws ClassNotFoundException {
        String name = cls.getName();
        String strSubstring = name;
        int iLastIndexOf = strSubstring.lastIndexOf(46);
        if (iLastIndexOf > 0) {
            strSubstring = strSubstring.substring(iLastIndexOf + 1);
        }
        return loadClass(cls.getResourceAsStream(strSubstring + ClassUtils.CLASS_FILE_SUFFIX), name);
    }

    private JavaClass loadClass(InputStream inputStream, String str) throws ClassNotFoundException, ClassFormatException {
        JavaClass javaClassFindClass = findClass(str);
        if (javaClassFindClass != null) {
            return javaClassFindClass;
        }
        if (inputStream == null) {
            throw new ClassNotFoundException("SyntheticRepository could not load " + str);
        }
        try {
            JavaClass javaClass = new ClassParser(inputStream, str).parse();
            storeClass(javaClass);
            return javaClass;
        } catch (IOException e) {
            throw new ClassNotFoundException("Exception while looking for class " + str + ": " + e.toString());
        }
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void clear() {
        this._loadedClasses.clear();
    }
}
