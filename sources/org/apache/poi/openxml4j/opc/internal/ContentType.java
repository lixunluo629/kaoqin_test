package org.apache.poi.openxml4j.opc.internal;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/opc/internal/ContentType.class */
public final class ContentType {
    private final String type;
    private final String subType;
    private final Map<String, String> parameters;
    private static final Pattern patternTypeSubType = Pattern.compile("^([\\x21-\\x7E&&[^\\(\\)<>@,;:\\\\/\"\\[\\]\\?={}\\x20\\x09]]+)/([\\x21-\\x7E&&[^\\(\\)<>@,;:\\\\/\"\\[\\]\\?={}\\x20\\x09]]+)$");
    private static final Pattern patternTypeSubTypeParams;
    private static final Pattern patternParams;

    static {
        String parameter = "([\\x21-\\x7E&&[^\\(\\)<>@,;:\\\\/\"\\[\\]\\?={}\\x20\\x09]]+)=(\"?[\\x21-\\x7E&&[^\\(\\)<>@,;:\\\\/\"\\[\\]\\?={}\\x20\\x09]]+\"?)";
        patternTypeSubTypeParams = Pattern.compile("^([\\x21-\\x7E&&[^\\(\\)<>@,;:\\\\/\"\\[\\]\\?={}\\x20\\x09]]+)/([\\x21-\\x7E&&[^\\(\\)<>@,;:\\\\/\"\\[\\]\\?={}\\x20\\x09]]+)(;" + parameter + ")*$");
        patternParams = Pattern.compile(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR + parameter);
    }

    public ContentType(String contentType) throws InvalidFormatException {
        Matcher mMediaType = patternTypeSubType.matcher(contentType);
        mMediaType = mMediaType.matches() ? mMediaType : patternTypeSubTypeParams.matcher(contentType);
        if (!mMediaType.matches()) {
            throw new InvalidFormatException("The specified content type '" + contentType + "' is not compliant with RFC 2616: malformed content type.");
        }
        if (mMediaType.groupCount() >= 2) {
            this.type = mMediaType.group(1);
            this.subType = mMediaType.group(2);
            this.parameters = new HashMap();
            if (mMediaType.groupCount() >= 5) {
                Matcher mParams = patternParams.matcher(contentType.substring(mMediaType.end(2)));
                while (mParams.find()) {
                    this.parameters.put(mParams.group(1), mParams.group(2));
                }
                return;
            }
            return;
        }
        this.type = "";
        this.subType = "";
        this.parameters = Collections.emptyMap();
    }

    public final String toString() {
        return toString(true);
    }

    public final String toString(boolean withParameters) {
        StringBuffer retVal = new StringBuffer();
        retVal.append(getType());
        retVal.append("/");
        retVal.append(getSubType());
        if (withParameters) {
            for (Map.Entry<String, String> me : this.parameters.entrySet()) {
                retVal.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                retVal.append(me.getKey());
                retVal.append(SymbolConstants.EQUAL_SYMBOL);
                retVal.append(me.getValue());
            }
        }
        return retVal.toString();
    }

    public boolean equals(Object obj) {
        return !(obj instanceof ContentType) || toString().equalsIgnoreCase(obj.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public String getSubType() {
        return this.subType;
    }

    public String getType() {
        return this.type;
    }

    public boolean hasParameters() {
        return (this.parameters == null || this.parameters.isEmpty()) ? false : true;
    }

    public String[] getParameterKeys() {
        if (this.parameters == null) {
            return new String[0];
        }
        return (String[]) this.parameters.keySet().toArray(new String[this.parameters.size()]);
    }

    public String getParameter(String key) {
        return this.parameters.get(key);
    }
}
