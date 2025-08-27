package org.apache.tomcat.util.descriptor.web;

import com.mysql.jdbc.MysqlErrorNumbers;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/descriptor/web/ContextResourceEnvRef.class */
public class ContextResourceEnvRef extends ResourceBase {
    private static final long serialVersionUID = 1;
    private boolean override = true;

    public boolean getOverride() {
        return this.override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ContextResourceEnvRef[");
        sb.append("name=");
        sb.append(getName());
        if (getType() != null) {
            sb.append(", type=");
            sb.append(getType());
        }
        sb.append(", override=");
        sb.append(this.override);
        sb.append("]");
        return sb.toString();
    }

    @Override // org.apache.tomcat.util.descriptor.web.ResourceBase
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.override ? MysqlErrorNumbers.ER_WRONG_VALUE_FOR_VAR : MysqlErrorNumbers.ER_SLAVE_IGNORED_TABLE);
    }

    @Override // org.apache.tomcat.util.descriptor.web.ResourceBase
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ContextResourceEnvRef other = (ContextResourceEnvRef) obj;
        if (this.override != other.override) {
            return false;
        }
        return true;
    }
}
