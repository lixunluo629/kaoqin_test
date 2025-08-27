package com.mysql.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NetworkResources.class */
class NetworkResources {
    private final Socket mysqlConnection;
    private final InputStream mysqlInput;
    private final OutputStream mysqlOutput;

    protected NetworkResources(Socket mysqlConnection, InputStream mysqlInput, OutputStream mysqlOutput) {
        this.mysqlConnection = mysqlConnection;
        this.mysqlInput = mysqlInput;
        this.mysqlOutput = mysqlOutput;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    protected final void forceClose() {
        /*
            r2 = this;
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> L50
            boolean r0 = com.mysql.jdbc.ExportControlled.isSSLEstablished(r0)     // Catch: java.io.IOException -> L50
            if (r0 != 0) goto L4d
            r0 = r2
            java.io.InputStream r0 = r0.mysqlInput     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L50
            if (r0 == 0) goto L18
            r0 = r2
            java.io.InputStream r0 = r0.mysqlInput     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L50
            r0.close()     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L50
        L18:
            r0 = jsr -> L24
        L1b:
            goto L4d
        L1e:
            r3 = move-exception
            r0 = jsr -> L24
        L22:
            r1 = r3
            throw r1     // Catch: java.io.IOException -> L50
        L24:
            r4 = r0
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> L50
            if (r0 == 0) goto L4b
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> L50
            boolean r0 = r0.isClosed()     // Catch: java.io.IOException -> L50
            if (r0 != 0) goto L4b
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> L50
            boolean r0 = r0.isInputShutdown()     // Catch: java.io.IOException -> L50
            if (r0 != 0) goto L4b
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.lang.UnsupportedOperationException -> L4a java.io.IOException -> L50
            r0.shutdownInput()     // Catch: java.lang.UnsupportedOperationException -> L4a java.io.IOException -> L50
            goto L4b
        L4a:
            r5 = move-exception
        L4b:
            ret r4     // Catch: java.io.IOException -> L50
        L4d:
            goto L51
        L50:
            r3 = move-exception
        L51:
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> La5
            boolean r0 = com.mysql.jdbc.ExportControlled.isSSLEstablished(r0)     // Catch: java.io.IOException -> La5
            if (r0 != 0) goto La2
            r0 = r2
            java.io.OutputStream r0 = r0.mysqlOutput     // Catch: java.lang.Throwable -> L6f java.io.IOException -> La5
            if (r0 == 0) goto L69
            r0 = r2
            java.io.OutputStream r0 = r0.mysqlOutput     // Catch: java.lang.Throwable -> L6f java.io.IOException -> La5
            r0.close()     // Catch: java.lang.Throwable -> L6f java.io.IOException -> La5
        L69:
            r0 = jsr -> L77
        L6c:
            goto La2
        L6f:
            r6 = move-exception
            r0 = jsr -> L77
        L74:
            r1 = r6
            throw r1     // Catch: java.io.IOException -> La5
        L77:
            r7 = r0
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> La5
            if (r0 == 0) goto La0
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> La5
            boolean r0 = r0.isClosed()     // Catch: java.io.IOException -> La5
            if (r0 != 0) goto La0
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> La5
            boolean r0 = r0.isOutputShutdown()     // Catch: java.io.IOException -> La5
            if (r0 != 0) goto La0
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.lang.UnsupportedOperationException -> L9e java.io.IOException -> La5
            r0.shutdownOutput()     // Catch: java.lang.UnsupportedOperationException -> L9e java.io.IOException -> La5
            goto La0
        L9e:
            r8 = move-exception
        La0:
            ret r7     // Catch: java.io.IOException -> La5
        La2:
            goto La6
        La5:
            r3 = move-exception
        La6:
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> Lb7
            if (r0 == 0) goto Lb4
            r0 = r2
            java.net.Socket r0 = r0.mysqlConnection     // Catch: java.io.IOException -> Lb7
            r0.close()     // Catch: java.io.IOException -> Lb7
        Lb4:
            goto Lb8
        Lb7:
            r3 = move-exception
        Lb8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.NetworkResources.forceClose():void");
    }
}
