package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.HttpRequest;
import java.nio.charset.Charset;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/HttpDataFactory.class */
public interface HttpDataFactory {
    void setMaxLimit(long j);

    Attribute createAttribute(HttpRequest httpRequest, String str);

    Attribute createAttribute(HttpRequest httpRequest, String str, long j);

    Attribute createAttribute(HttpRequest httpRequest, String str, String str2);

    FileUpload createFileUpload(HttpRequest httpRequest, String str, String str2, String str3, String str4, Charset charset, long j);

    void removeHttpDataFromClean(HttpRequest httpRequest, InterfaceHttpData interfaceHttpData);

    void cleanRequestHttpData(HttpRequest httpRequest);

    void cleanAllHttpData();

    @Deprecated
    void cleanRequestHttpDatas(HttpRequest httpRequest);

    @Deprecated
    void cleanAllHttpDatas();
}
