package org.springframework.boot.autoconfigure.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "security")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SecurityProperties.class */
public class SecurityProperties implements SecurityPrerequisite {
    public static final int ACCESS_OVERRIDE_ORDER = 2147483640;
    public static final int BASIC_AUTH_ORDER = 2147483642;
    public static final int IGNORED_ORDER = Integer.MIN_VALUE;
    public static final int DEFAULT_FILTER_ORDER = -100;
    private boolean requireSsl;
    private boolean enableCsrf = false;
    private Basic basic = new Basic();
    private final Headers headers = new Headers();
    private SessionCreationPolicy sessions = SessionCreationPolicy.STATELESS;
    private List<String> ignored = new ArrayList();
    private final User user = new User();
    private int filterOrder = -100;
    private Set<String> filterDispatcherTypes;

    public Headers getHeaders() {
        return this.headers;
    }

    public User getUser() {
        return this.user;
    }

    public SessionCreationPolicy getSessions() {
        return this.sessions;
    }

    public void setSessions(SessionCreationPolicy sessions) {
        this.sessions = sessions;
    }

    public Basic getBasic() {
        return this.basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public boolean isRequireSsl() {
        return this.requireSsl;
    }

    public void setRequireSsl(boolean requireSsl) {
        this.requireSsl = requireSsl;
    }

    public boolean isEnableCsrf() {
        return this.enableCsrf;
    }

    public void setEnableCsrf(boolean enableCsrf) {
        this.enableCsrf = enableCsrf;
    }

    public void setIgnored(List<String> ignored) {
        this.ignored = new ArrayList(ignored);
    }

    public List<String> getIgnored() {
        return this.ignored;
    }

    public int getFilterOrder() {
        return this.filterOrder;
    }

    public void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    public Set<String> getFilterDispatcherTypes() {
        return this.filterDispatcherTypes;
    }

    public void setFilterDispatcherTypes(Set<String> filterDispatcherTypes) {
        this.filterDispatcherTypes = filterDispatcherTypes;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SecurityProperties$Headers.class */
    public static class Headers {
        private String contentSecurityPolicy;
        private boolean xss = true;
        private boolean cache = true;
        private boolean frame = true;
        private boolean contentType = true;
        private ContentSecurityPolicyMode contentSecurityPolicyMode = ContentSecurityPolicyMode.DEFAULT;
        private HSTS hsts = HSTS.ALL;

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SecurityProperties$Headers$ContentSecurityPolicyMode.class */
        public enum ContentSecurityPolicyMode {
            DEFAULT,
            REPORT_ONLY
        }

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SecurityProperties$Headers$HSTS.class */
        public enum HSTS {
            NONE,
            DOMAIN,
            ALL
        }

        public boolean isXss() {
            return this.xss;
        }

        public void setXss(boolean xss) {
            this.xss = xss;
        }

        public boolean isCache() {
            return this.cache;
        }

        public void setCache(boolean cache) {
            this.cache = cache;
        }

        public boolean isFrame() {
            return this.frame;
        }

        public void setFrame(boolean frame) {
            this.frame = frame;
        }

        public boolean isContentType() {
            return this.contentType;
        }

        public void setContentType(boolean contentType) {
            this.contentType = contentType;
        }

        public String getContentSecurityPolicy() {
            return this.contentSecurityPolicy;
        }

        public void setContentSecurityPolicy(String contentSecurityPolicy) {
            this.contentSecurityPolicy = contentSecurityPolicy;
        }

        public ContentSecurityPolicyMode getContentSecurityPolicyMode() {
            return this.contentSecurityPolicyMode;
        }

        public void setContentSecurityPolicyMode(ContentSecurityPolicyMode contentSecurityPolicyMode) {
            this.contentSecurityPolicyMode = contentSecurityPolicyMode;
        }

        public HSTS getHsts() {
            return this.hsts;
        }

        public void setHsts(HSTS hsts) {
            this.hsts = hsts;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SecurityProperties$Basic.class */
    public static class Basic {
        private boolean enabled = true;
        private String realm = "Spring";
        private String[] path = {"/**"};
        private SecurityAuthorizeMode authorizeMode = SecurityAuthorizeMode.ROLE;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getRealm() {
            return this.realm;
        }

        public void setRealm(String realm) {
            this.realm = realm;
        }

        public String[] getPath() {
            return this.path;
        }

        public void setPath(String... paths) {
            this.path = paths;
        }

        public SecurityAuthorizeMode getAuthorizeMode() {
            return this.authorizeMode;
        }

        public void setAuthorizeMode(SecurityAuthorizeMode authorizeMode) {
            this.authorizeMode = authorizeMode;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/SecurityProperties$User.class */
    public static class User {
        private String name = "user";
        private String password = UUID.randomUUID().toString();
        private List<String> role = new ArrayList(Collections.singletonList("USER"));
        private boolean defaultPassword = true;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            if ((password.startsWith("${") && password.endsWith("}")) || !StringUtils.hasLength(password)) {
                return;
            }
            this.defaultPassword = false;
            this.password = password;
        }

        public List<String> getRole() {
            return this.role;
        }

        public void setRole(List<String> role) {
            this.role = new ArrayList(role);
        }

        public boolean isDefaultPassword() {
            return this.defaultPassword;
        }
    }
}
