package org.apache.catalina.core;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.DispatcherType;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.coyote.CloseNowException;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/StandardWrapperValve.class */
final class StandardWrapperValve extends ValveBase {
    private volatile long processingTime;
    private volatile long maxTime;
    private volatile long minTime;
    private final AtomicInteger requestCount;
    private final AtomicInteger errorCount;
    private static final StringManager sm = StringManager.getManager(Constants.Package);

    public StandardWrapperValve() {
        super(true);
        this.minTime = Long.MAX_VALUE;
        this.requestCount = new AtomicInteger(0);
        this.errorCount = new AtomicInteger(0);
    }

    @Override // org.apache.catalina.Valve
    public final void invoke(Request request, Response response) throws ServletException, IOException {
        boolean unavailable = false;
        Throwable throwable = null;
        long t1 = System.currentTimeMillis();
        this.requestCount.incrementAndGet();
        StandardWrapper wrapper = (StandardWrapper) getContainer();
        Servlet servlet = null;
        Context context = (Context) wrapper.getParent();
        if (!context.getState().isAvailable()) {
            response.sendError(503, sm.getString("standardContext.isUnavailable"));
            unavailable = true;
        }
        if (!unavailable && wrapper.isUnavailable()) {
            this.container.getLogger().info(sm.getString("standardWrapper.isUnavailable", wrapper.getName()));
            long available = wrapper.getAvailable();
            if (available > 0 && available < Long.MAX_VALUE) {
                response.setDateHeader("Retry-After", available);
                response.sendError(503, sm.getString("standardWrapper.isUnavailable", wrapper.getName()));
            } else if (available == Long.MAX_VALUE) {
                response.sendError(404, sm.getString("standardWrapper.notFound", wrapper.getName()));
            }
            unavailable = true;
        }
        if (!unavailable) {
            try {
                servlet = wrapper.allocate();
            } catch (UnavailableException e) {
                this.container.getLogger().error(sm.getString("standardWrapper.allocateException", wrapper.getName()), e);
                long available2 = wrapper.getAvailable();
                if (available2 > 0 && available2 < Long.MAX_VALUE) {
                    response.setDateHeader("Retry-After", available2);
                    response.sendError(503, sm.getString("standardWrapper.isUnavailable", wrapper.getName()));
                } else if (available2 == Long.MAX_VALUE) {
                    response.sendError(404, sm.getString("standardWrapper.notFound", wrapper.getName()));
                }
            } catch (ServletException e2) {
                this.container.getLogger().error(sm.getString("standardWrapper.allocateException", wrapper.getName()), StandardWrapper.getRootCause(e2));
                throwable = e2;
                exception(request, response, e2);
            } catch (Throwable e3) {
                ExceptionUtils.handleThrowable(e3);
                this.container.getLogger().error(sm.getString("standardWrapper.allocateException", wrapper.getName()), e3);
                throwable = e3;
                exception(request, response, e3);
                servlet = null;
            }
        }
        MessageBytes requestPathMB = request.getRequestPathMB();
        DispatcherType dispatcherType = DispatcherType.REQUEST;
        if (request.getDispatcherType() == DispatcherType.ASYNC) {
            dispatcherType = DispatcherType.ASYNC;
        }
        request.setAttribute(Globals.DISPATCHER_TYPE_ATTR, dispatcherType);
        request.setAttribute(Globals.DISPATCHER_REQUEST_PATH_ATTR, requestPathMB);
        ApplicationFilterChain filterChain = ApplicationFilterFactory.createFilterChain(request, wrapper, servlet);
        if (servlet != null && filterChain != null) {
            try {
                if (context.getSwallowOutput()) {
                    try {
                        SystemLogHandler.startCapture();
                        if (request.isAsyncDispatching()) {
                            request.getAsyncContextInternal().doInternalDispatch();
                        } else {
                            filterChain.doFilter(request.getRequest(), response.getResponse());
                        }
                        String log = SystemLogHandler.stopCapture();
                        if (log != null && log.length() > 0) {
                            context.getLogger().info(log);
                        }
                    } catch (Throwable th) {
                        String log2 = SystemLogHandler.stopCapture();
                        if (log2 != null && log2.length() > 0) {
                            context.getLogger().info(log2);
                        }
                        throw th;
                    }
                } else if (request.isAsyncDispatching()) {
                    request.getAsyncContextInternal().doInternalDispatch();
                } else {
                    filterChain.doFilter(request.getRequest(), response.getResponse());
                }
            } catch (ClientAbortException | CloseNowException e4) {
                if (this.container.getLogger().isDebugEnabled()) {
                    this.container.getLogger().debug(sm.getString("standardWrapper.serviceException", wrapper.getName(), context.getName()), e4);
                }
                throwable = e4;
                exception(request, response, e4);
            } catch (IOException e5) {
                this.container.getLogger().error(sm.getString("standardWrapper.serviceException", wrapper.getName(), context.getName()), e5);
                throwable = e5;
                exception(request, response, e5);
            } catch (UnavailableException e6) {
                this.container.getLogger().error(sm.getString("standardWrapper.serviceException", wrapper.getName(), context.getName()), e6);
                wrapper.unavailable(e6);
                long available3 = wrapper.getAvailable();
                if (available3 > 0 && available3 < Long.MAX_VALUE) {
                    response.setDateHeader("Retry-After", available3);
                    response.sendError(503, sm.getString("standardWrapper.isUnavailable", wrapper.getName()));
                } else if (available3 == Long.MAX_VALUE) {
                    response.sendError(404, sm.getString("standardWrapper.notFound", wrapper.getName()));
                }
            } catch (ServletException e7) {
                Throwable rootCause = StandardWrapper.getRootCause(e7);
                if (!(rootCause instanceof ClientAbortException)) {
                    this.container.getLogger().error(sm.getString("standardWrapper.serviceExceptionRoot", wrapper.getName(), context.getName(), e7.getMessage()), rootCause);
                }
                throwable = e7;
                exception(request, response, e7);
            } catch (Throwable e8) {
                ExceptionUtils.handleThrowable(e8);
                this.container.getLogger().error(sm.getString("standardWrapper.serviceException", wrapper.getName(), context.getName()), e8);
                throwable = e8;
                exception(request, response, e8);
            }
        }
        if (filterChain != null) {
            filterChain.release();
        }
        if (servlet != null) {
            try {
                wrapper.deallocate(servlet);
            } catch (Throwable e9) {
                ExceptionUtils.handleThrowable(e9);
                this.container.getLogger().error(sm.getString("standardWrapper.deallocateException", wrapper.getName()), e9);
                if (throwable == null) {
                    throwable = e9;
                    exception(request, response, e9);
                }
            }
        }
        if (servlet != null) {
            try {
                if (wrapper.getAvailable() == Long.MAX_VALUE) {
                    wrapper.unload();
                }
            } catch (Throwable e10) {
                ExceptionUtils.handleThrowable(e10);
                this.container.getLogger().error(sm.getString("standardWrapper.unloadException", wrapper.getName()), e10);
                if (throwable == null) {
                    exception(request, response, e10);
                }
            }
        }
        long t2 = System.currentTimeMillis();
        long time = t2 - t1;
        this.processingTime += time;
        if (time > this.maxTime) {
            this.maxTime = time;
        }
        if (time < this.minTime) {
            this.minTime = time;
        }
    }

    private void exception(Request request, Response response, Throwable exception) throws IOException {
        request.setAttribute("javax.servlet.error.exception", exception);
        response.setStatus(500);
        response.setError();
    }

    public long getProcessingTime() {
        return this.processingTime;
    }

    public long getMaxTime() {
        return this.maxTime;
    }

    public long getMinTime() {
        return this.minTime;
    }

    public int getRequestCount() {
        return this.requestCount.get();
    }

    public int getErrorCount() {
        return this.errorCount.get();
    }

    public void incrementErrorCount() {
        this.errorCount.incrementAndGet();
    }

    @Override // org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleMBeanBase, org.apache.catalina.util.LifecycleBase
    protected void initInternal() throws LifecycleException {
    }
}
