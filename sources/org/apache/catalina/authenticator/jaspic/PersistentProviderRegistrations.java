package org.apache.catalina.authenticator.jaspic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogConfigurationException;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.res.StringManager;
import org.xml.sax.SAXException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/authenticator/jaspic/PersistentProviderRegistrations.class */
final class PersistentProviderRegistrations {
    private static final StringManager sm = StringManager.getManager((Class<?>) PersistentProviderRegistrations.class);

    private PersistentProviderRegistrations() {
    }

    static Providers loadProviders(File configFile) throws IOException {
        try {
            InputStream is = new FileInputStream(configFile);
            Throwable th = null;
            try {
                Digester digester = new Digester();
                try {
                    digester.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
                    digester.setValidating(true);
                    digester.setNamespaceAware(true);
                    Providers result = new Providers();
                    digester.push(result);
                    digester.addObjectCreate("jaspic-providers/provider", Provider.class.getName());
                    digester.addSetProperties("jaspic-providers/provider");
                    digester.addSetNext("jaspic-providers/provider", "addProvider", Provider.class.getName());
                    digester.addObjectCreate("jaspic-providers/provider/property", Property.class.getName());
                    digester.addSetProperties("jaspic-providers/provider/property");
                    digester.addSetNext("jaspic-providers/provider/property", "addProperty", Property.class.getName());
                    digester.parse(is);
                    if (is != null) {
                        if (0 != 0) {
                            try {
                                is.close();
                            } catch (Throwable x2) {
                                th.addSuppressed(x2);
                            }
                        } else {
                            is.close();
                        }
                    }
                    return result;
                } catch (Exception e) {
                    throw new SecurityException(e);
                }
            } catch (Throwable th2) {
                if (is != null) {
                    if (0 != 0) {
                        try {
                            is.close();
                        } catch (Throwable x22) {
                            th.addSuppressed(x22);
                        }
                    } else {
                        is.close();
                    }
                }
                throw th2;
            }
        } catch (IOException | SAXException e2) {
            throw new SecurityException(e2);
        }
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r14v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x0208: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('fos' java.io.OutputStream)]) A[TRY_LEAVE], block:B:58:0x0208 */
    /* JADX WARN: Not initialized variable reg: 14, insn: 0x020d: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:60:0x020d */
    /* JADX WARN: Type inference failed for: r13v1, names: [fos], types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r14v1, types: [java.lang.Throwable] */
    static void writeProviders(Providers providers, File configFile) throws IOException, LogConfigurationException {
        File configFileOld = new File(configFile.getAbsolutePath() + ".old");
        File configFileNew = new File(configFile.getAbsolutePath() + ".new");
        if (configFileOld.exists() && configFileOld.delete()) {
            throw new SecurityException(sm.getString("persistentProviderRegistrations.existsDeleteFail", configFileOld.getAbsolutePath()));
        }
        if (configFileNew.exists() && configFileNew.delete()) {
            throw new SecurityException(sm.getString("persistentProviderRegistrations.existsDeleteFail", configFileNew.getAbsolutePath()));
        }
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(configFileNew);
                Throwable th = null;
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                Throwable th2 = null;
                try {
                    try {
                        outputStreamWriter.write("<?xml version='1.0' encoding='utf-8'?>\n<jaspic-providers\n    xmlns=\"http://tomcat.apache.org/xml\"\n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n    xsi:schemaLocation=\"http://tomcat.apache.org/xml jaspic-providers.xsd\"\n    version=\"1.0\">\n");
                        for (Provider provider : providers.providers) {
                            outputStreamWriter.write("  <provider");
                            writeOptional("className", provider.getClassName(), outputStreamWriter);
                            writeOptional("layer", provider.getLayer(), outputStreamWriter);
                            writeOptional("appContext", provider.getAppContext(), outputStreamWriter);
                            writeOptional("description", provider.getDescription(), outputStreamWriter);
                            outputStreamWriter.write(">\n");
                            for (Map.Entry<String, String> entry : provider.getProperties().entrySet()) {
                                outputStreamWriter.write("    <property name=\"");
                                outputStreamWriter.write(entry.getKey());
                                outputStreamWriter.write("\" value=\"");
                                outputStreamWriter.write(entry.getValue());
                                outputStreamWriter.write("\"/>\n");
                            }
                            outputStreamWriter.write("  </provider>\n");
                        }
                        outputStreamWriter.write("</jaspic-providers>\n");
                        if (outputStreamWriter != null) {
                            if (0 != 0) {
                                try {
                                    outputStreamWriter.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                outputStreamWriter.close();
                            }
                        }
                        if (fileOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    fileOutputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                fileOutputStream.close();
                            }
                        }
                        if (configFile.isFile() && !configFile.renameTo(configFileOld)) {
                            throw new SecurityException(sm.getString("persistentProviderRegistrations.moveFail", configFile.getAbsolutePath(), configFileOld.getAbsolutePath()));
                        }
                        if (!configFileNew.renameTo(configFile)) {
                            throw new SecurityException(sm.getString("persistentProviderRegistrations.moveFail", configFileNew.getAbsolutePath(), configFile.getAbsolutePath()));
                        }
                        if (!configFileOld.exists() || configFileOld.delete()) {
                            return;
                        }
                        LogFactory.getLog((Class<?>) PersistentProviderRegistrations.class).warn(sm.getString("persistentProviderRegistrations.deleteFail", configFileOld.getAbsolutePath()));
                    } catch (Throwable th5) {
                        if (outputStreamWriter != null) {
                            if (th2 != null) {
                                try {
                                    outputStreamWriter.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                outputStreamWriter.close();
                            }
                        }
                        throw th5;
                    }
                } catch (Throwable th7) {
                    th2 = th7;
                    throw th7;
                }
            } catch (IOException e) {
                if (!configFileNew.delete()) {
                    Log log = LogFactory.getLog((Class<?>) PersistentProviderRegistrations.class);
                    log.warn(sm.getString("persistentProviderRegistrations.deleteFail", configFileNew.getAbsolutePath()));
                }
                throw new SecurityException(e);
            }
        } finally {
        }
    }

    private static void writeOptional(String name, String value, Writer writer) throws IOException {
        if (value != null) {
            writer.write(SymbolConstants.SPACE_SYMBOL + name + "=\"");
            writer.write(value);
            writer.write(SymbolConstants.QUOTES_SYMBOL);
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/authenticator/jaspic/PersistentProviderRegistrations$Providers.class */
    public static class Providers {
        private final List<Provider> providers = new ArrayList();

        public void addProvider(Provider provider) {
            this.providers.add(provider);
        }

        public List<Provider> getProviders() {
            return this.providers;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/authenticator/jaspic/PersistentProviderRegistrations$Provider.class */
    public static class Provider {
        private String className;
        private String layer;
        private String appContext;
        private String description;
        private final Map<String, String> properties = new HashMap();

        public String getClassName() {
            return this.className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getLayer() {
            return this.layer;
        }

        public void setLayer(String layer) {
            this.layer = layer;
        }

        public String getAppContext() {
            return this.appContext;
        }

        public void setAppContext(String appContext) {
            this.appContext = appContext;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void addProperty(Property property) {
            this.properties.put(property.getName(), property.getValue());
        }

        void addProperty(String name, String value) {
            this.properties.put(name, value);
        }

        public Map<String, String> getProperties() {
            return this.properties;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/authenticator/jaspic/PersistentProviderRegistrations$Property.class */
    public static class Property {
        private String name;
        private String value;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
