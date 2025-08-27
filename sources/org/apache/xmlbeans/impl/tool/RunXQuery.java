package org.apache.xmlbeans.impl.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/RunXQuery.class */
public class RunXQuery {
    public static void printUsage() {
        System.out.println("Run an XQuery against an XML instance");
        System.out.println("Usage:");
        System.out.println("xquery [-verbose] [-pretty] [-q <query> | -qf query.xq] [file.xml]*");
        System.out.println(" -q <query> to specify a query on the command-line");
        System.out.println(" -qf <query> to specify a file containing a query");
        System.out.println(" -pretty pretty-prints the results");
        System.out.println(" -license prints license information");
        System.out.println(" the query is run on each XML file specified");
        System.out.println("");
    }

    public static void main(String[] args) throws Exception {
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("version");
        flags.add("verbose");
        flags.add("pretty");
        CommandLine cl = new CommandLine(args, flags, Arrays.asList("q", "qf"));
        if (cl.getOpt("h") != null || cl.getOpt("help") != null || cl.getOpt("usage") != null) {
            printUsage();
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
        if (cl.getOpt("license") != null) {
            CommandLine.printLicense();
            System.exit(0);
            return;
        }
        if (cl.getOpt("version") != null) {
            CommandLine.printVersion();
            System.exit(0);
            return;
        }
        String[] args2 = cl.args();
        if (args2.length == 0) {
            printUsage();
            System.exit(0);
            return;
        }
        boolean verbose = cl.getOpt("verbose") != null;
        boolean pretty = cl.getOpt("pretty") != null;
        String query = cl.getOpt("q");
        String queryfile = cl.getOpt("qf");
        if (query == null && queryfile == null) {
            System.err.println("No query specified");
            System.exit(0);
            return;
        }
        if (query != null && queryfile != null) {
            System.err.println("Specify -qf or -q, not both.");
            System.exit(0);
            return;
        }
        if (queryfile != null) {
            try {
                File queryFile = new File(queryfile);
                FileInputStream is = new FileInputStream(queryFile);
                InputStreamReader r = new InputStreamReader(is);
                StringBuffer sb = new StringBuffer();
                while (true) {
                    int ch2 = r.read();
                    if (ch2 < 0) {
                        break;
                    } else {
                        sb.append((char) ch2);
                    }
                }
                r.close();
                is.close();
                query = sb.toString();
            } catch (Throwable e) {
                System.err.println("Cannot read query file: " + e.getMessage());
                System.exit(1);
                return;
            }
        }
        if (verbose) {
            System.out.println("Compile Query:");
            System.out.println(query);
            System.out.println();
        }
        try {
            String query2 = XmlBeans.compileQuery(query);
            File[] files = cl.getFiles();
            for (int i = 0; i < files.length; i++) {
                if (verbose) {
                    try {
                        InputStream is2 = new FileInputStream(files[i]);
                        while (true) {
                            int ch3 = is2.read();
                            if (ch3 < 0) {
                                break;
                            } else {
                                System.out.write(ch3);
                            }
                        }
                        is2.close();
                        System.out.println();
                    } catch (Throwable e2) {
                        System.err.println("Error parsing instance: " + e2.getMessage());
                        System.exit(1);
                        return;
                    }
                }
                XmlObject x = XmlObject.Factory.parse(files[i]);
                if (verbose) {
                    System.out.println("Executing Query...");
                    System.err.println();
                }
                try {
                    XmlObject[] result = x.execQuery(query2);
                    if (verbose) {
                        System.out.println("Query Result:");
                    }
                    XmlOptions opts = new XmlOptions();
                    opts.setSaveOuter();
                    if (pretty) {
                        opts.setSavePrettyPrint();
                    }
                    for (XmlObject xmlObject : result) {
                        xmlObject.save(System.out, opts);
                        System.out.println();
                    }
                } catch (Throwable e3) {
                    System.err.println("Error executing query: " + e3.getMessage());
                    System.exit(1);
                    return;
                }
            }
        } catch (Exception e4) {
            System.err.println("Error compiling query: " + e4.getMessage());
            System.exit(1);
        }
    }
}
