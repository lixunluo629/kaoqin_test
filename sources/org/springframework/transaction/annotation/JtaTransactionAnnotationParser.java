package org.springframework.transaction.annotation;

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

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/annotation/JtaTransactionAnnotationParser.class */
public class JtaTransactionAnnotationParser implements TransactionAnnotationParser, Serializable {
    @Override // org.springframework.transaction.annotation.TransactionAnnotationParser
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement element) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(element, (Class<? extends Annotation>) javax.transaction.Transactional.class);
        if (attributes != null) {
            return parseTransactionAnnotation(attributes);
        }
        return null;
    }

    public TransactionAttribute parseTransactionAnnotation(javax.transaction.Transactional ann) {
        return parseTransactionAnnotation(AnnotationUtils.getAnnotationAttributes(ann, false, false));
    }

    protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
        RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
        rbta.setPropagationBehaviorName(DefaultTransactionDefinition.PREFIX_PROPAGATION + attributes.getEnum("value").toString());
        List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
        for (Class<?> rbRule : attributes.getClassArray("rollbackOn")) {
            rollbackRules.add(new RollbackRuleAttribute(rbRule));
        }
        for (Class<?> rbRule2 : attributes.getClassArray("dontRollbackOn")) {
            rollbackRules.add(new NoRollbackRuleAttribute(rbRule2));
        }
        rbta.setRollbackRules(rollbackRules);
        return rbta;
    }

    public boolean equals(Object other) {
        return this == other || (other instanceof JtaTransactionAnnotationParser);
    }

    public int hashCode() {
        return JtaTransactionAnnotationParser.class.hashCode();
    }
}
