package org.springframework.jdbc.support.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/xml/SqlXmlObjectMappingHandler.class */
public interface SqlXmlObjectMappingHandler extends SqlXmlHandler {
    Object getXmlAsObject(ResultSet resultSet, String str) throws SQLException;

    Object getXmlAsObject(ResultSet resultSet, int i) throws SQLException;

    SqlXmlValue newMarshallingSqlXmlValue(Object obj);
}
