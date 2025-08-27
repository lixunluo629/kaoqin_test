package org.apache.coyote;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/RequestGroupInfo.class */
public class RequestGroupInfo {
    private final ArrayList<RequestInfo> processors = new ArrayList<>();
    private long deadMaxTime = 0;
    private long deadProcessingTime = 0;
    private int deadRequestCount = 0;
    private int deadErrorCount = 0;
    private long deadBytesReceived = 0;
    private long deadBytesSent = 0;

    public synchronized void addRequestProcessor(RequestInfo rp) {
        this.processors.add(rp);
    }

    public synchronized void removeRequestProcessor(RequestInfo rp) {
        if (rp != null) {
            if (this.deadMaxTime < rp.getMaxTime()) {
                this.deadMaxTime = rp.getMaxTime();
            }
            this.deadProcessingTime += rp.getProcessingTime();
            this.deadRequestCount += rp.getRequestCount();
            this.deadErrorCount += rp.getErrorCount();
            this.deadBytesReceived += rp.getBytesReceived();
            this.deadBytesSent += rp.getBytesSent();
            this.processors.remove(rp);
        }
    }

    public synchronized long getMaxTime() {
        long maxTime = this.deadMaxTime;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            if (maxTime < rp.getMaxTime()) {
                maxTime = rp.getMaxTime();
            }
        }
        return maxTime;
    }

    public synchronized void setMaxTime(long maxTime) {
        this.deadMaxTime = maxTime;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            rp.setMaxTime(maxTime);
        }
    }

    public synchronized long getProcessingTime() {
        long time = this.deadProcessingTime;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            time += rp.getProcessingTime();
        }
        return time;
    }

    public synchronized void setProcessingTime(long totalTime) {
        this.deadProcessingTime = totalTime;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            rp.setProcessingTime(totalTime);
        }
    }

    public synchronized int getRequestCount() {
        int requestCount = this.deadRequestCount;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            requestCount += rp.getRequestCount();
        }
        return requestCount;
    }

    public synchronized void setRequestCount(int requestCount) {
        this.deadRequestCount = requestCount;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            rp.setRequestCount(requestCount);
        }
    }

    public synchronized int getErrorCount() {
        int requestCount = this.deadErrorCount;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            requestCount += rp.getErrorCount();
        }
        return requestCount;
    }

    public synchronized void setErrorCount(int errorCount) {
        this.deadErrorCount = errorCount;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            rp.setErrorCount(errorCount);
        }
    }

    public synchronized long getBytesReceived() {
        long bytes = this.deadBytesReceived;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            bytes += rp.getBytesReceived();
        }
        return bytes;
    }

    public synchronized void setBytesReceived(long bytesReceived) {
        this.deadBytesReceived = bytesReceived;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            rp.setBytesReceived(bytesReceived);
        }
    }

    public synchronized long getBytesSent() {
        long bytes = this.deadBytesSent;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            bytes += rp.getBytesSent();
        }
        return bytes;
    }

    public synchronized void setBytesSent(long bytesSent) {
        this.deadBytesSent = bytesSent;
        Iterator i$ = this.processors.iterator();
        while (i$.hasNext()) {
            RequestInfo rp = i$.next();
            rp.setBytesSent(bytesSent);
        }
    }

    public void resetCounters() {
        setBytesReceived(0L);
        setBytesSent(0L);
        setRequestCount(0);
        setProcessingTime(0L);
        setMaxTime(0L);
        setErrorCount(0);
    }
}
