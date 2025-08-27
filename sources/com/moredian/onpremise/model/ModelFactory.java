package com.moredian.onpremise.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/ModelFactory.class */
public class ModelFactory {
    private static final Gson GSON = new GsonBuilder().create();

    public static String toJson(Object model, IOTModelType type) {
        return GSON.toJson(model, type.clazz());
    }

    public static Object toModel(String json, IOTModelType type) {
        return GSON.fromJson(json, (Class) type.clazz());
    }
}
