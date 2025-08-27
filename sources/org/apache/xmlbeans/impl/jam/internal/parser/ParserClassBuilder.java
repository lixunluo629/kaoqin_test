package org.apache.xmlbeans.impl.jam.internal.parser;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.provider.JamClassBuilder;
import org.apache.xmlbeans.impl.jam.provider.JamClassPopulator;
import org.apache.xmlbeans.impl.jam.provider.JamServiceContext;
import org.apache.xmlbeans.impl.jam.provider.ResourcePath;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/parser/ParserClassBuilder.class */
public class ParserClassBuilder extends JamClassBuilder implements JamClassPopulator {
    private static final boolean VERBOSE = false;
    private ResourcePath mSourcePath;
    private boolean mVerbose;
    private PrintWriter mOut;

    private ParserClassBuilder() {
        this.mVerbose = false;
        this.mOut = new PrintWriter(System.out);
    }

    public ParserClassBuilder(JamServiceContext jsp) {
        this.mVerbose = false;
        this.mOut = new PrintWriter(System.out);
        this.mSourcePath = jsp.getInputSourcepath();
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:28:0x00e9
        	at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
        	at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
        */
    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public org.apache.xmlbeans.impl.jam.mutable.MClass build(java.lang.String r6, java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 255
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.jam.internal.parser.ParserClassBuilder.build(java.lang.String, java.lang.String):org.apache.xmlbeans.impl.jam.mutable.MClass");
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassPopulator
    public void populate(MClass m) {
        throw new IllegalStateException("NYI");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static MClass[] parse(Reader in, JamClassLoader loader) throws Exception {
        if (in == null) {
            throw new IllegalArgumentException("null in");
        }
        if (loader == null) {
            throw new IllegalArgumentException("null loader");
        }
        throw new IllegalStateException("temporarily NI");
    }

    public static void main(String[] files) {
        new MainTool().process(files);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/parser/ParserClassBuilder$MainTool.class */
    static class MainTool {
        private List mFailures = new ArrayList();
        private int mCount = 0;
        private PrintWriter mOut = new PrintWriter(System.out);
        private long mStartTime = System.currentTimeMillis();

        MainTool() {
        }

        public void process(String[] files) {
            for (String str : files) {
                try {
                    File input = new File(str);
                    parse(new ParserClassBuilder(), input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.mOut.println("\n\n\n");
            int fails = this.mFailures.size();
            if (fails != 0) {
                this.mOut.println("The following files failed to parse:");
                for (int i = 0; i < fails; i++) {
                    this.mOut.println(((File) this.mFailures.get(i)).getAbsolutePath());
                }
            }
            this.mOut.println((((this.mCount - fails) * 100) / this.mCount) + "% (" + (this.mCount - fails) + "/" + this.mCount + ") of input java files successfully parsed.");
            this.mOut.println("Total time: " + ((System.currentTimeMillis() - this.mStartTime) / 1000) + " seconds.");
            this.mOut.flush();
            System.out.flush();
            System.err.flush();
        }

        private void parse(ParserClassBuilder parser, File input) throws Exception {
            System.gc();
            if (input.isDirectory()) {
                File[] files = input.listFiles();
                for (File file : files) {
                    parse(parser, file);
                }
                return;
            }
            if (input.getName().endsWith(".java")) {
                this.mCount++;
                try {
                    MClass[] results = ParserClassBuilder.parse(new FileReader(input), null);
                    if (results == null) {
                        this.mOut.println("[error, parser result is null]");
                        addFailure(input);
                    }
                } catch (Throwable e) {
                    e.printStackTrace(this.mOut);
                    addFailure(input);
                }
            }
        }

        private void addFailure(File file) {
            this.mFailures.add(file);
        }
    }
}
