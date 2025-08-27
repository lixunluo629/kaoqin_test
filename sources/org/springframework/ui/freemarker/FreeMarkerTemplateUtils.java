package org.springframework.ui.freemarker;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/ui/freemarker/FreeMarkerTemplateUtils.class */
public abstract class FreeMarkerTemplateUtils {
    public static String processTemplateIntoString(Template template, Object model) throws TemplateException, IOException {
        StringWriter result = new StringWriter();
        template.process(model, result);
        return result.toString();
    }
}
