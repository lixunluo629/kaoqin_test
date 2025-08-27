package org.apache.xmlbeans.impl.xpath.saxon;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.dom.NodeWrapper;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.VirtualNode;
import net.sf.saxon.sxpath.IndependentContext;
import net.sf.saxon.sxpath.XPathDynamicContext;
import net.sf.saxon.sxpath.XPathEvaluator;
import net.sf.saxon.sxpath.XPathExpression;
import net.sf.saxon.sxpath.XPathVariable;
import net.sf.saxon.value.Value;
import org.apache.xmlbeans.impl.store.PathDelegate;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xpath/saxon/XBeansXPath.class */
public class XBeansXPath implements PathDelegate.SelectPathInterface {
    private Object[] namespaceMap;
    private String path;
    private String contextVar;
    private String defaultNS;

    public XBeansXPath(String path, String contextVar, Map namespaceMap, String defaultNS) {
        this.path = path;
        this.contextVar = contextVar;
        this.defaultNS = defaultNS;
        this.namespaceMap = namespaceMap.entrySet().toArray();
    }

    public List selectNodes(Object node) {
        try {
            Node contextNode = (Node) node;
            XPathEvaluator xpe = new XPathEvaluator();
            Configuration config = new Configuration();
            config.setDOMLevel(2);
            config.setTreeModel(0);
            IndependentContext sc = new IndependentContext(config);
            if (this.defaultNS != null) {
                sc.setDefaultElementNamespace(this.defaultNS);
            }
            for (int i = 0; i < this.namespaceMap.length; i++) {
                Map.Entry entry = (Map.Entry) this.namespaceMap[i];
                sc.declareNamespace((String) entry.getKey(), (String) entry.getValue());
            }
            xpe.setStaticContext(sc);
            XPathVariable thisVar = xpe.declareVariable("", this.contextVar);
            XPathExpression xpath = xpe.createExpression(this.path);
            NodeInfo contextItem = config.unravel(new DOMSource(contextNode));
            XPathDynamicContext dc = xpath.createDynamicContext((Item) null);
            dc.setContextItem(contextItem);
            dc.setVariable(thisVar, contextItem);
            List saxonNodes = xpath.evaluate(dc);
            ListIterator it = saxonNodes.listIterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof NodeInfo) {
                    if (o instanceof NodeWrapper) {
                        Node n = getUnderlyingNode((NodeWrapper) o);
                        it.set(n);
                    } else {
                        it.set(((NodeInfo) o).getStringValue());
                    }
                } else if (o instanceof Item) {
                    it.set(Value.convertToJava((Item) o));
                }
            }
            return saxonNodes;
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.apache.xmlbeans.impl.store.PathDelegate.SelectPathInterface
    public List selectPath(Object node) {
        return selectNodes(node);
    }

    private static Node getUnderlyingNode(VirtualNode v) {
        Object underlyingNode = v;
        while (true) {
            Object o = underlyingNode;
            if (o instanceof VirtualNode) {
                underlyingNode = ((VirtualNode) o).getUnderlyingNode();
            } else {
                return (Node) o;
            }
        }
    }
}
