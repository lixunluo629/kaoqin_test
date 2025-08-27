package springfox.documentation.service;

import java.util.List;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ApiDescription.class */
public class ApiDescription {
    private final String path;
    private final String description;
    private final List<Operation> operations;
    private final Boolean hidden;

    public ApiDescription(String path, String description, List<Operation> operations, Boolean hidden) {
        this.path = path;
        this.description = description;
        this.operations = operations;
        this.hidden = hidden;
    }

    public String getPath() {
        return this.path;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Operation> getOperations() {
        return this.operations;
    }

    public Boolean isHidden() {
        return this.hidden;
    }
}
