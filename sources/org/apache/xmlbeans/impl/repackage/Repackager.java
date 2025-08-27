package org.apache.xmlbeans.impl.repackage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/repackage/Repackager.class */
public class Repackager {
    private List _fromPackages = new ArrayList();
    private List _toPackages = new ArrayList();
    private Matcher[] _fromMatchers;
    private String[] _toPackageNames;

    public Repackager(String repackageSpecs) {
        boolean swapped;
        List repackages = splitPath(repackageSpecs, ';');
        do {
            swapped = false;
            for (int i = 1; i < repackages.size(); i++) {
                String spec1 = (String) repackages.get(i - 1);
                String spec2 = (String) repackages.get(i);
                if (spec1.indexOf(58) < spec2.indexOf(58)) {
                    repackages.set(i - 1, spec2);
                    repackages.set(i, spec1);
                    swapped = true;
                }
            }
        } while (swapped);
        for (int i2 = 0; i2 < repackages.size(); i2++) {
            String spec = (String) repackages.get(i2);
            int j = spec.indexOf(58);
            if (j < 0 || spec.indexOf(58, j + 1) >= 0) {
                throw new RuntimeException("Illegal repackage specification: " + spec);
            }
            String from = spec.substring(0, j);
            String to = spec.substring(j + 1);
            this._fromPackages.add(splitPath(from, '.'));
            this._toPackages.add(splitPath(to, '.'));
        }
        this._fromMatchers = new Matcher[this._fromPackages.size() * 2];
        this._toPackageNames = new String[this._fromPackages.size() * 2];
        addPatterns('.', 0);
        addPatterns('/', this._fromPackages.size());
    }

    void addPatterns(char sep, int off) {
        for (int i = 0; i < this._fromPackages.size(); i++) {
            List from = (List) this._fromPackages.get(i);
            List to = (List) this._toPackages.get(i);
            String pattern = "";
            for (int j = 0; j < from.size(); j++) {
                if (j > 0) {
                    pattern = pattern + "\\" + sep;
                }
                pattern = pattern + from.get(j);
            }
            String toPackage = "";
            for (int j2 = 0; j2 < to.size(); j2++) {
                if (j2 > 0) {
                    toPackage = toPackage + sep;
                }
                toPackage = toPackage + to.get(j2);
            }
            this._fromMatchers[off + i] = Pattern.compile(pattern).matcher("");
            this._toPackageNames[off + i] = toPackage;
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:7:0x0025 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.StringBuffer repackage(java.lang.StringBuffer r6) {
        /*
            r5 = this;
            r0 = 0
            r7 = r0
            r0 = 0
            r8 = r0
        L4:
            r0 = r8
            r1 = r5
            java.util.regex.Matcher[] r1 = r1._fromMatchers
            int r1 = r1.length
            if (r0 >= r1) goto L60
            r0 = r5
            java.util.regex.Matcher[] r0 = r0._fromMatchers
            r1 = r8
            r0 = r0[r1]
            r9 = r0
            r0 = r9
            r1 = r6
            java.util.regex.Matcher r0 = r0.reset(r1)
            r0 = r9
            boolean r0 = r0.find()
            r10 = r0
        L23:
            r0 = r10
            if (r0 == 0) goto L4b
            r0 = r7
            if (r0 != 0) goto L34
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r1 = r0
            r1.<init>()
            r7 = r0
        L34:
            r0 = r9
            r1 = r7
            r2 = r5
            java.lang.String[] r2 = r2._toPackageNames
            r3 = r8
            r2 = r2[r3]
            java.util.regex.Matcher r0 = r0.appendReplacement(r1, r2)
            r0 = r9
            boolean r0 = r0.find()
            r10 = r0
            goto L23
        L4b:
            r0 = r7
            if (r0 == 0) goto L5a
            r0 = r9
            r1 = r7
            java.lang.StringBuffer r0 = r0.appendTail(r1)
            r0 = r7
            r6 = r0
            r0 = 0
            r7 = r0
        L5a:
            int r8 = r8 + 1
            goto L4
        L60:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.repackage.Repackager.repackage(java.lang.StringBuffer):java.lang.StringBuffer");
    }

    public List getFromPackages() {
        return this._fromPackages;
    }

    public List getToPackages() {
        return this._toPackages;
    }

    public static ArrayList splitPath(String path, char separator) {
        ArrayList components = new ArrayList();
        while (true) {
            int i = path.indexOf(separator);
            if (i < 0) {
                break;
            }
            components.add(path.substring(0, i));
            path = path.substring(i + 1);
        }
        if (path.length() > 0) {
            components.add(path);
        }
        return components;
    }

    public static String dirForPath(String path) {
        return new File(path).getParent();
    }
}
