package org.apache.xmlbeans.impl.jam;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.xmlbeans.impl.jam.internal.JamPrinter;
import org.apache.xmlbeans.impl.jam.provider.JamServiceFactoryImpl;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JamServiceFactory.class */
public abstract class JamServiceFactory {
    private static final JamServiceFactory DEFAULT = new JamServiceFactoryImpl();

    public abstract JamServiceParams createServiceParams();

    public abstract JamService createService(JamServiceParams jamServiceParams) throws IOException;

    public abstract JamClassLoader createSystemJamClassLoader();

    public abstract JamClassLoader createJamClassLoader(ClassLoader classLoader);

    public static JamServiceFactory getInstance() {
        return DEFAULT;
    }

    protected JamServiceFactory() {
    }

    public static void main(String[] args) {
        try {
            JamServiceParams sp = getInstance().createServiceParams();
            for (String str : args) {
                sp.includeSourcePattern(new File[]{new File(".")}, str);
            }
            JamService service = getInstance().createService(sp);
            JamPrinter jp = JamPrinter.newInstance();
            PrintWriter out = new PrintWriter(System.out);
            JamClassIterator i = service.getClasses();
            while (i.hasNext()) {
                out.println("-------- ");
                jp.print(i.nextClass(), out);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.flush();
        System.err.flush();
    }
}
