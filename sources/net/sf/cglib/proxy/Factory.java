package net.sf.cglib.proxy;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Factory.class */
public interface Factory {
    Object newInstance(Callback callback);

    Object newInstance(Callback[] callbackArr);

    Object newInstance(Class[] clsArr, Object[] objArr, Callback[] callbackArr);

    Callback getCallback(int i);

    void setCallback(int i, Callback callback);

    void setCallbacks(Callback[] callbackArr);

    Callback[] getCallbacks();
}
