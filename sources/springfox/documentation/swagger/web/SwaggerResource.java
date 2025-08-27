package springfox.documentation.swagger.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ComparisonChain;

/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/web/SwaggerResource.class */
public class SwaggerResource implements Comparable<SwaggerResource> {
    private String name;
    private String location;
    private String swaggerVersion;

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("location")
    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("swaggerVersion")
    public String getSwaggerVersion() {
        return this.swaggerVersion;
    }

    public void setSwaggerVersion(String swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
    }

    @Override // java.lang.Comparable
    public int compareTo(SwaggerResource other) {
        return ComparisonChain.start().compare(this.swaggerVersion, other.swaggerVersion).compare(this.name, other.name).result();
    }
}
