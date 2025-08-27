package org.apache.ibatis.builder.xml;

import java.util.HashMap;
import java.util.Properties;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/xml/XMLIncludeTransformer.class */
public class XMLIncludeTransformer {
    private final Configuration configuration;
    private final MapperBuilderAssistant builderAssistant;

    public XMLIncludeTransformer(Configuration configuration, MapperBuilderAssistant builderAssistant) {
        this.configuration = configuration;
        this.builderAssistant = builderAssistant;
    }

    public void applyIncludes(Node source) throws DOMException {
        Properties variablesContext = new Properties();
        Properties configurationVariables = this.configuration.getVariables();
        if (configurationVariables != null) {
            variablesContext.putAll(configurationVariables);
        }
        applyIncludes(source, variablesContext, false);
    }

    private void applyIncludes(Node source, Properties variablesContext, boolean included) throws DOMException {
        if (source.getNodeName().equals("include")) {
            Node toInclude = findSqlFragment(getStringAttribute(source, "refid"), variablesContext);
            Properties toIncludeContext = getVariablesContext(source, variablesContext);
            applyIncludes(toInclude, toIncludeContext, true);
            if (toInclude.getOwnerDocument() != source.getOwnerDocument()) {
                toInclude = source.getOwnerDocument().importNode(toInclude, true);
            }
            source.getParentNode().replaceChild(toInclude, source);
            while (toInclude.hasChildNodes()) {
                toInclude.getParentNode().insertBefore(toInclude.getFirstChild(), toInclude);
            }
            toInclude.getParentNode().removeChild(toInclude);
            return;
        }
        if (source.getNodeType() == 1) {
            if (included && !variablesContext.isEmpty()) {
                NamedNodeMap attributes = source.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attr = attributes.item(i);
                    attr.setNodeValue(PropertyParser.parse(attr.getNodeValue(), variablesContext));
                }
            }
            NodeList children = source.getChildNodes();
            for (int i2 = 0; i2 < children.getLength(); i2++) {
                applyIncludes(children.item(i2), variablesContext, included);
            }
            return;
        }
        if (included && source.getNodeType() == 3 && !variablesContext.isEmpty()) {
            source.setNodeValue(PropertyParser.parse(source.getNodeValue(), variablesContext));
        }
    }

    private Node findSqlFragment(String refid, Properties variables) {
        String refid2 = this.builderAssistant.applyCurrentNamespace(PropertyParser.parse(refid, variables), true);
        try {
            XNode nodeToInclude = this.configuration.getSqlFragments().get(refid2);
            return nodeToInclude.getNode().cloneNode(true);
        } catch (IllegalArgumentException e) {
            throw new IncompleteElementException("Could not find SQL statement to include with refid '" + refid2 + "'", e);
        }
    }

    private String getStringAttribute(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getNodeValue();
    }

    private Properties getVariablesContext(Node node, Properties inheritedVariablesContext) {
        HashMap map = null;
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() == 1) {
                String name = getStringAttribute(n, "name");
                String value = PropertyParser.parse(getStringAttribute(n, "value"), inheritedVariablesContext);
                if (map == null) {
                    map = new HashMap();
                }
                if (map.put(name, value) != null) {
                    throw new BuilderException("Variable " + name + " defined twice in the same include definition");
                }
            }
        }
        if (map == null) {
            return inheritedVariablesContext;
        }
        Properties newProperties = new Properties();
        newProperties.putAll(inheritedVariablesContext);
        newProperties.putAll(map);
        return newProperties;
    }
}
