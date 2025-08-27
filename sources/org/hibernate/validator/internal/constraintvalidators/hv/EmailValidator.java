package org.hibernate.validator.internal.constraintvalidators.hv;

import java.net.IDN;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraints.Email;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/hv/EmailValidator.class */
public class EmailValidator implements ConstraintValidator<Email, CharSequence> {
    private static final String LOCAL_PART_ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uffff-]";
    private static final String DOMAIN_LABEL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    private static final String DOMAIN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*";
    private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";
    private static final int MAX_LOCAL_PART_LENGTH = 64;
    private static final int MAX_DOMAIN_PART_LENGTH = 255;
    private static final Pattern LOCAL_PART_PATTERN = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uffff-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uffff-]+)*", 2);
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]", 2);

    @Override // javax.validation.ConstraintValidator
    public void initialize(Email annotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            return true;
        }
        String[] emailParts = value.toString().split("@", 3);
        if (emailParts.length != 2 || !matchLocalPart(emailParts[0])) {
            return false;
        }
        return matchDomain(emailParts[1]);
    }

    private boolean matchLocalPart(String localPart) {
        if (localPart.length() > 64) {
            return false;
        }
        Matcher matcher = LOCAL_PART_PATTERN.matcher(localPart);
        return matcher.matches();
    }

    private boolean matchDomain(String domain) {
        if (domain.endsWith(".")) {
            return false;
        }
        try {
            String asciiString = IDN.toASCII(domain);
            if (asciiString.length() > 255) {
                return false;
            }
            Matcher matcher = DOMAIN_PATTERN.matcher(asciiString);
            return matcher.matches();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
