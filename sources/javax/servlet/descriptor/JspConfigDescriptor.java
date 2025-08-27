package javax.servlet.descriptor;

import java.util.Collection;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/descriptor/JspConfigDescriptor.class */
public interface JspConfigDescriptor {
    Collection<TaglibDescriptor> getTaglibs();

    Collection<JspPropertyGroupDescriptor> getJspPropertyGroups();
}
