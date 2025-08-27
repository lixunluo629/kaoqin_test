package org.springframework.boot.autoconfigure.mustache.web;

import com.samskivert.mustache.Template;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractTemplateView;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/web/MustacheView.class */
public class MustacheView extends AbstractTemplateView {
    private Template template;

    public MustacheView() {
    }

    public MustacheView(Template template) {
        this.template = template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    @Override // org.springframework.web.servlet.view.AbstractTemplateView
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (this.template != null) {
            this.template.execute(model, response.getWriter());
        }
    }
}
