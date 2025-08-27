package org.springframework.jmx.export.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import org.springframework.jmx.export.metadata.JmxMetadataUtils;
import org.springframework.jmx.export.metadata.ManagedNotification;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/assembler/AbstractConfigurableMBeanInfoAssembler.class */
public abstract class AbstractConfigurableMBeanInfoAssembler extends AbstractReflectiveMBeanInfoAssembler {
    private ModelMBeanNotificationInfo[] notificationInfos;
    private final Map<String, ModelMBeanNotificationInfo[]> notificationInfoMappings = new HashMap();

    public void setNotificationInfos(ManagedNotification[] notificationInfos) {
        ModelMBeanNotificationInfo[] infos = new ModelMBeanNotificationInfo[notificationInfos.length];
        for (int i = 0; i < notificationInfos.length; i++) {
            ManagedNotification notificationInfo = notificationInfos[i];
            infos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(notificationInfo);
        }
        this.notificationInfos = infos;
    }

    public void setNotificationInfoMappings(Map<String, Object> notificationInfoMappings) {
        for (Map.Entry<String, Object> entry : notificationInfoMappings.entrySet()) {
            this.notificationInfoMappings.put(entry.getKey(), extractNotificationMetadata(entry.getValue()));
        }
    }

    @Override // org.springframework.jmx.export.assembler.AbstractMBeanInfoAssembler
    protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) {
        ModelMBeanNotificationInfo[] result = null;
        if (StringUtils.hasText(beanKey)) {
            result = this.notificationInfoMappings.get(beanKey);
        }
        if (result == null) {
            result = this.notificationInfos;
        }
        return result != null ? result : new ModelMBeanNotificationInfo[0];
    }

    private ModelMBeanNotificationInfo[] extractNotificationMetadata(Object mapValue) {
        if (mapValue instanceof ManagedNotification) {
            ManagedNotification mn = (ManagedNotification) mapValue;
            return new ModelMBeanNotificationInfo[]{JmxMetadataUtils.convertToModelMBeanNotificationInfo(mn)};
        }
        if (mapValue instanceof Collection) {
            Collection<?> col = (Collection) mapValue;
            List<ModelMBeanNotificationInfo> result = new ArrayList<>();
            for (Object colValue : col) {
                if (!(colValue instanceof ManagedNotification)) {
                    throw new IllegalArgumentException("Property 'notificationInfoMappings' only accepts ManagedNotifications for Map values");
                }
                ManagedNotification mn2 = (ManagedNotification) colValue;
                result.add(JmxMetadataUtils.convertToModelMBeanNotificationInfo(mn2));
            }
            return (ModelMBeanNotificationInfo[]) result.toArray(new ModelMBeanNotificationInfo[result.size()]);
        }
        throw new IllegalArgumentException("Property 'notificationInfoMappings' only accepts ManagedNotifications for Map values");
    }
}
