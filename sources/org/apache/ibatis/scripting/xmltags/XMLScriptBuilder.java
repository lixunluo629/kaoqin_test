package org.apache.ibatis.scripting.xmltags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.w3c.dom.NodeList;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder.class */
public class XMLScriptBuilder extends BaseBuilder {
    private final XNode context;
    private boolean isDynamic;
    private final Class<?> parameterType;
    private final Map<String, NodeHandler> nodeHandlerMap;

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$NodeHandler.class */
    private interface NodeHandler {
        void handleNode(XNode xNode, List<SqlNode> list);
    }

    public XMLScriptBuilder(Configuration configuration, XNode context) {
        this(configuration, context, null);
    }

    public XMLScriptBuilder(Configuration configuration, XNode context, Class<?> parameterType) {
        super(configuration);
        this.nodeHandlerMap = new HashMap();
        this.context = context;
        this.parameterType = parameterType;
        initNodeHandlerMap();
    }

    private void initNodeHandlerMap() {
        this.nodeHandlerMap.put("trim", new TrimHandler());
        this.nodeHandlerMap.put("where", new WhereHandler());
        this.nodeHandlerMap.put("set", new SetHandler());
        this.nodeHandlerMap.put("foreach", new ForEachHandler());
        this.nodeHandlerMap.put("if", new IfHandler());
        this.nodeHandlerMap.put("choose", new ChooseHandler());
        this.nodeHandlerMap.put("when", new IfHandler());
        this.nodeHandlerMap.put("otherwise", new OtherwiseHandler());
        this.nodeHandlerMap.put("bind", new BindHandler());
    }

    public SqlSource parseScriptNode() {
        SqlSource sqlSource;
        MixedSqlNode rootSqlNode = parseDynamicTags(this.context);
        if (this.isDynamic) {
            sqlSource = new DynamicSqlSource(this.configuration, rootSqlNode);
        } else {
            sqlSource = new RawSqlSource(this.configuration, rootSqlNode, this.parameterType);
        }
        return sqlSource;
    }

    protected MixedSqlNode parseDynamicTags(XNode node) {
        List<SqlNode> contents = new ArrayList<>();
        NodeList children = node.getNode().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            XNode child = node.newXNode(children.item(i));
            if (child.getNode().getNodeType() == 4 || child.getNode().getNodeType() == 3) {
                String data = child.getStringBody("");
                TextSqlNode textSqlNode = new TextSqlNode(data);
                if (textSqlNode.isDynamic()) {
                    contents.add(textSqlNode);
                    this.isDynamic = true;
                } else {
                    contents.add(new StaticTextSqlNode(data));
                }
            } else if (child.getNode().getNodeType() != 1) {
                continue;
            } else {
                String nodeName = child.getNode().getNodeName();
                NodeHandler handler = this.nodeHandlerMap.get(nodeName);
                if (handler == null) {
                    throw new BuilderException("Unknown element <" + nodeName + "> in SQL statement.");
                }
                handler.handleNode(child, contents);
                this.isDynamic = true;
            }
        }
        return new MixedSqlNode(contents);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$BindHandler.class */
    private class BindHandler implements NodeHandler {
        public BindHandler() {
        }

        @Override // org.apache.ibatis.scripting.xmltags.XMLScriptBuilder.NodeHandler
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            String name = nodeToHandle.getStringAttribute("name");
            String expression = nodeToHandle.getStringAttribute("value");
            VarDeclSqlNode node = new VarDeclSqlNode(name, expression);
            targetContents.add(node);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$TrimHandler.class */
    private class TrimHandler implements NodeHandler {
        public TrimHandler() {
        }

        @Override // org.apache.ibatis.scripting.xmltags.XMLScriptBuilder.NodeHandler
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = XMLScriptBuilder.this.parseDynamicTags(nodeToHandle);
            String prefix = nodeToHandle.getStringAttribute("prefix");
            String prefixOverrides = nodeToHandle.getStringAttribute("prefixOverrides");
            String suffix = nodeToHandle.getStringAttribute("suffix");
            String suffixOverrides = nodeToHandle.getStringAttribute("suffixOverrides");
            TrimSqlNode trim = new TrimSqlNode(XMLScriptBuilder.this.configuration, mixedSqlNode, prefix, prefixOverrides, suffix, suffixOverrides);
            targetContents.add(trim);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$WhereHandler.class */
    private class WhereHandler implements NodeHandler {
        public WhereHandler() {
        }

