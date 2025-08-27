package org.apache.ibatis.parsing;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/parsing/XNode.class */
public class XNode {
    private final Node node;
    private final String name;
    private final String body;
    private final Properties attributes;
    private final Properties variables;
    private final XPathParser xpathParser;

    public XNode(XPathParser xpathParser, Node node, Properties variables) {
        this.xpathParser = xpathParser;
        this.node = node;
        this.name = node.getNodeName();
        this.variables = variables;
        this.attributes = parseAttributes(node);
        this.body = parseBody(node);
    }

    public XNode newXNode(Node node) {
        return new XNode(this.xpathParser, node, this.variables);
    }

    public XNode getParent() {
        Node parent = this.node.getParentNode();
        if (parent == null || !(parent instanceof Element)) {
            return null;
        }
        return new XNode(this.xpathParser, parent, this.variables);
    }

    public String getPath() {
        StringBuilder builder = new StringBuilder();
        Node parentNode = this.node;
        while (true) {
            Node current = parentNode;
            if (current == null || !(current instanceof Element)) {
                break;
            }
            if (current != this.node) {
                builder.insert(0, "/");
            }
            builder.insert(0, current.getNodeName());
            parentNode = current.getParentNode();
        }
        return builder.toString();
    }

    public String getValueBasedIdentifier() {
        StringBuilder builder = new StringBuilder();
        XNode parent = this;
        while (true) {
            XNode current = parent;
            if (current != null) {
                if (current != this) {
                    builder.insert(0, "_");
                }
                String value = current.getStringAttribute("id", current.getStringAttribute("value", current.getStringAttribute(BeanDefinitionParserDelegate.PROPERTY_ELEMENT, null)));
                if (value != null) {
                    String value2 = value.replace('.', '_');
                    builder.insert(0, "]");
                    builder.insert(0, value2);
                    builder.insert(0, PropertyAccessor.PROPERTY_KEY_PREFIX);
                }
                builder.insert(0, current.getName());
                parent = current.getParent();
            } else {
                return builder.toString();
            }
        }
    }

    public String evalString(String expression) {
        return this.xpathParser.evalString(this.node, expression);
    }

    public Boolean evalBoolean(String expression) {
        return this.xpathParser.evalBoolean(this.node, expression);
    }

    public Double evalDouble(String expression) {
        return this.xpathParser.evalDouble(this.node, expression);
    }

    public List<XNode> evalNodes(String expression) {
        return this.xpathParser.evalNodes(this.node, expression);
    }

    public XNode evalNode(String expression) {
        return this.xpathParser.evalNode(this.node, expression);
    }

    public Node getNode() {
        return this.node;
    }

    public String getName() {
        return this.name;
    }

    public String getStringBody() {
        return getStringBody(null);
    }

    public String getStringBody(String def) {
        if (this.body == null) {
            return def;
        }
        return this.body;
    }

    public Boolean getBooleanBody() {
        return getBooleanBody(null);
    }

    public Boolean getBooleanBody(Boolean def) {
        if (this.body == null) {
            return def;
        }
        return Boolean.valueOf(this.body);
    }

    public Integer getIntBody() {
        return getIntBody(null);
    }

    public Integer getIntBody(Integer def) {
        if (this.body == null) {
            return def;
        }
        return Integer.valueOf(Integer.parseInt(this.body));
    }

    public Long getLongBody() {
        return getLongBody(null);
    }

    public Long getLongBody(Long def) {
        if (this.body == null) {
            return def;
        }
        return Long.valueOf(Long.parseLong(this.body));
    }

    public Double getDoubleBody() {
        return getDoubleBody(null);
    }

    public Double getDoubleBody(Double def) {
        if (this.body == null) {
            return def;
        }
        return Double.valueOf(Double.parseDouble(this.body));
    }

    public Float getFloatBody() {
        return getFloatBody(null);
    }

    public Float getFloatBody(Float def) {
        if (this.body == null) {
            return def;
        }
        return Float.valueOf(Float.parseFloat(this.body));
    }

    public <T extends Enum<T>> T getEnumAttribute(Class<T> cls, String str) {
        return (T) getEnumAttribute(cls, str, null);
    }

    public <T extends Enum<T>> T getEnumAttribute(Class<T> cls, String str, T t) {
        String stringAttribute = getStringAttribute(str);
        if (stringAttribute == null) {
            return t;
        }
        return (T) Enum.valueOf(cls, stringAttribute);
    }

