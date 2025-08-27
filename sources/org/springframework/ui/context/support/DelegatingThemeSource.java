package org.springframework.ui.context.support;

import org.springframework.ui.context.HierarchicalThemeSource;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ui/context/support/DelegatingThemeSource.class */
public class DelegatingThemeSource implements HierarchicalThemeSource {
    private ThemeSource parentThemeSource;

    @Override // org.springframework.ui.context.HierarchicalThemeSource
    public void setParentThemeSource(ThemeSource parentThemeSource) {
        this.parentThemeSource = parentThemeSource;
    }

    @Override // org.springframework.ui.context.HierarchicalThemeSource
    public ThemeSource getParentThemeSource() {
        return this.parentThemeSource;
    }

    @Override // org.springframework.ui.context.ThemeSource
    public Theme getTheme(String themeName) {
        if (this.parentThemeSource != null) {
            return this.parentThemeSource.getTheme(themeName);
        }
        return null;
    }
}
