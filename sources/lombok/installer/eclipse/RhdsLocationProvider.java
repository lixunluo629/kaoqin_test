package lombok.installer.eclipse;

import java.util.Collections;

/* loaded from: lombok-1.16.22.jar:lombok/installer/eclipse/RhdsLocationProvider.SCL.lombok */
public class RhdsLocationProvider extends EclipseProductLocationProvider {
    private static final EclipseProductDescriptor RHDS = new StandardProductDescriptor("Red Hat JBoss Developer Studio", "devstudio", "studio", RhdsLocationProvider.class.getResource("rhds.png"), Collections.emptySet());

    public RhdsLocationProvider() {
        super(RHDS);
    }
}
