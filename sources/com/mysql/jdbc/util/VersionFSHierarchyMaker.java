package com.mysql.jdbc.util;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/util/VersionFSHierarchyMaker.class */
public class VersionFSHierarchyMaker {
    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    public static void main(java.lang.String[] r5) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 440
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.util.VersionFSHierarchyMaker.main(java.lang.String[]):void");
    }

    public static String removeWhitespaceChars(String input) {
        if (input == null) {
            return input;
        }
        int strLen = input.length();
        StringBuilder output = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char c = input.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c)) {
                if (Character.isWhitespace(c)) {
                    output.append("_");
                } else {
                    output.append(".");
                }
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }

    private static void usage() {
        System.err.println("Creates a fs hierarchy representing MySQL version, OS version and JVM version.");
        System.err.println("Stores the full path as 'outputDirectory' property in file 'directoryPropPath'");
        System.err.println();
        System.err.println("Usage: java VersionFSHierarchyMaker baseDirectory directoryPropPath jdbcUrlIter");
    }
}
