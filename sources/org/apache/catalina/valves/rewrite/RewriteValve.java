package org.apache.catalina.valves.rewrite;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Pipeline;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.util.URLEncoder;
import org.apache.catalina.valves.ValveBase;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.CharChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.buf.UDecoder;
import org.apache.tomcat.util.buf.UriUtil;
import org.apache.tomcat.util.http.RequestUtil;
import org.springframework.beans.PropertyAccessor;
import org.springframework.hateoas.Link;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/rewrite/RewriteValve.class */
public class RewriteValve extends ValveBase {
    protected RewriteRule[] rules;
    protected ThreadLocal<Boolean> invoked;
    protected String resourcePath;
    protected boolean context;
    protected boolean enabled;
    protected Map<String, RewriteMap> maps;
    protected ArrayList<String> mapsConfiguration;

    public RewriteValve() {
        super(true);
        this.rules = null;
        this.invoked = new ThreadLocal<>();
        this.resourcePath = "rewrite.config";
        this.context = false;
        this.enabled = true;
        this.maps = new Hashtable();
        this.mapsConfiguration = new ArrayList<>();
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override // org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleMBeanBase, org.apache.catalina.util.LifecycleBase
    protected void initInternal() throws LifecycleException {
        super.initInternal();
        this.containerLog = LogFactory.getLog(getContainer().getLogName() + ".rewrite");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.io.File, java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.io.InputStreamReader, java.lang.String] */
    @Override // org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleBase
    protected synchronized void startInternal() throws LifecycleException, IOException {
        ?? hostConfigPath;
        ?? file;
        super.startInternal();
        InputStream is = null;
        if (getContainer() instanceof Context) {
            this.context = true;
            is = ((Context) getContainer()).getServletContext().getResourceAsStream("/WEB-INF/" + this.resourcePath);
            if (this.containerLog.isDebugEnabled()) {
                if (is == null) {
                    this.containerLog.debug("No configuration resource found: /WEB-INF/" + this.resourcePath);
                } else {
                    this.containerLog.debug("Read configuration from: /WEB-INF/" + this.resourcePath);
                }
            }
        } else if (getContainer() instanceof Host) {
            hostConfigPath = getHostConfigPath(this.resourcePath);
            file = new File(getConfigBase(), (String) hostConfigPath);
            try {
                if (file.exists()) {
                    if (this.containerLog.isDebugEnabled()) {
                        this.containerLog.debug("Read configuration from " + file.getAbsolutePath());
                    }
                    is = new FileInputStream((File) file);
                } else {
                    is = getClass().getClassLoader().getResourceAsStream(hostConfigPath);
                    if (is != null && this.containerLog.isDebugEnabled()) {
                        this.containerLog.debug("Read configuration from CL at " + ((String) hostConfigPath));
                    }
                }
                if (is == null && this.containerLog.isDebugEnabled()) {
                    this.containerLog.debug("No configuration resource found: " + ((String) hostConfigPath) + " in " + getConfigBase() + " or in the classloader");
                }
            } catch (Exception e) {
                this.containerLog.error("Error opening configuration", e);
            }
        }
        try {
            if (is == null) {
                return;
            }
            try {
                try {
                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    Throwable th = null;
                    BufferedReader reader = new BufferedReader(isr);
                    Throwable th2 = null;
                    try {
                        try {
                            parse(reader);
                            if (reader != null) {
                                if (0 != 0) {
                                    try {
                                        reader.close();
                                    } catch (Throwable x2) {
                                        th2.addSuppressed(x2);
                                    }
                                } else {
                                    reader.close();
                                }
                            }
                            if (isr != null) {
                                if (0 != 0) {
                                    try {
                                        isr.close();
                                    } catch (Throwable x22) {
                                        th.addSuppressed(x22);
                                    }
                                } else {
                                    isr.close();
                                }
                            }
                        } catch (Throwable th3) {
                            if (reader != null) {
                                if (th2 != null) {
                                    try {
                                        reader.close();
                                    } catch (Throwable x23) {
                                        th2.addSuppressed(x23);
                                    }
                                } else {
                                    reader.close();
                                }
                            }
                            throw th3;
                        }
                    } catch (Throwable th4) {
                        th2 = th4;
                        throw th4;
                    }
                } catch (Throwable th5) {
                    if (hostConfigPath != 0) {
                        if (file != 0) {
                            try {
                                hostConfigPath.close();
                            } catch (Throwable x24) {
                                file.addSuppressed(x24);
                            }
                        } else {
                            hostConfigPath.close();
                        }
                    }
                    throw th5;
                }
            } catch (IOException ioe) {
                this.containerLog.error("Error closing configuration", ioe);
                try {
                    is.close();
                } catch (IOException e2) {
                    this.containerLog.error("Error closing configuration", e2);
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e3) {
                this.containerLog.error("Error closing configuration", e3);
            }
        }
    }

    public void setConfiguration(String configuration) throws Exception {
        if (this.containerLog == null) {
            this.containerLog = LogFactory.getLog(getContainer().getLogName() + ".rewrite");
        }
        this.maps.clear();
        parse(new BufferedReader(new StringReader(configuration)));
    }

    public String getConfiguration() {
        StringBuffer buffer = new StringBuffer();
        Iterator i$ = this.mapsConfiguration.iterator();
        while (i$.hasNext()) {
            String mapConfiguration = i$.next();
            buffer.append(mapConfiguration).append("\r\n");
        }
        if (this.mapsConfiguration.size() > 0) {
            buffer.append("\r\n");
        }
        for (int i = 0; i < this.rules.length; i++) {
            for (int j = 0; j < this.rules[i].getConditions().length; j++) {
                buffer.append(this.rules[i].getConditions()[j].toString()).append("\r\n");
            }
            buffer.append(this.rules[i].toString()).append("\r\n").append("\r\n");
        }
        return buffer.toString();
    }

    protected void parse(BufferedReader reader) throws LifecycleException, IOException, NumberFormatException {
        String line;
        ArrayList<RewriteRule> rules = new ArrayList<>();
        ArrayList<RewriteCond> conditions = new ArrayList<>();
        while (true) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                this.containerLog.error("Error reading configuration", e);
            }
            if (line == null) {
                break;
            }
            Object result = parse(line);
            if (result instanceof RewriteRule) {
                RewriteRule rule = (RewriteRule) result;
                if (this.containerLog.isDebugEnabled()) {
                    this.containerLog.debug("Add rule with pattern " + rule.getPatternString() + " and substitution " + rule.getSubstitutionString());
                }
                for (int i = conditions.size() - 1; i > 0; i--) {
                    if (conditions.get(i - 1).isOrnext()) {
                        conditions.get(i).setOrnext(true);
                    }
                }
                for (int i2 = 0; i2 < conditions.size(); i2++) {
                    if (this.containerLog.isDebugEnabled()) {
                        RewriteCond cond = conditions.get(i2);
                        this.containerLog.debug("Add condition " + cond.getCondPattern() + " test " + cond.getTestString() + " to rule with pattern " + rule.getPatternString() + " and substitution " + rule.getSubstitutionString() + (cond.isOrnext() ? " [OR]" : "") + (cond.isNocase() ? " [NC]" : ""));
                    }
                    rule.addCondition(conditions.get(i2));
                }
                conditions.clear();
                rules.add(rule);
            } else if (result instanceof RewriteCond) {
                conditions.add((RewriteCond) result);
            } else if (result instanceof Object[]) {
                String mapName = (String) ((Object[]) result)[0];
                RewriteMap map = (RewriteMap) ((Object[]) result)[1];
                this.maps.put(mapName, map);
                this.mapsConfiguration.add(line);
                if (map instanceof Lifecycle) {
                    ((Lifecycle) map).start();
                }
            }
        }
        this.rules = (RewriteRule[]) rules.toArray(new RewriteRule[0]);
        for (int i3 = 0; i3 < this.rules.length; i3++) {
            this.rules[i3].parse(this.maps);
        }
    }

