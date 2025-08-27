package org.apache.xmlbeans.impl.xquery.saxon;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlRuntimeException;
import org.apache.xmlbeans.XmlTokenSource;
import org.apache.xmlbeans.impl.store.QueryDelegate;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xquery/saxon/XBeansXQuery.class */
public class XBeansXQuery implements QueryDelegate.QueryInterface {
    private XQueryExpression xquery;
    private String contextVar;
    private Configuration config = new Configuration();

    public XBeansXQuery(String query, String contextVar, Integer boundary, XmlOptions xmlOptions) {
        this.config.setDOMLevel(2);
        this.config.setTreeModel(0);
        StaticQueryContext sc = new StaticQueryContext(this.config);
        Map<String, String> nsMap = (Map) xmlOptions.get(XmlOptions.LOAD_ADDITIONAL_NAMESPACES);
        if (nsMap != null) {
            for (Map.Entry<String, String> me : nsMap.entrySet()) {
                sc.declareNamespace(me.getKey(), me.getValue());
            }
        }
        this.contextVar = contextVar;
        try {
            this.xquery = sc.compileQuery(boundary.intValue() == 0 ? "declare variable $" + contextVar + " external;" + query : query.substring(0, boundary.intValue()) + "declare variable $" + contextVar + " external;" + query.substring(boundary.intValue()));
        } catch (TransformerException e) {
            throw new XmlRuntimeException(e);
        }
    }

    @Override // org.apache.xmlbeans.impl.store.QueryDelegate.QueryInterface
    public List execQuery(Object node, Map variableBindings) {
        try {
            Node contextNode = (Node) node;
            DocumentInfo documentInfoBuildDocument = this.config.buildDocument(new DOMSource(contextNode));
            DynamicQueryContext dc = new DynamicQueryContext(this.config);
            dc.setContextItem(documentInfoBuildDocument);
            dc.setParameter(this.contextVar, documentInfoBuildDocument);
            if (variableBindings != null) {
                for (Map.Entry entry : variableBindings.entrySet()) {
                    String key = (String) entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof XmlTokenSource) {
                        Node paramObject = ((XmlTokenSource) value).getDomNode();
                        dc.setParameter(key, paramObject);
                    } else if (value instanceof String) {
                        dc.setParameter(key, value);
                    }
                }
            }
            List saxonNodes = this.xquery.evaluate(dc);
            ListIterator it = saxonNodes.listIterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof NodeInfo) {
                    it.set(NodeOverNodeInfo.wrap((NodeInfo) o));
                }
            }
            return saxonNodes;
        } catch (TransformerException e) {
            throw new RuntimeException("Error binding " + this.contextVar, e);
        }
    }
}
