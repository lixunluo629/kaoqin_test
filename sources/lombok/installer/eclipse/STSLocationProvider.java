package lombok.installer.eclipse;

import java.util.Collections;

/* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/STSLocationProvider.SCL.lombok */
public class STSLocationProvider extends EclipseProductLocationProvider {
    private static final EclipseProductDescriptor STS = new StandardProductDescriptor("STS", "STS", "sts", STSLocationProvider.class.getResource("STS.png"), Collections.singleton("springsource"));

    public STSLocationProvider() {
        super(STS);
    }
}
