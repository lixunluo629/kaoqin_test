package org.springframework.data.redis.config;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/config/RedisListenerContainerParser.class */
class RedisListenerContainerParser extends AbstractSimpleBeanDefinitionParser {
    RedisListenerContainerParser() {
    }

    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected Class<RedisMessageListenerContainer> getBeanClass(Element element) {
        return RedisMessageListenerContainer.class;
    }

    @Override // org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser, org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        NamedNodeMap attributes = element.getAttributes();
        for (int x = 0; x < attributes.getLength(); x++) {
            Attr attribute = (Attr) attributes.item(x);
            if (isEligibleAttribute(attribute, parserContext)) {
                String propertyName = extractPropertyName(attribute.getLocalName());
                Assert.state(StringUtils.hasText(propertyName), "Illegal property name returned from 'extractPropertyName(String)': cannot be null or empty.");
                builder.addPropertyReference(propertyName, attribute.getValue());
            }
        }
        String phase = element.getAttribute("phase");
        if (StringUtils.hasText(phase)) {
            builder.addPropertyValue("phase", phase);
        }
        postProcess(builder, element);
        List<Element> listDefs = DomUtils.getChildElementsByTagName(element, "listener");
        if (!listDefs.isEmpty()) {
            ManagedMap<BeanDefinition, Collection<? extends BeanDefinition>> listeners = new ManagedMap<>(listDefs.size());
            for (Element listElement : listDefs) {
                Object[] listenerDefinition = parseListener(listElement);
                listeners.put((BeanDefinition) listenerDefinition[0], (Collection) listenerDefinition[1]);
            }
            builder.addPropertyValue("messageListeners", listeners);
        }
    }

    @Override // org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser
    protected boolean isEligibleAttribute(String attributeName) {
        return !"phase".equals(attributeName);
    }

    private Object[] parseListener(Element element) {
        Object[] ret = new Object[2];
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition((Class<?>) MessageListenerAdapter.class);
        builder.addConstructorArgReference(element.getAttribute("ref"));
        String method = element.getAttribute(JamXmlElements.METHOD);
        if (StringUtils.hasText(method)) {
            builder.addPropertyValue("defaultListenerMethod", method);
        }
        String serializer = element.getAttribute("serializer");
        if (StringUtils.hasText(serializer)) {
            builder.addPropertyReference("serializer", serializer);
        }
        Collection<Topic> topics = new ArrayList<>();
        String xTopics = element.getAttribute("topic");
        if (StringUtils.hasText(xTopics)) {
            String[] array = StringUtils.delimitedListToStringArray(xTopics, SymbolConstants.SPACE_SYMBOL);
            for (String string : array) {
                topics.add(string.contains("*") ? new PatternTopic(string) : new ChannelTopic(string));
            }
        }
        ret[0] = builder.getBeanDefinition();
        ret[1] = topics;
        return ret;
    }

    @Override // org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
    protected boolean shouldGenerateId() {
        return true;
    }
}
