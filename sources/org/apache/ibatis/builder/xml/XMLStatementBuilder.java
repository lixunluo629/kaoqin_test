package org.apache.ibatis.builder.xml;

import io.netty.handler.codec.rtsp.RtspHeaders;
import java.util.List;
import java.util.Locale;
import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.w3c.dom.DOMException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/xml/XMLStatementBuilder.class */
public class XMLStatementBuilder extends BaseBuilder {
    private final MapperBuilderAssistant builderAssistant;
    private final XNode context;
    private final String requiredDatabaseId;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context) {
        this(configuration, builderAssistant, context, null);
    }

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context, String databaseId) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.context = context;
        this.requiredDatabaseId = databaseId;
    }

    public void parseStatementNode() {
        KeyGenerator keyGenerator;
        String id = this.context.getStringAttribute("id");
        String databaseId = this.context.getStringAttribute("databaseId");
        if (!databaseIdMatchesCurrent(id, databaseId, this.requiredDatabaseId)) {
            return;
        }
        Integer fetchSize = this.context.getIntAttribute("fetchSize");
        Integer timeout = this.context.getIntAttribute(RtspHeaders.Values.TIMEOUT);
        String parameterMap = this.context.getStringAttribute("parameterMap");
        String parameterType = this.context.getStringAttribute("parameterType");
        Class<?> parameterTypeClass = resolveClass(parameterType);
        String resultMap = this.context.getStringAttribute("resultMap");
        String resultType = this.context.getStringAttribute("resultType");
        String lang = this.context.getStringAttribute(AbstractHtmlElementTag.LANG_ATTRIBUTE);
        LanguageDriver langDriver = getLanguageDriver(lang);
        Class<?> resultTypeClass = resolveClass(resultType);
        String resultSetType = this.context.getStringAttribute("resultSetType");
        StatementType statementType = StatementType.valueOf(this.context.getStringAttribute("statementType", StatementType.PREPARED.toString()));
        ResultSetType resultSetTypeEnum = resolveResultSetType(resultSetType);
        String nodeName = this.context.getNode().getNodeName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
        boolean flushCache = this.context.getBooleanAttribute("flushCache", Boolean.valueOf(!isSelect)).booleanValue();
        boolean useCache = this.context.getBooleanAttribute("useCache", Boolean.valueOf(isSelect)).booleanValue();
        boolean resultOrdered = this.context.getBooleanAttribute("resultOrdered", false).booleanValue();
        XMLIncludeTransformer includeParser = new XMLIncludeTransformer(this.configuration, this.builderAssistant);
        includeParser.applyIncludes(this.context.getNode());
        processSelectKeyNodes(id, parameterTypeClass, langDriver);
        SqlSource sqlSource = langDriver.createSqlSource(this.configuration, this.context, parameterTypeClass);
        String resultSets = this.context.getStringAttribute("resultSets");
        String keyProperty = this.context.getStringAttribute("keyProperty");
        String keyColumn = this.context.getStringAttribute("keyColumn");
        String keyStatementId = this.builderAssistant.applyCurrentNamespace(id + SelectKeyGenerator.SELECT_KEY_SUFFIX, true);
        if (this.configuration.hasKeyGenerator(keyStatementId)) {
            keyGenerator = this.configuration.getKeyGenerator(keyStatementId);
        } else {
            keyGenerator = this.context.getBooleanAttribute("useGeneratedKeys", Boolean.valueOf(this.configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType))).booleanValue() ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
        }
        this.builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout, parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache, resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, langDriver, resultSets);
    }

    private void processSelectKeyNodes(String id, Class<?> parameterTypeClass, LanguageDriver langDriver) throws DOMException {
        List<XNode> selectKeyNodes = this.context.evalNodes("selectKey");
        if (this.configuration.getDatabaseId() != null) {
            parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, this.configuration.getDatabaseId());
        }
        parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, null);
        removeSelectKeyNodes(selectKeyNodes);
    }

    private void parseSelectKeyNodes(String parentId, List<XNode> list, Class<?> parameterTypeClass, LanguageDriver langDriver, String skRequiredDatabaseId) {
        for (XNode nodeToHandle : list) {
            String id = parentId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
            String databaseId = nodeToHandle.getStringAttribute("databaseId");
            if (databaseIdMatchesCurrent(id, databaseId, skRequiredDatabaseId)) {
                parseSelectKeyNode(id, nodeToHandle, parameterTypeClass, langDriver, databaseId);
            }
        }
    }

    private void parseSelectKeyNode(String id, XNode nodeToHandle, Class<?> parameterTypeClass, LanguageDriver langDriver, String databaseId) {
        String resultType = nodeToHandle.getStringAttribute("resultType");
        Class<?> resultTypeClass = resolveClass(resultType);
        StatementType statementType = StatementType.valueOf(nodeToHandle.getStringAttribute("statementType", StatementType.PREPARED.toString()));
        String keyProperty = nodeToHandle.getStringAttribute("keyProperty");
        String keyColumn = nodeToHandle.getStringAttribute("keyColumn");
        boolean executeBefore = "BEFORE".equals(nodeToHandle.getStringAttribute("order", "AFTER"));
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        SqlSource sqlSource = langDriver.createSqlSource(this.configuration, nodeToHandle, parameterTypeClass);
        SqlCommandType sqlCommandType = SqlCommandType.SELECT;
        this.builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, null, null, null, parameterTypeClass, null, resultTypeClass, null, false, false, false, keyGenerator, keyProperty, keyColumn, databaseId, langDriver, null);
        String id2 = this.builderAssistant.applyCurrentNamespace(id, false);
        MappedStatement keyStatement = this.configuration.getMappedStatement(id2, false);
        this.configuration.addKeyGenerator(id2, new SelectKeyGenerator(keyStatement, executeBefore));
    }

    private void removeSelectKeyNodes(List<XNode> selectKeyNodes) throws DOMException {
        for (XNode nodeToHandle : selectKeyNodes) {
            nodeToHandle.getParent().getNode().removeChild(nodeToHandle.getNode());
        }
    }

    private boolean databaseIdMatchesCurrent(String id, String databaseId, String requiredDatabaseId) {
        if (requiredDatabaseId != null) {
            if (!requiredDatabaseId.equals(databaseId)) {
                return false;
            }
            return true;
        }
        if (databaseId != null) {
            return false;
        }
        String id2 = this.builderAssistant.applyCurrentNamespace(id, false);
        if (this.configuration.hasStatement(id2, false)) {
            MappedStatement previous = this.configuration.getMappedStatement(id2, false);
            if (previous.getDatabaseId() != null) {
                return false;
            }
            return true;
        }
        return true;
    }

    private LanguageDriver getLanguageDriver(String lang) {
        Class<?> langClass = null;
        if (lang != null) {
            langClass = resolveClass(lang);
        }
        return this.builderAssistant.getLanguageDriver(langClass);
    }
}
