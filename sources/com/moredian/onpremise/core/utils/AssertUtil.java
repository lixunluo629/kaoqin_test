package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/AssertUtil.class */
public class AssertUtil {
    public static void isTrue(Boolean flag, String message) {
        isTrue(flag, OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode(), message);
    }

    public static void isTrue(Boolean flag, OnpremiseErrorEnum errorEnum) {
        isTrue(flag, errorEnum.getErrorCode(), errorEnum.getMessage());
    }

    public static void isTrue(Boolean flag, String errorCode, String message) {
        if (!flag.booleanValue()) {
            throw new BizException(errorCode, message);
        }
    }

    public static void isTrue(Boolean flag, OnpremiseErrorEnum errorEnum, String... messages) {
        if (!flag.booleanValue()) {
            String message = errorEnum.getMessage();
            if (messages != null && message.contains("_param_") && messages.length > 0) {
                message = message.replaceFirst("_param_", messages[0]);
                int i = 0 + 1;
            }
            throw new BizException(errorEnum.getErrorCode(), message);
        }
    }

    public static void isNullOrEmpty(Object obj, String message) {
        isNullOrEmpty(obj, OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode(), message);
    }

    public static void isNullOrEmpty(Object obj, OnpremiseErrorEnum errorEnum) {
        isNullOrEmpty(obj, errorEnum.getErrorCode(), errorEnum.getMessage());
    }

    public static void isNullOrEmpty(Object obj, String errorCode, String message) {
        if (obj == null) {
            throw new BizException(errorCode, message);
        }
    }

    public static void isNullOrEmpty(Object obj, OnpremiseErrorEnum errorEnum, String... messages) {
        if (obj == null) {
            String message = errorEnum.getMessage();
            if (messages != null && message.indexOf("\\{\\}") != -1 && messages.length > 0) {
                message.replaceFirst("\\{\\}", messages[0]);
                int i = 0 + 1;
            }
            throw new BizException(errorEnum.getErrorCode(), message);
        }
    }

    public static void isNullOrEmpty(String str, String message) {
        isNullOrEmpty(str, OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode(), message);
    }

    public static void isNullOrEmpty(String str, OnpremiseErrorEnum errorEnum) {
        isNullOrEmpty(str, errorEnum.getErrorCode(), errorEnum.getMessage());
    }

    public static void isNullOrEmpty(String str, String errorCode, String message) {
        if (str == null || str.trim().length() <= 0) {
            throw new BizException(errorCode, message);
        }
    }

    public static void isNullOrEmpty(String str, OnpremiseErrorEnum errorEnum, String... messages) {
        if (str == null || str.trim().length() <= 0) {
            String message = errorEnum.getMessage();
            if (messages != null && message.indexOf("\\{\\}") != -1 && messages.length > 0) {
                message.replaceFirst("\\{\\}", messages[0]);
                int i = 0 + 1;
            }
            throw new BizException(errorEnum.getErrorCode(), message);
        }
    }

    public static void checkOrgId(Long orgId) {
        isTrue(Boolean.valueOf(orgId != null && orgId.longValue() > 0), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
    }

    public static void checkId(Long id, OnpremiseErrorEnum errorEnum) {
        isTrue(Boolean.valueOf(id != null && id.longValue() > 0), errorEnum);
    }
}
