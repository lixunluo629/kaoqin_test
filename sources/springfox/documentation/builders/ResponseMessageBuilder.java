package springfox.documentation.builders;

import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ResponseMessageBuilder.class */
public class ResponseMessageBuilder {
    private int code;
    private String message;
    private ModelRef responseModel;

    public ResponseMessageBuilder code(int code) {
        this.code = code;
        return this;
    }

    public ResponseMessageBuilder message(String message) {
        this.message = (String) BuilderDefaults.defaultIfAbsent(message, this.message);
        return this;
    }

    public ResponseMessageBuilder responseModel(ModelRef responseModel) {
        this.responseModel = (ModelRef) BuilderDefaults.defaultIfAbsent(responseModel, this.responseModel);
        return this;
    }

    public ResponseMessage build() {
        return new ResponseMessage(this.code, this.message, this.responseModel);
    }
}
