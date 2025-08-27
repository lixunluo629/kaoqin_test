package org.apache.poi.hssf.usermodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ddf.DefaultEscherRecordFactory;
import org.apache.poi.ddf.EscherBoolProperty;
import org.apache.poi.ddf.EscherChildAnchorRecord;
import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherClientDataRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.EscherSpRecord;
import org.apache.poi.ddf.EscherSpgrRecord;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.EndSubRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.GroupMarkerSubRecord;
import org.apache.poi.hssf.record.ObjRecord;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFShapeGroup.class */
public class HSSFShapeGroup extends HSSFShape implements HSSFShapeContainer {
    private final List<HSSFShape> shapes;
    private EscherSpgrRecord _spgrRecord;

    public HSSFShapeGroup(EscherContainerRecord spgrContainer, ObjRecord objRecord) {
        super(spgrContainer, objRecord);
        this.shapes = new ArrayList();
        EscherContainerRecord spContainer = spgrContainer.getChildContainers().get(0);
        this._spgrRecord = (EscherSpgrRecord) spContainer.getChild(0);
        for (EscherRecord ch2 : spContainer.getChildRecords()) {
            switch (ch2.getRecordId()) {
                case EscherChildAnchorRecord.RECORD_ID /* -4081 */:
                    this.anchor = new HSSFChildAnchor((EscherChildAnchorRecord) ch2);
                    break;
                case EscherClientAnchorRecord.RECORD_ID /* -4080 */:
                    this.anchor = new HSSFClientAnchor((EscherClientAnchorRecord) ch2);
                    break;
            }
        }
    }

