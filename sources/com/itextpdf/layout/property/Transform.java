package com.itextpdf.layout.property;

import com.itextpdf.kernel.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/Transform.class */
public class Transform {
    private List<SingleTransform> multipleTransform;

    public Transform(int length) {
        this.multipleTransform = new ArrayList(length);
    }

    public void addSingleTransform(SingleTransform singleTransform) {
        this.multipleTransform.add(singleTransform);
    }

    private List<SingleTransform> getMultipleTransform() {
        return this.multipleTransform;
    }

    public static AffineTransform getAffineTransform(Transform t, float width, float height) {
        float value;
        List<SingleTransform> multipleTransform = t.getMultipleTransform();
        AffineTransform affineTransform = new AffineTransform();
        for (int k = multipleTransform.size() - 1; k >= 0; k--) {
            SingleTransform transform = multipleTransform.get(k);
            float[] floats = new float[6];
            for (int i = 0; i < 4; i++) {
                floats[i] = transform.getFloats()[i];
            }
            int i2 = 4;
            while (i2 < 6) {
                int i3 = i2;
                if (transform.getUnitValues()[i2 - 4].getUnitType() == 1) {
                    value = transform.getUnitValues()[i2 - 4].getValue();
                } else {
                    value = (transform.getUnitValues()[i2 - 4].getValue() / 100.0f) * (i2 == 4 ? width : height);
                }
                floats[i3] = value;
                i2++;
            }
            affineTransform.preConcatenate(new AffineTransform(floats));
        }
        return affineTransform;
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/Transform$SingleTransform.class */
    public static class SingleTransform {
        private float a;
        private float b;
        private float c;
        private float d;
        private UnitValue tx;
        private UnitValue ty;

        public SingleTransform() {
            this.a = 1.0f;
            this.b = 0.0f;
            this.c = 0.0f;
            this.d = 1.0f;
            this.tx = new UnitValue(1, 0.0f);
            this.ty = new UnitValue(1, 0.0f);
        }

        public SingleTransform(float a, float b, float c, float d, UnitValue tx, UnitValue ty) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.tx = tx;
            this.ty = ty;
        }

        public float[] getFloats() {
            return new float[]{this.a, this.b, this.c, this.d};
        }

        public UnitValue[] getUnitValues() {
            return new UnitValue[]{this.tx, this.ty};
        }
    }
}
