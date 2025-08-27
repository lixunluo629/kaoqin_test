package org.apache.xmlbeans.impl.tool;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc;
import org.apache.xmlbeans.impl.xb.ltgfmt.TestsDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/XSTCTester.class */
public class XSTCTester {
    private static final Pattern leadingSpace = Pattern.compile("^\\s+", 8);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/XSTCTester$Harness.class */
    public interface Harness {
        void runTestCase(TestCaseResult testCaseResult);
    }

    public static void printUsage() {
        System.out.println("Usage: xstc [-showpass] [-errcode] foo_LTGfmt.xml ...");
    }

    public static void main(String[] args) throws Exception {
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("version");
        flags.add("showpass");
        flags.add("errcode");
        long start = System.currentTimeMillis();
        CommandLine cl = new CommandLine(args, flags, Collections.EMPTY_SET);
        if (cl.getOpt("h") != null || cl.getOpt("help") != null || cl.getOpt("usage") != null) {
            printUsage();
            System.exit(0);
            return;
        }
        if (cl.getOpt("version") != null) {
            CommandLine.printVersion();
            System.exit(0);
            return;
        }
        String[] badopts = cl.getBadOpts();
        if (badopts.length > 0) {
            for (String str : badopts) {
                System.out.println("Unrecognized option: " + str);
            }
            printUsage();
            System.exit(0);
            return;
        }
        if (cl.args().length == 0) {
            printUsage();
            return;
        }
        boolean showpass = cl.getOpt("showpass") != null;
        boolean errcode = cl.getOpt("errcode") != null;
        File[] allFiles = cl.getFiles();
        Collection<File> ltgFiles = new ArrayList();
        Harness harness = new XMLBeanXSTCHarness();
        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].getName().indexOf("LTG") >= 0) {
                ltgFiles.add(allFiles[i]);
            }
        }
        File resultsFile = new File("out.html");
        PrintWriter writer = new PrintWriter(new FileWriter(resultsFile));
        writer.println("<html>");
        writer.println("<style>td {border-bottom: 1px solid black} xmp {white-space: normal; word-wrap: break-word; word-break: break-all} </style>");
        writer.println("<body>");
        writer.println("<script language='JavaScript' type='text/javascript'>");
        writer.println("var w;");
        writer.println("function openWindow(schema, instance) {");
        writer.println("  if (w == null) {");
        writer.println("    w = window.open('about:blank', 'xstc');");
        writer.println("  }");
        writer.println("  if (w.closed) {");
        writer.println("    w = window.open('about:blank', 'xstc');");
        writer.println("  }");
        writer.println("  w.document.open();");
        writer.println("  w.document.write(\"<frameset rows=*,*><frame src='\" + schema + \"'><frame src='\" + instance + \"'></frameset>\");");
        writer.println("  w.document.close();");
        writer.println("  w.focus();");
        writer.println("}");
        writer.println("</script>");
        writer.println("<h1>XML Schema Test Collection Results</h1>");
        writer.println("<p>Run on " + new XmlCalendar(new Date()) + "</p>");
        writer.println("<p>Values in schema or instance valid columns are results from compiling or validating respectively.");
        writer.println("Red or orange background mean the test failed.</p>");
        writer.println("<table style='border: 1px solid black' cellpadding=0 cellspacing=0>");
        writer.println("<tr><td witdh=10%>id</td><td width=70%>Description</td><td width=10%>sch v</td><td width=10%>ins v</td></tr>");
        int failures = 0;
        int cases = 0;
        for (File ltgFile : ltgFiles) {
            System.out.println("Processing test cases in " + ltgFile);
            Collection ltgErrors = new ArrayList();
            TestCase[] testCases = parseLTGFile(ltgFile, ltgErrors);
            Collection results = new ArrayList();
            if (testCases != null) {
                for (TestCase testCase : testCases) {
                    TestCaseResult result = new TestCaseResult();
                    result.testCase = testCase;
                    harness.runTestCase(result);
                    cases++;
                    if (!result.succeeded(errcode)) {
                        failures++;
                    } else {
                        if (!showpass) {
                        }
                    }
                    results.add(result);
                }
            }
            writer.println("<tr><td colspan=4 bgcolor=skyblue>" + ltgFile + "</td></tr>");
            if (!ltgErrors.isEmpty()) {
                writer.println("<tr><td>Errors within the LTG file:");
                writer.println("<xmp>");
                Iterator j = ltgErrors.iterator();
                while (j.hasNext()) {
                    writer.println(j.next());
                }
                writer.println("</xmp>");
                writer.println("</td></tr>");
            } else if (results.size() == 0) {
                writer.println("<tr><td colspan=4 bgcolor=green>Nothing to report</td></tr>");
            }
            if (results != null) {
                Iterator j2 = results.iterator();
                while (j2.hasNext()) {
                    summarizeResultAsHTMLTableRows((TestCaseResult) j2.next(), writer, errcode);
                }
            }
        }
        writer.println("<tr><td colspan=4>Summary: " + failures + " failures out of " + cases + " cases run.</td></tr>");
        writer.println("</table>");
        writer.close();
        long finish = System.currentTimeMillis();
        System.out.println("Time run tests: " + ((finish - start) / 1000.0d) + " seconds");
        System.out.println("Results output to " + resultsFile);
        if (SystemProperties.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
            Runtime.getRuntime().exec("cmd /c start iexplore \"" + resultsFile.getAbsolutePath() + SymbolConstants.QUOTES_SYMBOL);
        } else {
            Runtime.getRuntime().exec("mozilla file://" + resultsFile.getAbsolutePath());
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/XSTCTester$TestCase.class */
    public static class TestCase {
        private File ltgFile;
        private String id;
        private String origin;
        private String description;
        private File schemaFile;
        private File instanceFile;
        private File resourceFile;
        private boolean svExpected;
        private boolean ivExpected;
        private boolean rvExpected;
        private String errorCode;

        public File getLtgFile() {
            return this.ltgFile;
        }

        public String getId() {
            return this.id;
        }

        public String getOrigin() {
            return this.origin;
        }

        public String getDescription() {
            return this.description;
        }

        public File getSchemaFile() {
            return this.schemaFile;
        }

        public File getInstanceFile() {
            return this.instanceFile;
        }

        public File getResourceFile() {
            return this.resourceFile;
        }

        public boolean isSvExpected() {
            return this.svExpected;
        }

        public boolean isIvExpected() {
            return this.ivExpected;
        }

        public boolean isRvExpected() {
            return this.rvExpected;
        }

        public String getErrorCode() {
            return this.errorCode;
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/XSTCTester$TestCaseResult.class */
    public static class TestCaseResult {
        private TestCase testCase;
        private boolean svActual;
        private boolean ivActual;
        private boolean crash;
        private Collection svMessages = new ArrayList();
        private Collection ivMessages = new ArrayList();

        public TestCase getTestCase() {
            return this.testCase;
        }

        public boolean isSvActual() {
            return this.svActual;
        }

        public void setSvActual(boolean svActual) {
            this.svActual = svActual;
        }

        public boolean isIvActual() {
            return this.ivActual;
        }

        public void setIvActual(boolean ivActual) {
            this.ivActual = ivActual;
        }

        public Collection getSvMessages() {
            return Collections.unmodifiableCollection(this.svMessages);
        }

        public void addSvMessages(Collection svMessages) {
            this.svMessages.addAll(svMessages);
        }

        public Collection getIvMessages() {
            return Collections.unmodifiableCollection(this.ivMessages);
        }

        public void addIvMessages(Collection ivMessages) {
            this.ivMessages.addAll(ivMessages);
        }

        public void setCrash(boolean crash) {
            this.crash = crash;
        }

        public boolean isCrash() {
            return this.crash;
        }

        public boolean succeeded(boolean errcode) {
            boolean success = !this.crash && isIvActual() == this.testCase.isIvExpected() && isSvActual() == this.testCase.isSvExpected();
            if (errcode && this.testCase.getErrorCode() != null) {
                success &= XSTCTester.errorReported(this.testCase.getErrorCode(), this.svMessages) || XSTCTester.errorReported(this.testCase.getErrorCode(), this.ivMessages);
            }
            return success;
        }
    }

    public static String makeHTMLLink(File file, boolean value) {
        if (file == null) {
            return "&nbsp;";
        }
        URI uri = file.getAbsoluteFile().toURI();
        return "<a href=\"" + uri + "\" target=_blank>" + Boolean.toString(value) + "</a>";
    }

    public static String makeHTMLDescription(TestCase testCase) {
        StringBuffer sb = new StringBuffer();
        sb.append("<a class=noline href='javascript:openWindow(\"");
        if (testCase.getSchemaFile() == null) {
            sb.append("about:No schema");
        } else {
            sb.append(testCase.getSchemaFile().getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"));
        }
        sb.append("\", \"");
        if (testCase.getInstanceFile() == null) {
            sb.append("about:No instance");
        } else {
            sb.append(testCase.getInstanceFile().getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"));
        }
        sb.append("\")'><xmp>");
        sb.append(leadingSpace.matcher(testCase.getDescription()).replaceAll(""));
        sb.append("</xmp></a>");
        return sb.toString();
    }

    public static void summarizeResultAsHTMLTableRows(TestCaseResult result, PrintWriter out, boolean errcode) {
        String sLinks;
        TestCase testCase = result.getTestCase();
        boolean errorRow = errcode && testCase.getErrorCode() != null;
        boolean messagesRow = (result.getIvMessages().isEmpty() && result.getSvMessages().isEmpty()) ? false : true;
        boolean sRight = testCase.getSchemaFile() == null || testCase.isSvExpected() == result.isSvActual();
        boolean iRight = testCase.getInstanceFile() == null || testCase.isIvExpected() == result.isIvActual();
        boolean codeRight = true;
        if (errorRow) {
            codeRight = errorReported(testCase.getErrorCode(), result.svMessages) || errorReported(testCase.getErrorCode(), result.ivMessages);
        }
        out.println(result.isCrash() ? "<tr bgcolor=black color=white>" : "<tr>");
        int idRowSpan = 1 + (errorRow ? 1 : 0) + (messagesRow ? 1 : 0);
        out.println("<td rowspan=" + idRowSpan + " valign=top>" + testCase.getId() + "</td>");
        out.println("<td valign=top>" + makeHTMLDescription(testCase) + "</td>");
        if (testCase.getResourceFile() == null) {
            sLinks = makeHTMLLink(testCase.getSchemaFile(), result.isSvActual());
        } else {
            sLinks = makeHTMLLink(testCase.getSchemaFile(), result.isSvActual()) + "<br>" + makeHTMLLink(testCase.getResourceFile(), result.isSvActual());
        }
        out.println((sRight ? "<td valign=top>" : result.isSvActual() ? "<td bgcolor=orange valign=top>" : "<td bgcolor=red valign=top>") + sLinks + "</td>");
        out.println((iRight ? "<td valign=top>" : result.isIvActual() ? "<td bgcolor=orange valign=top>" : "<td bgcolor=red valign=top>") + makeHTMLLink(testCase.getInstanceFile(), result.isIvActual()) + "</td>");
        out.println("</tr>");
        if (errorRow) {
            out.println("<tr>");
            out.println((codeRight ? "<td colspan=4 valid=top>" : "<td colspan=4 bgcolor=orange valign=top>") + "expected error: " + testCase.getErrorCode() + "</td>");
            out.println("</tr>");
        }
        if (messagesRow) {
            if (!result.succeeded(errcode)) {
                out.println("<tr><td colspan=4 bgcolor=yellow><xmp>");
            } else {
                out.println("<tr><td colspan=4><xmp>");
            }
            Iterator j = result.getSvMessages().iterator();
            while (j.hasNext()) {
                out.println(j.next());
            }
            Iterator j2 = result.getIvMessages().iterator();
            while (j2.hasNext()) {
                out.println(j2.next());
            }
            out.println("</xmp></tr></td>");
        }
    }

    public static TestCase[] parseLTGFile(File ltgFile, Collection outerErrors) throws Exception {
        Collection errors = new ArrayList();
        try {
            XmlOptions ltgOptions = new XmlOptions();
            ltgOptions.setLoadSubstituteNamespaces(Collections.singletonMap("", "http://www.bea.com/2003/05/xmlbean/ltgfmt"));
            ltgOptions.setErrorListener(errors);
            ltgOptions.setLoadLineNumbers();
            TestsDocument doc = TestsDocument.Factory.parse(ltgFile, ltgOptions);
            if (!doc.validate(ltgOptions)) {
                throw new Exception("Document " + ltgFile + " not valid.");
            }
            org.apache.xmlbeans.impl.xb.ltgfmt.TestCase[] testCases = doc.getTests().getTestArray();
            Collection result = new ArrayList();
            for (int i = 0; i < testCases.length; i++) {
                TestCase newCase = new TestCase();
                newCase.ltgFile = ltgFile;
                newCase.id = testCases[i].getId();
                newCase.origin = testCases[i].getOrigin();
                newCase.description = testCases[i].getDescription();
                FileDesc[] filedescs = testCases[i].getFiles().getFileArray();
                testCases[i].getOrigin();
                for (int j = 0; j < filedescs.length; j++) {
                    String dir = filedescs[j].getFolder();
                    String filename = filedescs[j].getFileName();
                    File theFile = new File(ltgFile.getParentFile(), dir + "/" + filename);
                    if (theFile.exists() && theFile.isFile() && theFile.canRead()) {
                        switch (filedescs[j].getRole().intValue()) {
                            case 1:
                                if (newCase.schemaFile != null) {
                                    outerErrors.add(XmlError.forObject("More than one schema file speicifed - ignoring all but last", filedescs[j]).toString());
                                }
                                newCase.schemaFile = theFile;
                                newCase.svExpected = filedescs[j].getValidity();
                                break;
                            case 2:
                                if (newCase.instanceFile != null) {
                                    outerErrors.add(XmlError.forObject("More than one instance file speicifed - ignoring all but last", filedescs[j]).toString());
                                }
                                newCase.instanceFile = theFile;
                                newCase.ivExpected = filedescs[j].getValidity();
                                break;
                            case 3:
                                if (newCase.resourceFile != null) {
                                    outerErrors.add(XmlError.forObject("More than one resource file speicifed - ignoring all but last", filedescs[j]).toString());
                                }
                                newCase.resourceFile = theFile;
                                newCase.rvExpected = filedescs[j].getValidity();
                                break;
                            default:
                                throw new XmlException(XmlError.forObject("Unexpected file role", filedescs[j]));
                        }
                        if (filedescs[j].getCode() != null) {
                            newCase.errorCode = filedescs[j].getCode().getID();
                        }
                    } else {
                        outerErrors.add(XmlError.forObject("Can't read file " + theFile, filedescs[j]).toString());
                    }
                }
                result.add(newCase);
            }
            return (TestCase[]) result.toArray(new TestCase[result.size()]);
        } catch (Exception e) {
            if (errors.isEmpty()) {
                outerErrors.add(e.getMessage());
                return null;
            }
            Iterator i2 = errors.iterator();
            while (i2.hasNext()) {
                outerErrors.add(i2.next().toString());
            }
            return null;
        }
    }

    public static boolean errorReported(String errorCode, Collection set) {
        if (errorCode == null || set == null || set.size() == 0) {
            return false;
        }
        Iterator i = set.iterator();
        while (i.hasNext()) {
            if (errorCode.equals(((XmlError) i.next()).getErrorCode())) {
                return true;
            }
        }
        return false;
    }
}
