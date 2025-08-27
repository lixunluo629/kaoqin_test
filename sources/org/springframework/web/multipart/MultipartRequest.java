package org.springframework.web.multipart;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/MultipartRequest.class */
public interface MultipartRequest {
    Iterator<String> getFileNames();

    MultipartFile getFile(String str);

    List<MultipartFile> getFiles(String str);

    Map<String, MultipartFile> getFileMap();

    MultiValueMap<String, MultipartFile> getMultiFileMap();

    String getMultipartContentType(String str);
}
