package org.springframework.jdbc.support.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.w3c.dom.Document;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/xml/Jdbc4SqlXmlHandler.class */
public class Jdbc4SqlXmlHandler implements SqlXmlHandler {
    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public String getXmlAsString(ResultSet rs, String columnName) throws SQLException {
        SQLXML xmlObject = rs.getSQLXML(columnName);
        if (xmlObject != null) {
            return xmlObject.getString();
        }
        return null;
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public String getXmlAsString(ResultSet rs, int columnIndex) throws SQLException {
        SQLXML xmlObject = rs.getSQLXML(columnIndex);
        if (xmlObject != null) {
            return xmlObject.getString();
        }
        return null;
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public InputStream getXmlAsBinaryStream(ResultSet rs, String columnName) throws SQLException {
        SQLXML xmlObject = rs.getSQLXML(columnName);
        if (xmlObject != null) {
            return xmlObject.getBinaryStream();
        }
        return null;
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public InputStream getXmlAsBinaryStream(ResultSet rs, int columnIndex) throws SQLException {
        SQLXML xmlObject = rs.getSQLXML(columnIndex);
        if (xmlObject != null) {
            return xmlObject.getBinaryStream();
        }
        return null;
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public Reader getXmlAsCharacterStream(ResultSet rs, String columnName) throws SQLException {
        SQLXML xmlObject = rs.getSQLXML(columnName);
        if (xmlObject != null) {
            return xmlObject.getCharacterStream();
        }
        return null;
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public Reader getXmlAsCharacterStream(ResultSet rs, int columnIndex) throws SQLException {
        SQLXML xmlObject = rs.getSQLXML(columnIndex);
        if (xmlObject != null) {
            return xmlObject.getCharacterStream();
        }
        return null;
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public Source getXmlAsSource(ResultSet rs, String columnName, Class<? extends Source> sourceClass) throws SQLException {
        SQLXML xmlObject = rs.getSQLXML(columnName);
        if (xmlObject == null) {
            return null;
        }
        return sourceClass != null ? xmlObject.getSource(sourceClass) : xmlObject.getSource(DOMSource.class);
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public Source getXmlAsSource(ResultSet rs, int columnIndex, Class<? extends Source> sourceClass) throws SQLException {
        SQLXML xmlObject = rs.getSQLXML(columnIndex);
        if (xmlObject == null) {
            return null;
        }
        return sourceClass != null ? xmlObject.getSource(sourceClass) : xmlObject.getSource(DOMSource.class);
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public SqlXmlValue newSqlXmlValue(final String value) {
        return new AbstractJdbc4SqlXmlValue() { // from class: org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.AbstractJdbc4SqlXmlValue
            protected void provideXml(SQLXML xmlObject) throws SQLException, IOException {
                xmlObject.setString(value);
            }
        };
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public SqlXmlValue newSqlXmlValue(final XmlBinaryStreamProvider provider) {
        return new AbstractJdbc4SqlXmlValue() { // from class: org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.AbstractJdbc4SqlXmlValue
            protected void provideXml(SQLXML xmlObject) throws SQLException, IOException {
                provider.provideXml(xmlObject.setBinaryStream());
            }
        };
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public SqlXmlValue newSqlXmlValue(final XmlCharacterStreamProvider provider) {
        return new AbstractJdbc4SqlXmlValue() { // from class: org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.AbstractJdbc4SqlXmlValue
            protected void provideXml(SQLXML xmlObject) throws SQLException, IOException {
                provider.provideXml(xmlObject.setCharacterStream());
            }
        };
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public SqlXmlValue newSqlXmlValue(final Class<? extends Result> resultClass, final XmlResultProvider provider) {
        return new AbstractJdbc4SqlXmlValue() { // from class: org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.4
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.AbstractJdbc4SqlXmlValue
            protected void provideXml(SQLXML xmlObject) throws SQLException, IOException {
                provider.provideXml(xmlObject.setResult(resultClass));
            }
        };
    }

    @Override // org.springframework.jdbc.support.xml.SqlXmlHandler
    public SqlXmlValue newSqlXmlValue(final Document document) {
        return new AbstractJdbc4SqlXmlValue() { // from class: org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.5
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler.AbstractJdbc4SqlXmlValue
            protected void provideXml(SQLXML xmlObject) throws SQLException, IOException {
                ((DOMResult) xmlObject.setResult(DOMResult.class)).setNode(document);
            }
        };
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/xml/Jdbc4SqlXmlHandler$AbstractJdbc4SqlXmlValue.class */
    private static abstract class AbstractJdbc4SqlXmlValue implements SqlXmlValue {
        private SQLXML xmlObject;

        protected abstract void provideXml(SQLXML sqlxml) throws SQLException, IOException;

        private AbstractJdbc4SqlXmlValue() {
        }

        @Override // org.springframework.jdbc.support.SqlValue
        public void setValue(PreparedStatement ps, int paramIndex) throws SQLException {
            this.xmlObject = ps.getConnection().createSQLXML();
            try {
                provideXml(this.xmlObject);
                ps.setSQLXML(paramIndex, this.xmlObject);
            } catch (IOException ex) {
                throw new DataAccessResourceFailureException("Failure encountered while providing XML", ex);
            }
        }

        @Override // org.springframework.jdbc.support.SqlValue
        public void cleanup() throws SQLException {
            try {
                this.xmlObject.free();
            } catch (SQLException ex) {
                throw new DataAccessResourceFailureException("Could not free SQLXML object", ex);
            }
        }
    }
}
