package org.springframework.transaction.interceptor;

import java.beans.PropertyEditorSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAttributeEditor.class */
public class TransactionAttributeEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasLength(text)) {
            String[] tokens = StringUtils.commaDelimitedListToStringArray(text);
            RuleBasedTransactionAttribute attr = new RuleBasedTransactionAttribute();
            for (String token : tokens) {
                String trimmedToken = StringUtils.trimWhitespace(token.trim());
                if (StringUtils.containsWhitespace(trimmedToken)) {
                    throw new IllegalArgumentException("Transaction attribute token contains illegal whitespace: [" + trimmedToken + "]");
                }
                if (trimmedToken.startsWith(DefaultTransactionDefinition.PREFIX_PROPAGATION)) {
                    attr.setPropagationBehaviorName(trimmedToken);
                } else if (trimmedToken.startsWith(DefaultTransactionDefinition.PREFIX_ISOLATION)) {
                    attr.setIsolationLevelName(trimmedToken);
                } else if (trimmedToken.startsWith(DefaultTransactionDefinition.PREFIX_TIMEOUT)) {
                    String value = trimmedToken.substring(DefaultTransactionDefinition.PREFIX_TIMEOUT.length());
                    attr.setTimeout(Integer.parseInt(value));
                } else if (trimmedToken.equals(DefaultTransactionDefinition.READ_ONLY_MARKER)) {
                    attr.setReadOnly(true);
                } else if (trimmedToken.startsWith("+")) {
                    attr.getRollbackRules().add(new NoRollbackRuleAttribute(trimmedToken.substring(1)));
                } else if (trimmedToken.startsWith("-")) {
                    attr.getRollbackRules().add(new RollbackRuleAttribute(trimmedToken.substring(1)));
                } else {
                    throw new IllegalArgumentException("Invalid transaction attribute token: [" + trimmedToken + "]");
                }
            }
            setValue(attr);
            return;
        }
        setValue(null);
    }
}
