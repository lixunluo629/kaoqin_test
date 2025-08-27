package org.springframework.dao.support;

import java.util.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/support/DataAccessUtils.class */
public abstract class DataAccessUtils {
    public static <T> T singleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        if (CollectionUtils.isEmpty((Collection<?>) results)) {
            return null;
        }
        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, results.size());
        }
        return results.iterator().next();
    }

    public static <T> T requiredSingleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        if (CollectionUtils.isEmpty((Collection<?>) results)) {
            throw new EmptyResultDataAccessException(1);
        }
        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, results.size());
        }
        return results.iterator().next();
    }

    public static <T> T uniqueResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        if (CollectionUtils.isEmpty((Collection<?>) results)) {
            return null;
        }
        if (!CollectionUtils.hasUniqueObject(results)) {
            throw new IncorrectResultSizeDataAccessException(1, results.size());
        }
        return results.iterator().next();
    }

    public static <T> T requiredUniqueResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        if (CollectionUtils.isEmpty((Collection<?>) results)) {
            throw new EmptyResultDataAccessException(1);
        }
        if (!CollectionUtils.hasUniqueObject(results)) {
            throw new IncorrectResultSizeDataAccessException(1, results.size());
        }
        return results.iterator().next();
    }

    public static <T> T objectResult(Collection<?> collection, Class<T> cls) throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {
        Object objRequiredUniqueResult = requiredUniqueResult(collection);
        if (cls != null && !cls.isInstance(objRequiredUniqueResult)) {
            if (String.class == cls) {
                objRequiredUniqueResult = objRequiredUniqueResult.toString();
            } else if (Number.class.isAssignableFrom(cls) && Number.class.isInstance(objRequiredUniqueResult)) {
                try {
                    objRequiredUniqueResult = NumberUtils.convertNumberToTargetClass((Number) objRequiredUniqueResult, cls);
                } catch (IllegalArgumentException e) {
                    throw new TypeMismatchDataAccessException(e.getMessage());
                }
            } else {
                throw new TypeMismatchDataAccessException("Result object is of type [" + objRequiredUniqueResult.getClass().getName() + "] and could not be converted to required type [" + cls.getName() + "]");
            }
        }
        return (T) objRequiredUniqueResult;
    }

    public static int intResult(Collection<?> results) throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {
        return ((Number) objectResult(results, Number.class)).intValue();
    }

    public static long longResult(Collection<?> results) throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {
        return ((Number) objectResult(results, Number.class)).longValue();
    }

    public static RuntimeException translateIfNecessary(RuntimeException rawException, PersistenceExceptionTranslator pet) {
        Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
        DataAccessException dex = pet.translateExceptionIfPossible(rawException);
        return dex != null ? dex : rawException;
    }
}
