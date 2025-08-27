package springfox.documentation.service;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ApiKey.class */
public class ApiKey extends SecurityScheme {
    private final String keyname;
    private final String passAs;

    public ApiKey(String name, String keyname, String passAs) {
        super(name, "apiKey");
        this.keyname = keyname;
        this.passAs = passAs;
    }

    public String getKeyname() {
        return this.keyname;
    }

    public String getPassAs() {
        return this.passAs;
    }
}