        @Override // org.apache.ibatis.scripting.xmltags.XMLScriptBuilder.NodeHandler
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = XMLScriptBuilder.this.parseDynamicTags(nodeToHandle);
            WhereSqlNode where = new WhereSqlNode(XMLScriptBuilder.this.configuration, mixedSqlNode);
            targetContents.add(where);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$SetHandler.class */
    private class SetHandler implements NodeHandler {
        public SetHandler() {
        }

        @Override // org.apache.ibatis.scripting.xmltags.XMLScriptBuilder.NodeHandler
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = XMLScriptBuilder.this.parseDynamicTags(nodeToHandle);
            SetSqlNode set = new SetSqlNode(XMLScriptBuilder.this.configuration, mixedSqlNode);
            targetContents.add(set);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$ForEachHandler.class */
    private class ForEachHandler implements NodeHandler {
        public ForEachHandler() {
        }

        @Override // org.apache.ibatis.scripting.xmltags.XMLScriptBuilder.NodeHandler
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = XMLScriptBuilder.this.parseDynamicTags(nodeToHandle);
            String collection = nodeToHandle.getStringAttribute("collection");
            String item = nodeToHandle.getStringAttribute("item");
            String index = nodeToHandle.getStringAttribute(BeanDefinitionParserDelegate.INDEX_ATTRIBUTE);
            String open = nodeToHandle.getStringAttribute("open");
            String close = nodeToHandle.getStringAttribute("close");
            String separator = nodeToHandle.getStringAttribute("separator");
            ForEachSqlNode forEachSqlNode = new ForEachSqlNode(XMLScriptBuilder.this.configuration, mixedSqlNode, collection, index, item, open, close, separator);
            targetContents.add(forEachSqlNode);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$IfHandler.class */
    private class IfHandler implements NodeHandler {
        public IfHandler() {
        }

        @Override // org.apache.ibatis.scripting.xmltags.XMLScriptBuilder.NodeHandler
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = XMLScriptBuilder.this.parseDynamicTags(nodeToHandle);
            String test = nodeToHandle.getStringAttribute("test");
            IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode, test);
            targetContents.add(ifSqlNode);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$OtherwiseHandler.class */
    private class OtherwiseHandler implements NodeHandler {
        public OtherwiseHandler() {
        }

        @Override // org.apache.ibatis.scripting.xmltags.XMLScriptBuilder.NodeHandler
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = XMLScriptBuilder.this.parseDynamicTags(nodeToHandle);
            targetContents.add(mixedSqlNode);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/XMLScriptBuilder$ChooseHandler.class */
    private class ChooseHandler implements NodeHandler {
        public ChooseHandler() {
        }

        @Override // org.apache.ibatis.scripting.xmltags.XMLScriptBuilder.NodeHandler
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> whenSqlNodes = new ArrayList<>();
            List<SqlNode> otherwiseSqlNodes = new ArrayList<>();
            handleWhenOtherwiseNodes(nodeToHandle, whenSqlNodes, otherwiseSqlNodes);
            SqlNode defaultSqlNode = getDefaultSqlNode(otherwiseSqlNodes);
            ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes, defaultSqlNode);
            targetContents.add(chooseSqlNode);
        }

        private void handleWhenOtherwiseNodes(XNode chooseSqlNode, List<SqlNode> ifSqlNodes, List<SqlNode> defaultSqlNodes) {
            List<XNode> children = chooseSqlNode.getChildren();
            for (XNode child : children) {
                String nodeName = child.getNode().getNodeName();
                NodeHandler handler = (NodeHandler) XMLScriptBuilder.this.nodeHandlerMap.get(nodeName);
                if (handler instanceof IfHandler) {
                    handler.handleNode(child, ifSqlNodes);
                } else if (handler instanceof OtherwiseHandler) {
                    handler.handleNode(child, defaultSqlNodes);
                }
            }
        }

        private SqlNode getDefaultSqlNode(List<SqlNode> defaultSqlNodes) {
            SqlNode defaultSqlNode = null;
            if (defaultSqlNodes.size() == 1) {
                defaultSqlNode = defaultSqlNodes.get(0);
            } else if (defaultSqlNodes.size() > 1) {
                throw new BuilderException("Too many default (otherwise) elements in choose statement.");
            }
            return defaultSqlNode;
        }
    }
}
