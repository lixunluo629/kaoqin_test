package org.apache.ibatis.ognl;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.compress.archivers.cpio.CpioConstants;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlParser.class */
public class OgnlParser implements OgnlParserTreeConstants, OgnlParserConstants {
    protected JJTOgnlParserState jjtree;
    public OgnlParserTokenManager token_source;
    JavaCharStream jj_input_stream;
    public Token token;
    public Token jj_nt;
    private int jj_ntk;
    private Token jj_scanpos;
    private Token jj_lastpos;
    private int jj_la;
    private boolean jj_lookingAhead;
    private boolean jj_semLA;
    private int jj_gen;
    private final int[] jj_la1;
    private static int[] jj_la1_0;
    private static int[] jj_la1_1;
    private static int[] jj_la1_2;
    private final JJCalls[] jj_2_rtns;
    private boolean jj_rescan;
    private int jj_gc;
    private final LookaheadSuccess jj_ls;
    private List jj_expentries;
    private int[] jj_expentry;
    private int jj_kind;
    private int[] jj_lasttokens;
    private int jj_endpos;

    public final Node topLevelExpression() throws ParseException {
        expression();
        jj_consume_token(0);
        return this.jjtree.rootNode();
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void expression() throws org.apache.ibatis.ognl.ParseException {
        /*
            r4 = this;
            r0 = r4
            r0.assignmentExpression()
        L4:
            r0 = r4
            int r0 = r0.jj_ntk
            r1 = -1
            if (r0 != r1) goto L13
            r0 = r4
            int r0 = r0.jj_ntk()
            goto L17
        L13:
            r0 = r4
            int r0 = r0.jj_ntk
        L17:
            switch(r0) {
                case 1: goto L28;
                default: goto L2b;
            }
        L28:
            goto L38
        L2b:
            r0 = r4
            int[] r0 = r0.jj_la1
            r1 = 0
            r2 = r4
            int r2 = r2.jj_gen
            r0[r1] = r2
            goto Lb1
        L38:
            r0 = r4
            r1 = 1
            org.apache.ibatis.ognl.Token r0 = r0.jj_consume_token(r1)
            org.apache.ibatis.ognl.ASTSequence r0 = new org.apache.ibatis.ognl.ASTSequence
            r1 = r0
            r2 = 1
            r1.<init>(r2)
            r5 = r0
            r0 = 1
            r6 = r0
            r0 = r4
            org.apache.ibatis.ognl.JJTOgnlParserState r0 = r0.jjtree
            r1 = r5
            r0.openNodeScope(r1)
            r0 = r4
            r0.assignmentExpression()     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L9c
            r0 = r6
            if (r0 == 0) goto Lae
            r0 = r4
            org.apache.ibatis.ognl.JJTOgnlParserState r0 = r0.jjtree
            r1 = r5
            r2 = 2
            r0.closeNodeScope(r1, r2)
            goto Lae
        L65:
            r7 = move-exception
            r0 = r6
            if (r0 == 0) goto L77
            r0 = r4
            org.apache.ibatis.ognl.JJTOgnlParserState r0 = r0.jjtree     // Catch: java.lang.Throwable -> L9c
            r1 = r5
            r0.clearNodeScope(r1)     // Catch: java.lang.Throwable -> L9c
            r0 = 0
            r6 = r0
            goto L7f
        L77:
            r0 = r4
            org.apache.ibatis.ognl.JJTOgnlParserState r0 = r0.jjtree     // Catch: java.lang.Throwable -> L9c
            org.apache.ibatis.ognl.Node r0 = r0.popNode()     // Catch: java.lang.Throwable -> L9c
        L7f:
            r0 = r7
            boolean r0 = r0 instanceof java.lang.RuntimeException     // Catch: java.lang.Throwable -> L9c
            if (r0 == 0) goto L8b
            r0 = r7
            java.lang.RuntimeException r0 = (java.lang.RuntimeException) r0     // Catch: java.lang.Throwable -> L9c
            throw r0     // Catch: java.lang.Throwable -> L9c
        L8b:
            r0 = r7
            boolean r0 = r0 instanceof org.apache.ibatis.ognl.ParseException     // Catch: java.lang.Throwable -> L9c
            if (r0 == 0) goto L97
            r0 = r7
            org.apache.ibatis.ognl.ParseException r0 = (org.apache.ibatis.ognl.ParseException) r0     // Catch: java.lang.Throwable -> L9c
            throw r0     // Catch: java.lang.Throwable -> L9c
        L97:
            r0 = r7
            java.lang.Error r0 = (java.lang.Error) r0     // Catch: java.lang.Throwable -> L9c
            throw r0     // Catch: java.lang.Throwable -> L9c
        L9c:
            r8 = move-exception
            r0 = r6
            if (r0 == 0) goto Lab
            r0 = r4
            org.apache.ibatis.ognl.JJTOgnlParserState r0 = r0.jjtree
            r1 = r5
            r2 = 2
            r0.closeNodeScope(r1, r2)
        Lab:
            r0 = r8
            throw r0
        Lae:
            goto L4
        Lb1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.expression():void");
    }

    public final void assignmentExpression() throws ParseException {
        conditionalTestExpression();
        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
            case 2:
                jj_consume_token(2);
                ASTAssign jjtn001 = new ASTAssign(2);
                this.jjtree.openNodeScope(jjtn001);
                try {
                    try {
                        assignmentExpression();
                        if (1 != 0) {
                            this.jjtree.closeNodeScope(jjtn001, 2);
                            return;
                        }
                        return;
                    } catch (Throwable jjte001) {
                        if (1 != 0) {
                            this.jjtree.clearNodeScope(jjtn001);
                        } else {
                            this.jjtree.popNode();
                        }
                        if (jjte001 instanceof RuntimeException) {
                            throw ((RuntimeException) jjte001);
                        }
                        if (jjte001 instanceof ParseException) {
                            throw ((ParseException) jjte001);
                        }
                        throw ((Error) jjte001);
                    }
                } catch (Throwable th) {
                    if (1 != 0) {
                        this.jjtree.closeNodeScope(jjtn001, 2);
                    }
                    throw th;
                }
            default:
                this.jj_la1[1] = this.jj_gen;
                return;
        }
    }

    public final void conditionalTestExpression() throws ParseException {
        logicalOrExpression();
        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
            case 3:
                jj_consume_token(3);
                conditionalTestExpression();
                jj_consume_token(4);
                ASTTest jjtn001 = new ASTTest(3);
                this.jjtree.openNodeScope(jjtn001);
                try {
                    try {
                        conditionalTestExpression();
                        if (1 != 0) {
                            this.jjtree.closeNodeScope(jjtn001, 3);
                            return;
                        }
                        return;
                    } catch (Throwable jjte001) {
                        if (1 != 0) {
                            this.jjtree.clearNodeScope(jjtn001);
                        } else {
                            this.jjtree.popNode();
                        }
                        if (jjte001 instanceof RuntimeException) {
                            throw ((RuntimeException) jjte001);
                        }
                        if (jjte001 instanceof ParseException) {
                            throw ((ParseException) jjte001);
                        }
                        throw ((Error) jjte001);
                    }
                } catch (Throwable th) {
                    if (1 != 0) {
                        this.jjtree.closeNodeScope(jjtn001, 3);
                    }
                    throw th;
                }
            default:
                this.jj_la1[2] = this.jj_gen;
                return;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void logicalOrExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 267
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.logicalOrExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void logicalAndExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 269
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.logicalAndExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void inclusiveOrExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 274
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.inclusiveOrExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void exclusiveOrExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 274
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.exclusiveOrExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void andExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 274
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.andExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void equalityExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 570
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.equalityExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void relationalExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 1348
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.relationalExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void shiftExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 807
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.shiftExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void additiveExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 395
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.additiveExpression():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void multiplicativeExpression() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 534
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.multiplicativeExpression():void");
    }

    public final void unaryExpression() throws ParseException {
        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
            case 4:
            case 44:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 54:
            case 56:
            case 57:
            case 64:
            case 67:
            case 73:
            case 76:
            case 79:
            case 80:
            case 81:
                navigationChain();
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 42:
                        jj_consume_token(42);
                        Token t = jj_consume_token(64);
                        ASTInstanceof jjtn004 = new ASTInstanceof(28);
                        boolean jjtc004 = true;
                        this.jjtree.openNodeScope(jjtn004);
                        try {
                            this.jjtree.closeNodeScope(jjtn004, 1);
                            jjtc004 = false;
                            StringBuffer sb = new StringBuffer(t.image);
                            if (0 != 0) {
                                this.jjtree.closeNodeScope(jjtn004, 1);
                            }
                            while (true) {
                                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                    case 43:
                                        jj_consume_token(43);
                                        Token t2 = jj_consume_token(64);
                                        sb.append('.').append(t2.image);
                                    default:
                                        this.jj_la1[33] = this.jj_gen;
                                        jjtn004.setTargetType(new String(sb));
                                        return;
                                }
                            }
                        } catch (Throwable th) {
                            if (jjtc004) {
                                this.jjtree.closeNodeScope(jjtn004, 1);
                            }
                            throw th;
                        }
                    default:
                        this.jj_la1[34] = this.jj_gen;
                        return;
                }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 37:
            case 38:
            case 39:
            case 42:
            case 43:
            case 45:
            case 53:
            case 55:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 74:
            case 75:
            case 77:
            case 78:
            default:
                this.jj_la1[35] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            case 28:
            case 41:
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 28:
                        jj_consume_token(28);
                        break;
                    case 41:
                        jj_consume_token(41);
                        break;
                    default:
                        this.jj_la1[32] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                ASTNot jjtn003 = new ASTNot(27);
                this.jjtree.openNodeScope(jjtn003);
                try {
                    try {
                        unaryExpression();
                        if (1 != 0) {
                            this.jjtree.closeNodeScope(jjtn003, 1);
                            return;
                        }
                        return;
                    } catch (Throwable th2) {
                        if (1 != 0) {
                            this.jjtree.closeNodeScope(jjtn003, 1);
                        }
                        throw th2;
                    }
                } catch (Throwable jjte003) {
                    if (1 != 0) {
                        this.jjtree.clearNodeScope(jjtn003);
                    } else {
                        this.jjtree.popNode();
                    }
                    if (jjte003 instanceof RuntimeException) {
                        throw ((RuntimeException) jjte003);
                    }
                    if (jjte003 instanceof ParseException) {
                        throw ((ParseException) jjte003);
                    }
                    throw ((Error) jjte003);
                }
            case 35:
                jj_consume_token(35);
                unaryExpression();
                return;
            case 36:
                jj_consume_token(36);
                ASTNegate jjtn001 = new ASTNegate(25);
                this.jjtree.openNodeScope(jjtn001);
                try {
                    try {
                        unaryExpression();
                        if (1 != 0) {
                            this.jjtree.closeNodeScope(jjtn001, 1);
                            return;
                        }
                        return;
                    } catch (Throwable th3) {
                        if (1 != 0) {
                            this.jjtree.closeNodeScope(jjtn001, 1);
                        }
                        throw th3;
                    }
                } catch (Throwable jjte001) {
                    if (1 != 0) {
                        this.jjtree.clearNodeScope(jjtn001);
                    } else {
                        this.jjtree.popNode();
                    }
                    if (jjte001 instanceof RuntimeException) {
                        throw ((RuntimeException) jjte001);
                    }
                    if (jjte001 instanceof ParseException) {
                        throw ((ParseException) jjte001);
                    }
                    throw ((Error) jjte001);
                }
            case 40:
                jj_consume_token(40);
                ASTBitNegate jjtn002 = new ASTBitNegate(26);
                this.jjtree.openNodeScope(jjtn002);
                try {
                    try {
                        unaryExpression();
                        if (1 != 0) {
                            this.jjtree.closeNodeScope(jjtn002, 1);
                            return;
                        }
                        return;
                    } catch (Throwable jjte002) {
                        if (1 != 0) {
                            this.jjtree.clearNodeScope(jjtn002);
                        } else {
                            this.jjtree.popNode();
                        }
                        if (jjte002 instanceof RuntimeException) {
                            throw ((RuntimeException) jjte002);
                        }
                        if (jjte002 instanceof ParseException) {
                            throw ((ParseException) jjte002);
                        }
                        throw ((Error) jjte002);
                    }
                } catch (Throwable th4) {
                    if (1 != 0) {
                        this.jjtree.closeNodeScope(jjtn002, 1);
                    }
                    throw th4;
                }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void navigationChain() throws org.apache.ibatis.ognl.ParseException {
        /*
            Method dump skipped, instructions count: 765
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlParser.navigationChain():void");
    }

    public final void primaryExpression() throws ParseException {
        ASTConst jjtn001;
        String className = null;
        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
            case 46:
                jj_consume_token(46);
                ASTConst jjtn002 = new ASTConst(31);
                boolean jjtc002 = true;
                this.jjtree.openNodeScope(jjtn002);
                try {
                    this.jjtree.closeNodeScope(jjtn002, 0);
                    jjtc002 = false;
                    jjtn002.setValue(Boolean.TRUE);
                    if (0 != 0) {
                        this.jjtree.closeNodeScope(jjtn002, 0);
                        return;
                    }
                    return;
                } finally {
                }
            case 47:
                jj_consume_token(47);
                jjtn001 = new ASTConst(31);
                boolean jjtc001 = true;
                this.jjtree.openNodeScope(jjtn001);
                try {
                    this.jjtree.closeNodeScope(jjtn001, 0);
                    jjtc001 = false;
                    jjtn001.setValue(Boolean.FALSE);
                    if (0 != 0) {
                        this.jjtree.closeNodeScope(jjtn001, 0);
                        return;
                    }
                    return;
                } finally {
                    if (jjtc001) {
                        this.jjtree.closeNodeScope(jjtn001, 0);
                    }
                }
            case 48:
                ASTConst jjtn004 = new ASTConst(31);
                this.jjtree.openNodeScope(jjtn004);
                try {
                    jj_consume_token(48);
                    if (1 != 0) {
                        this.jjtree.closeNodeScope(jjtn004, 0);
                        return;
                    }
                    return;
                } catch (Throwable th) {
                    if (1 != 0) {
                        this.jjtree.closeNodeScope(jjtn004, 0);
                    }
                    throw th;
                }
            case 73:
            case 76:
            case 79:
            case 80:
            case 81:
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 73:
                        jj_consume_token(73);
                        break;
                    case 74:
                    case 75:
                    case 77:
                    case 78:
                    default:
                        this.jj_la1[41] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                    case 76:
                        jj_consume_token(76);
                        break;
                    case 79:
                        jj_consume_token(79);
                        break;
                    case 80:
                        jj_consume_token(80);
                        break;
                    case 81:
                        jj_consume_token(81);
                        break;
                }
                jjtn001 = new ASTConst(31);
                boolean jjtc0012 = true;
                this.jjtree.openNodeScope(jjtn001);
                try {
                    this.jjtree.closeNodeScope(jjtn001, 0);
                    jjtc0012 = false;
                    jjtn001.setValue(this.token_source.literalValue);
                    if (0 != 0) {
                        this.jjtree.closeNodeScope(jjtn001, 0);
                        return;
                    }
                    return;
                } finally {
                }
            default:
                this.jj_la1[48] = this.jj_gen;
                if (jj_2_4(2)) {
                    jj_consume_token(49);
                    ASTThisVarRef jjtn005 = new ASTThisVarRef(32);
                    boolean jjtc005 = true;
                    this.jjtree.openNodeScope(jjtn005);
                    try {
                        this.jjtree.closeNodeScope(jjtn005, 0);
                        jjtc005 = false;
                        jjtn005.setName(OgnlContext.THIS_CONTEXT_KEY);
                        if (0 != 0) {
                            this.jjtree.closeNodeScope(jjtn005, 0);
                            return;
                        }
                        return;
                    } catch (Throwable th2) {
                        if (jjtc005) {
                            this.jjtree.closeNodeScope(jjtn005, 0);
                        }
                        throw th2;
                    }
                }
                if (jj_2_5(2)) {
                    jj_consume_token(50);
                    ASTRootVarRef jjtn006 = new ASTRootVarRef(33);
                    boolean jjtc006 = true;
                    this.jjtree.openNodeScope(jjtn006);
                    try {
                        this.jjtree.closeNodeScope(jjtn006, 0);
                        jjtc006 = false;
                        jjtn006.setName(OgnlContext.ROOT_CONTEXT_KEY);
                        if (0 != 0) {
                            this.jjtree.closeNodeScope(jjtn006, 0);
                            return;
                        }
                        return;
                    } catch (Throwable th3) {
                        if (jjtc006) {
                            this.jjtree.closeNodeScope(jjtn006, 0);
                        }
                        throw th3;
                    }
                }
                if (jj_2_6(2)) {
                    jj_consume_token(51);
                    Token t = jj_consume_token(64);
                    ASTVarRef jjtn007 = new ASTVarRef(34);
                    boolean jjtc007 = true;
                    this.jjtree.openNodeScope(jjtn007);
                    try {
                        this.jjtree.closeNodeScope(jjtn007, 0);
                        jjtc007 = false;
                        jjtn007.setName(t.image);
                        if (0 != 0) {
                            this.jjtree.closeNodeScope(jjtn007, 0);
                            return;
                        }
                        return;
                    } catch (Throwable th4) {
                        if (jjtc007) {
                            this.jjtree.closeNodeScope(jjtn007, 0);
                        }
                        throw th4;
                    }
                }
                if (jj_2_7(2)) {
                    jj_consume_token(4);
                    jj_consume_token(52);
                    expression();
                    jj_consume_token(53);
                    ASTConst jjtn008 = new ASTConst(31);
                    boolean jjtc008 = true;
                    this.jjtree.openNodeScope(jjtn008);
                    try {
                        this.jjtree.closeNodeScope(jjtn008, 1);
                        jjtc008 = false;
                        jjtn008.setValue(jjtn008.jjtGetChild(0));
                        if (0 != 0) {
                            this.jjtree.closeNodeScope(jjtn008, 1);
                            return;
                        }
                        return;
                    } catch (Throwable th5) {
                        if (jjtc008) {
                            this.jjtree.closeNodeScope(jjtn008, 1);
                        }
                        throw th5;
                    }
                }
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 56:
                        staticReference();
                        return;
                    default:
                        this.jj_la1[49] = this.jj_gen;
                        if (jj_2_8(2)) {
                            constructorCall();
                            return;
                        }
                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                            case 44:
                                jj_consume_token(44);
                                expression();
                                jj_consume_token(45);
                                return;
                            case 52:
                            case 67:
                                index();
                                return;
                            case 54:
                                jj_consume_token(54);
                                ASTList jjtn009 = new ASTList(35);
                                this.jjtree.openNodeScope(jjtn009);
                                try {
                                    try {
                                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                            case 4:
                                            case 28:
                                            case 35:
                                            case 36:
                                            case 40:
                                            case 41:
                                            case 44:
                                            case 46:
                                            case 47:
                                            case 48:
                                            case 49:
                                            case 50:
                                            case 51:
                                            case 52:
                                            case 54:
                                            case 56:
                                            case 57:
                                            case 64:
                                            case 67:
                                            case 73:
                                            case 76:
                                            case 79:
                                            case 80:
                                            case 81:
                                                assignmentExpression();
                                                while (true) {
                                                    switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                                        case 1:
                                                            jj_consume_token(1);
                                                            assignmentExpression();
                                                        default:
                                                            this.jj_la1[43] = this.jj_gen;
                                                            break;
                                                    }
                                                }
                                            case 5:
                                            case 6:
                                            case 7:
                                            case 8:
                                            case 9:
                                            case 10:
                                            case 11:
                                            case 12:
                                            case 13:
                                            case 14:
                                            case 15:
                                            case 16:
                                            case 17:
                                            case 18:
                                            case 19:
                                            case 20:
                                            case 21:
                                            case 22:
                                            case 23:
                                            case 24:
                                            case 25:
                                            case 26:
                                            case 27:
                                            case 29:
                                            case 30:
                                            case 31:
                                            case 32:
                                            case 33:
                                            case 34:
                                            case 37:
                                            case 38:
                                            case 39:
                                            case 42:
                                            case 43:
                                            case 45:
                                            case 53:
                                            case 55:
                                            case 58:
                                            case 59:
                                            case 60:
                                            case 61:
                                            case 62:
                                            case 63:
                                            case 65:
                                            case 66:
                                            case 68:
                                            case 69:
                                            case 70:
                                            case 71:
                                            case 72:
                                            case 74:
                                            case 75:
                                            case 77:
                                            case 78:
                                            default:
                                                this.jj_la1[44] = this.jj_gen;
                                                break;
                                        }
                                        jj_consume_token(55);
                                        return;
                                    } catch (Throwable jjte009) {
                                        if (1 != 0) {
                                            this.jjtree.clearNodeScope(jjtn009);
                                        } else {
                                            this.jjtree.popNode();
                                        }
                                        if (jjte009 instanceof RuntimeException) {
                                            throw ((RuntimeException) jjte009);
                                        }
                                        if (jjte009 instanceof ParseException) {
                                            throw ((ParseException) jjte009);
                                        }
                                        throw ((Error) jjte009);
                                    }
                                } finally {
                                    if (1 != 0) {
                                        this.jjtree.closeNodeScope((Node) jjtn009, true);
                                    }
                                }
                            case 64:
                                if (jj_2_3(2)) {
                                    methodCall();
                                    return;
                                }
                                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                    case 64:
                                        propertyName();
                                        return;
                                    default:
                                        this.jj_la1[42] = this.jj_gen;
                                        jj_consume_token(-1);
                                        throw new ParseException();
                                }
                            default:
                                this.jj_la1[50] = this.jj_gen;
                                if (jj_2_9(2)) {
                                    ASTMap jjtn010 = new ASTMap(36);
                                    this.jjtree.openNodeScope(jjtn010);
                                    try {
                                        try {
                                            jj_consume_token(51);
                                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                                case 56:
                                                    className = classReference();
                                                    break;
                                                default:
                                                    this.jj_la1[45] = this.jj_gen;
                                                    break;
                                            }
                                            jj_consume_token(54);
                                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                                case 4:
                                                case 28:
                                                case 35:
                                                case 36:
                                                case 40:
                                                case 41:
                                                case 44:
                                                case 46:
                                                case 47:
                                                case 48:
                                                case 49:
                                                case 50:
                                                case 51:
                                                case 52:
                                                case 54:
                                                case 56:
                                                case 57:
                                                case 64:
                                                case 67:
                                                case 73:
                                                case 76:
                                                case 79:
                                                case 80:
                                                case 81:
                                                    keyValueExpression();
                                                    while (true) {
                                                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                                            case 1:
                                                                jj_consume_token(1);
                                                                keyValueExpression();
                                                            default:
                                                                this.jj_la1[46] = this.jj_gen;
                                                                break;
                                                        }
                                                    }
                                                case 5:
                                                case 6:
                                                case 7:
                                                case 8:
                                                case 9:
                                                case 10:
                                                case 11:
                                                case 12:
                                                case 13:
                                                case 14:
                                                case 15:
                                                case 16:
                                                case 17:
                                                case 18:
                                                case 19:
                                                case 20:
                                                case 21:
                                                case 22:
                                                case 23:
                                                case 24:
                                                case 25:
                                                case 26:
                                                case 27:
                                                case 29:
                                                case 30:
                                                case 31:
                                                case 32:
                                                case 33:
                                                case 34:
                                                case 37:
                                                case 38:
                                                case 39:
                                                case 42:
                                                case 43:
                                                case 45:
                                                case 53:
                                                case 55:
                                                case 58:
                                                case 59:
                                                case 60:
                                                case 61:
                                                case 62:
                                                case 63:
                                                case 65:
                                                case 66:
                                                case 68:
                                                case 69:
                                                case 70:
                                                case 71:
                                                case 72:
                                                case 74:
                                                case 75:
                                                case 77:
                                                case 78:
                                                default:
                                                    this.jj_la1[47] = this.jj_gen;
                                                    break;
                                            }
                                            jjtn010.setClassName(className);
                                            jj_consume_token(55);
                                            if (1 != 0) {
                                                this.jjtree.closeNodeScope((Node) jjtn010, true);
                                                return;
                                            }
                                            return;
                                        } catch (Throwable jjte010) {
                                            if (1 != 0) {
                                                this.jjtree.clearNodeScope(jjtn010);
                                            } else {
                                                this.jjtree.popNode();
                                            }
                                            if (jjte010 instanceof RuntimeException) {
                                                throw ((RuntimeException) jjte010);
                                            }
                                            if (jjte010 instanceof ParseException) {
                                                throw ((ParseException) jjte010);
                                            }
                                            throw ((Error) jjte010);
                                        }
                                    } catch (Throwable th6) {
                                        if (1 != 0) {
                                            this.jjtree.closeNodeScope((Node) jjtn010, true);
                                        }
                                        throw th6;
                                    }
                                }
                                jj_consume_token(-1);
                                throw new ParseException();
                        }
                }
        }
    }

    public final void keyValueExpression() throws ParseException {
        ASTKeyValue jjtn001 = new ASTKeyValue(37);
        this.jjtree.openNodeScope(jjtn001);
        try {
            try {
                assignmentExpression();
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 4:
                        jj_consume_token(4);
                        assignmentExpression();
                        break;
                    default:
                        this.jj_la1[51] = this.jj_gen;
                        break;
                }
            } catch (Throwable jjte001) {
                if (1 != 0) {
                    this.jjtree.clearNodeScope(jjtn001);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte001 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte001);
                }
                if (jjte001 instanceof ParseException) {
                    throw ((ParseException) jjte001);
                }
                throw ((Error) jjte001);
            }
        } finally {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn001, true);
            }
        }
    }

    public final void staticReference() throws ParseException {
        String className = classReference();
        if (jj_2_10(2)) {
            staticMethodCall(className);
            return;
        }
        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
            case 64:
                Token t = jj_consume_token(64);
                ASTStaticField jjtn001 = new ASTStaticField(38);
                boolean jjtc001 = true;
                this.jjtree.openNodeScope(jjtn001);
                try {
                    this.jjtree.closeNodeScope(jjtn001, 0);
                    jjtc001 = false;
                    jjtn001.init(className, t.image);
                    if (0 != 0) {
                        this.jjtree.closeNodeScope(jjtn001, 0);
                        return;
                    }
                    return;
                } catch (Throwable th) {
                    if (jjtc001) {
                        this.jjtree.closeNodeScope(jjtn001, 0);
                    }
                    throw th;
                }
            default:
                this.jj_la1[52] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    public final String classReference() throws ParseException {
        String result = "java.lang.Math";
        jj_consume_token(56);
        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
            case 64:
                result = className();
                break;
            default:
                this.jj_la1[53] = this.jj_gen;
                break;
        }
        jj_consume_token(56);
        return result;
    }

    public final String className() throws ParseException {
        Token t = jj_consume_token(64);
        StringBuffer result = new StringBuffer(t.image);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                case 43:
                    jj_consume_token(43);
                    Token t2 = jj_consume_token(64);
                    result.append('.').append(t2.image);
                default:
                    this.jj_la1[54] = this.jj_gen;
                    return new String(result);
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public final void constructorCall() throws ParseException {
        boolean jjtc000;
        ASTCtor jjtn000 = new ASTCtor(39);
        this.jjtree.openNodeScope(jjtn000);
        try {
            try {
                jj_consume_token(57);
                String className = className();
                if (jj_2_11(2)) {
                    jj_consume_token(44);
                    switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                        case 4:
                        case 28:
                        case 35:
                        case 36:
                        case 40:
                        case 41:
                        case 44:
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 54:
                        case 56:
                        case 57:
                        case 64:
                        case 67:
                        case 73:
                        case 76:
                        case 79:
                        case 80:
                        case 81:
                            assignmentExpression();
                            while (true) {
                                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                    case 1:
                                        jj_consume_token(1);
                                        assignmentExpression();
                                    default:
                                        this.jj_la1[55] = this.jj_gen;
                                        break;
                                }
                            }
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 37:
                        case 38:
                        case 39:
                        case 42:
                        case 43:
                        case 45:
                        case 53:
                        case 55:
                        case 58:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 63:
                        case 65:
                        case 66:
                        case 68:
                        case 69:
                        case 70:
                        case 71:
                        case 72:
                        case 74:
                        case 75:
                        case 77:
                        case 78:
                        default:
                            this.jj_la1[56] = this.jj_gen;
                            break;
                    }
                    jj_consume_token(45);
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                    jjtc000 = false;
                    jjtn000.setClassName(className);
                } else if (jj_2_12(2)) {
                    jj_consume_token(52);
                    jj_consume_token(53);
                    jj_consume_token(54);
                    ASTList jjtn001 = new ASTList(35);
                    this.jjtree.openNodeScope(jjtn001);
                    try {
                        try {
                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                case 4:
                                case 28:
                                case 35:
                                case 36:
                                case 40:
                                case 41:
                                case 44:
                                case 46:
                                case 47:
                                case 48:
                                case 49:
                                case 50:
                                case 51:
                                case 52:
                                case 54:
                                case 56:
                                case 57:
                                case 64:
                                case 67:
                                case 73:
                                case 76:
                                case 79:
                                case 80:
                                case 81:
                                    assignmentExpression();
                                    while (true) {
                                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                            case 1:
                                                jj_consume_token(1);
                                                assignmentExpression();
                                            default:
                                                this.jj_la1[57] = this.jj_gen;
                                                break;
                                        }
                                    }
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                case 15:
                                case 16:
                                case 17:
                                case 18:
                                case 19:
                                case 20:
                                case 21:
                                case 22:
                                case 23:
                                case 24:
                                case 25:
                                case 26:
                                case 27:
                                case 29:
                                case 30:
                                case 31:
                                case 32:
                                case 33:
                                case 34:
                                case 37:
                                case 38:
                                case 39:
                                case 42:
                                case 43:
                                case 45:
                                case 53:
                                case 55:
                                case 58:
                                case 59:
                                case 60:
                                case 61:
                                case 62:
                                case 63:
                                case 65:
                                case 66:
                                case 68:
                                case 69:
                                case 70:
                                case 71:
                                case 72:
                                case 74:
                                case 75:
                                case 77:
                                case 78:
                                default:
                                    this.jj_la1[58] = this.jj_gen;
                                    break;
                            }
                            if (1 != 0) {
                                this.jjtree.closeNodeScope((Node) jjtn001, true);
                            }
                            jj_consume_token(55);
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                            jjtc000 = false;
                            jjtn000.setClassName(className);
                            jjtn000.setArray(true);
                        } catch (Throwable jjte001) {
                            if (1 != 0) {
                                this.jjtree.clearNodeScope(jjtn001);
                            } else {
                                this.jjtree.popNode();
                            }
                            if (jjte001 instanceof RuntimeException) {
                                throw ((RuntimeException) jjte001);
                            }
                            if (jjte001 instanceof ParseException) {
                                throw ((ParseException) jjte001);
                            }
                            throw ((Error) jjte001);
                        }
                    } catch (Throwable th) {
                        if (1 != 0) {
                            this.jjtree.closeNodeScope((Node) jjtn001, true);
                        }
                        throw th;
                    }
                } else if (jj_2_13(2)) {
                    jj_consume_token(52);
                    assignmentExpression();
                    jj_consume_token(53);
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                    jjtc000 = false;
                    jjtn000.setClassName(className);
                    jjtn000.setArray(true);
                } else {
                    jj_consume_token(-1);
                    throw new ParseException();
                }
                if (jjtc000) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
            } catch (Throwable jjte000) {
                if (1 != 0) {
                    this.jjtree.clearNodeScope(jjtn000);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte000 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte000);
                }
                if (jjte000 instanceof ParseException) {
                    throw ((ParseException) jjte000);
                }
                throw ((Error) jjte000);
            }
        } catch (Throwable th2) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th2;
        }
    }

    public final void propertyName() throws ParseException {
        ASTProperty jjtn000 = new ASTProperty(40);
        this.jjtree.openNodeScope(jjtn000);
        try {
            Token t = jj_consume_token(64);
            ASTConst jjtn001 = new ASTConst(31);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope((Node) jjtn001, true);
                jjtc001 = false;
                jjtn001.setValue(t.image);
                if (0 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn001, true);
                }
            } catch (Throwable th) {
                if (jjtc001) {
                    this.jjtree.closeNodeScope((Node) jjtn001, true);
                }
                throw th;
            }
        } finally {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
        }
    }

    public final void staticMethodCall(String className) throws ParseException {
        ASTStaticMethod jjtn000 = new ASTStaticMethod(41);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        try {
            try {
                Token t = jj_consume_token(64);
                jj_consume_token(44);
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 4:
                    case 28:
                    case 35:
                    case 36:
                    case 40:
                    case 41:
                    case 44:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 54:
                    case 56:
                    case 57:
                    case 64:
                    case 67:
                    case 73:
                    case 76:
                    case 79:
                    case 80:
                    case 81:
                        assignmentExpression();
                        while (true) {
                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                case 1:
                                    jj_consume_token(1);
                                    assignmentExpression();
                                default:
                                    this.jj_la1[59] = this.jj_gen;
                                    break;
                            }
                        }
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 37:
                    case 38:
                    case 39:
                    case 42:
                    case 43:
                    case 45:
                    case 53:
                    case 55:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 65:
                    case 66:
                    case 68:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 74:
                    case 75:
                    case 77:
                    case 78:
                    default:
                        this.jj_la1[60] = this.jj_gen;
                        break;
                }
                jj_consume_token(45);
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtc000 = false;
                jjtn000.init(className, t.image);
                if (0 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
            } catch (Throwable jjte000) {
                if (jjtc000) {
                    this.jjtree.clearNodeScope(jjtn000);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte000 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte000);
                }
                if (jjte000 instanceof ParseException) {
                    throw ((ParseException) jjte000);
                }
                throw ((Error) jjte000);
            }
        } catch (Throwable th) {
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final void methodCall() throws ParseException {
        ASTMethod jjtn000 = new ASTMethod(42);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        try {
            try {
                Token t = jj_consume_token(64);
                jj_consume_token(44);
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 4:
                    case 28:
                    case 35:
                    case 36:
                    case 40:
                    case 41:
                    case 44:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 54:
                    case 56:
                    case 57:
                    case 64:
                    case 67:
                    case 73:
                    case 76:
                    case 79:
                    case 80:
                    case 81:
                        assignmentExpression();
                        while (true) {
                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                case 1:
                                    jj_consume_token(1);
                                    assignmentExpression();
                                default:
                                    this.jj_la1[61] = this.jj_gen;
                                    break;
                            }
                        }
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 37:
                    case 38:
                    case 39:
                    case 42:
                    case 43:
                    case 45:
                    case 53:
                    case 55:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 65:
                    case 66:
                    case 68:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 74:
                    case 75:
                    case 77:
                    case 78:
                    default:
                        this.jj_la1[62] = this.jj_gen;
                        break;
                }
                jj_consume_token(45);
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtc000 = false;
                jjtn000.setMethodName(t.image);
                if (0 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
            } catch (Throwable jjte000) {
                if (jjtc000) {
                    this.jjtree.clearNodeScope(jjtn000);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte000 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte000);
                }
                if (jjte000 instanceof ParseException) {
                    throw ((ParseException) jjte000);
                }
                throw ((Error) jjte000);
            }
        } catch (Throwable th) {
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final void projection() throws ParseException {
        ASTProject jjtn000 = new ASTProject(43);
        this.jjtree.openNodeScope(jjtn000);
        try {
            try {
                jj_consume_token(54);
                expression();
                jj_consume_token(55);
                if (1 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
            } catch (Throwable jjte000) {
                if (1 != 0) {
                    this.jjtree.clearNodeScope(jjtn000);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte000 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte000);
                }
                if (jjte000 instanceof ParseException) {
                    throw ((ParseException) jjte000);
                }
                throw ((Error) jjte000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final void selection() throws ParseException {
        if (jj_2_14(2)) {
            selectAll();
            return;
        }
        if (jj_2_15(2)) {
            selectFirst();
        } else if (jj_2_16(2)) {
            selectLast();
        } else {
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void selectAll() throws ParseException {
        ASTSelect jjtn000 = new ASTSelect(44);
        this.jjtree.openNodeScope(jjtn000);
        try {
            try {
                jj_consume_token(54);
                jj_consume_token(3);
                expression();
                jj_consume_token(55);
                if (1 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
            } catch (Throwable jjte000) {
                if (1 != 0) {
                    this.jjtree.clearNodeScope(jjtn000);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte000 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte000);
                }
                if (jjte000 instanceof ParseException) {
                    throw ((ParseException) jjte000);
                }
                throw ((Error) jjte000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final void selectFirst() throws ParseException {
        ASTSelectFirst jjtn000 = new ASTSelectFirst(45);
        this.jjtree.openNodeScope(jjtn000);
        try {
            try {
                jj_consume_token(54);
                jj_consume_token(11);
                expression();
                jj_consume_token(55);
                if (1 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
            } catch (Throwable jjte000) {
                if (1 != 0) {
                    this.jjtree.clearNodeScope(jjtn000);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte000 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte000);
                }
                if (jjte000 instanceof ParseException) {
                    throw ((ParseException) jjte000);
                }
                throw ((Error) jjte000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final void selectLast() throws ParseException {
        ASTSelectLast jjtn000 = new ASTSelectLast(46);
        this.jjtree.openNodeScope(jjtn000);
        try {
            try {
                jj_consume_token(54);
                jj_consume_token(58);
                expression();
                jj_consume_token(55);
                if (1 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
            } catch (Throwable jjte000) {
                if (1 != 0) {
                    this.jjtree.clearNodeScope(jjtn000);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte000 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte000);
                }
                if (jjte000 instanceof ParseException) {
                    throw ((ParseException) jjte000);
                }
                throw ((Error) jjte000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final void index() throws ParseException {
        boolean jjtc000;
        ASTProperty jjtn000 = new ASTProperty(40);
        this.jjtree.openNodeScope(jjtn000);
        try {
            try {
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 52:
                        jj_consume_token(52);
                        expression();
                        jj_consume_token(53);
                        this.jjtree.closeNodeScope((Node) jjtn000, true);
                        jjtc000 = false;
                        jjtn000.setIndexedAccess(true);
                        break;
                    case 67:
                        jj_consume_token(67);
                        ASTConst jjtn001 = new ASTConst(31);
                        boolean jjtc001 = true;
                        this.jjtree.openNodeScope(jjtn001);
                        try {
                            this.jjtree.closeNodeScope((Node) jjtn001, true);
                            jjtc001 = false;
                            jjtn001.setValue(this.token_source.literalValue);
                            if (0 != 0) {
                                this.jjtree.closeNodeScope((Node) jjtn001, true);
                            }
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                            jjtc000 = false;
                            jjtn000.setIndexedAccess(true);
                            break;
                        } catch (Throwable th) {
                            if (jjtc001) {
                                this.jjtree.closeNodeScope((Node) jjtn001, true);
                            }
                            throw th;
                        }
                    default:
                        this.jj_la1[63] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                if (jjtc000) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
            } catch (Throwable jjte000) {
                if (1 != 0) {
                    this.jjtree.clearNodeScope(jjtn000);
                } else {
                    this.jjtree.popNode();
                }
                if (jjte000 instanceof RuntimeException) {
                    throw ((RuntimeException) jjte000);
                }
                if (jjte000 instanceof ParseException) {
                    throw ((ParseException) jjte000);
                }
                throw ((Error) jjte000);
            }
        } catch (Throwable th2) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th2;
        }
    }

    private boolean jj_2_1(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_1();
            jj_save(0, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(0, xla);
            return true;
        } catch (Throwable th) {
            jj_save(0, xla);
            throw th;
        }
    }

    private boolean jj_2_2(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_2();
            jj_save(1, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(1, xla);
            return true;
        } catch (Throwable th) {
            jj_save(1, xla);
            throw th;
        }
    }

    private boolean jj_2_3(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_3();
            jj_save(2, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(2, xla);
            return true;
        } catch (Throwable th) {
            jj_save(2, xla);
            throw th;
        }
    }

    private boolean jj_2_4(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_4();
            jj_save(3, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(3, xla);
            return true;
        } catch (Throwable th) {
            jj_save(3, xla);
            throw th;
        }
    }

    private boolean jj_2_5(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_5();
            jj_save(4, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(4, xla);
            return true;
        } catch (Throwable th) {
            jj_save(4, xla);
            throw th;
        }
    }

    private boolean jj_2_6(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_6();
            jj_save(5, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(5, xla);
            return true;
        } catch (Throwable th) {
            jj_save(5, xla);
            throw th;
        }
    }

    private boolean jj_2_7(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_7();
            jj_save(6, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(6, xla);
            return true;
        } catch (Throwable th) {
            jj_save(6, xla);
            throw th;
        }
    }

    private boolean jj_2_8(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_8();
            jj_save(7, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(7, xla);
            return true;
        } catch (Throwable th) {
            jj_save(7, xla);
            throw th;
        }
    }

    private boolean jj_2_9(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_9();
            jj_save(8, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(8, xla);
            return true;
        } catch (Throwable th) {
            jj_save(8, xla);
            throw th;
        }
    }

    private boolean jj_2_10(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_10();
            jj_save(9, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(9, xla);
            return true;
        } catch (Throwable th) {
            jj_save(9, xla);
            throw th;
        }
    }

    private boolean jj_2_11(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_11();
            jj_save(10, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(10, xla);
            return true;
        } catch (Throwable th) {
            jj_save(10, xla);
            throw th;
        }
    }

    private boolean jj_2_12(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_12();
            jj_save(11, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(11, xla);
            return true;
        } catch (Throwable th) {
            jj_save(11, xla);
            throw th;
        }
    }

    private boolean jj_2_13(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_13();
            jj_save(12, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(12, xla);
            return true;
        } catch (Throwable th) {
            jj_save(12, xla);
            throw th;
        }
    }

    private boolean jj_2_14(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_14();
            jj_save(13, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(13, xla);
            return true;
        } catch (Throwable th) {
            jj_save(13, xla);
            throw th;
        }
    }

    private boolean jj_2_15(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_15();
            jj_save(14, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(14, xla);
            return true;
        } catch (Throwable th) {
            jj_save(14, xla);
            throw th;
        }
    }

    private boolean jj_2_16(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            boolean z = !jj_3_16();
            jj_save(15, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(15, xla);
            return true;
        } catch (Throwable th) {
            jj_save(15, xla);
            throw th;
        }
    }

    private boolean jj_3R_56() {
        return jj_scan_token(48);
    }

    private boolean jj_3R_55() {
        return jj_scan_token(47);
    }

    private boolean jj_3R_54() {
        return jj_scan_token(46);
    }

    private boolean jj_3R_31() {
        return jj_3R_27();
    }

    private boolean jj_3_13() {
        return jj_scan_token(52) || jj_3R_27();
    }

    private boolean jj_3R_53() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(73)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(76)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(79)) {
                    this.jj_scanpos = xsp;
                    if (jj_scan_token(80)) {
                        this.jj_scanpos = xsp;
                        return jj_scan_token(81);
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_26() {
        return jj_3R_27();
    }

    private boolean jj_3R_52() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_53()) {
            this.jj_scanpos = xsp;
            if (jj_3R_54()) {
                this.jj_scanpos = xsp;
                if (jj_3R_55()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_56()) {
                        this.jj_scanpos = xsp;
                        if (jj_3_4()) {
                            this.jj_scanpos = xsp;
                            if (jj_3_5()) {
                                this.jj_scanpos = xsp;
                                if (jj_3_6()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3_7()) {
                                        this.jj_scanpos = xsp;
                                        if (jj_3R_57()) {
                                            this.jj_scanpos = xsp;
                                            if (jj_3_8()) {
                                                this.jj_scanpos = xsp;
                                                if (jj_3R_58()) {
                                                    this.jj_scanpos = xsp;
                                                    if (jj_3R_59()) {
                                                        this.jj_scanpos = xsp;
                                                        if (jj_3R_60()) {
                                                            this.jj_scanpos = xsp;
                                                            if (jj_3R_61()) {
                                                                this.jj_scanpos = xsp;
                                                                return jj_3_9();
                                                            }
                                                            return false;
                                                        }
                                                        return false;
                                                    }
                                                    return false;
                                                }
                                                return false;
                                            }
                                            return false;
                                        }
                                        return false;
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_42() {
        return jj_3R_43();
    }

    private boolean jj_3_12() {
        return jj_scan_token(52) || jj_scan_token(53);
    }

    private boolean jj_3_11() {
        if (jj_scan_token(44)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_26()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(45);
    }

    private boolean jj_3R_67() {
        return jj_scan_token(67);
    }

    private boolean jj_3_2() {
        return jj_3R_22();
    }

    private boolean jj_3R_66() {
        return jj_scan_token(52);
    }

    private boolean jj_3R_64() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_66()) {
            this.jj_scanpos = xsp;
            return jj_3R_67();
        }
        return false;
    }

    private boolean jj_3_1() {
        return jj_3R_21();
    }

    private boolean jj_3R_23() {
        return jj_scan_token(57) || jj_3R_32();
    }

    private boolean jj_3R_41() {
        return jj_3R_42();
    }

    private boolean jj_3R_30() {
        return jj_scan_token(54) || jj_scan_token(58);
    }

    private boolean jj_3R_32() {
        return jj_scan_token(64);
    }

    private boolean jj_3R_51() {
        return jj_3R_52();
    }

    private boolean jj_3R_29() {
        return jj_scan_token(54) || jj_scan_token(11);
    }

    private boolean jj_3R_40() {
        return jj_3R_41();
    }

    private boolean jj_3R_33() {
        return jj_scan_token(56);
    }

    private boolean jj_3R_63() {
        return jj_3R_65();
    }

    private boolean jj_3R_28() {
        return jj_scan_token(54) || jj_scan_token(3);
    }

    private boolean jj_3R_50() {
        return jj_3R_51();
    }

    private boolean jj_3R_39() {
        return jj_3R_40();
    }

    private boolean jj_3_10() {
        return jj_3R_25();
    }

    private boolean jj_3R_24() {
        return jj_3R_33();
    }

    private boolean jj_3R_49() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(41)) {
            this.jj_scanpos = xsp;
            return jj_scan_token(28);
        }
        return false;
    }

    private boolean jj_3R_48() {
        return jj_scan_token(40);
    }

    private boolean jj_3_16() {
        return jj_3R_30();
    }

    private boolean jj_3R_47() {
        return jj_scan_token(35);
    }

    private boolean jj_3_15() {
        return jj_3R_29();
    }

    private boolean jj_3R_38() {
        return jj_3R_39();
    }

    private boolean jj_3R_46() {
        return jj_scan_token(36);
    }

    private boolean jj_3_14() {
        return jj_3R_28();
    }

    private boolean jj_3R_62() {
        return jj_3R_33();
    }

    private boolean jj_3R_45() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_46()) {
            this.jj_scanpos = xsp;
            if (jj_3R_47()) {
                this.jj_scanpos = xsp;
                if (jj_3R_48()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_49()) {
                        this.jj_scanpos = xsp;
                        return jj_3R_50();
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean jj_3R_37() {
        return jj_3R_38();
    }

    private boolean jj_3R_22() {
        return jj_scan_token(54) || jj_3R_31();
    }

    private boolean jj_3_9() {
        if (jj_scan_token(51)) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_24()) {
            this.jj_scanpos = xsp;
        }
        return jj_scan_token(54);
    }

    private boolean jj_3R_36() {
        return jj_3R_37();
    }

    private boolean jj_3R_61() {
        return jj_scan_token(54);
    }

    private boolean jj_3R_60() {
        return jj_scan_token(44);
    }

    private boolean jj_3R_59() {
        return jj_3R_64();
    }

    private boolean jj_3_3() {
        return jj_3R_21();
    }

    private boolean jj_3R_21() {
        return jj_scan_token(64) || jj_scan_token(44);
    }

    private boolean jj_3R_58() {
        Token xsp = this.jj_scanpos;
        if (jj_3_3()) {
            this.jj_scanpos = xsp;
            return jj_3R_63();
        }
        return false;
    }

    private boolean jj_3R_35() {
        return jj_3R_36();
    }

    private boolean jj_3R_44() {
        return jj_3R_45();
    }

    private boolean jj_3_8() {
        return jj_3R_23();
    }

    private boolean jj_3R_57() {
        return jj_3R_62();
    }

    private boolean jj_3R_34() {
        return jj_3R_35();
    }

    private boolean jj_3_7() {
        return jj_scan_token(4) || jj_scan_token(52);
    }

    private boolean jj_3R_25() {
        return jj_scan_token(64) || jj_scan_token(44);
    }

    private boolean jj_3_6() {
        return jj_scan_token(51) || jj_scan_token(64);
    }

    private boolean jj_3_5() {
        return jj_scan_token(50);
    }

    private boolean jj_3R_27() {
        return jj_3R_34();
    }

    private boolean jj_3_4() {
        return jj_scan_token(49);
    }

    private boolean jj_3R_65() {
        return jj_scan_token(64);
    }

    private boolean jj_3R_43() {
        return jj_3R_44();
    }

    static {
        jj_la1_init_0();
        jj_la1_init_1();
        jj_la1_init_2();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[]{2, 4, 8, 96, 96, 384, 384, 1536, 1536, 6144, 6144, CpioConstants.C_ISBLK, CpioConstants.C_ISBLK, 491520, 98304, 393216, 491520, 536346624, 1572864, 6291456, 25165824, 100663296, 536346624, -536870912, 1610612736, Integer.MIN_VALUE, 0, -536870912, 0, 0, 0, 0, 268435456, 0, 0, 268435472, 0, 0, 0, 0, 0, 0, 0, 2, 268435472, 0, 2, 268435472, 0, 0, 0, 16, 0, 0, 0, 2, 268435472, 2, 268435472, 2, 268435472, 2, 268435472, 0};
    }

    private static void jj_la1_init_1() {
        jj_la1_1 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 1, 6, 7, 24, 24, 224, 224, 512, 2048, 1024, 56611608, 1054720, 0, 4194304, 4198400, 1054720, 0, 0, 0, 56611608, 16777216, 0, 56611608, 114688, 16777216, 5246976, 0, 0, 0, 2048, 0, 56611608, 0, 56611608, 0, 56611608, 0, 56611608, 1048576};
    }

    private static void jj_la1_init_2() {
        jj_la1_2 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 233993, 8, 1, 0, 1, 8, 233984, 1, 0, 233993, 0, 0, 233993, 233984, 0, 9, 0, 1, 1, 0, 0, 233993, 0, 233993, 0, 233993, 0, 233993, 8};
    }

    public OgnlParser(InputStream stream) {
        this(stream, null);
    }

    public OgnlParser(InputStream stream, String encoding) {
        this.jjtree = new JJTOgnlParserState();
        this.jj_lookingAhead = false;
        this.jj_la1 = new int[64];
        this.jj_2_rtns = new JJCalls[16];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        try {
            this.jj_input_stream = new JavaCharStream(stream, encoding, 1, 1);
            this.token_source = new OgnlParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            for (int i = 0; i < 64; i++) {
                this.jj_la1[i] = -1;
            }
            for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                this.jj_2_rtns[i2] = new JJCalls();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void ReInit(InputStream stream) {
        ReInit(stream, null);
    }

    public void ReInit(InputStream stream, String encoding) {
        try {
            this.jj_input_stream.ReInit(stream, encoding, 1, 1);
            this.token_source.ReInit(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jjtree.reset();
            this.jj_gen = 0;
            for (int i = 0; i < 64; i++) {
                this.jj_la1[i] = -1;
            }
            for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                this.jj_2_rtns[i2] = new JJCalls();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public OgnlParser(Reader stream) {
        this.jjtree = new JJTOgnlParserState();
        this.jj_lookingAhead = false;
        this.jj_la1 = new int[64];
        this.jj_2_rtns = new JJCalls[16];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.jj_input_stream = new JavaCharStream(stream, 1, 1);
        this.token_source = new OgnlParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 64; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(Reader stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        this.jj_gen = 0;
        for (int i = 0; i < 64; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public OgnlParser(OgnlParserTokenManager tm) {
        this.jjtree = new JJTOgnlParserState();
        this.jj_lookingAhead = false;
        this.jj_la1 = new int[64];
        this.jj_2_rtns = new JJCalls[16];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 64; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(OgnlParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        this.jj_gen = 0;
        for (int i = 0; i < 64; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken = this.token;
        if (oldToken.next != null) {
            this.token = this.token.next;
        } else {
            Token token = this.token;
            Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        if (this.token.kind == kind) {
            this.jj_gen++;
            int i = this.jj_gc + 1;
            this.jj_gc = i;
            if (i > 100) {
                this.jj_gc = 0;
                for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                    JJCalls jJCalls = this.jj_2_rtns[i2];
                    while (true) {
                        JJCalls c = jJCalls;
                        if (c != null) {
                            if (c.gen < this.jj_gen) {
                                c.first = null;
                            }
                            jJCalls = c.next;
                        }
                    }
                }
            }
            return this.token;
        }
        this.token = oldToken;
        this.jj_kind = kind;
        throw generateParseException();
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlParser$LookaheadSuccess.class */
    private static final class LookaheadSuccess extends Error {
        private LookaheadSuccess() {
        }
    }

    private boolean jj_scan_token(int kind) {
        Token tok;
        if (this.jj_scanpos == this.jj_lastpos) {
            this.jj_la--;
            if (this.jj_scanpos.next == null) {
                Token token = this.jj_scanpos;
                Token nextToken = this.token_source.getNextToken();
                token.next = nextToken;
                this.jj_scanpos = nextToken;
                this.jj_lastpos = nextToken;
            } else {
                Token token2 = this.jj_scanpos.next;
                this.jj_scanpos = token2;
                this.jj_lastpos = token2;
            }
        } else {
            this.jj_scanpos = this.jj_scanpos.next;
        }
        if (this.jj_rescan) {
            int i = 0;
            Token token3 = this.token;
            while (true) {
                tok = token3;
                if (tok == null || tok == this.jj_scanpos) {
                    break;
                }
                i++;
                token3 = tok.next;
            }
            if (tok != null) {
                jj_add_error_token(kind, i);
            }
        }
        if (this.jj_scanpos.kind != kind) {
            return true;
        }
        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) {
            throw this.jj_ls;
        }
        return false;
    }

    public final Token getNextToken() {
        if (this.token.next != null) {
            this.token = this.token.next;
        } else {
            Token token = this.token;
            Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        this.jj_gen++;
        return this.token;
    }

    public final Token getToken(int index) {
        Token token;
        Token t = this.jj_lookingAhead ? this.jj_scanpos : this.token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) {
                token = t.next;
            } else {
                Token nextToken = this.token_source.getNextToken();
                token = nextToken;
                t.next = nextToken;
            }
            t = token;
        }
        return t;
    }

