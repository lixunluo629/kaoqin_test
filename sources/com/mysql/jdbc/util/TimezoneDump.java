package com.mysql.jdbc.util;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/util/TimezoneDump.class */
public class TimezoneDump {
    private static final String DEFAULT_URL = "jdbc:mysql:///test";

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    public static void main(java.lang.String[] r4) throws java.lang.Exception {
        /*
            java.lang.String r0 = "jdbc:mysql:///test"
            r5 = r0
            r0 = r4
            int r0 = r0.length
            r1 = 1
            if (r0 != r1) goto L13
            r0 = r4
            r1 = 0
            r0 = r0[r1]
            if (r0 == 0) goto L13
            r0 = r4
            r1 = 0
            r0 = r0[r1]
            r5 = r0
        L13:
            java.lang.String r0 = "com.mysql.jdbc.Driver"
            java.lang.Class r0 = java.lang.Class.forName(r0)
            java.lang.Object r0 = r0.newInstance()
            r0 = 0
            r6 = r0
            r0 = r5
            java.sql.Connection r0 = java.sql.DriverManager.getConnection(r0)     // Catch: java.lang.Throwable -> L83
            java.sql.Statement r0 = r0.createStatement()     // Catch: java.lang.Throwable -> L83
            java.lang.String r1 = "SHOW VARIABLES LIKE 'timezone'"
            java.sql.ResultSet r0 = r0.executeQuery(r1)     // Catch: java.lang.Throwable -> L83
            r6 = r0
        L2f:
            r0 = r6
            boolean r0 = r0.next()     // Catch: java.lang.Throwable -> L83
            if (r0 == 0) goto L7d
            r0 = r6
            r1 = 2
            java.lang.String r0 = r0.getString(r1)     // Catch: java.lang.Throwable -> L83
            r7 = r0
            java.io.PrintStream r0 = java.lang.System.out     // Catch: java.lang.Throwable -> L83
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L83
            r2 = r1
            r2.<init>()     // Catch: java.lang.Throwable -> L83
            java.lang.String r2 = "MySQL timezone name: "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L83
            r2 = r7
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L83
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L83
            r0.println(r1)     // Catch: java.lang.Throwable -> L83
            r0 = r7
            r1 = 0
            java.lang.String r0 = com.mysql.jdbc.TimeUtil.getCanonicalTimezone(r0, r1)     // Catch: java.lang.Throwable -> L83
            r8 = r0
            java.io.PrintStream r0 = java.lang.System.out     // Catch: java.lang.Throwable -> L83
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L83
            r2 = r1
            r2.<init>()     // Catch: java.lang.Throwable -> L83
            java.lang.String r2 = "Java timezone name: "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L83
            r2 = r8
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L83
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L83
            r0.println(r1)     // Catch: java.lang.Throwable -> L83
            goto L2f
        L7d:
            r0 = jsr -> L8b
        L80:
            goto L99
        L83:
            r9 = move-exception
            r0 = jsr -> L8b
        L88:
            r1 = r9
            throw r1
        L8b:
            r10 = r0
            r0 = r6
            if (r0 == 0) goto L97
            r0 = r6
            r0.close()
        L97:
            ret r10
        L99:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.util.TimezoneDump.main(java.lang.String[]):void");
    }
}
