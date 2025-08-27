package org.mybatis.spring.boot.autoconfigure;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.io.VFS;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/* loaded from: mybatis-spring-boot-autoconfigure-1.3.2.jar:org/mybatis/spring/boot/autoconfigure/SpringBootVFS.class */
public class SpringBootVFS extends VFS {
    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());

    @Override // org.apache.ibatis.io.VFS
    public boolean isValid() {
        return true;
    }

    @Override // org.apache.ibatis.io.VFS
    protected List<String> list(URL url, String path) throws IOException {
        Resource[] resources = this.resourceResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + path + "/**/*.class");
        List<String> resourcePaths = new ArrayList<>();
        for (Resource resource : resources) {
            resourcePaths.add(preserveSubpackageName(resource.getURI(), path));
        }
        return resourcePaths;
    }

    private static String preserveSubpackageName(URI uri, String rootPath) {
        String uriStr = uri.toString();
        int start = uriStr.indexOf(rootPath);
        return uriStr.substring(start);
    }
}