    private int jj_ntk() {
        Token token = this.token.next;
        this.jj_nt = token;
        if (token == null) {
            Token token2 = this.token;
            Token nextToken = this.token_source.getNextToken();
            token2.next = nextToken;
            int i = nextToken.kind;
            this.jj_ntk = i;
            return i;
        }
        int i2 = this.jj_nt.kind;
        this.jj_ntk = i2;
        return i2;
    }

    private void jj_add_error_token(int kind, int pos) {
        if (pos >= 100) {
            return;
        }
        if (pos == this.jj_endpos + 1) {
            int[] iArr = this.jj_lasttokens;
            int i = this.jj_endpos;
            this.jj_endpos = i + 1;
            iArr[i] = kind;
            return;
        }
        if (this.jj_endpos != 0) {
            this.jj_expentry = new int[this.jj_endpos];
            for (int i2 = 0; i2 < this.jj_endpos; i2++) {
                this.jj_expentry[i2] = this.jj_lasttokens[i2];
            }
            Iterator it = this.jj_expentries.iterator();
            loop1: while (true) {
                if (!it.hasNext()) {
                    break;
                }
                int[] oldentry = (int[]) it.next();
                if (oldentry.length == this.jj_expentry.length) {
                    for (int i3 = 0; i3 < this.jj_expentry.length; i3++) {
                        if (oldentry[i3] != this.jj_expentry[i3]) {
                            break;
                        }
                    }
                    this.jj_expentries.add(this.jj_expentry);
                    break loop1;
                }
            }
            if (pos != 0) {
                int[] iArr2 = this.jj_lasttokens;
                this.jj_endpos = pos;
                iArr2[pos - 1] = kind;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v16, types: [int[], int[][]] */
    public ParseException generateParseException() {
        this.jj_expentries.clear();
        boolean[] la1tokens = new boolean[86];
        if (this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int i = 0; i < 64; i++) {
            if (this.jj_la1[i] == this.jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                    if ((jj_la1_1[i] & (1 << j)) != 0) {
                        la1tokens[32 + j] = true;
                    }
                    if ((jj_la1_2[i] & (1 << j)) != 0) {
                        la1tokens[64 + j] = true;
                    }
                }
            }
        }
        for (int i2 = 0; i2 < 86; i2++) {
            if (la1tokens[i2]) {
                this.jj_expentry = new int[1];
                this.jj_expentry[0] = i2;
                this.jj_expentries.add(this.jj_expentry);
            }
        }
        this.jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        ?? r0 = new int[this.jj_expentries.size()];
        for (int i3 = 0; i3 < this.jj_expentries.size(); i3++) {
            r0[i3] = (int[]) this.jj_expentries.get(i3);
        }
        return new ParseException(this.token, r0, tokenImage);
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }

    private void jj_rescan_token() {
        this.jj_rescan = true;
        for (int i = 0; i < 16; i++) {
            try {
                JJCalls p = this.jj_2_rtns[i];
                do {
                    if (p.gen > this.jj_gen) {
                        this.jj_la = p.arg;
                        Token token = p.first;
                        this.jj_scanpos = token;
                        this.jj_lastpos = token;
                        switch (i) {
                            case 0:
                                jj_3_1();
                                break;
                            case 1:
                                jj_3_2();
                                break;
                            case 2:
                                jj_3_3();
                                break;
                            case 3:
                                jj_3_4();
                                break;
                            case 4:
                                jj_3_5();
                                break;
                            case 5:
                                jj_3_6();
                                break;
                            case 6:
                                jj_3_7();
                                break;
                            case 7:
                                jj_3_8();
                                break;
                            case 8:
                                jj_3_9();
                                break;
                            case 9:
                                jj_3_10();
                                break;
                            case 10:
                                jj_3_11();
                                break;
                            case 11:
                                jj_3_12();
                                break;
                            case 12:
                                jj_3_13();
                                break;
                            case 13:
                                jj_3_14();
                                break;
                            case 14:
                                jj_3_15();
                                break;
                            case 15:
                                jj_3_16();
                                break;
                        }
                    }
                    p = p.next;
                } while (p != null);
            } catch (LookaheadSuccess e) {
            }
        }
        this.jj_rescan = false;
    }

    private void jj_save(int index, int xla) {
        JJCalls p;
        JJCalls jJCalls = this.jj_2_rtns[index];
        while (true) {
            p = jJCalls;
            if (p.gen <= this.jj_gen) {
                break;
            }
            if (p.next == null) {
                JJCalls jJCalls2 = new JJCalls();
                p.next = jJCalls2;
                p = jJCalls2;
                break;
            }
            jJCalls = p.next;
        }
        p.gen = (this.jj_gen + xla) - this.jj_la;
        p.first = this.token;
        p.arg = xla;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlParser$JJCalls.class */
    static final class JJCalls {
        int gen;
        Token first;
        int arg;
        JJCalls next;

        JJCalls() {
        }
    }
}
