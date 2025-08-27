package org.springframework.web.servlet.theme;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/theme/CookieThemeResolver.class */
public class CookieThemeResolver extends CookieGenerator implements ThemeResolver {
    public static final String ORIGINAL_DEFAULT_THEME_NAME = "theme";
    public static final String THEME_REQUEST_ATTRIBUTE_NAME = CookieThemeResolver.class.getName() + ".THEME";
    public static final String DEFAULT_COOKIE_NAME = CookieThemeResolver.class.getName() + ".THEME";
    private String defaultThemeName = "theme";

    public CookieThemeResolver() {
        setCookieName(DEFAULT_COOKIE_NAME);
    }

    public void setDefaultThemeName(String defaultThemeName) {
        this.defaultThemeName = defaultThemeName;
    }

    public String getDefaultThemeName() {
        return this.defaultThemeName;
    }

    @Override // org.springframework.web.servlet.ThemeResolver
    public String resolveThemeName(HttpServletRequest request) {
        String themeName = (String) request.getAttribute(THEME_REQUEST_ATTRIBUTE_NAME);
        if (themeName != null) {
            return themeName;
        }
        Cookie cookie = WebUtils.getCookie(request, getCookieName());
        if (cookie != null) {
            String value = cookie.getValue();
            if (StringUtils.hasText(value)) {
                themeName = value;
            }
        }
        if (themeName == null) {
            themeName = getDefaultThemeName();
        }
        request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, themeName);
        return themeName;
    }

    @Override // org.springframework.web.servlet.ThemeResolver
    public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName) {
        if (StringUtils.hasText(themeName)) {
            request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, themeName);
            addCookie(response, themeName);
        } else {
            request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, getDefaultThemeName());
            removeCookie(response);
        }
    }
}
