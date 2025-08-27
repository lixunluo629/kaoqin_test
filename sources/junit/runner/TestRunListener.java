package junit.runner;

/* loaded from: junit-4.12.jar:junit/runner/TestRunListener.class */
public interface TestRunListener {
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_FAILURE = 2;

    void testRunStarted(String str, int i);

    void testRunEnded(long j);

    void testRunStopped(long j);

    void testStarted(String str);

    void testEnded(String str);

    void testFailed(int i, String str, String str2);
}
