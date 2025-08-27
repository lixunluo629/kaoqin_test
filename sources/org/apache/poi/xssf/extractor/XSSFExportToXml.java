package org.apache.poi.xssf.extractor;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.DocumentHelper;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFMap;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.helpers.XSSFSingleXmlCell;
import org.apache.poi.xssf.usermodel.helpers.XSSFXmlColumnPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/extractor/XSSFExportToXml.class */
public class XSSFExportToXml implements Comparator<String> {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) XSSFExportToXml.class);
    private XSSFMap map;

    public XSSFExportToXml(XSSFMap map) {
        this.map = map;
    }

    public void exportToXML(OutputStream os, boolean validate) throws IllegalStateException, TransformerException, DOMException, SAXException, TransformerFactoryConfigurationError, IOException, IllegalArgumentException {
        exportToXML(os, "UTF-8", validate);
    }

    public void exportToXML(OutputStream os, String encoding, boolean validate) throws IllegalStateException, TransformerException, DOMException, SAXException, TransformerFactoryConfigurationError, IOException, IllegalArgumentException {
        Element root;
        int tableColumnIndex;
        XSSFCell cell;
        List<XSSFSingleXmlCell> singleXMLCells = this.map.getRelatedSingleXMLCell();
        List<XSSFTable> tables = this.map.getRelatedTables();
        String rootElement = this.map.getCtMap().getRootElement();
        Document doc = DocumentHelper.createDocument();
        if (isNamespaceDeclared()) {
            root = doc.createElementNS(getNamespace(), rootElement);
        } else {
            root = doc.createElementNS("", rootElement);
        }
        doc.appendChild(root);
        List<String> xpaths = new Vector<>();
        Map<String, XSSFSingleXmlCell> singleXmlCellsMappings = new HashMap<>();
        Map<String, XSSFTable> tableMappings = new HashMap<>();
        for (XSSFSingleXmlCell simpleXmlCell : singleXMLCells) {
            xpaths.add(simpleXmlCell.getXpath());
            singleXmlCellsMappings.put(simpleXmlCell.getXpath(), simpleXmlCell);
        }
        for (XSSFTable table : tables) {
            String commonXPath = table.getCommonXpath();
            xpaths.add(commonXPath);
            tableMappings.put(commonXPath, table);
        }
        Collections.sort(xpaths, this);
        for (String xpath : xpaths) {
            XSSFSingleXmlCell simpleXmlCell2 = singleXmlCellsMappings.get(xpath);
            XSSFTable table2 = tableMappings.get(xpath);
            if (!xpath.matches(".*\\[.*")) {
                if (simpleXmlCell2 != null && (cell = simpleXmlCell2.getReferencedCell()) != null) {
                    Node currentNode = getNodeByXPath(xpath, doc.getFirstChild(), doc, false);
                    mapCellOnNode(cell, currentNode);
                    if ("".equals(currentNode.getTextContent()) && currentNode.getParentNode() != null) {
                        currentNode.getParentNode().removeChild(currentNode);
                    }
                }
                if (table2 != null) {
                    List<CTTableColumn> tableColumns = table2.getCTTable().getTableColumns().getTableColumnList();
                    XSSFSheet sheet = table2.getXSSFSheet();
                    int startRow = table2.getStartCellReference().getRow();
                    int startRow2 = startRow + 1;
                    int endRow = table2.getEndCellReference().getRow();
                    for (int i = startRow2; i <= endRow; i++) {
                        XSSFRow row = sheet.getRow(i);
                        Node tableRootNode = getNodeByXPath(table2.getCommonXpath(), doc.getFirstChild(), doc, true);
                        short startColumnIndex = table2.getStartCellReference().getCol();
                        for (int j = startColumnIndex; j <= table2.getEndCellReference().getCol(); j++) {
                            XSSFCell cell2 = row.getCell(j);
                            if (cell2 != null && (tableColumnIndex = j - startColumnIndex) < tableColumns.size()) {
                                CTTableColumn ctTableColumn = tableColumns.get(tableColumnIndex);
                                if (ctTableColumn.getXmlColumnPr() != null) {
                                    XSSFXmlColumnPr pointer = new XSSFXmlColumnPr(table2, ctTableColumn, ctTableColumn.getXmlColumnPr());
                                    String localXPath = pointer.getLocalXPath();
                                    mapCellOnNode(cell2, getNodeByXPath(localXPath, tableRootNode, doc, false));
                                }
                            }
                        }
                    }
                }
            }
        }
        boolean isValid = true;
        if (validate) {
            isValid = isValid(doc);
        }
        if (isValid) {
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty("omit-xml-declaration", CustomBooleanEditor.VALUE_YES);
            trans.setOutputProperty("indent", CustomBooleanEditor.VALUE_YES);
            trans.setOutputProperty("encoding", encoding);
            StreamResult result = new StreamResult(os);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
        }
    }

    private boolean isValid(Document xml) throws SAXException, IOException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Source source = new DOMSource(this.map.getSchema());
            Schema schema = factory.newSchema(source);
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(xml));
            return true;
        } catch (IOException e) {
            LOG.log(7, "document is not valid", e);
            return false;
        }
    }

    private void mapCellOnNode(XSSFCell cell, Node node) throws IllegalStateException, DOMException {
        String value = "";
        switch (cell.getCellTypeEnum()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = value + cell.getBooleanCellValue();
                break;
            case ERROR:
                value = cell.getErrorCellString();
                break;
            case FORMULA:
                if (cell.getCachedFormulaResultTypeEnum() == CellType.STRING) {
                    value = cell.getStringCellValue();
                    break;
                } else if (DateUtil.isCellDateFormatted(cell)) {
                    value = getFormattedDate(cell);
                    break;
                } else {
                    value = value + cell.getNumericCellValue();
                    break;
                }
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = getFormattedDate(cell);
                    break;
                } else {
                    value = value + cell.getRawValue();
                    break;
                }
        }
        if (node instanceof Element) {
            Element currentElement = (Element) node;
            currentElement.setTextContent(value);
        } else {
            node.setNodeValue(value);
        }
    }

    private String removeNamespace(String elementName) {
        return elementName.matches(".*:.*") ? elementName.split(":")[1] : elementName;
    }

    private String getFormattedDate(XSSFCell cell) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        sdf.setTimeZone(LocaleUtil.getUserTimeZone());
        return sdf.format(cell.getDateCellValue());
    }

    private Node getNodeByXPath(String xpath, Node rootNode, Document doc, boolean createMultipleInstances) throws DOMException {
        Node nodeCreateAttribute;
        String[] xpathTokens = xpath.split("/");
        Node currentNode = rootNode;
        for (int i = 2; i < xpathTokens.length; i++) {
            String axisName = removeNamespace(xpathTokens[i]);
            if (!axisName.startsWith("@")) {
                NodeList list = currentNode.getChildNodes();
                Node selectedNode = null;
                if (!createMultipleInstances || i != xpathTokens.length - 1) {
                    selectedNode = selectNode(axisName, list);
                }
                if (selectedNode == null) {
                    selectedNode = createElement(doc, currentNode, axisName);
                }
                nodeCreateAttribute = selectedNode;
            } else {
                nodeCreateAttribute = createAttribute(doc, currentNode, axisName);
            }
            currentNode = nodeCreateAttribute;
        }
        return currentNode;
    }

    private Node createAttribute(Document doc, Node currentNode, String axisName) throws DOMException {
        String attributeName = axisName.substring(1);
        NamedNodeMap attributesMap = currentNode.getAttributes();
        Node attribute = attributesMap.getNamedItem(attributeName);
        if (attribute == null) {
            attribute = doc.createAttributeNS("", attributeName);
            attributesMap.setNamedItem(attribute);
        }
        return attribute;
    }

    private Node createElement(Document doc, Node currentNode, String axisName) throws DOMException {
        Node selectedNode;
        if (isNamespaceDeclared()) {
            selectedNode = doc.createElementNS(getNamespace(), axisName);
        } else {
            selectedNode = doc.createElementNS("", axisName);
        }
        currentNode.appendChild(selectedNode);
        return selectedNode;
    }

    private Node selectNode(String axisName, NodeList list) {
        Node selectedNode = null;
        int j = 0;
        while (true) {
            if (j >= list.getLength()) {
                break;
            }
            Node node = list.item(j);
            if (!node.getNodeName().equals(axisName)) {
                j++;
            } else {
                selectedNode = node;
                break;
            }
        }
        return selectedNode;
    }

    private boolean isNamespaceDeclared() {
        String schemaNamespace = getNamespace();
        return (schemaNamespace == null || schemaNamespace.equals("")) ? false : true;
    }

    private String getNamespace() {
        return this.map.getCTSchema().getNamespace();
    }

    @Override // java.util.Comparator
    public int compare(String leftXpath, String rightXpath) throws DOMException {
        Node xmlSchema = this.map.getSchema();
        String[] leftTokens = leftXpath.split("/");
        String[] rightTokens = rightXpath.split("/");
        int minLength = leftTokens.length < rightTokens.length ? leftTokens.length : rightTokens.length;
        Node localComplexTypeRootNode = xmlSchema;
        for (int i = 1; i < minLength; i++) {
            String leftElementName = leftTokens[i];
            String rightElementName = rightTokens[i];
            if (leftElementName.equals(rightElementName)) {
                localComplexTypeRootNode = getComplexTypeForElement(leftElementName, xmlSchema, localComplexTypeRootNode);
            } else {
                int leftIndex = indexOfElementInComplexType(leftElementName, localComplexTypeRootNode);
                int rightIndex = indexOfElementInComplexType(rightElementName, localComplexTypeRootNode);
                if (leftIndex != -1 && rightIndex != -1) {
                    if (leftIndex < rightIndex) {
                        return -1;
                    }
                    if (leftIndex > rightIndex) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    private int indexOfElementInComplexType(String elementName, Node complexType) {
        if (complexType == null) {
            return -1;
        }
        NodeList list = complexType.getChildNodes();
        int indexOf = -1;
        int i = 0;
        while (true) {
            if (i >= list.getLength()) {
                break;
            }
            Node node = list.item(i);
            if ((node instanceof Element) && node.getLocalName().equals("element")) {
                Node element = getNameOrRefElement(node);
                if (element.getNodeValue().equals(removeNamespace(elementName))) {
                    indexOf = i;
                    break;
                }
            }
            i++;
        }
        return indexOf;
    }

    private Node getNameOrRefElement(Node node) {
        Node returnNode = node.getAttributes().getNamedItem("name");
        if (returnNode != null) {
            return returnNode;
        }
        return node.getAttributes().getNamedItem("ref");
    }

    private Node getComplexTypeForElement(String elementName, Node xmlSchema, Node localComplexTypeRootNode) throws DOMException {
        String elementNameWithoutNamespace = removeNamespace(elementName);
        String complexTypeName = getComplexTypeNameFromChildren(localComplexTypeRootNode, elementNameWithoutNamespace);
        Node complexTypeNode = null;
        if (!"".equals(complexTypeName)) {
            complexTypeNode = getComplexTypeNodeFromSchemaChildren(xmlSchema, null, complexTypeName);
        }
        return complexTypeNode;
    }

    private String getComplexTypeNameFromChildren(Node localComplexTypeRootNode, String elementNameWithoutNamespace) throws DOMException {
        Node complexTypeAttribute;
        if (localComplexTypeRootNode == null) {
            return "";
        }
        NodeList list = localComplexTypeRootNode.getChildNodes();
        String complexTypeName = "";
        int i = 0;
        while (true) {
            if (i >= list.getLength()) {
                break;
            }
            Node node = list.item(i);
            if ((node instanceof Element) && node.getLocalName().equals("element")) {
                Node nameAttribute = getNameOrRefElement(node);
                if (nameAttribute.getNodeValue().equals(elementNameWithoutNamespace) && (complexTypeAttribute = node.getAttributes().getNamedItem("type")) != null) {
                    complexTypeName = complexTypeAttribute.getNodeValue();
                    break;
                }
            }
            i++;
        }
        return complexTypeName;
    }

    private Node getComplexTypeNodeFromSchemaChildren(Node xmlSchema, Node complexTypeNode, String complexTypeName) {
        NodeList complexTypeList = xmlSchema.getChildNodes();
        for (int i = 0; i < complexTypeList.getLength(); i++) {
            Node node = complexTypeList.item(i);
            if ((node instanceof Element) && node.getLocalName().equals("complexType")) {
                Node nameAttribute = getNameOrRefElement(node);
                if (nameAttribute.getNodeValue().equals(complexTypeName)) {
                    NodeList complexTypeChildList = node.getChildNodes();
                    for (int j = 0; j < complexTypeChildList.getLength(); j++) {
                        Node sequence = complexTypeChildList.item(j);
                        if ((sequence instanceof Element) && (sequence.getLocalName().equals("sequence") || sequence.getLocalName().equals("all"))) {
                            complexTypeNode = sequence;
                            break;
                        }
                    }
                    if (complexTypeNode != null) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        return complexTypeNode;
    }
}
