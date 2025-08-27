package org.apache.poi.hssf.usermodel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ddf.EscherClientDataRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherProperty;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.EscherTextboxRecord;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SubRecord;
import org.apache.poi.hssf.record.TextObjectRecord;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFShapeFactory.class */
public class HSSFShapeFactory {
    public static void createShapeTree(EscherContainerRecord container, EscherAggregate agg, HSSFShapeContainer out, DirectoryNode root) {
        HSSFShape shape;
        if (container.getRecordId() == -4093) {
            ObjRecord obj = null;
            EscherClientDataRecord clientData = (EscherClientDataRecord) ((EscherContainerRecord) container.getChild(0)).getChildById((short) -4079);
            if (null != clientData) {
                obj = (ObjRecord) agg.getShapeToObjMapping().get(clientData);
            }
            HSSFShapeGroup group = new HSSFShapeGroup(container, obj);
            List<EscherContainerRecord> children = container.getChildContainers();
            for (int i = 0; i < children.size(); i++) {
                EscherContainerRecord spContainer = children.get(i);
                if (i != 0) {
                    createShapeTree(spContainer, agg, group, root);
                }
            }
            out.addShape(group);
            return;
        }
        if (container.getRecordId() == -4092) {
            Map<EscherRecord, Record> shapeToObj = agg.getShapeToObjMapping();
            ObjRecord objRecord = null;
            TextObjectRecord txtRecord = null;
            Iterator i$ = container.iterator();
            while (i$.hasNext()) {
                EscherRecord record = i$.next();
                switch (record.getRecordId()) {
                    case EscherTextboxRecord.RECORD_ID /* -4083 */:
                        txtRecord = (TextObjectRecord) shapeToObj.get(record);
                        break;
                    case EscherClientDataRecord.RECORD_ID /* -4079 */:
                        objRecord = (ObjRecord) shapeToObj.get(record);
                        break;
                }
            }
            if (objRecord == null) {
                throw new RecordFormatException("EscherClientDataRecord can't be found.");
            }
            if (isEmbeddedObject(objRecord)) {
                HSSFObjectData objectData = new HSSFObjectData(container, objRecord, root);
                out.addShape(objectData);
                return;
            }
            CommonObjectDataSubRecord cmo = (CommonObjectDataSubRecord) objRecord.getSubRecords().get(0);
            switch (cmo.getObjectType()) {
                case 1:
                    shape = new HSSFSimpleShape(container, objRecord);
                    break;
                case 2:
                    shape = new HSSFSimpleShape(container, objRecord, txtRecord);
                    break;
                case 6:
                    shape = new HSSFTextbox(container, objRecord, txtRecord);
                    break;
                case 8:
                    shape = new HSSFPicture(container, objRecord);
                    break;
                case 20:
                    shape = new HSSFCombobox(container, objRecord);
                    break;
                case 25:
                    shape = new HSSFComment(container, objRecord, txtRecord, agg.getNoteRecordByObj(objRecord));
                    break;
                case 30:
                    EscherOptRecord optRecord = (EscherOptRecord) container.getChildById((short) -4085);
                    if (optRecord == null) {
                        shape = new HSSFSimpleShape(container, objRecord, txtRecord);
                        break;
                    } else {
                        EscherProperty property = optRecord.lookup(325);
                        if (null != property) {
                            shape = new HSSFPolygon(container, objRecord, txtRecord);
                            break;
                        } else {
                            shape = new HSSFSimpleShape(container, objRecord, txtRecord);
                            break;
                        }
                    }
                default:
                    shape = new HSSFSimpleShape(container, objRecord, txtRecord);
                    break;
            }
            out.addShape(shape);
        }
    }

    private static boolean isEmbeddedObject(ObjRecord obj) {
        for (SubRecord sub : obj.getSubRecords()) {
            if (sub instanceof EmbeddedObjectRefSubRecord) {
                return true;
            }
        }
        return false;
    }
}
