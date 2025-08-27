package org.springframework.web.servlet.mvc.support;

import java.util.Collection;
import java.util.Map;
import org.springframework.ui.Model;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/support/RedirectAttributes.class */
public interface RedirectAttributes extends Model {
    @Override // org.springframework.ui.Model
    RedirectAttributes addAttribute(String str, Object obj);

    @Override // org.springframework.ui.Model
    RedirectAttributes addAttribute(Object obj);

    @Override // org.springframework.ui.Model
    RedirectAttributes addAllAttributes(Collection<?> collection);

    @Override // org.springframework.ui.Model
    RedirectAttributes mergeAttributes(Map<String, ?> map);

    RedirectAttributes addFlashAttribute(String str, Object obj);

    RedirectAttributes addFlashAttribute(Object obj);

    Map<String, ?> getFlashAttributes();
}
