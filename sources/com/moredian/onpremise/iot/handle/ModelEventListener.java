package com.moredian.onpremise.iot.handle;

import com.moredian.onpremise.event.ModelTransferEvent;
import com.moredian.onpremise.iot.IOTContext;
import com.moredian.onpremise.model.IOTModelType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/handle/ModelEventListener.class */
public class ModelEventListener extends AbstractIOTEventListener<ModelTransferEvent> {
    protected final Map<Class<?>, ModelEventHandler> handlerMap = new HashMap(5);

    @Override // com.moredian.onpremise.iot.handle.IOTEventListener
    public void handleEvent(IOTContext<ModelTransferEvent> context) throws Exception {
        ModelTransferEvent event = context.getSourceEvent();
        IOTModelType modelType = IOTModelType.from(event.getModelType());
        ModelEventHandler handler = this.handlerMap.get(modelType.clazz());
        handler.handle(event.getModel(), context);
    }

    public void registry(Class<?> type, ModelEventHandler handler) {
        this.handlerMap.put(type, handler);
    }
}
