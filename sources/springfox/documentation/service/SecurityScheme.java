package springfox.documentation.service;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/SecurityScheme.class */
public abstract class SecurityScheme {
    protected final String name;
    protected final String type;

    protected SecurityScheme(String name, String type) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
