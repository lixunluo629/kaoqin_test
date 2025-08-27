package org.springframework.transaction.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.data.repository.util.TxUtils;
import org.w3c.dom.Element;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/config/JtaTransactionManagerBeanDefinitionParser.class */
public class JtaTransactionManagerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected String getBeanClassName(Element element) {
        return JtaTransactionManagerFactoryBean.resolveJtaTransactionManagerClassName();
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
        return TxUtils.DEFAULT_TRANSACTION_MANAGER;
    }
}
