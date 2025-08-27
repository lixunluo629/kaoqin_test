package org.springframework.data.repository.query;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/QueryCreationException.class */
public final class QueryCreationException extends RuntimeException {
    private static final long serialVersionUID = -1238456123580L;
    private static final String MESSAGE_TEMPLATE = "Could not create query for method %s! Could not find property %s on domain class %s.";

    private QueryCreationException(String message) {
        super(message);
    }

    public static QueryCreationException invalidProperty(QueryMethod method, String propertyName) {
        return new QueryCreationException(String.format(MESSAGE_TEMPLATE, method, propertyName, method.getDomainClass().getName()));
    }

    public static QueryCreationException create(QueryMethod method, String message) {
        return new QueryCreationException(String.format("Could not create query for %s! Reason: %s", method, message));
    }

    public static QueryCreationException create(QueryMethod method, Throwable cause) {
        return create(method, cause.getMessage());
    }
}
