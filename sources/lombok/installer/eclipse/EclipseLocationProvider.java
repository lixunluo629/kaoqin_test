package lombok.installer.eclipse;

import java.util.Collections;

/* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/EclipseLocationProvider.SCL.lombok */
public class EclipseLocationProvider extends EclipseProductLocationProvider {
    private static final EclipseProductDescriptor ECLIPSE = new StandardProductDescriptor("Eclipse", "eclipse", "eclipse", EclipseLocationProvider.class.getResource("eclipse.png"), Collections.emptySet());

    public EclipseLocationProvider() {
        super(ECLIPSE);
    }
}
