package org.hyperic.sigar;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/Humidor.class */
public class Humidor {
    private static final Humidor INSTANCE = new Humidor();
    private Object LOCK = new Object();
    private InvocationHandler _handler;
    private SigarProxy _sigar;
    private Sigar _impl;
    private Sigar _inst;
    static Class class$org$hyperic$sigar$Humidor;
    static Class class$org$hyperic$sigar$SigarProxy;

    private Humidor() {
    }

    public Humidor(Sigar sigar) {
        this._impl = sigar;
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/Humidor$MyHandler.class */
    private static class MyHandler implements InvocationHandler {
        private SigarProxy _sigar;
        private Object _lock = new Object();

        public MyHandler(SigarProxy sigar) {
            this._sigar = sigar;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method meth, Object[] args) throws Throwable {
            Object objInvoke;
            try {
                synchronized (this._lock) {
                    objInvoke = meth.invoke(this._sigar, args);
                }
                return objInvoke;
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }

    public SigarProxy getSigar() {
        SigarProxy sigarProxy;
        Class clsClass$;
        Class clsClass$2;
        synchronized (this.LOCK) {
            if (this._sigar == null) {
                if (this._impl == null) {
                    Sigar sigar = new Sigar();
                    this._impl = sigar;
                    this._inst = sigar;
                }
                this._handler = new MyHandler(this._impl);
                if (class$org$hyperic$sigar$Humidor == null) {
                    clsClass$ = class$("org.hyperic.sigar.Humidor");
                    class$org$hyperic$sigar$Humidor = clsClass$;
                } else {
                    clsClass$ = class$org$hyperic$sigar$Humidor;
                }
                ClassLoader classLoader = clsClass$.getClassLoader();
                Class[] clsArr = new Class[1];
                if (class$org$hyperic$sigar$SigarProxy == null) {
                    clsClass$2 = class$("org.hyperic.sigar.SigarProxy");
                    class$org$hyperic$sigar$SigarProxy = clsClass$2;
                } else {
                    clsClass$2 = class$org$hyperic$sigar$SigarProxy;
                }
                clsArr[0] = clsClass$2;
                this._sigar = (SigarProxy) Proxy.newProxyInstance(classLoader, clsArr, this._handler);
            }
            sigarProxy = this._sigar;
        }
        return sigarProxy;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public static Humidor getInstance() {
        return INSTANCE;
    }

    public void close() {
        if (this._inst != null) {
            this._inst.close();
            this._inst = null;
            this._impl = null;
        }
        this._sigar = null;
    }
}
