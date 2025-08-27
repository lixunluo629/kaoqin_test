package tk.mybatis.mapper.entity;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/entity/EntityColumn.class */
public class EntityColumn {
    private EntityTable table;
    private String property;
    private String column;
    private Class<?> javaType;
    private String sequenceName;
    private boolean id = false;
    private boolean uuid = false;
    private boolean identity = false;
    private String generator;
    private String orderBy;

    public EntityColumn() {
    }

    public EntityColumn(EntityTable table) {
        this.table = table;
    }

    public EntityTable getTable() {
        return this.table;
    }

    public void setTable(EntityTable table) {
        this.table = table;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class<?> getJavaType() {
        return this.javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public String getSequenceName() {
        return this.sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public boolean isId() {
        return this.id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isUuid() {
        return this.uuid;
    }

    public void setUuid(boolean uuid) {
        this.uuid = uuid;
    }

    public boolean isIdentity() {
        return this.identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public String getGenerator() {
        return this.generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityColumn that = (EntityColumn) o;
        if (this.id != that.id || this.identity != that.identity || this.uuid != that.uuid) {
            return false;
        }
        if (this.column != null) {
            if (!this.column.equals(that.column)) {
                return false;
            }
        } else if (that.column != null) {
            return false;
        }
        if (this.generator != null) {
            if (!this.generator.equals(that.generator)) {
                return false;
            }
        } else if (that.generator != null) {
            return false;
        }
        if (this.javaType != null) {
            if (!this.javaType.equals(that.javaType)) {
                return false;
            }
        } else if (that.javaType != null) {
            return false;
        }
        if (this.orderBy != null) {
            if (!this.orderBy.equals(that.orderBy)) {
                return false;
            }
        } else if (that.orderBy != null) {
            return false;
        }
        if (this.property != null) {
            if (!this.property.equals(that.property)) {
                return false;
            }
        } else if (that.property != null) {
            return false;
        }
        if (this.sequenceName != null) {
            if (!this.sequenceName.equals(that.sequenceName)) {
                return false;
            }
            return true;
        }
        if (that.sequenceName != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.property != null ? this.property.hashCode() : 0;
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.column != null ? this.column.hashCode() : 0))) + (this.javaType != null ? this.javaType.hashCode() : 0))) + (this.sequenceName != null ? this.sequenceName.hashCode() : 0))) + (this.id ? 1 : 0))) + (this.uuid ? 1 : 0))) + (this.identity ? 1 : 0))) + (this.generator != null ? this.generator.hashCode() : 0))) + (this.orderBy != null ? this.orderBy.hashCode() : 0);
    }
}
