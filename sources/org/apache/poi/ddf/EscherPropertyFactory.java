package org.apache.poi.ddf;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.LittleEndian;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherPropertyFactory.class */
public final class EscherPropertyFactory {
    public List<EscherProperty> createProperties(byte[] data, int offset, short numProperties) {
        EscherProperty escherComplexProperty;
        List<EscherProperty> results = new ArrayList<>();
        int pos = offset;
        for (int i = 0; i < numProperties; i++) {
            short propId = LittleEndian.getShort(data, pos);
            int propData = LittleEndian.getInt(data, pos + 2);
            short propNumber = (short) (propId & 16383);
            boolean isComplex = (propId & Short.MIN_VALUE) != 0;
            byte propertyType = EscherProperties.getPropertyType(propNumber);
            switch (propertyType) {
                case 1:
                    escherComplexProperty = new EscherBoolProperty(propId, propData);
                    break;
                case 2:
                    escherComplexProperty = new EscherRGBProperty(propId, propData);
                    break;
                case 3:
                    escherComplexProperty = new EscherShapePathProperty(propId, propData);
                    break;
                default:
                    if (!isComplex) {
                        escherComplexProperty = new EscherSimpleProperty(propId, propData);
                        break;
                    } else if (propertyType == 5) {
                        escherComplexProperty = new EscherArrayProperty(propId, new byte[propData]);
                        break;
                    } else {
                        escherComplexProperty = new EscherComplexProperty(propId, new byte[propData]);
                        break;
                    }
            }
            EscherProperty ep = escherComplexProperty;
            results.add(ep);
            pos += 6;
        }
        for (EscherProperty p : results) {
            if (p instanceof EscherComplexProperty) {
                if (p instanceof EscherArrayProperty) {
                    pos += ((EscherArrayProperty) p).setArrayData(data, pos);
                } else {
                    byte[] complexData = ((EscherComplexProperty) p).getComplexData();
                    int leftover = data.length - pos;
                    if (leftover < complexData.length) {
                        throw new IllegalStateException("Could not read complex escher property, length was " + complexData.length + ", but had only " + leftover + " bytes left");
                    }
                    System.arraycopy(data, pos, complexData, 0, complexData.length);
                    pos += complexData.length;
                }
            }
        }
        return results;
    }
}
