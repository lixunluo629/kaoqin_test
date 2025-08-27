package com.moredian.onpremise.event;

import com.moredian.onpremise.model.IOTModelType;
import com.moredian.onpremise.model.ModelFactory;
import java.nio.ByteBuffer;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/ModelTransferEvent.class */
public class ModelTransferEvent extends IOTEvent {
    private static final long serialVersionUID = -4623593538499136350L;
    private int modelType;
    private String body;
    private Object model;

    @Override // com.moredian.onpremise.event.IOTEvent
    public String toString() {
        return "ModelTransferEvent(super=" + super.toString() + ", modelType=" + getModelType() + ", body=" + getBody() + ")";
    }

    public ModelTransferEvent(int modelType, Object model) {
        super(IOTEventType.MODEL_TRANSFER.getType());
        this.modelType = modelType;
        this.model = model;
        this.body = ModelFactory.toJson(this.model, IOTModelType.from(this.modelType));
    }

    public ModelTransferEvent(EventHeader header, ByteBuffer buffer) {
        super(header, buffer);
    }

    public ModelTransferEvent(byte[] packet) {
        super(packet);
    }

    @Override // com.moredian.onpremise.event.IOTEvent
    protected byte[] buildBody() {
        byte[] parent = super.buildBody();
        int len = parent.length;
        int len2 = len + 4;
        byte[] bodyBuffer = this.body.getBytes(DEFAULT_CHARSET);
        ByteBuffer buffer = ByteBuffer.allocate(len2 + 4 + bodyBuffer.length);
        buffer.put(parent);
        buffer.putInt(this.modelType);
        buffer.putInt(bodyBuffer.length);
        buffer.put(bodyBuffer);
        return buffer.array();
    }

    @Override // com.moredian.onpremise.event.IOTEvent
    protected void parseBody() {
        super.parseBody();
        this.modelType = this.eventBody.getInt();
        this.body = EventDecoder.bytes2String(this.eventBody, this.eventBody.getInt());
    }

    public Object getModel() {
        if (this.model == null) {
            this.model = ModelFactory.toModel(this.body, IOTModelType.from(this.modelType));
        }
        return this.model;
    }

    public int getModelType() {
        return this.modelType;
    }

    public String getBody() {
        return this.body;
    }
}
