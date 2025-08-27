package org.apache.poi.xssf.extractor;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.DocumentHelper;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFMap;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.helpers.XSSFSingleXmlCell;
import org.apache.poi.xssf.usermodel.helpers.XSSFXmlColumnPr;
import org.apache.xmlbeans.impl.common.Sax2Dom;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/extractor/XSSFImportFromXML.class */
public class XSSFImportFromXML {
    private final XSSFMap _map;
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) XSSFImportFromXML.class);

    public XSSFImportFromXML(XSSFMap map) {
        this._map = map;
    }

    public void importFromXML(String xmlInputString) throws XPathExpressionException, ParserConfigurationException, SAXException, DOMException, IOException, ParseException {
        DocumentBuilder builder = DocumentHelper.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlInputString.trim())));
        List<XSSFSingleXmlCell> singleXmlCells = this._map.getRelatedSingleXMLCell();
        List<XSSFTable> tables = this._map.getRelatedTables();
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        xpath.setNamespaceContext(new DefaultNamespaceContext(doc));
        for (XSSFSingleXmlCell singleXmlCell : singleXmlCells) {
            STXmlDataType.Enum xmlDataType = singleXmlCell.getXmlDataType();
            String xpathString = singleXmlCell.getXpath();
            Node result = (Node) xpath.evaluate(xpathString, doc, XPathConstants.NODE);
            if (result != null) {
                String textContent = result.getTextContent();
                logger.log(1, "Extracting with xpath " + xpathString + " : value is '" + textContent + "'");
                XSSFCell cell = singleXmlCell.getReferencedCell();
                logger.log(1, "Setting '" + textContent + "' to cell " + cell.getColumnIndex() + "-" + cell.getRowIndex() + " in sheet " + cell.getSheet().getSheetName());
                setCellValue(textContent, cell, xmlDataType);
            }
        }
        for (XSSFTable table : tables) {
            String commonXPath = table.getCommonXpath();
            NodeList result2 = (NodeList) xpath.evaluate(commonXPath, doc, XPathConstants.NODESET);
            int rowOffset = table.getStartCellReference().getRow() + 1;
            int columnOffset = table.getStartCellReference().getCol() - 1;
            for (int i = 0; i < result2.getLength(); i++) {
                Node singleNode = result2.item(i).cloneNode(true);
                for (XSSFXmlColumnPr xmlColumnPr : table.getXmlColumnPrs()) {
                    int localColumnId = (int) xmlColumnPr.getId();
                    int rowId = rowOffset + i;
                    int columnId = columnOffset + localColumnId;
                    String localXPath = xmlColumnPr.getLocalXPath();
                    String localXPath2 = localXPath.substring(localXPath.substring(1).indexOf(47) + 2);
                    String value = (String) xpath.evaluate(localXPath2, singleNode, XPathConstants.STRING);
                    logger.log(1, "Extracting with xpath " + localXPath2 + " : value is '" + value + "'");
                    XSSFRow row = table.getXSSFSheet().getRow(rowId);
                    if (row == null) {
                        row = table.getXSSFSheet().createRow(rowId);
                    }
                    XSSFCell cell2 = row.getCell(columnId);
                    if (cell2 == null) {
                        cell2 = row.createCell(columnId);
                    }
                    logger.log(1, "Setting '" + value + "' to cell " + cell2.getColumnIndex() + "-" + cell2.getRowIndex() + " in sheet " + table.getXSSFSheet().getSheetName());
                    setCellValue(value, cell2, xmlColumnPr.getXmlDataType());
                }
            }
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/extractor/XSSFImportFromXML$DataType.class */
    private enum DataType {
        BOOLEAN(STXmlDataType.BOOLEAN),
        DOUBLE(STXmlDataType.DOUBLE),
        INTEGER(STXmlDataType.INT, STXmlDataType.UNSIGNED_INT, STXmlDataType.INTEGER),
        STRING(STXmlDataType.STRING),
        DATE(STXmlDataType.DATE);

        private Set<STXmlDataType.Enum> xmlDataTypes;

        DataType(STXmlDataType.Enum... xmlDataTypes) {
            this.xmlDataTypes = new HashSet(Arrays.asList(xmlDataTypes));
        }

        public static DataType getDataType(STXmlDataType.Enum xmlDataType) {
            DataType[] arr$ = values();
            for (DataType dataType : arr$) {
                if (dataType.xmlDataTypes.contains(xmlDataType)) {
                    return dataType;
                }
            }
            return null;
        }
    }

    private void setCellValue(String value, XSSFCell cell, STXmlDataType.Enum xmlDataType) throws ParseException {
        DataType type = DataType.getDataType(xmlDataType);
        try {
            if (value.isEmpty() || type == null) {
                cell.setCellValue((String) null);
            } else {
                switch (type) {
                    case BOOLEAN:
                        cell.setCellValue(Boolean.parseBoolean(value));
                        break;
                    case DOUBLE:
                        cell.setCellValue(Double.parseDouble(value));
                        break;
                    case INTEGER:
                        cell.setCellValue(Integer.parseInt(value));
                        break;
                    case DATE:
                        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", LocaleUtil.getUserLocale());
                        Date date = sdf.parse(value);
                        cell.setCellValue(date);
                        if (!DateUtil.isValidExcelDate(cell.getNumericCellValue())) {
                            cell.setCellValue(value);
                            break;
                        }
                        break;
                    case STRING:
                    default:
                        cell.setCellValue(value.trim());
                        break;
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(LocaleUtil.getUserLocale(), "Unable to format value '%s' as %s for cell %s", value, type, new CellReference(cell).formatAsString()));
        } catch (ParseException e2) {
            throw new IllegalArgumentException(String.format(LocaleUtil.getUserLocale(), "Unable to format value '%s' as %s for cell %s", value, type, new CellReference(cell).formatAsString()));
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/extractor/XSSFImportFromXML$DefaultNamespaceContext.class */
    private static final class DefaultNamespaceContext implements NamespaceContext {
        private final Element _docElem;

        public DefaultNamespaceContext(Document doc) {
            this._docElem = doc.getDocumentElement();
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getNamespaceURI(String prefix) {
            return getNamespaceForPrefix(prefix);
        }

        private String getNamespaceForPrefix(String prefix) {
            if (prefix.equals("xml")) {
                return "http://www.w3.org/XML/1998/namespace";
            }
            Node parent = this._docElem;
            while (parent != null) {
                int type = parent.getNodeType();
                if (type == 1) {
                    if (parent.getNodeName().startsWith(prefix + ":")) {
                        return parent.getNamespaceURI();
                    }
                    NamedNodeMap nnm = parent.getAttributes();
                    for (int i = 0; i < nnm.getLength(); i++) {
                        Node attr = nnm.item(i);
                        String aname = attr.getNodeName();
                        boolean isPrefix = aname.startsWith(Sax2Dom.XMLNS_STRING);
                        if (isPrefix || aname.equals("xmlns")) {
                            int index = aname.indexOf(58);
                            String p = isPrefix ? aname.substring(index + 1) : "";
                            if (p.equals(prefix)) {
                                return attr.getNodeValue();
                            }
                        }
                    }
                    parent = parent.getParentNode();
                } else if (type != 5) {
                    return null;
                }
            }
            return null;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator<?> getPrefixes(String val) {
            return null;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String uri) {
            return null;
        }
    }
}
