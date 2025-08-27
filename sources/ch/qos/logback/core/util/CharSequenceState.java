package ch.qos.logback.core.util;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/util/CharSequenceState.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/util/CharSequenceState.class */
class CharSequenceState {
    final char c;
    int occurrences = 1;

    public CharSequenceState(char c) {
        this.c = c;
    }

    void incrementOccurrences() {
        this.occurrences++;
    }
}
