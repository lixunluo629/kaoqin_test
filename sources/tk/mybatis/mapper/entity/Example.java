package tk.mybatis.mapper.entity;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/entity/Example.class */
public class Example {
    protected String orderByClause;
    protected boolean distinct;
    protected boolean exists;
    protected Set<String> selectColumns;
    protected List<Criteria> oredCriteria;
    protected Class<?> entityClass;
    protected EntityTable table;
    protected Map<String, EntityColumn> propertyMap;

    public Example(Class<?> entityClass) {
        this(entityClass, true);
    }

    public Example(Class<?> entityClass, boolean exists) {
        this.exists = exists;
        this.oredCriteria = new LinkedList();
        this.entityClass = entityClass;
        this.table = EntityHelper.getEntityTable(entityClass);
        this.propertyMap = new HashMap(this.table.getEntityClassColumns().size());
        for (EntityColumn column : this.table.getEntityClassColumns()) {
            this.propertyMap.put(column.getProperty(), column);
        }
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public Set<String> getSelectColumns() {
        return this.selectColumns;
    }

    public Example selectProperties(String... properties) {
        if (properties != null && properties.length > 0) {
            if (this.selectColumns == null) {
                this.selectColumns = new LinkedHashSet();
            }
            for (String property : properties) {
                if (this.propertyMap.containsKey(property)) {
                    this.selectColumns.add(this.propertyMap.get(property).getColumn());
                }
            }
        }
        return this;
    }

    public boolean isDistinct() {
        return this.distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getOredCriteria() {
        return this.oredCriteria;
    }

    public void or(Criteria criteria) {
        this.oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        this.oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (this.oredCriteria.size() == 0) {
            this.oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria(this.propertyMap, this.exists);
        return criteria;
    }

    public void clear() {
        this.oredCriteria.clear();
        this.orderByClause = null;
        this.distinct = false;
    }

    /* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/entity/Example$GeneratedCriteria.class */
    protected static abstract class GeneratedCriteria {
        protected List<Criterion> criteria;
        protected boolean exists;
        protected Map<String, EntityColumn> propertyMap;

        protected GeneratedCriteria(Map<String, EntityColumn> propertyMap) {
            this(propertyMap, true);
        }

        protected GeneratedCriteria(Map<String, EntityColumn> propertyMap, boolean exists) {
            this.exists = exists;
            this.criteria = new LinkedList();
            this.propertyMap = propertyMap;
        }

        private String column(String property) {
            if (this.propertyMap.containsKey(property)) {
                return this.propertyMap.get(property).getColumn();
            }
            if (this.exists) {
                throw new RuntimeException("当前实体类不包含名为" + property + "的属性!");
            }
            return null;
        }

        private String property(String property) {
            if (this.propertyMap.containsKey(property)) {
                return property;
            }
            if (this.exists) {
                throw new RuntimeException("当前实体类不包含名为" + property + "的属性!");
            }
            return null;
        }

        public boolean isValid() {
            return this.criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return this.criteria;
        }

        public List<Criterion> getCriteria() {
            return this.criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            if (condition.startsWith("null")) {
                return;
            }
            this.criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            if (property == null) {
                return;
            }
            this.criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            if (property == null) {
                return;
            }
            this.criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIsNull(String property) {
            addCriterion(column(property) + " is null");
            return (Criteria) this;
        }

        public Criteria andIsNotNull(String property) {
            addCriterion(column(property) + " is not null");
            return (Criteria) this;
        }

        public Criteria andEqualTo(String property, Object value) {
            addCriterion(column(property) + " =", value, property(property));
            return (Criteria) this;
        }

        public Criteria andNotEqualTo(String property, Object value) {
            addCriterion(column(property) + " <>", value, property(property));
            return (Criteria) this;
        }

        public Criteria andGreaterThan(String property, Object value) {
            addCriterion(column(property) + " >", value, property(property));
            return (Criteria) this;
        }

        public Criteria andGreaterThanOrEqualTo(String property, Object value) {
            addCriterion(column(property) + " >=", value, property(property));
            return (Criteria) this;
        }

        public Criteria andLessThan(String property, Object value) {
            addCriterion(column(property) + " <", value, property(property));
            return (Criteria) this;
        }

        public Criteria andLessThanOrEqualTo(String property, Object value) {
            addCriterion(column(property) + " <=", value, property(property));
            return (Criteria) this;
        }

        public Criteria andIn(String property, List<Object> values) {
            addCriterion(column(property) + " in", values, property(property));
            return (Criteria) this;
        }

        public Criteria andNotIn(String property, List<Object> values) {
            addCriterion(column(property) + " not in", values, property(property));
            return (Criteria) this;
        }

        public Criteria andBetween(String property, Object value1, Object value2) {
            addCriterion(column(property) + " between", value1, value2, property(property));
            return (Criteria) this;
        }

        public Criteria andNotBetween(String property, Object value1, Object value2) {
            addCriterion(column(property) + " not between", value1, value2, property(property));
            return (Criteria) this;
        }

        public Criteria andLike(String property, String value) {
            addCriterion(column(property) + "  like", value, property(property));
            return (Criteria) this;
        }

        public Criteria andNotLike(String property, String value) {
            addCriterion(column(property) + "  not like", value, property(property));
            return (Criteria) this;
        }

        public Criteria andEqualTo(Object param) {
            Object value;
            MetaObject metaObject = SystemMetaObject.forObject(param);
            String[] properties = metaObject.getGetterNames();
            for (String property : properties) {
                if (this.propertyMap.get(property) != null && (value = metaObject.getValue(property)) != null) {
                    andEqualTo(property, value);
                }
            }
            return (Criteria) this;
        }
    }

    /* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/entity/Example$Criteria.class */
    public static class Criteria extends GeneratedCriteria {
        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andEqualTo(Object obj) {
            return super.andEqualTo(obj);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andNotLike(String str, String str2) {
            return super.andNotLike(str, str2);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andLike(String str, String str2) {
            return super.andLike(str, str2);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andNotBetween(String str, Object obj, Object obj2) {
            return super.andNotBetween(str, obj, obj2);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andBetween(String str, Object obj, Object obj2) {
            return super.andBetween(str, obj, obj2);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andNotIn(String str, List list) {
            return super.andNotIn(str, list);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andIn(String str, List list) {
            return super.andIn(str, list);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andLessThanOrEqualTo(String str, Object obj) {
            return super.andLessThanOrEqualTo(str, obj);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andLessThan(String str, Object obj) {
            return super.andLessThan(str, obj);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andGreaterThanOrEqualTo(String str, Object obj) {
            return super.andGreaterThanOrEqualTo(str, obj);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andGreaterThan(String str, Object obj) {
            return super.andGreaterThan(str, obj);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andNotEqualTo(String str, Object obj) {
            return super.andNotEqualTo(str, obj);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andEqualTo(String str, Object obj) {
            return super.andEqualTo(str, obj);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andIsNotNull(String str) {
            return super.andIsNotNull(str);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ Criteria andIsNull(String str) {
            return super.andIsNull(str);
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ List getCriteria() {
            return super.getCriteria();
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ List getAllCriteria() {
            return super.getAllCriteria();
        }

        @Override // tk.mybatis.mapper.entity.Example.GeneratedCriteria
        public /* bridge */ /* synthetic */ boolean isValid() {
            return super.isValid();
        }

        protected Criteria(Map<String, EntityColumn> propertyMap) {
            super(propertyMap);
        }

        protected Criteria(Map<String, EntityColumn> propertyMap, boolean exists) {
            super(propertyMap, exists);
        }
    }

    /* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/entity/Example$Criterion.class */
    public static class Criterion {
        private String condition;
        private Object value;
        private Object secondValue;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;
        private String typeHandler;

        protected Criterion(String condition) {
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, (String) null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return this.condition;
        }

        public Object getValue() {
            return this.value;
        }

        public Object getSecondValue() {
            return this.secondValue;
        }

        public boolean isNoValue() {
            return this.noValue;
        }

        public boolean isSingleValue() {
            return this.singleValue;
        }

        public boolean isBetweenValue() {
            return this.betweenValue;
        }

        public boolean isListValue() {
            return this.listValue;
        }

        public String getTypeHandler() {
            return this.typeHandler;
        }
    }
}
