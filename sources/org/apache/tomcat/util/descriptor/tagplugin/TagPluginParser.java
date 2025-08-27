package org.apache.tomcat.util.descriptor.tagplugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.DigesterFactory;
import org.apache.tomcat.util.descriptor.XmlErrorHandler;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/descriptor/tagplugin/TagPluginParser.class */
public class TagPluginParser {
    private static final String PREFIX = "tag-plugins/tag-plugin";
    private final Digester digester;
    private final Log log = LogFactory.getLog((Class<?>) TagPluginParser.class);
    private final Map<String, String> plugins = new HashMap();

    public TagPluginParser(ServletContext context, boolean blockExternal) {
        this.digester = DigesterFactory.newDigester(false, false, new TagPluginRuleSet(), blockExternal);
        this.digester.setClassLoader(context.getClassLoader());
    }

    public void parse(URL url) throws SAXException, IOException {
        try {
            InputStream is = url.openStream();
            Throwable th = null;
            try {
                try {
                    XmlErrorHandler handler = new XmlErrorHandler();
                    this.digester.setErrorHandler(handler);
                    this.digester.push(this);
                    InputSource source = new InputSource(url.toExternalForm());
                    source.setByteStream(is);
                    this.digester.parse(source);
                    if (!handler.getWarnings().isEmpty() || !handler.getErrors().isEmpty()) {
                        handler.logFindings(this.log, source.getSystemId());
                        if (!handler.getErrors().isEmpty()) {
                            throw handler.getErrors().iterator().next();
                        }
                    }
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
                } finally {
                }
            } finally {
            }
        } finally {
            this.digester.reset();
        }
    }

    public void addPlugin(String tagClass, String pluginClass) {
        this.plugins.put(tagClass, pluginClass);
    }

    public Map<String, String> getPlugins() {
        return this.plugins;
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/descriptor/tagplugin/TagPluginParser$TagPluginRuleSet.class */
    private static class TagPluginRuleSet extends RuleSetBase {
        private TagPluginRuleSet() {
        }

        @Override // org.apache.tomcat.util.digester.RuleSetBase, org.apache.tomcat.util.digester.RuleSet
        public void addRuleInstances(Digester digester) {
            digester.addCallMethod(TagPluginParser.PREFIX, "addPlugin", 2);
            digester.addCallParam("tag-plugins/tag-plugin/tag-class", 0);
            digester.addCallParam("tag-plugins/tag-plugin/plugin-class", 1);
        }
    }
}
