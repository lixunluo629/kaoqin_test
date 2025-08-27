package org.apache.xmlbeans;

import java.util.Map;
import org.apache.xmlbeans.SchemaComponent;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaIdentityConstraint.class */
public interface SchemaIdentityConstraint extends SchemaComponent, SchemaAnnotated {
    public static final int CC_KEY = 1;
    public static final int CC_KEYREF = 2;
    public static final int CC_UNIQUE = 3;

    String getSelector();

    Object getSelectorPath();

    String[] getFields();

    Object getFieldPath(int i);

    Map getNSMap();

    int getConstraintCategory();

    SchemaIdentityConstraint getReferencedKey();

    Object getUserData();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaIdentityConstraint$Ref.class */
    public static final class Ref extends SchemaComponent.Ref {
        public Ref(SchemaIdentityConstraint idc) {
            super(idc);
        }

        public Ref(SchemaTypeSystem system, String handle) {
            super(system, handle);
        }

        @Override // org.apache.xmlbeans.SchemaComponent.Ref
        public final int getComponentType() {
            return 5;
        }

        public final SchemaIdentityConstraint get() {
            return (SchemaIdentityConstraint) getComponent();
        }
    }
}