    @Override // org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleBase
    protected synchronized void stopInternal() throws LifecycleException {
        super.stopInternal();
        for (RewriteMap map : this.maps.values()) {
            if (map instanceof Lifecycle) {
                ((Lifecycle) map).stop();
            }
        }
        this.maps.clear();
        this.rules = null;
    }

    @Override // org.apache.catalina.Valve
    public void invoke(Request request, Response response) throws ServletException, IOException {
        Connector connector;
        String rewrittenQueryStringDecoded;
        if (!getEnabled() || this.rules == null || this.rules.length == 0) {
            getNext().invoke(request, response);
            return;
        }
        if (Boolean.TRUE.equals(this.invoked.get())) {
            try {
                getNext().invoke(request, response);
                this.invoked.set(null);
                return;
            } finally {
            }
        }
        try {
            Resolver resolver = new ResolverImpl(request);
            this.invoked.set(Boolean.TRUE);
            Charset uriCharset = request.getConnector().getURICharset();
            String originalQueryStringEncoded = request.getQueryString();
            MessageBytes urlMB = this.context ? request.getRequestPathMB() : request.getDecodedRequestURIMB();
            urlMB.toChars();
            CharSequence urlDecoded = urlMB.getCharChunk();
            CharSequence host = request.getServerName();
            boolean rewritten = false;
            boolean done = false;
            boolean qsa = false;
            boolean qsd = false;
            int i = 0;
            while (true) {
                if (i >= this.rules.length) {
                    break;
                }
                RewriteRule rule = this.rules[i];
                CharSequence test = rule.isHost() ? host : urlDecoded;
                CharSequence newtest = rule.evaluate(test, resolver);
                if (newtest != null && !test.equals(newtest.toString())) {
                    if (this.containerLog.isDebugEnabled()) {
                        this.containerLog.debug("Rewrote " + ((Object) test) + " as " + ((Object) newtest) + " with rule pattern " + rule.getPatternString());
                    }
                    if (rule.isHost()) {
                        host = newtest;
                    } else {
                        urlDecoded = newtest;
                    }
                    rewritten = true;
                }
                if (!qsa && newtest != null && rule.isQsappend()) {
                    qsa = true;
                }
                if (!qsa && newtest != null && rule.isQsdiscard()) {
                    qsd = true;
                }
                if (rule.isForbidden() && newtest != null) {
                    response.sendError(403);
                    done = true;
                    break;
                }
                if (rule.isGone() && newtest != null) {
                    response.sendError(410);
                    done = true;
                    break;
                }
                if (rule.isRedirect() && newtest != null) {
                    String urlStringDecoded = urlDecoded.toString();
                    int index = urlStringDecoded.indexOf("?");
                    if (index == -1) {
                        rewrittenQueryStringDecoded = null;
                    } else {
                        rewrittenQueryStringDecoded = urlStringDecoded.substring(index + 1);
                        urlStringDecoded = urlStringDecoded.substring(0, index);
                    }
                    StringBuffer urlStringEncoded = new StringBuffer(URLEncoder.DEFAULT.encode(urlStringDecoded, uriCharset));
                    if (!qsd && originalQueryStringEncoded != null && originalQueryStringEncoded.length() > 0) {
                        if (rewrittenQueryStringDecoded == null) {
                            urlStringEncoded.append('?');
                            urlStringEncoded.append(originalQueryStringEncoded);
                        } else if (qsa) {
                            urlStringEncoded.append('?');
                            urlStringEncoded.append(URLEncoder.QUERY.encode(rewrittenQueryStringDecoded, uriCharset));
                            urlStringEncoded.append('&');
                            urlStringEncoded.append(originalQueryStringEncoded);
                        } else if (index == urlStringEncoded.length() - 1) {
                            urlStringEncoded.deleteCharAt(index);
                        } else {
                            urlStringEncoded.append('?');
                            urlStringEncoded.append(URLEncoder.QUERY.encode(rewrittenQueryStringDecoded, uriCharset));
                        }
                    } else if (rewrittenQueryStringDecoded != null) {
                        urlStringEncoded.append('?');
                        urlStringEncoded.append(URLEncoder.QUERY.encode(rewrittenQueryStringDecoded, uriCharset));
                    }
                    if (this.context && urlStringEncoded.charAt(0) == '/' && !UriUtil.hasScheme(urlStringEncoded)) {
                        urlStringEncoded.insert(0, request.getContext().getEncodedPath());
                    }
                    if (rule.isNoescape()) {
                        response.sendRedirect(UDecoder.URLDecode(urlStringEncoded.toString(), uriCharset));
                    } else {
                        response.sendRedirect(urlStringEncoded.toString());
                    }
                    response.setStatus(rule.getRedirectCode());
                    done = true;
                } else {
                    if (rule.isCookie() && newtest != null) {
                        Cookie cookie = new Cookie(rule.getCookieName(), rule.getCookieResult());
                        cookie.setDomain(rule.getCookieDomain());
                        cookie.setMaxAge(rule.getCookieLifetime());
                        cookie.setPath(rule.getCookiePath());
                        cookie.setSecure(rule.isCookieSecure());
                        cookie.setHttpOnly(rule.isCookieHttpOnly());
                        response.addCookie(cookie);
                    }
                    if (rule.isEnv() && newtest != null) {
                        for (int j = 0; j < rule.getEnvSize(); j++) {
                            request.setAttribute(rule.getEnvName(j), rule.getEnvResult(j));
                        }
                    }
                    if (rule.isType() && newtest != null) {
                        request.setContentType(rule.getTypeValue());
                    }
                    if (rule.isChain() && newtest == null) {
                        int j2 = i;
                        while (true) {
                            if (j2 >= this.rules.length) {
                                break;
                            }
                            if (this.rules[j2].isChain()) {
                                j2++;
                            } else {
                                i = j2;
                                break;
                            }
                        }
                    } else {
                        if (rule.isLast() && newtest != null) {
                            break;
                        }
                        if (rule.isNext() && newtest != null) {
                            i = 0;
                        } else if (newtest != null) {
                            i += rule.getSkip();
                        }
                    }
                    i++;
                }
            }
            if (rewritten) {
                if (!done) {
                    String urlStringDecoded2 = urlDecoded.toString();
                    String queryStringDecoded = null;
                    int queryIndex = urlStringDecoded2.indexOf(63);
                    if (queryIndex != -1) {
                        queryStringDecoded = urlStringDecoded2.substring(queryIndex + 1);
                        urlStringDecoded2 = urlStringDecoded2.substring(0, queryIndex);
                    }
                    String contextPath = null;
                    if (this.context) {
                        contextPath = request.getContextPath();
                    }
                    request.getCoyoteRequest().requestURI().setString(null);
                    CharChunk chunk = request.getCoyoteRequest().requestURI().getCharChunk();
                    chunk.recycle();
                    if (this.context) {
                        chunk.append(contextPath);
                    }
                    chunk.append(URLEncoder.DEFAULT.encode(urlStringDecoded2, uriCharset));
                    request.getCoyoteRequest().requestURI().toChars();
                    String urlStringDecoded3 = RequestUtil.normalize(urlStringDecoded2);
                    request.getCoyoteRequest().decodedURI().setString(null);
                    CharChunk chunk2 = request.getCoyoteRequest().decodedURI().getCharChunk();
                    chunk2.recycle();
                    if (this.context) {
                        chunk2.append(request.getServletContext().getContextPath());
                    }
                    chunk2.append(urlStringDecoded3);
                    request.getCoyoteRequest().decodedURI().toChars();
                    if (queryStringDecoded != null) {
                        request.getCoyoteRequest().queryString().setString(null);
                        CharChunk chunk3 = request.getCoyoteRequest().queryString().getCharChunk();
                        chunk3.recycle();
                        chunk3.append(URLEncoder.QUERY.encode(queryStringDecoded, uriCharset));
                        if (qsa && originalQueryStringEncoded != null && originalQueryStringEncoded.length() > 0) {
                            chunk3.append('&');
                            chunk3.append(originalQueryStringEncoded);
                        }
                        if (!chunk3.isNull()) {
                            request.getCoyoteRequest().queryString().toChars();
                        }
                    }
                    if (!host.equals(request.getServerName())) {
                        request.getCoyoteRequest().serverName().setString(null);
                        CharChunk chunk4 = request.getCoyoteRequest().serverName().getCharChunk();
                        chunk4.recycle();
                        chunk4.append(host.toString());
                        request.getCoyoteRequest().serverName().toChars();
                    }
                    request.getMappingData().recycle();
                    try {
                        connector = request.getConnector();
                    } catch (Exception e) {
                    }
                    if (!connector.getProtocolHandler().getAdapter().prepare(request.getCoyoteRequest(), response.getCoyoteResponse())) {
                        return;
                    }
                    Pipeline pipeline = connector.getService().getContainer().getPipeline();
                    request.setAsyncSupported(pipeline.isAsyncSupported());
                    pipeline.getFirst().invoke(request, response);
                }
            } else {
                getNext().invoke(request, response);
            }
            this.invoked.set(null);
        } finally {
            this.invoked.set(null);
        }
    }