    public String getStringAttribute(String name) {
        return getStringAttribute(name, null);
    }

    public String getStringAttribute(String name, String def) {
        String value = this.attributes.getProperty(name);
        if (value == null) {
            return def;
        }
        return value;
    }

    public Boolean getBooleanAttribute(String name) {
        return getBooleanAttribute(name, null);
    }

    public Boolean getBooleanAttribute(String name, Boolean def) {
        String value = this.attributes.getProperty(name);
        if (value == null) {
            return def;
        }
        return Boolean.valueOf(value);
    }

    public Integer getIntAttribute(String name) {
        return getIntAttribute(name, null);
    }

    public Integer getIntAttribute(String name, Integer def) {
        String value = this.attributes.getProperty(name);
        if (value == null) {
            return def;
        }
        return Integer.valueOf(Integer.parseInt(value));
    }

    public Long getLongAttribute(String name) {
        return getLongAttribute(name, null);
    }

    public Long getLongAttribute(String name, Long def) {
        String value = this.attributes.getProperty(name);
        if (value == null) {
            return def;
        }
        return Long.valueOf(Long.parseLong(value));
    }

    public Double getDoubleAttribute(String name) {
        return getDoubleAttribute(name, null);
    }

    public Double getDoubleAttribute(String name, Double def) {
        String value = this.attributes.getProperty(name);
        if (value == null) {
            return def;
        }
        return Double.valueOf(Double.parseDouble(value));
    }

    public Float getFloatAttribute(String name) {
        return getFloatAttribute(name, null);
    }

    public Float getFloatAttribute(String name, Float def) {
        String value = this.attributes.getProperty(name);
        if (value == null) {
            return def;
        }
        return Float.valueOf(Float.parseFloat(value));
    }

    public List<XNode> getChildren() {
        List<XNode> children = new ArrayList<>();
        NodeList nodeList = this.node.getChildNodes();
        if (nodeList != null) {
            int n = nodeList.getLength();
            for (int i = 0; i < n; i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == 1) {
                    children.add(new XNode(this.xpathParser, node, this.variables));
                }
            }
        }
        return children;
    }

    public Properties getChildrenAsProperties() {
        Properties properties = new Properties();
        for (XNode child : getChildren()) {
            String name = child.getStringAttribute("name");
            String value = child.getStringAttribute("value");
            if (name != null && value != null) {
                properties.setProperty(name, value);
            }
        }
        return properties;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<");
        builder.append(this.name);
        for (Map.Entry<Object, Object> entry : this.attributes.entrySet()) {
            builder.append(SymbolConstants.SPACE_SYMBOL);
            builder.append(entry.getKey());
            builder.append("=\"");
            builder.append(entry.getValue());
            builder.append(SymbolConstants.QUOTES_SYMBOL);
        }
        List<XNode> children = getChildren();
        if (!children.isEmpty()) {
            builder.append(">\n");
            for (XNode node : children) {
                builder.append(node.toString());
            }
            builder.append("</");
            builder.append(this.name);
            builder.append(">");
        } else if (this.body != null) {
            builder.append(">");
            builder.append(this.body);
            builder.append("</");
            builder.append(this.name);
            builder.append(">");
        } else {
            builder.append("/>");
        }
        builder.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        return builder.toString();
    }

    private Properties parseAttributes(Node n) {
        Properties attributes = new Properties();
        NamedNodeMap attributeNodes = n.getAttributes();
        if (attributeNodes != null) {
            for (int i = 0; i < attributeNodes.getLength(); i++) {
                Node attribute = attributeNodes.item(i);
                String value = PropertyParser.parse(attribute.getNodeValue(), this.variables);
                attributes.put(attribute.getNodeName(), value);
            }
        }
        return attributes;
    }

    private String parseBody(Node node) throws DOMException {
        String data = getBodyData(node);
        if (data == null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                data = getBodyData(child);
                if (data != null) {
                    break;
                }
            }
        }
        return data;
    }

    private String getBodyData(Node child) throws DOMException {
        if (child.getNodeType() == 4 || child.getNodeType() == 3) {
            String data = ((CharacterData) child).getData();
            return PropertyParser.parse(data, this.variables);
        }
        return null;
    }
}
