package org.aspectj.bridge;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/IProgressListener.class */
public interface IProgressListener {
    void setText(String str);

    void setProgress(double d);

    void setCancelledRequested(boolean z);

    boolean isCancelledRequested();
}
