package tk.mybatis.mapper.entity;

import java.util.Properties;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.util.StringUtil;

/* loaded from: mapper-3.2.2.jar:tk/mybatis/mapper/entity/Config.class */
public class Config {
    private String UUID;
    private String IDENTITY;
    private String seqFormat;
    private String catalog;
    private String schema;
    private Style style;
    private boolean BEFORE = false;
    private boolean notEmpty = false;

    public boolean getBEFORE() {
        return this.BEFORE;
    }

    public void setBEFORE(boolean BEFORE) {
        this.BEFORE = BEFORE;
    }

    public void setOrder(String order) {
        this.BEFORE = "BEFORE".equalsIgnoreCase(order);
    }

    public String getCatalog() {
        return this.catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getIDENTITY() {
        if (StringUtil.isNotEmpty(this.IDENTITY)) {
            return this.IDENTITY;
        }
        return IdentityDialect.MYSQL.getIdentityRetrievalStatement();
    }

    public void setIDENTITY(String IDENTITY) {
        IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(IDENTITY);
        if (identityDialect != null) {
            this.IDENTITY = identityDialect.getIdentityRetrievalStatement();
        } else {
            this.IDENTITY = IDENTITY;
        }
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSeqFormat() {
        if (StringUtil.isNotEmpty(this.seqFormat)) {
            return this.seqFormat;
        }
        return "{0}.nextval";
    }

    public void setSeqFormat(String seqFormat) {
        this.seqFormat = seqFormat;
    }

    public String getUUID() {
        if (StringUtil.isNotEmpty(this.UUID)) {
            return this.UUID;
        }
        return "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")";
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public boolean isNotEmpty() {
        return this.notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public Style getStyle() {
        return this.style == null ? Style.camelhump : this.style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getPrefix() {
        if (StringUtil.isNotEmpty(this.catalog)) {
            return this.catalog;
        }
        if (StringUtil.isNotEmpty(this.schema)) {
            return this.schema;
        }
        return "";
    }

    public void setProperties(Properties properties) {
        if (properties == null) {
            this.style = Style.camelhump;
            return;
        }
        String UUID = properties.getProperty("UUID");
        if (StringUtil.isNotEmpty(UUID)) {
            setUUID(UUID);
        }
        String IDENTITY = properties.getProperty("IDENTITY");
        if (StringUtil.isNotEmpty(IDENTITY)) {
            setIDENTITY(IDENTITY);
        }
        String seqFormat = properties.getProperty("seqFormat");
        if (StringUtil.isNotEmpty(seqFormat)) {
            setSeqFormat(seqFormat);
        }
        String catalog = properties.getProperty("catalog");
        if (StringUtil.isNotEmpty(catalog)) {
            setCatalog(catalog);
        }
        String schema = properties.getProperty("schema");
        if (StringUtil.isNotEmpty(schema)) {
            setSchema(schema);
        }
        String ORDER = properties.getProperty("ORDER");
        if (StringUtil.isNotEmpty(ORDER)) {
            setOrder(ORDER);
        }
        String notEmpty = properties.getProperty("notEmpty");
        if (StringUtil.isNotEmpty(notEmpty)) {
            this.notEmpty = notEmpty.equalsIgnoreCase("TRUE");
        }
        String styleStr = properties.getProperty(AbstractHtmlElementTag.STYLE_ATTRIBUTE);
        if (StringUtil.isNotEmpty(styleStr)) {
            try {
                this.style = Style.valueOf(styleStr);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(styleStr + "不是合法的Style值!");
            }
        } else {
            this.style = Style.camelhump;
        }
    }
}
