package org.apache.catalina.ssi;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/ssi/SSIConditionalState.class */
class SSIConditionalState {
    boolean branchTaken = false;
    int nestingCount = 0;
    boolean processConditionalCommandsOnly = false;

    SSIConditionalState() {
    }
}
