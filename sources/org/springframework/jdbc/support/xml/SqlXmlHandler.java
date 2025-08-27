package org.springframework.jdbc.support.xml;

import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Document;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/xml/SqlXmlHandler.class */
public interface SqlXmlHandler {
    String getXmlAsString(ResultSet resultSet, String str) throws SQLException;

    String getXmlAsString(ResultSet resultSet, int i) throws SQLException;

    InputStream getXmlAsBinaryStream(ResultSet resultSet, String str) throws SQLException;

    InputStream getXmlAsBinaryStream(ResultSet resultSet, int i) throws SQLException;

    Reader getXmlAsCharacterStream(ResultSet resultSet, String str) throws SQLException;

    Reader getXmlAsCharacterStream(ResultSet resultSet, int i) throws SQLException;

    Source getXmlAsSource(ResultSet resultSet, String str, Class<? extends Source> cls) throws SQLException;

    Source getXmlAsSource(ResultSet resultSet, int i, Class<? extends Source> cls) throws SQLException;

    SqlXmlValue newSqlXmlValue(String str);

    SqlXmlValue newSqlXmlValue(XmlBinaryStreamProvider xmlBinaryStreamProvider);

    SqlXmlValue newSqlXmlValue(XmlCharacterStreamProvider xmlCharacterStreamProvider);

    SqlXmlValue newSqlXmlValue(Class<? extends Result> cls, XmlResultProvider xmlResultProvider);

    SqlXmlValue newSqlXmlValue(Document document);
}
