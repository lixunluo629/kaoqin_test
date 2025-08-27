package org.hibernate.validator.internal.util.privilegedactions;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import javax.validation.ConstraintValidator;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/GetConstraintValidatorList.class */
public class GetConstraintValidatorList implements PrivilegedAction<List<ConstraintValidator<?, ?>>> {
    public static List<ConstraintValidator<?, ?>> getConstraintValidatorList() {
        GetConstraintValidatorList action = new GetConstraintValidatorList();
        if (System.getSecurityManager() != null) {
            return (List) AccessController.doPrivileged(action);
        }
        return action.run();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public List<ConstraintValidator<?, ?>> run() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        List<ConstraintValidator<?, ?>> constraintValidatorList = loadConstraintValidators(classloader);
        if (constraintValidatorList.isEmpty()) {
            ClassLoader classloader2 = GetConstraintValidatorList.class.getClassLoader();
            constraintValidatorList = loadConstraintValidators(classloader2);
        }
        return constraintValidatorList;
    }

    private List<ConstraintValidator<?, ?>> loadConstraintValidators(ClassLoader classloader) {
        ServiceLoader<ConstraintValidator> loader = ServiceLoader.load(ConstraintValidator.class, classloader);
        Iterator<ConstraintValidator> constraintValidatorIterator = loader.iterator();
        List<ConstraintValidator<?, ?>> constraintValidators = new ArrayList<>();
        while (constraintValidatorIterator.hasNext()) {
            try {
                constraintValidators.add(constraintValidatorIterator.next());
            } catch (ServiceConfigurationError e) {
            }
        }
        return constraintValidators;
    }
}
