package org.springframework.transaction.annotation;

import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/annotation/SpringTransactionAnnotationParser.class */
public class SpringTransactionAnnotationParser implements TransactionAnnotationParser, Serializable {
    @Override // org.springframework.transaction.annotation.TransactionAnnotationParser
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement element) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(element, (Class<? extends Annotation>) Transactional.class);
        if (attributes != null) {
            return parseTransactionAnnotation(attributes);
        }
        return null;
    }

    public TransactionAttribute parseTransactionAnnotation(Transactional ann) {
        return parseTransactionAnnotation(AnnotationUtils.getAnnotationAttributes(ann, false, false));
    }

    protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
        RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
        Propagation propagation = (Propagation) attributes.getEnum("propagation");
        rbta.setPropagationBehavior(propagation.value());
        Isolation isolation = (Isolation) attributes.getEnum("isolation");
        rbta.setIsolationLevel(isolation.value());
        rbta.setTimeout(attributes.getNumber(RtspHeaders.Values.TIMEOUT).intValue());
        rbta.setReadOnly(attributes.getBoolean(DefaultTransactionDefinition.READ_ONLY_MARKER));
        rbta.setQualifier(attributes.getString("value"));
        List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
        for (Class<?> rbRule : attributes.getClassArray("rollbackFor")) {
            rollbackRules.add(new RollbackRuleAttribute(rbRule));
        }
        for (String rbRule2 : attributes.getStringArray("rollbackForClassName")) {
            rollbackRules.add(new RollbackRuleAttribute(rbRule2));
        }
        for (Class<?> rbRule3 : attributes.getClassArray("noRollbackFor")) {
            rollbackRules.add(new NoRollbackRuleAttribute(rbRule3));
        }
        for (String rbRule4 : attributes.getStringArray("noRollbackForClassName")) {
            rollbackRules.add(new NoRollbackRuleAttribute(rbRule4));
        }
        rbta.setRollbackRules(rollbackRules);
        return rbta;
    }

    public boolean equals(Object other) {
        return this == other || (other instanceof SpringTransactionAnnotationParser);
    }

    public int hashCode() {
        return SpringTransactionAnnotationParser.class.hashCode();
    }
}
