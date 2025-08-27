package org.springframework.jdbc.core.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/CallMetaDataContext.class */
public class CallMetaDataContext {
    private String procedureName;
    private String catalogName;
    private String schemaName;
    private String actualFunctionReturnName;
    private boolean namedBinding;
    private CallMetaDataProvider metaDataProvider;
    protected final Log logger = LogFactory.getLog(getClass());
    private List<SqlParameter> callParameters = new ArrayList();
    private Set<String> limitedInParameterNames = new HashSet();
    private List<String> outParameterNames = new ArrayList();
    private boolean function = false;
    private boolean returnValueRequired = false;
    private boolean accessCallParameterMetaData = true;

    public void setFunctionReturnName(String functionReturnName) {
        this.actualFunctionReturnName = functionReturnName;
    }

    public String getFunctionReturnName() {
        return this.actualFunctionReturnName != null ? this.actualFunctionReturnName : "return";
    }

    public void setLimitedInParameterNames(Set<String> limitedInParameterNames) {
        this.limitedInParameterNames = limitedInParameterNames;
    }

    public Set<String> getLimitedInParameterNames() {
        return this.limitedInParameterNames;
    }

    public void setOutParameterNames(List<String> outParameterNames) {
        this.outParameterNames = outParameterNames;
    }

    public List<String> getOutParameterNames() {
        return this.outParameterNames;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getProcedureName() {
        return this.procedureName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogName() {
        return this.catalogName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public void setFunction(boolean function) {
        this.function = function;
    }

    public boolean isFunction() {
        return this.function;
    }

    public void setReturnValueRequired(boolean returnValueRequired) {
        this.returnValueRequired = returnValueRequired;
    }

    public boolean isReturnValueRequired() {
        return this.returnValueRequired;
    }

    public void setAccessCallParameterMetaData(boolean accessCallParameterMetaData) {
        this.accessCallParameterMetaData = accessCallParameterMetaData;
    }

    public boolean isAccessCallParameterMetaData() {
        return this.accessCallParameterMetaData;
    }

    public void setNamedBinding(boolean namedBinding) {
        this.namedBinding = namedBinding;
    }

    public boolean isNamedBinding() {
        return this.namedBinding;
    }

    public void initializeMetaData(DataSource dataSource) {
        this.metaDataProvider = CallMetaDataProviderFactory.createMetaDataProvider(dataSource, this);
    }

    public SqlParameter createReturnResultSetParameter(String parameterName, RowMapper<?> rowMapper) {
        if (this.metaDataProvider.isReturnResultSetSupported()) {
            return new SqlReturnResultSet(parameterName, rowMapper);
        }
        if (this.metaDataProvider.isRefCursorSupported()) {
            return new SqlOutParameter(parameterName, this.metaDataProvider.getRefCursorSqlType(), rowMapper);
        }
        throw new InvalidDataAccessApiUsageException("Return of a ResultSet from a stored procedure is not supported");
    }

    public String getScalarOutParameterName() {
        if (isFunction()) {
            return getFunctionReturnName();
        }
        if (this.outParameterNames.size() > 1) {
            this.logger.warn("Accessing single output value when procedure has more than one output parameter");
        }
        if (this.outParameterNames.isEmpty()) {
            return null;
        }
        return this.outParameterNames.get(0);
    }

    public List<SqlParameter> getCallParameters() {
        return this.callParameters;
    }

    public void processParameters(List<SqlParameter> parameters) {
        this.callParameters = reconcileParameters(parameters);
    }

    protected List<SqlParameter> reconcileParameters(List<SqlParameter> parameters) {
        SqlParameter param;
        ArrayList arrayList = new ArrayList();
        Map<String, SqlParameter> declaredParams = new LinkedHashMap<>();
        boolean returnDeclared = false;
        List<String> outParamNames = new ArrayList<>();
        List<String> metaDataParamNames = new ArrayList<>();
        for (CallParameterMetaData meta : this.metaDataProvider.getCallParameterMetaData()) {
            if (!meta.isReturnParameter()) {
                metaDataParamNames.add(meta.getParameterName().toLowerCase());
            }
        }
        for (SqlParameter param2 : parameters) {
            if (param2.isResultsParameter()) {
                arrayList.add(param2);
            } else {
                String paramName = param2.getName();
                if (paramName == null) {
                    throw new IllegalArgumentException("Anonymous parameters not supported for calls - please specify a name for the parameter of SQL type " + param2.getSqlType());
                }
                String paramNameToMatch = this.metaDataProvider.parameterNameToUse(paramName).toLowerCase();
                declaredParams.put(paramNameToMatch, param2);
                if (param2 instanceof SqlOutParameter) {
                    outParamNames.add(paramName);
                    if (isFunction() && !metaDataParamNames.contains(paramNameToMatch) && !returnDeclared) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Using declared out parameter '" + paramName + "' for function return value");
                        }
                        setFunctionReturnName(paramName);
                        returnDeclared = true;
                    }
                }
            }
        }
        setOutParameterNames(outParamNames);
        List<SqlParameter> workParams = new ArrayList<>();
        workParams.addAll(arrayList);
        if (!this.metaDataProvider.isProcedureColumnMetaDataUsed()) {
            workParams.addAll(declaredParams.values());
            return workParams;
        }
        Map<String, String> limitedInParamNamesMap = new HashMap<>(this.limitedInParameterNames.size());
        for (String limitedParamName : this.limitedInParameterNames) {
            limitedInParamNamesMap.put(this.metaDataProvider.parameterNameToUse(limitedParamName).toLowerCase(), limitedParamName);
        }
        for (CallParameterMetaData meta2 : this.metaDataProvider.getCallParameterMetaData()) {
            String paramNameToCheck = null;
            if (meta2.getParameterName() != null) {
                paramNameToCheck = this.metaDataProvider.parameterNameToUse(meta2.getParameterName()).toLowerCase();
            }
            String paramNameToUse = this.metaDataProvider.parameterNameToUse(meta2.getParameterName());
            if (declaredParams.containsKey(paramNameToCheck) || (meta2.isReturnParameter() && returnDeclared)) {
                if (meta2.isReturnParameter()) {
                    param = declaredParams.get(getFunctionReturnName());
                    if (param == null && !getOutParameterNames().isEmpty()) {
                        param = declaredParams.get(getOutParameterNames().get(0).toLowerCase());
                    }
                    if (param == null) {
                        throw new InvalidDataAccessApiUsageException("Unable to locate declared parameter for function return value -  add a SqlOutParameter with name '" + getFunctionReturnName() + "'");
                    }
                    setFunctionReturnName(param.getName());
                } else {
                    param = declaredParams.get(paramNameToCheck);
                }
                if (param != null) {
                    workParams.add(param);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Using declared parameter for '" + (paramNameToUse != null ? paramNameToUse : getFunctionReturnName()) + "'");
                    }
                }
            } else if (meta2.isReturnParameter()) {
                if (!isFunction() && !isReturnValueRequired() && this.metaDataProvider.byPassReturnParameter(meta2.getParameterName())) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Bypassing meta-data return parameter for '" + meta2.getParameterName() + "'");
                    }
                } else {
                    String returnNameToUse = StringUtils.hasLength(meta2.getParameterName()) ? paramNameToUse : getFunctionReturnName();
                    workParams.add(this.metaDataProvider.createDefaultOutParameter(returnNameToUse, meta2));
                    if (isFunction()) {
                        setFunctionReturnName(returnNameToUse);
                        outParamNames.add(returnNameToUse);
                    }
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Added meta-data return parameter for '" + returnNameToUse + "'");
                    }
                }
            } else if (meta2.getParameterType() == 4) {
                workParams.add(this.metaDataProvider.createDefaultOutParameter(paramNameToUse, meta2));
                outParamNames.add(paramNameToUse);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Added meta-data out parameter for '" + paramNameToUse + "'");
                }
            } else if (meta2.getParameterType() == 2) {
                workParams.add(this.metaDataProvider.createDefaultInOutParameter(paramNameToUse, meta2));
                outParamNames.add(paramNameToUse);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Added meta-data in-out parameter for '" + paramNameToUse + "'");
                }
            } else if (this.limitedInParameterNames.isEmpty() || limitedInParamNamesMap.containsKey(paramNameToUse.toLowerCase())) {
                workParams.add(this.metaDataProvider.createDefaultInParameter(paramNameToUse, meta2));
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Added meta-data in parameter for '" + paramNameToUse + "'");
                }
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Limited set of parameters " + limitedInParamNamesMap.keySet() + " skipped parameter for '" + paramNameToUse + "'");
            }
        }
        return workParams;
    }

    public Map<String, Object> matchInParameterValuesWithCallParameters(SqlParameterSource parameterSource) {
        Map<String, String> caseInsensitiveParameterNames = SqlParameterSourceUtils.extractCaseInsensitiveParameterNames(parameterSource);
        Map<String, String> callParameterNames = new HashMap<>(this.callParameters.size());
        Map<String, Object> matchedParameters = new HashMap<>(this.callParameters.size());
        for (SqlParameter parameter : this.callParameters) {
            if (parameter.isInputValueProvided()) {
                String parameterName = parameter.getName();
                String parameterNameToMatch = this.metaDataProvider.parameterNameToUse(parameterName);
                if (parameterNameToMatch != null) {
                    callParameterNames.put(parameterNameToMatch.toLowerCase(), parameterName);
                }
                if (parameterName != null) {
                    if (parameterSource.hasValue(parameterName)) {
                        matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, parameterName));
                    } else {
                        String lowerCaseName = parameterName.toLowerCase();
                        if (parameterSource.hasValue(lowerCaseName)) {
                            matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, lowerCaseName));
                        } else {
                            String englishLowerCaseName = parameterName.toLowerCase(Locale.ENGLISH);
                            if (parameterSource.hasValue(englishLowerCaseName)) {
                                matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, englishLowerCaseName));
                            } else {
                                String propertyName = JdbcUtils.convertUnderscoreNameToPropertyName(parameterName);
                                if (parameterSource.hasValue(propertyName)) {
                                    matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, propertyName));
                                } else if (caseInsensitiveParameterNames.containsKey(lowerCaseName)) {
                                    String sourceName = caseInsensitiveParameterNames.get(lowerCaseName);
                                    matchedParameters.put(parameterName, SqlParameterSourceUtils.getTypedValue(parameterSource, sourceName));
                                } else if (this.logger.isWarnEnabled()) {
                                    this.logger.warn("Unable to locate the corresponding parameter value for '" + parameterName + "' within the parameter values provided: " + caseInsensitiveParameterNames.values());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Matching " + caseInsensitiveParameterNames.values() + " with " + callParameterNames.values());
            this.logger.debug("Found match for " + matchedParameters.keySet());
        }
        return matchedParameters;
    }

    public Map<String, ?> matchInParameterValuesWithCallParameters(Map<String, ?> inParameters) {
        String parameterName;
        String parameterNameToMatch;
        if (!this.metaDataProvider.isProcedureColumnMetaDataUsed()) {
            return inParameters;
        }
        Map<String, String> callParameterNames = new HashMap<>(this.callParameters.size());
        for (SqlParameter parameter : this.callParameters) {
            if (parameter.isInputValueProvided() && (parameterNameToMatch = this.metaDataProvider.parameterNameToUse((parameterName = parameter.getName()))) != null) {
                callParameterNames.put(parameterNameToMatch.toLowerCase(), parameterName);
            }
        }
        Map<String, Object> matchedParameters = new HashMap<>(inParameters.size());
        for (String parameterName2 : inParameters.keySet()) {
            String parameterNameToMatch2 = this.metaDataProvider.parameterNameToUse(parameterName2);
            String callParameterName = callParameterNames.get(parameterNameToMatch2.toLowerCase());
            if (callParameterName == null) {
                if (this.logger.isDebugEnabled()) {
                    Object value = inParameters.get(parameterName2);
                    if (value instanceof SqlParameterValue) {
                        value = ((SqlParameterValue) value).getValue();
                    }
                    if (value != null) {
                        this.logger.debug("Unable to locate the corresponding IN or IN-OUT parameter for \"" + parameterName2 + "\" in the parameters used: " + callParameterNames.keySet());
                    }
                }
            } else {
                matchedParameters.put(callParameterName, inParameters.get(parameterName2));
            }
        }
        if (matchedParameters.size() < callParameterNames.size()) {
            for (String parameterName3 : callParameterNames.keySet()) {
                String parameterNameToMatch3 = this.metaDataProvider.parameterNameToUse(parameterName3);
                if (!matchedParameters.containsKey(callParameterNames.get(parameterNameToMatch3.toLowerCase())) && this.logger.isWarnEnabled()) {
                    this.logger.warn("Unable to locate the corresponding parameter value for '" + parameterName3 + "' within the parameter values provided: " + inParameters.keySet());
                }
            }
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Matching " + inParameters.keySet() + " with " + callParameterNames.values());
            this.logger.debug("Found match for " + matchedParameters.keySet());
        }
        return matchedParameters;
    }

    public Map<String, ?> matchInParameterValuesWithCallParameters(Object[] parameterValues) {
        Map<String, Object> matchedParameters = new HashMap<>(parameterValues.length);
        int i = 0;
        for (SqlParameter parameter : this.callParameters) {
            if (parameter.isInputValueProvided()) {
                String parameterName = parameter.getName();
                int i2 = i;
                i++;
                matchedParameters.put(parameterName, parameterValues[i2]);
            }
        }
        return matchedParameters;
    }

    public String createCallString() {
        String catalogNameToUse;
        String schemaNameToUse;
        StringBuilder callString;
        int parameterCount = 0;
        if (this.metaDataProvider.isSupportsSchemasInProcedureCalls() && !this.metaDataProvider.isSupportsCatalogsInProcedureCalls()) {
            schemaNameToUse = this.metaDataProvider.catalogNameToUse(getCatalogName());
            catalogNameToUse = this.metaDataProvider.schemaNameToUse(getSchemaName());
        } else {
            catalogNameToUse = this.metaDataProvider.catalogNameToUse(getCatalogName());
            schemaNameToUse = this.metaDataProvider.schemaNameToUse(getSchemaName());
        }
        String procedureNameToUse = this.metaDataProvider.procedureNameToUse(getProcedureName());
        if (isFunction() || isReturnValueRequired()) {
            callString = new StringBuilder().append("{? = call ").append(StringUtils.hasLength(catalogNameToUse) ? catalogNameToUse + "." : "").append(StringUtils.hasLength(schemaNameToUse) ? schemaNameToUse + "." : "").append(procedureNameToUse).append("(");
            parameterCount = -1;
        } else {
            callString = new StringBuilder().append("{call ").append(StringUtils.hasLength(catalogNameToUse) ? catalogNameToUse + "." : "").append(StringUtils.hasLength(schemaNameToUse) ? schemaNameToUse + "." : "").append(procedureNameToUse).append("(");
        }
        for (SqlParameter parameter : this.callParameters) {
            if (!parameter.isResultsParameter()) {
                if (parameterCount > 0) {
                    callString.append(", ");
                }
                if (parameterCount >= 0) {
                    callString.append(createParameterBinding(parameter));
                }
                parameterCount++;
            }
        }
        callString.append(")}");
        return callString.toString();
    }

    protected String createParameterBinding(SqlParameter parameter) {
        return isNamedBinding() ? parameter.getName() + " => ?" : "?";
    }
}