    public HSSFShapeGroup(HSSFShape parent, HSSFAnchor anchor) {
        super(parent, anchor);
        this.shapes = new ArrayList();
        this._spgrRecord = (EscherSpgrRecord) ((EscherContainerRecord) getEscherContainer().getChild(0)).getChildById((short) -4087);
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShape
    protected EscherContainerRecord createSpContainer() {
        EscherContainerRecord escherContainerRecord = new EscherContainerRecord();
        EscherContainerRecord spContainer = new EscherContainerRecord();
        EscherSpgrRecord spgr = new EscherSpgrRecord();
        EscherSpRecord sp = new EscherSpRecord();
        EscherOptRecord opt = new EscherOptRecord();
        EscherClientDataRecord clientData = new EscherClientDataRecord();
        escherContainerRecord.setRecordId((short) -4093);
        escherContainerRecord.setOptions((short) 15);
        spContainer.setRecordId((short) -4092);
        spContainer.setOptions((short) 15);
        spgr.setRecordId((short) -4087);
        spgr.setOptions((short) 1);
        spgr.setRectX1(0);
        spgr.setRectY1(0);
        spgr.setRectX2(1023);
        spgr.setRectY2(255);
        sp.setRecordId((short) -4086);
        sp.setOptions((short) 2);
        if (getAnchor() instanceof HSSFClientAnchor) {
            sp.setFlags(513);
        } else {
            sp.setFlags(515);
        }
        opt.setRecordId((short) -4085);
        opt.setOptions((short) 35);
        opt.addEscherProperty(new EscherBoolProperty((short) 127, 262148));
        opt.addEscherProperty(new EscherBoolProperty((short) 959, 524288));
        EscherRecord anchor = getAnchor().getEscherAnchor();
        clientData.setRecordId((short) -4079);
        clientData.setOptions((short) 0);
        escherContainerRecord.addChildRecord(spContainer);
        spContainer.addChildRecord(spgr);
        spContainer.addChildRecord(sp);
        spContainer.addChildRecord(opt);
        spContainer.addChildRecord(anchor);
        spContainer.addChildRecord(clientData);
        return escherContainerRecord;
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShape
    protected ObjRecord createObjRecord() {
        ObjRecord obj = new ObjRecord();
        CommonObjectDataSubRecord cmo = new CommonObjectDataSubRecord();
        cmo.setObjectType((short) 0);
        cmo.setLocked(true);
        cmo.setPrintable(true);
        cmo.setAutofill(true);
        cmo.setAutoline(true);
        GroupMarkerSubRecord gmo = new GroupMarkerSubRecord();
        EndSubRecord end = new EndSubRecord();
        obj.addSubRecord(cmo);
        obj.addSubRecord(gmo);
        obj.addSubRecord(end);
        return obj;
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShape
    protected void afterRemove(HSSFPatriarch patriarch) {
        patriarch.getBoundAggregate().removeShapeToObjRecord(getEscherContainer().getChildContainers().get(0).getChildById((short) -4079));
        for (int i = 0; i < this.shapes.size(); i++) {
            HSSFShape shape = this.shapes.get(i);
            removeShape(shape);
            shape.afterRemove(getPatriarch());
        }
        this.shapes.clear();
    }

    private void onCreate(HSSFShape shape) {
        EscherSpRecord sp;
        if (getPatriarch() != null) {
            EscherContainerRecord spContainer = shape.getEscherContainer();
            int shapeId = getPatriarch().newShapeId();
            shape.setShapeId(shapeId);
            getEscherContainer().addChildRecord(spContainer);
            shape.afterInsert(getPatriarch());
            if (shape instanceof HSSFShapeGroup) {
                sp = (EscherSpRecord) shape.getEscherContainer().getChildContainers().get(0).getChildById((short) -4086);
            } else {
                sp = (EscherSpRecord) shape.getEscherContainer().getChildById((short) -4086);
            }
            sp.setFlags(sp.getFlags() | 2);
        }
    }

    public HSSFShapeGroup createGroup(HSSFChildAnchor anchor) {
        HSSFShapeGroup group = new HSSFShapeGroup(this, anchor);
        group.setParent(this);
        group.setAnchor(anchor);
        this.shapes.add(group);
        onCreate(group);
        return group;
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public void addShape(HSSFShape shape) {
        shape.setPatriarch(getPatriarch());
        shape.setParent(this);
        this.shapes.add(shape);
    }

    public HSSFSimpleShape createShape(HSSFChildAnchor anchor) {
        HSSFSimpleShape shape = new HSSFSimpleShape(this, anchor);
        shape.setParent(this);
        shape.setAnchor(anchor);
        this.shapes.add(shape);
        onCreate(shape);
        EscherSpRecord sp = (EscherSpRecord) shape.getEscherContainer().getChildById((short) -4086);
        if (shape.getAnchor().isHorizontallyFlipped()) {
            sp.setFlags(sp.getFlags() | 64);
        }
        if (shape.getAnchor().isVerticallyFlipped()) {
            sp.setFlags(sp.getFlags() | 128);
        }
        return shape;
    }

    public HSSFTextbox createTextbox(HSSFChildAnchor anchor) {
        HSSFTextbox shape = new HSSFTextbox(this, anchor);
        shape.setParent(this);
        shape.setAnchor(anchor);
        this.shapes.add(shape);
        onCreate(shape);
        return shape;
    }

    public HSSFPolygon createPolygon(HSSFChildAnchor anchor) {
        HSSFPolygon shape = new HSSFPolygon(this, anchor);
        shape.setParent(this);
        shape.setAnchor(anchor);
        this.shapes.add(shape);
        onCreate(shape);
        return shape;
    }

    public HSSFPicture createPicture(HSSFChildAnchor anchor, int pictureIndex) {
        HSSFPicture shape = new HSSFPicture(this, anchor);
        shape.setParent(this);
        shape.setAnchor(anchor);
        shape.setPictureIndex(pictureIndex);
        this.shapes.add(shape);
        onCreate(shape);
        EscherSpRecord sp = (EscherSpRecord) shape.getEscherContainer().getChildById((short) -4086);
        if (shape.getAnchor().isHorizontallyFlipped()) {
            sp.setFlags(sp.getFlags() | 64);
        }
        if (shape.getAnchor().isVerticallyFlipped()) {
            sp.setFlags(sp.getFlags() | 128);
        }
        return shape;
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public List<HSSFShape> getChildren() {
        return Collections.unmodifiableList(this.shapes);
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public void setCoordinates(int x1, int y1, int x2, int y2) {
        this._spgrRecord.setRectX1(x1);
        this._spgrRecord.setRectX2(x2);
        this._spgrRecord.setRectY1(y1);
        this._spgrRecord.setRectY2(y2);
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public void clear() {
        ArrayList<HSSFShape> copy = new ArrayList<>(this.shapes);
        Iterator i$ = copy.iterator();
        while (i$.hasNext()) {
            HSSFShape shape = i$.next();
            removeShape(shape);
        }
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public int getX1() {
        return this._spgrRecord.getRectX1();
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public int getY1() {
        return this._spgrRecord.getRectY1();
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public int getX2() {
        return this._spgrRecord.getRectX2();
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public int getY2() {
        return this._spgrRecord.getRectY2();
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShape
    public int countOfAllChildren() {
        int count = this.shapes.size();
        for (HSSFShape shape : this.shapes) {
            count += shape.countOfAllChildren();
        }
        return count;
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShape
    void afterInsert(HSSFPatriarch patriarch) {
        EscherAggregate agg = patriarch.getBoundAggregate();
        EscherContainerRecord containerRecord = (EscherContainerRecord) getEscherContainer().getChildById((short) -4092);
        agg.associateShapeToObjRecord(containerRecord.getChildById((short) -4079), getObjRecord());
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShape
    void setShapeId(int shapeId) {
        EscherContainerRecord containerRecord = (EscherContainerRecord) getEscherContainer().getChildById((short) -4092);
        EscherSpRecord spRecord = (EscherSpRecord) containerRecord.getChildById((short) -4086);
        spRecord.setShapeId(shapeId);
        CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord) getObjRecord().getSubRecords().get(0);
        cod.setObjectId((short) (shapeId % 1024));
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShape
    int getShapeId() {
        EscherContainerRecord containerRecord = (EscherContainerRecord) getEscherContainer().getChildById((short) -4092);
        return ((EscherSpRecord) containerRecord.getChildById((short) -4086)).getShapeId();
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShape
    protected HSSFShape cloneShape() {
        throw new IllegalStateException("Use method cloneShape(HSSFPatriarch patriarch)");
    }

    protected HSSFShape cloneShape(HSSFPatriarch patriarch) {
        HSSFShape hSSFShapeCloneShape;
        EscherContainerRecord spgrContainer = new EscherContainerRecord();
        spgrContainer.setRecordId((short) -4093);
        spgrContainer.setOptions((short) 15);
        EscherContainerRecord spContainer = new EscherContainerRecord();
        EscherContainerRecord cont = (EscherContainerRecord) getEscherContainer().getChildById((short) -4092);
        byte[] inSp = cont.serialize();
        spContainer.fillFields(inSp, 0, new DefaultEscherRecordFactory());
        spgrContainer.addChildRecord(spContainer);
        ObjRecord obj = null;
        if (null != getObjRecord()) {
            obj = (ObjRecord) getObjRecord().cloneViaReserialise();
        }
        HSSFShapeGroup group = new HSSFShapeGroup(spgrContainer, obj);
        group.setPatriarch(patriarch);
        for (HSSFShape shape : getChildren()) {
            if (shape instanceof HSSFShapeGroup) {
                hSSFShapeCloneShape = ((HSSFShapeGroup) shape).cloneShape(patriarch);
            } else {
                hSSFShapeCloneShape = shape.cloneShape();
            }
            HSSFShape newShape = hSSFShapeCloneShape;
            group.addShape(newShape);
            group.onCreate(newShape);
        }
        return group;
    }

    @Override // org.apache.poi.hssf.usermodel.HSSFShapeContainer
    public boolean removeShape(HSSFShape shape) {
        boolean isRemoved = getEscherContainer().removeChildRecord(shape.getEscherContainer());
        if (isRemoved) {
            shape.afterRemove(getPatriarch());
            this.shapes.remove(shape);
        }
        return isRemoved;
    }

    @Override // java.lang.Iterable
    public Iterator<HSSFShape> iterator() {
        return this.shapes.iterator();
    }
}
