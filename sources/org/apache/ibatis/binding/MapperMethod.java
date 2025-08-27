package org.apache.ibatis.binding;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Flush;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/binding/MapperMethod.class */
public class MapperMethod {
    private final SqlCommand command;
    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        this.command = new SqlCommand(config, mapperInterface, method);
        this.method = new MethodSignature(config, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result;
        switch (this.command.getType()) {
            case INSERT:
                Object param = this.method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.insert(this.command.getName(), param));
                break;
            case UPDATE:
                Object param2 = this.method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.update(this.command.getName(), param2));
                break;
            case DELETE:
                Object param3 = this.method.convertArgsToSqlCommandParam(args);
                result = rowCountResult(sqlSession.delete(this.command.getName(), param3));
                break;
            case SELECT:
                if (this.method.returnsVoid() && this.method.hasResultHandler()) {
                    executeWithResultHandler(sqlSession, args);
                    result = null;
                    break;
                } else if (this.method.returnsMany()) {
                    result = executeForMany(sqlSession, args);
                    break;
                } else if (this.method.returnsMap()) {
                    result = executeForMap(sqlSession, args);
                    break;
                } else if (this.method.returnsCursor()) {
                    result = executeForCursor(sqlSession, args);
                    break;
                } else {
                    Object param4 = this.method.convertArgsToSqlCommandParam(args);
                    result = sqlSession.selectOne(this.command.getName(), param4);
                    break;
                }
                break;
            case FLUSH:
                result = sqlSession.flushStatements();
                break;
            default:
                throw new BindingException("Unknown execution method for: " + this.command.getName());
        }
        if (result == null && this.method.getReturnType().isPrimitive() && !this.method.returnsVoid()) {
            throw new BindingException("Mapper method '" + this.command.getName() + " attempted to return null from a method with a primitive return type (" + this.method.getReturnType() + ").");
        }
        return result;
    }

    private Object rowCountResult(int rowCount) {
        Object result;
        if (this.method.returnsVoid()) {
            result = null;
        } else if (Integer.class.equals(this.method.getReturnType()) || Integer.TYPE.equals(this.method.getReturnType())) {
            result = Integer.valueOf(rowCount);
        } else if (Long.class.equals(this.method.getReturnType()) || Long.TYPE.equals(this.method.getReturnType())) {
            result = Long.valueOf(rowCount);
        } else if (Boolean.class.equals(this.method.getReturnType()) || Boolean.TYPE.equals(this.method.getReturnType())) {
            result = Boolean.valueOf(rowCount > 0);
        } else {
            throw new BindingException("Mapper method '" + this.command.getName() + "' has an unsupported return type: " + this.method.getReturnType());
        }
        return result;
    }

    private void executeWithResultHandler(SqlSession sqlSession, Object[] args) {
        MappedStatement ms = sqlSession.getConfiguration().getMappedStatement(this.command.getName());
        if (!StatementType.CALLABLE.equals(ms.getStatementType()) && Void.TYPE.equals(ms.getResultMaps().get(0).getType())) {
            throw new BindingException("method " + this.command.getName() + " needs either a @ResultMap annotation, a @ResultType annotation, or a resultType attribute in XML so a ResultHandler can be used as a parameter.");
        }
        Object param = this.method.convertArgsToSqlCommandParam(args);
        if (this.method.hasRowBounds()) {
            RowBounds rowBounds = this.method.extractRowBounds(args);
            sqlSession.select(this.command.getName(), param, rowBounds, this.method.extractResultHandler(args));
        } else {
            sqlSession.select(this.command.getName(), param, this.method.extractResultHandler(args));
        }
    }

    private <E> Object executeForMany(SqlSession sqlSession, Object[] args) {
        List<E> result;
        Object param = this.method.convertArgsToSqlCommandParam(args);
        if (this.method.hasRowBounds()) {
            RowBounds rowBounds = this.method.extractRowBounds(args);
            result = sqlSession.selectList(this.command.getName(), param, rowBounds);
        } else {
            result = sqlSession.selectList(this.command.getName(), param);
        }
        if (!this.method.getReturnType().isAssignableFrom(result.getClass())) {
            if (this.method.getReturnType().isArray()) {
                return convertToArray(result);
            }
            return convertToDeclaredCollection(sqlSession.getConfiguration(), result);
        }
        return result;
    }

    private <T> Cursor<T> executeForCursor(SqlSession sqlSession, Object[] args) {
        Cursor<T> result;
        Object param = this.method.convertArgsToSqlCommandParam(args);
        if (this.method.hasRowBounds()) {
            RowBounds rowBounds = this.method.extractRowBounds(args);
            result = sqlSession.selectCursor(this.command.getName(), param, rowBounds);
        } else {
            result = sqlSession.selectCursor(this.command.getName(), param);
        }
        return result;
    }

    private <E> Object convertToDeclaredCollection(Configuration config, List<E> list) {
        Object collection = config.getObjectFactory().create(this.method.getReturnType());
        MetaObject metaObject = config.newMetaObject(collection);
        metaObject.addAll(list);
        return collection;
    }

    private <E> Object convertToArray(List<E> list) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Class<?> arrayComponentType = this.method.getReturnType().getComponentType();
        Object array = Array.newInstance(arrayComponentType, list.size());
        if (arrayComponentType.isPrimitive()) {
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, list.get(i));
            }
            return array;
        }
        return list.toArray((Object[]) array);
    }

    private <K, V> Map<K, V> executeForMap(SqlSession sqlSession, Object[] args) {
        Map<K, V> result;
        Object param = this.method.convertArgsToSqlCommandParam(args);
        if (this.method.hasRowBounds()) {
            RowBounds rowBounds = this.method.extractRowBounds(args);
            result = sqlSession.selectMap(this.command.getName(), param, this.method.getMapKey(), rowBounds);
        } else {
            result = sqlSession.selectMap(this.command.getName(), param, this.method.getMapKey());
        }
        return result;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/binding/MapperMethod$ParamMap.class */
    public static class ParamMap<V> extends HashMap<String, V> {
        private static final long serialVersionUID = -2212268410512043556L;

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            if (!super.containsKey(obj)) {
                throw new BindingException("Parameter '" + obj + "' not found. Available parameters are " + keySet());
            }
            return (V) super.get(obj);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/binding/MapperMethod$SqlCommand.class */
    public static class SqlCommand {
        private final String name;
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String methodName = method.getName();
            Class<?> declaringClass = method.getDeclaringClass();
            MappedStatement ms = resolveMappedStatement(mapperInterface, methodName, declaringClass, configuration);
            if (ms == null) {
                if (method.getAnnotation(Flush.class) != null) {
                    this.name = null;
                    this.type = SqlCommandType.FLUSH;
                    return;
                }
                throw new BindingException("Invalid bound statement (not found): " + mapperInterface.getName() + "." + methodName);
            }
            this.name = ms.getId();
            this.type = ms.getSqlCommandType();
            if (this.type == SqlCommandType.UNKNOWN) {
                throw new BindingException("Unknown execution method for: " + this.name);
            }
        }

        public String getName() {
            return this.name;
        }

        public SqlCommandType getType() {
            return this.type;
        }

        private MappedStatement resolveMappedStatement(Class<?> mapperInterface, String methodName, Class<?> declaringClass, Configuration configuration) {
            MappedStatement ms;
            String statementId = mapperInterface.getName() + "." + methodName;
            if (configuration.hasStatement(statementId)) {
                return configuration.getMappedStatement(statementId);
            }
            if (mapperInterface.equals(declaringClass)) {
                return null;
            }
            for (Class<?> superInterface : mapperInterface.getInterfaces()) {
                if (declaringClass.isAssignableFrom(superInterface) && (ms = resolveMappedStatement(superInterface, methodName, declaringClass, configuration)) != null) {
                    return ms;
                }
            }
            return null;
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/binding/MapperMethod$MethodSignature.class */
    public static class MethodSignature {
        private final boolean returnsMany;
        private final boolean returnsMap;
        private final boolean returnsVoid;
        private final boolean returnsCursor;
        private final Class<?> returnType;
        private final String mapKey;
        private final Integer resultHandlerIndex;
        private final Integer rowBoundsIndex;
        private final ParamNameResolver paramNameResolver;

        public MethodSignature(Configuration configuration, Class<?> mapperInterface, Method method) {
            Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface);
            if (resolvedReturnType instanceof Class) {
                this.returnType = (Class) resolvedReturnType;
            } else if (resolvedReturnType instanceof ParameterizedType) {
                this.returnType = (Class) ((ParameterizedType) resolvedReturnType).getRawType();
            } else {
                this.returnType = method.getReturnType();
            }
            this.returnsVoid = Void.TYPE.equals(this.returnType);
            this.returnsMany = configuration.getObjectFactory().isCollection(this.returnType) || this.returnType.isArray();
            this.returnsCursor = Cursor.class.equals(this.returnType);
            this.mapKey = getMapKey(method);
            this.returnsMap = this.mapKey != null;
            this.rowBoundsIndex = getUniqueParamIndex(method, RowBounds.class);
            this.resultHandlerIndex = getUniqueParamIndex(method, ResultHandler.class);
            this.paramNameResolver = new ParamNameResolver(configuration, method);
        }

        public Object convertArgsToSqlCommandParam(Object[] args) {
            return this.paramNameResolver.getNamedParams(args);
        }

        public boolean hasRowBounds() {
            return this.rowBoundsIndex != null;
        }

        public RowBounds extractRowBounds(Object[] args) {
            if (hasRowBounds()) {
                return (RowBounds) args[this.rowBoundsIndex.intValue()];
            }
            return null;
        }

        public boolean hasResultHandler() {
            return this.resultHandlerIndex != null;
        }

        public ResultHandler extractResultHandler(Object[] args) {
            if (hasResultHandler()) {
                return (ResultHandler) args[this.resultHandlerIndex.intValue()];
            }
            return null;
        }

        public String getMapKey() {
            return this.mapKey;
        }

        public Class<?> getReturnType() {
            return this.returnType;
        }

        public boolean returnsMany() {
            return this.returnsMany;
        }

        public boolean returnsMap() {
            return this.returnsMap;
        }

        public boolean returnsVoid() {
            return this.returnsVoid;
        }

        public boolean returnsCursor() {
            return this.returnsCursor;
        }

        private Integer getUniqueParamIndex(Method method, Class<?> paramType) {
            Integer index = null;
            Class<?>[] argTypes = method.getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                if (paramType.isAssignableFrom(argTypes[i])) {
                    if (index == null) {
                        index = Integer.valueOf(i);
                    } else {
                        throw new BindingException(method.getName() + " cannot have multiple " + paramType.getSimpleName() + " parameters");
                    }
                }
            }
            return index;
        }

        private String getMapKey(Method method) {
            MapKey mapKeyAnnotation;
            String mapKey = null;
            if (Map.class.isAssignableFrom(method.getReturnType()) && (mapKeyAnnotation = (MapKey) method.getAnnotation(MapKey.class)) != null) {
                mapKey = mapKeyAnnotation.value();
            }
            return mapKey;
        }
    }
}
