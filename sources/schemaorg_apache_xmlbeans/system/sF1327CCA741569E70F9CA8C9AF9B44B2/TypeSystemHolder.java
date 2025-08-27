package schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2;

import java.lang.reflect.Constructor;
import org.apache.xmlbeans.SchemaTypeSystem;

/* loaded from: poi-ooxml-schemas-3.17.jar:schemaorg_apache_xmlbeans/system/sF1327CCA741569E70F9CA8C9AF9B44B2/TypeSystemHolder.class */
public class TypeSystemHolder {
    public static final SchemaTypeSystem typeSystem = loadTypeSystem();
    static Class class$schemaorg_apache_xmlbeans$system$sF1327CCA741569E70F9CA8C9AF9B44B2$TypeSystemHolder;
    static Class class$java$lang$Class;

    private TypeSystemHolder() {
    }

    private static final SchemaTypeSystem loadTypeSystem() throws Throwable {
        Class clsClass$;
        Class<?> clsClass$2;
        Class clsClass$3;
        try {
            if (class$schemaorg_apache_xmlbeans$system$sF1327CCA741569E70F9CA8C9AF9B44B2$TypeSystemHolder == null) {
                clsClass$ = class$("schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2.TypeSystemHolder");
                class$schemaorg_apache_xmlbeans$system$sF1327CCA741569E70F9CA8C9AF9B44B2$TypeSystemHolder = clsClass$;
            } else {
                clsClass$ = class$schemaorg_apache_xmlbeans$system$sF1327CCA741569E70F9CA8C9AF9B44B2$TypeSystemHolder;
            }
            Class<?> cls = Class.forName("org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl", true, clsClass$.getClassLoader());
            Class<?>[] clsArr = new Class[1];
            if (class$java$lang$Class == null) {
                clsClass$2 = class$("java.lang.Class");
                class$java$lang$Class = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$Class;
            }
            clsArr[0] = clsClass$2;
            Constructor<?> constructor = cls.getConstructor(clsArr);
            Object[] objArr = new Object[1];
            if (class$schemaorg_apache_xmlbeans$system$sF1327CCA741569E70F9CA8C9AF9B44B2$TypeSystemHolder == null) {
                clsClass$3 = class$("schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2.TypeSystemHolder");
                class$schemaorg_apache_xmlbeans$system$sF1327CCA741569E70F9CA8C9AF9B44B2$TypeSystemHolder = clsClass$3;
            } else {
                clsClass$3 = class$schemaorg_apache_xmlbeans$system$sF1327CCA741569E70F9CA8C9AF9B44B2$TypeSystemHolder;
            }
            objArr[0] = clsClass$3;
            return (SchemaTypeSystem) constructor.newInstance(objArr);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load org.apache.xmlbeans.impl.SchemaTypeSystemImpl: make sure xbean.jar is on the classpath.", e);
        } catch (Exception e2) {
            throw new RuntimeException(new StringBuffer().append("Could not instantiate SchemaTypeSystemImpl (").append(e2.toString()).append("): is the version of xbean.jar correct?").toString(), e2);
        }
    }

    static Class class$(String str) throws Throwable {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }
}
