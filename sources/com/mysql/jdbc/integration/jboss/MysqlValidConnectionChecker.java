package com.mysql.jdbc.integration.jboss;

import java.io.Serializable;
import org.jboss.resource.adapter.jdbc.ValidConnectionChecker;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/integration/jboss/MysqlValidConnectionChecker.class */
public final class MysqlValidConnectionChecker implements ValidConnectionChecker, Serializable {
    private static final long serialVersionUID = 8909421133577519177L;

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException: Cannot read field "wordsInUse" because "<parameter1>" is null
        	at java.base/java.util.BitSet.or(Unknown Source)
        	at jadx.core.utils.BlockUtils.getPathCross(BlockUtils.java:775)
        	at jadx.core.utils.BlockUtils.getPathCross(BlockUtils.java:850)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.restructureIf(IfRegionMaker.java:183)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.processExcHandler(ExcHandlersRegionMaker.java:144)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:77)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public java.sql.SQLException isValidConnection(java.sql.Connection r4) {
        /*
            r3 = this;
            r0 = 0
            r5 = r0
            r0 = r4
            java.sql.Statement r0 = r0.createStatement()     // Catch: java.sql.SQLException -> L1d java.lang.Throwable -> L27
            r5 = r0
            r0 = r5
        */
        //  java.lang.String r1 = "/* ping */ SELECT 1"
        /*
            java.sql.ResultSet r0 = r0.executeQuery(r1)     // Catch: java.sql.SQLException -> L1d java.lang.Throwable -> L27
            r0.close()     // Catch: java.sql.SQLException -> L1d java.lang.Throwable -> L27
            r0 = 0
            r6 = r0
            r0 = jsr -> L2f
        L1b:
            r1 = r6
            return r1
        L1d:
            r6 = move-exception
            r0 = r6
            r7 = r0
            r0 = jsr -> L2f
        L24:
            r1 = r7
            return r1
        L27:
            r8 = move-exception
            r0 = jsr -> L2f
        L2c:
            r1 = r8
            throw r1
        L2f:
            r9 = r0
            r0 = r5
            if (r0 == 0) goto L40
            r0 = r5
            r0.close()     // Catch: java.sql.SQLException -> L3e
            goto L40
        L3e:
            r10 = move-exception
        L40:
            ret r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.integration.jboss.MysqlValidConnectionChecker.isValidConnection(java.sql.Connection):java.sql.SQLException");
    }
}