    protected File getConfigBase() {
        File configBase = new File(System.getProperty("catalina.base"), "conf");
        if (!configBase.exists()) {
            return null;
        }
        return configBase;
    }

    protected String getHostConfigPath(String resourceName) {
        StringBuffer result = new StringBuffer();
        Container host = null;
        Container engine = null;
        for (Container container = getContainer(); container != null; container = container.getParent()) {
            if (container instanceof Host) {
                host = container;
            }
            if (container instanceof Engine) {
                engine = container;
            }
        }
        if (engine != null) {
            result.append(engine.getName()).append('/');
        }
        if (host != null) {
            result.append(host.getName()).append('/');
        }
        result.append(resourceName);
        return result.toString();
    }

    public static Object parse(String line) throws NumberFormatException {
        StringTokenizer tokenizer = new StringTokenizer(line);
        if (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.equals("RewriteCond")) {
                RewriteCond condition = new RewriteCond();
                if (tokenizer.countTokens() < 2) {
                    throw new IllegalArgumentException("Invalid line: " + line);
                }
                condition.setTestString(tokenizer.nextToken());
                condition.setCondPattern(tokenizer.nextToken());
                if (tokenizer.hasMoreTokens()) {
                    String flags = tokenizer.nextToken();
                    condition.setFlagsString(flags);
                    if (flags.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX) && flags.endsWith("]")) {
                        flags = flags.substring(1, flags.length() - 1);
                    }
                    StringTokenizer flagsTokenizer = new StringTokenizer(flags, ",");
                    while (flagsTokenizer.hasMoreElements()) {
                        parseCondFlag(line, condition, flagsTokenizer.nextToken());
                    }
                }
                return condition;
            }
            if (token.equals("RewriteRule")) {
                RewriteRule rule = new RewriteRule();
                if (tokenizer.countTokens() < 2) {
                    throw new IllegalArgumentException("Invalid line: " + line);
                }
                rule.setPatternString(tokenizer.nextToken());
                rule.setSubstitutionString(tokenizer.nextToken());
                if (tokenizer.hasMoreTokens()) {
                    String flags2 = tokenizer.nextToken();
                    rule.setFlagsString(flags2);
                    if (flags2.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX) && flags2.endsWith("]")) {
                        flags2 = flags2.substring(1, flags2.length() - 1);
                    }
                    StringTokenizer flagsTokenizer2 = new StringTokenizer(flags2, ",");
                    while (flagsTokenizer2.hasMoreElements()) {
                        parseRuleFlag(line, rule, flagsTokenizer2.nextToken());
                    }
                }
                return rule;
            }
            if (token.equals("RewriteMap")) {
                if (tokenizer.countTokens() < 2) {
                    throw new IllegalArgumentException("Invalid line: " + line);
                }
                String name = tokenizer.nextToken();
                String rewriteMapClassName = tokenizer.nextToken();
                try {
                    RewriteMap map = (RewriteMap) Class.forName(rewriteMapClassName).getConstructor(new Class[0]).newInstance(new Object[0]);
                    if (tokenizer.hasMoreTokens()) {
                        map.setParameters(tokenizer.nextToken());
                    }
                    Object[] result = {name, map};
                    return result;
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid map className: " + line);
                }
            }
            if (!token.startsWith("#")) {
                throw new IllegalArgumentException("Invalid line: " + line);
            }
            return null;
        }
        return null;
    }

    protected static void parseCondFlag(String line, RewriteCond condition, String flag) {
        if (flag.equals("NC") || flag.equals("nocase")) {
            condition.setNocase(true);
        } else {
            if (flag.equals("OR") || flag.equals("ornext")) {
                condition.setOrnext(true);
                return;
            }
            throw new IllegalArgumentException("Invalid flag in: " + line + " flags: " + flag);
        }
    }

    protected static void parseRuleFlag(String line, RewriteRule rule, String flag) throws NumberFormatException {
        if (flag.equals("B")) {
            rule.setEscapeBackReferences(true);
            return;
        }
        if (flag.equals("chain") || flag.equals("C")) {
            rule.setChain(true);
            return;
        }
        if (flag.startsWith("cookie=") || flag.startsWith("CO=")) {
            rule.setCookie(true);
            if (flag.startsWith("cookie")) {
                flag = flag.substring("cookie=".length());
            } else if (flag.startsWith("CO=")) {
                flag = flag.substring("CO=".length());
            }
            StringTokenizer tokenizer = new StringTokenizer(flag, ":");
            if (tokenizer.countTokens() < 2) {
                throw new IllegalArgumentException("Invalid flag in: " + line);
            }
            rule.setCookieName(tokenizer.nextToken());
            rule.setCookieValue(tokenizer.nextToken());
            if (tokenizer.hasMoreTokens()) {
                rule.setCookieDomain(tokenizer.nextToken());
            }
            if (tokenizer.hasMoreTokens()) {
                try {
                    rule.setCookieLifetime(Integer.parseInt(tokenizer.nextToken()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid flag in: " + line, e);
                }
            }
            if (tokenizer.hasMoreTokens()) {
                rule.setCookiePath(tokenizer.nextToken());
            }
            if (tokenizer.hasMoreTokens()) {
                rule.setCookieSecure(Boolean.parseBoolean(tokenizer.nextToken()));
            }
            if (tokenizer.hasMoreTokens()) {
                rule.setCookieHttpOnly(Boolean.parseBoolean(tokenizer.nextToken()));
                return;
            }
            return;
        }
        if (flag.startsWith("env=") || flag.startsWith("E=")) {
            rule.setEnv(true);
            if (flag.startsWith("env=")) {
                flag = flag.substring("env=".length());
            } else if (flag.startsWith("E=")) {
                flag = flag.substring("E=".length());
            }
            int pos = flag.indexOf(58);
            if (pos == -1 || pos + 1 == flag.length()) {
                throw new IllegalArgumentException("Invalid flag in: " + line);
            }
            rule.addEnvName(flag.substring(0, pos));
            rule.addEnvValue(flag.substring(pos + 1));
            return;
        }
        if (flag.startsWith("forbidden") || flag.startsWith("F")) {
            rule.setForbidden(true);
            return;
        }
        if (flag.startsWith("gone") || flag.startsWith("G")) {
            rule.setGone(true);
            return;
        }
        if (flag.startsWith("host") || flag.startsWith(StandardRoles.H)) {
            rule.setHost(true);
            return;
        }
        if (flag.startsWith(Link.REL_LAST) || flag.startsWith(StandardRoles.L)) {
            rule.setLast(true);
            return;
        }
        if (flag.startsWith("nocase") || flag.startsWith("NC")) {
            rule.setNocase(true);
            return;
        }
        if (flag.startsWith("noescape") || flag.startsWith("NE")) {
            rule.setNoescape(true);
            return;
        }
        if (flag.startsWith(Link.REL_NEXT) || flag.startsWith("N")) {
            rule.setNext(true);
            return;
        }
        if (flag.startsWith("qsappend") || flag.startsWith("QSA")) {
            rule.setQsappend(true);
            return;
        }
        if (flag.startsWith("qsdiscard") || flag.startsWith("QSD")) {
            rule.setQsdiscard(true);
            return;
        }
        if (!flag.startsWith("redirect") && !flag.startsWith("R")) {
            if (flag.startsWith("skip") || flag.startsWith("S")) {
                if (flag.startsWith("skip=")) {
                    flag = flag.substring("skip=".length());
                } else if (flag.startsWith("S=")) {
                    flag = flag.substring("S=".length());
                }
                rule.setSkip(Integer.parseInt(flag));
                return;
            }
            if (flag.startsWith("type") || flag.startsWith("T")) {
                if (flag.startsWith("type=")) {
                    flag = flag.substring("type=".length());
                } else if (flag.startsWith("T=")) {
                    flag = flag.substring("T=".length());
                }
                rule.setType(true);
                rule.setTypeValue(flag);
                return;
            }
            throw new IllegalArgumentException("Invalid flag in: " + line + " flag: " + flag);
        }
        rule.setRedirect(true);
        int redirectCode = 302;
        if (flag.startsWith("redirect=") || flag.startsWith("R=")) {
            if (flag.startsWith("redirect=")) {
                flag = flag.substring("redirect=".length());
            } else if (flag.startsWith("R=")) {
                flag = flag.substring("R=".length());
            }
            switch (flag) {
                case "temp":
                    redirectCode = 302;
                    break;
                case "permanent":
                    redirectCode = 301;
                    break;
                case "seeother":
                    redirectCode = 303;
                    break;
                default:
                    redirectCode = Integer.parseInt(flag);
                    break;
            }
        }
        rule.setRedirectCode(redirectCode);
    }
}
