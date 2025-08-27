package org.bouncycastle.util.test;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/test/SimpleTestResult.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/test/SimpleTestResult.class */
public class SimpleTestResult implements TestResult {
    private static final String SEPARATOR = System.getProperty("line.separator");
    private boolean success;
    private String message;
    private Throwable exception;

    public SimpleTestResult(boolean z, String str) {
        this.success = z;
        this.message = str;
    }

    public SimpleTestResult(boolean z, String str, Throwable th) {
        this.success = z;
        this.message = str;
        this.exception = th;
    }

    public static TestResult successful(Test test, String str) {
        return new SimpleTestResult(true, test.getName() + ": " + str);
    }

    public static TestResult failed(Test test, String str) {
        return new SimpleTestResult(false, test.getName() + ": " + str);
    }

    public static TestResult failed(Test test, String str, Throwable th) {
        return new SimpleTestResult(false, test.getName() + ": " + str, th);
    }

    public static TestResult failed(Test test, String str, Object obj, Object obj2) {
        return failed(test, str + SEPARATOR + "Expected: " + obj + SEPARATOR + "Found   : " + obj2);
    }

    public static String failedMessage(String str, String str2, String str3, String str4) {
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.append(" failing ").append(str2);
        stringBuffer.append(SEPARATOR).append("    expected: ").append(str3);
        stringBuffer.append(SEPARATOR).append("    got     : ").append(str4);
        return stringBuffer.toString();
    }

    @Override // org.bouncycastle.util.test.TestResult
    public boolean isSuccessful() {
        return this.success;
    }

    @Override // org.bouncycastle.util.test.TestResult
    public String toString() {
        return this.message;
    }

    @Override // org.bouncycastle.util.test.TestResult
    public Throwable getException() {
        return this.exception;
    }
}
