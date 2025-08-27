package springfox.documentation.service;

import springfox.documentation.schema.ModelRef;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ResponseMessage.class */
public class ResponseMessage {
    private final int code;
    private final String message;
    private final ModelRef responseModel;

    public ResponseMessage(int code, String message, ModelRef responseModel) {
        this.code = code;
        this.message = message;
        this.responseModel = responseModel;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public ModelRef getResponseModel() {
        return this.responseModel;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseMessage that = (ResponseMessage) o;
        return this.code == that.code;
    }

    public int hashCode() {
        return this.code;
    }
}
