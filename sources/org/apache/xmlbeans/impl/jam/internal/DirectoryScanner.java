package org.apache.xmlbeans.impl.jam.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/DirectoryScanner.class */
public class DirectoryScanner {
    private File mRoot;
    private JamLogger mLogger;
    private String[] mIncludes;
    private String[] mExcludes;
    private Vector mFilesIncluded;
    private Vector mDirsIncluded;
    private boolean mCaseSensitive = true;
    private List mIncludeList = null;
    private List mExcludeList = null;
    private boolean mIsDirty = false;
    private String[] mIncludedFilesCache = null;

    public DirectoryScanner(File dirToScan, JamLogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("null logger");
        }
        this.mLogger = logger;
        this.mRoot = dirToScan;
    }

    public void include(String pattern) {
        if (this.mIncludeList == null) {
            this.mIncludeList = new ArrayList();
        }
        this.mIncludeList.add(pattern);
        this.mIsDirty = true;
    }

    public void exclude(String pattern) {
        if (this.mExcludeList == null) {
            this.mExcludeList = new ArrayList();
        }
        this.mExcludeList.add(pattern);
        this.mIsDirty = true;
    }

    public String[] getIncludedFiles() throws IllegalStateException, IOException {
        if (!this.mIsDirty && this.mIncludedFilesCache != null) {
            return this.mIncludedFilesCache;
        }
        if (this.mIncludeList != null) {
            String[] inc = new String[this.mIncludeList.size()];
            this.mIncludeList.toArray(inc);
            setIncludes(inc);
        } else {
            setIncludes(null);
        }
        if (this.mExcludeList != null) {
            String[] exc = new String[this.mExcludeList.size()];
            this.mExcludeList.toArray(exc);
            setExcludes(exc);
        } else {
            setExcludes(null);
        }
        scan();
        this.mIncludedFilesCache = new String[this.mFilesIncluded.size()];
        this.mFilesIncluded.copyInto(this.mIncludedFilesCache);
        return this.mIncludedFilesCache;
    }

    public void setDirty() {
        this.mIsDirty = true;
    }

    public File getRoot() {
        return this.mRoot;
    }

    private void setIncludes(String[] includes) {
        if (includes == null) {
            this.mIncludes = null;
            return;
        }
        this.mIncludes = new String[includes.length];
        for (int i = 0; i < includes.length; i++) {
            String pattern = includes[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
            if (pattern.endsWith(File.separator)) {
                pattern = pattern + SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS;
            }
            this.mIncludes[i] = pattern;
        }
    }

    private void setExcludes(String[] excludes) {
        if (excludes == null) {
            this.mExcludes = null;
            return;
        }
        this.mExcludes = new String[excludes.length];
        for (int i = 0; i < excludes.length; i++) {
            String pattern = excludes[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
            if (pattern.endsWith(File.separator)) {
                pattern = pattern + SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS;
            }
            this.mExcludes[i] = pattern;
        }
    }

    private void scan() throws IllegalStateException, IOException {
        if (this.mIncludes == null) {
            this.mIncludes = new String[1];
            this.mIncludes[0] = SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS;
        }
        if (this.mExcludes == null) {
            this.mExcludes = new String[0];
        }
        this.mFilesIncluded = new Vector();
        this.mDirsIncluded = new Vector();
        if (isIncluded("") && !isExcluded("")) {
            this.mDirsIncluded.addElement("");
        }
        scandir(this.mRoot, "", true);
    }

    private void scandir(File dir, String vpath, boolean fast) throws IOException {
        if (this.mLogger.isVerbose(this)) {
            this.mLogger.verbose("[DirectoryScanner] scanning dir " + dir + " for '" + vpath + "'");
        }
        String[] newfiles = dir.list();
        if (newfiles == null) {
            throw new IOException("IO error scanning directory " + dir.getAbsolutePath());
        }
        for (int i = 0; i < newfiles.length; i++) {
            String name = vpath + newfiles[i];
            File file = new File(dir, newfiles[i]);
            if (file.isDirectory()) {
                if (isIncluded(name) && !isExcluded(name)) {
                    this.mDirsIncluded.addElement(name);
                    if (this.mLogger.isVerbose(this)) {
                        this.mLogger.verbose("...including dir " + name);
                    }
                    scandir(file, name + File.separator, fast);
                } else if (couldHoldIncluded(name)) {
                    scandir(file, name + File.separator, fast);
                }
            } else if (file.isFile() && isIncluded(name)) {
                if (!isExcluded(name)) {
                    this.mFilesIncluded.addElement(name);
                    if (this.mLogger.isVerbose(this)) {
                        this.mLogger.verbose("...including " + name + " under '" + dir);
                    }
                } else if (this.mLogger.isVerbose(this)) {
                    this.mLogger.verbose("...EXCLUDING " + name + " under '" + dir);
                }
            }
        }
    }

    private boolean isIncluded(String name) {
        for (int i = 0; i < this.mIncludes.length; i++) {
            if (matchPath(this.mIncludes[i], name, this.mCaseSensitive)) {
                return true;
            }
        }
        return false;
    }

    private boolean couldHoldIncluded(String name) {
        for (int i = 0; i < this.mIncludes.length; i++) {
            if (matchPatternStart(this.mIncludes[i], name, this.mCaseSensitive)) {
                return true;
            }
        }
        return false;
    }

    private boolean isExcluded(String name) {
        for (int i = 0; i < this.mExcludes.length; i++) {
            if (matchPath(this.mExcludes[i], name, this.mCaseSensitive)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchPatternStart(String pattern, String str, boolean mCaseSensitive) {
        if (str.startsWith(File.separator) != pattern.startsWith(File.separator)) {
            return false;
        }
        Vector patDirs = tokenizePath(pattern);
        Vector strDirs = tokenizePath(str);
        int patIdxStart = 0;
        int patIdxEnd = patDirs.size() - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.size() - 1;
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = (String) patDirs.elementAt(patIdxStart);
            if (patDir.equals(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS)) {
                break;
            }
            if (!match(patDir, (String) strDirs.elementAt(strIdxStart), mCaseSensitive)) {
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart <= strIdxEnd && patIdxStart > patIdxEnd) {
            return false;
        }
        return true;
    }

    private static boolean matchPath(String pattern, String str, boolean mCaseSensitive) {
        if (str.startsWith(File.separator) != pattern.startsWith(File.separator)) {
            return false;
        }
        Vector patDirs = tokenizePath(pattern);
        Vector strDirs = tokenizePath(str);
        int patIdxStart = 0;
        int patIdxEnd = patDirs.size() - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.size() - 1;
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = (String) patDirs.elementAt(patIdxStart);
            if (patDir.equals(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS)) {
                break;
            }
            if (!match(patDir, (String) strDirs.elementAt(strIdxStart), mCaseSensitive)) {
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (!patDirs.elementAt(i).equals(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS)) {
                    return false;
                }
            }
            return true;
        }
        if (patIdxStart > patIdxEnd) {
            return false;
        }
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir2 = (String) patDirs.elementAt(patIdxEnd);
            if (patDir2.equals(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS)) {
                break;
            }
            if (!match(patDir2, (String) strDirs.elementAt(strIdxEnd), mCaseSensitive)) {
                return false;
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i2 = patIdxStart; i2 <= patIdxEnd; i2++) {
                if (!patDirs.elementAt(i2).equals(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS)) {
                    return false;
                }
            }
            return true;
        }
        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            int i3 = patIdxStart + 1;
            while (true) {
                if (i3 > patIdxEnd) {
                    break;
                }
                if (!patDirs.elementAt(i3).equals(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS)) {
                    i3++;
                } else {
                    patIdxTmp = i3;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                patIdxStart++;
            } else {
                int patLength = (patIdxTmp - patIdxStart) - 1;
                int strLength = (strIdxEnd - strIdxStart) + 1;
                int foundIdx = -1;
                int i4 = 0;
                while (true) {
                    if (i4 > strLength - patLength) {
                        break;
                    }
                    for (int j = 0; j < patLength; j++) {
                        String subPat = (String) patDirs.elementAt(patIdxStart + j + 1);
                        String subStr = (String) strDirs.elementAt(strIdxStart + i4 + j);
                        if (!match(subPat, subStr, mCaseSensitive)) {
                            break;
                        }
                    }
                    foundIdx = strIdxStart + i4;
                    break;
                    i4++;
                }
                if (foundIdx == -1) {
                    return false;
                }
                patIdxStart = patIdxTmp;
                strIdxStart = foundIdx + patLength;
            }
        }
        for (int i5 = patIdxStart; i5 <= patIdxEnd; i5++) {
            if (!patDirs.elementAt(i5).equals(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS)) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x017a, code lost:
    
        if (r10 == r11) goto L177;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0181, code lost:
    
        if (r12 > r13) goto L178;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0184, code lost:
    
        r16 = -1;
        r17 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0191, code lost:
    
        if (r17 > r11) goto L185;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x019a, code lost:
    
        if (r0[r17] != '*') goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x019d, code lost:
    
        r16 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x01a4, code lost:
    
        r17 = r17 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x01b0, code lost:
    
        if (r16 != (r10 + 1)) goto L179;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x01b3, code lost:
    
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x01b9, code lost:
    
        r0 = (r16 - r10) - 1;
        r0 = (r13 - r12) + 1;
        r19 = -1;
        r20 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01d8, code lost:
    
        if (r20 > (r0 - r0)) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x01db, code lost:
    
        r21 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x01e2, code lost:
    
        if (r21 >= r0) goto L188;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x01e5, code lost:
    
        r0 = r0[(r10 + r21) + 1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x01f4, code lost:
    
        if (r0 == '?') goto L191;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x01f8, code lost:
    
        if (r7 == false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0208, code lost:
    
        if (r0 == r0[(r12 + r20) + r21]) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x020f, code lost:
    
        if (r7 != false) goto L192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0225, code lost:
    
        if (java.lang.Character.toUpperCase(r0) == java.lang.Character.toUpperCase(r0[(r12 + r20) + r21])) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x022b, code lost:
    
        r21 = r21 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0231, code lost:
    
        r19 = r12 + r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x023b, code lost:
    
        r20 = r20 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0244, code lost:
    
        if (r19 != (-1)) goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0247, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0249, code lost:
    
        r10 = r16;
        r12 = r19 + r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0257, code lost:
    
        r16 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x025f, code lost:
    
        if (r16 > r11) goto L195;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x0268, code lost:
    
        if (r0[r16] == '*') goto L148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x026b, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x026d, code lost:
    
        r16 = r16 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x0273, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00e7, code lost:
    
        if (r12 <= r13) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00ea, code lost:
    
        r16 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00f2, code lost:
    
        if (r16 > r11) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00fb, code lost:
    
        if (r0[r16] == '*') goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00fe, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0100, code lost:
    
        r16 = r16 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0106, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0108, code lost:
    
        r0 = r0[r11];
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0111, code lost:
    
        if (r0 == '*') goto L168;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0118, code lost:
    
        if (r12 > r13) goto L169;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x011f, code lost:
    
        if (r0 == '?') goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0123, code lost:
    
        if (r7 == false) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x012d, code lost:
    
        if (r0 == r0[r13]) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0130, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0133, code lost:
    
        if (r7 != false) goto L173;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0143, code lost:
    
        if (java.lang.Character.toUpperCase(r0) == java.lang.Character.toUpperCase(r0[r13])) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0146, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0148, code lost:
    
        r11 = r11 - 1;
        r13 = r13 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0155, code lost:
    
        if (r12 <= r13) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0158, code lost:
    
        r16 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0160, code lost:
    
        if (r16 > r11) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0169, code lost:
    
        if (r0[r16] == '*') goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x016c, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x016e, code lost:
    
        r16 = r16 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0174, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean match(java.lang.String r5, java.lang.String r6, boolean r7) {
        /*
            Method dump skipped, instructions count: 629
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.jam.internal.DirectoryScanner.match(java.lang.String, java.lang.String, boolean):boolean");
    }

    private static Vector tokenizePath(String path) {
        Vector ret = new Vector();
        StringTokenizer st = new StringTokenizer(path, File.separator);
        while (st.hasMoreTokens()) {
            ret.addElement(st.nextToken());
        }
        return ret;
    }
}
