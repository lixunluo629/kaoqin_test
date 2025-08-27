package org.apache.commons.httpclient.methods;

import java.util.Iterator;
import java.util.Vector;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.support.WebContentGenerator;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/PostMethod.class */
public class PostMethod extends EntityEnclosingMethod {
    private static final Log LOG;
    public static final String FORM_URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private Vector params;
    static Class class$org$apache$commons$httpclient$methods$PostMethod;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$PostMethod == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.PostMethod");
            class$org$apache$commons$httpclient$methods$PostMethod = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$PostMethod;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public PostMethod() {
        this.params = new Vector();
    }

    public PostMethod(String uri) {
        super(uri);
        this.params = new Vector();
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public String getName() {
        return WebContentGenerator.METHOD_POST;
    }

    @Override // org.apache.commons.httpclient.methods.EntityEnclosingMethod, org.apache.commons.httpclient.methods.ExpectContinueMethod
    protected boolean hasRequestContent() {
        LOG.trace("enter PostMethod.hasRequestContent()");
        if (!this.params.isEmpty()) {
            return true;
        }
        return super.hasRequestContent();
    }

    @Override // org.apache.commons.httpclient.methods.EntityEnclosingMethod
    protected void clearRequestBody() {
        LOG.trace("enter PostMethod.clearRequestBody()");
        this.params.clear();
        super.clearRequestBody();
    }

    @Override // org.apache.commons.httpclient.methods.EntityEnclosingMethod
    protected RequestEntity generateRequestEntity() {
        if (!this.params.isEmpty()) {
            String content = EncodingUtil.formUrlEncode(getParameters(), getRequestCharSet());
            ByteArrayRequestEntity entity = new ByteArrayRequestEntity(EncodingUtil.getAsciiBytes(content), "application/x-www-form-urlencoded");
            return entity;
        }
        return super.generateRequestEntity();
    }

    public void setParameter(String parameterName, String parameterValue) throws IllegalArgumentException {
        LOG.trace("enter PostMethod.setParameter(String, String)");
        removeParameter(parameterName);
        addParameter(parameterName, parameterValue);
    }

    public NameValuePair getParameter(String paramName) {
        LOG.trace("enter PostMethod.getParameter(String)");
        if (paramName == null) {
            return null;
        }
        Iterator iter = this.params.iterator();
        while (iter.hasNext()) {
            NameValuePair parameter = (NameValuePair) iter.next();
            if (paramName.equals(parameter.getName())) {
                return parameter;
            }
        }
        return null;
    }

    public NameValuePair[] getParameters() {
        LOG.trace("enter PostMethod.getParameters()");
        int numPairs = this.params.size();
        Object[] objectArr = this.params.toArray();
        NameValuePair[] nvPairArr = new NameValuePair[numPairs];
        for (int i = 0; i < numPairs; i++) {
            nvPairArr[i] = (NameValuePair) objectArr[i];
        }
        return nvPairArr;
    }

    public void addParameter(String paramName, String paramValue) throws IllegalArgumentException {
        LOG.trace("enter PostMethod.addParameter(String, String)");
        if (paramName == null || paramValue == null) {
            throw new IllegalArgumentException("Arguments to addParameter(String, String) cannot be null");
        }
        super.clearRequestBody();
        this.params.add(new NameValuePair(paramName, paramValue));
    }

    public void addParameter(NameValuePair param) throws IllegalArgumentException {
        LOG.trace("enter PostMethod.addParameter(NameValuePair)");
        if (param == null) {
            throw new IllegalArgumentException("NameValuePair may not be null");
        }
        addParameter(param.getName(), param.getValue());
    }

    public void addParameters(NameValuePair[] parameters) {
        LOG.trace("enter PostMethod.addParameters(NameValuePair[])");
        if (parameters == null) {
            LOG.warn("Attempt to addParameters(null) ignored");
            return;
        }
        super.clearRequestBody();
        for (NameValuePair nameValuePair : parameters) {
            this.params.add(nameValuePair);
        }
    }

    public boolean removeParameter(String paramName) throws IllegalArgumentException {
        LOG.trace("enter PostMethod.removeParameter(String)");
        if (paramName == null) {
            throw new IllegalArgumentException("Argument passed to removeParameter(String) cannot be null");
        }
        boolean removed = false;
        Iterator iter = this.params.iterator();
        while (iter.hasNext()) {
            NameValuePair pair = (NameValuePair) iter.next();
            if (paramName.equals(pair.getName())) {
                iter.remove();
                removed = true;
            }
        }
        return removed;
    }

    public boolean removeParameter(String paramName, String paramValue) throws IllegalArgumentException {
        LOG.trace("enter PostMethod.removeParameter(String, String)");
        if (paramName == null) {
            throw new IllegalArgumentException("Parameter name may not be null");
        }
        if (paramValue == null) {
            throw new IllegalArgumentException("Parameter value may not be null");
        }
        Iterator iter = this.params.iterator();
        while (iter.hasNext()) {
            NameValuePair pair = (NameValuePair) iter.next();
            if (paramName.equals(pair.getName()) && paramValue.equals(pair.getValue())) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    public void setRequestBody(NameValuePair[] parametersBody) throws IllegalArgumentException {
        LOG.trace("enter PostMethod.setRequestBody(NameValuePair[])");
        if (parametersBody == null) {
            throw new IllegalArgumentException("Array of parameters may not be null");
        }
        clearRequestBody();
        addParameters(parametersBody);
    }
}
