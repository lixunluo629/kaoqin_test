package org.springframework.boot.orm.jpa.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.internal.util.StringHelper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/orm/jpa/hibernate/SpringNamingStrategy.class */
public class SpringNamingStrategy extends ImprovedNamingStrategy {
    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
        String name = propertyTableName;
        if (propertyName != null) {
            name = StringHelper.unqualify(propertyName);
        }
        Assert.state(StringUtils.hasLength(name), "Unable to generate foreignKeyColumnName");
        return columnName(name) + "_" + referencedColumnName;
    }
}
