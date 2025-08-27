package org.hyperic.sigar.test;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.ptql.MalformedQueryException;
import org.hyperic.sigar.ptql.ProcessQuery;
import org.hyperic.sigar.ptql.ProcessQueryFactory;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestPTQL.class */
public class TestPTQL extends SigarTestCase {
    private static final String OTHER_PROCESS = "Pid.Pid.ne=$$";
    private ProcessQueryFactory qf;
    private static final String JAVA_PROCESS = "State.Name.eq=java";
    private static final String THIS_PROCESS = "Pid.Pid.eq=$$";
    private static final String OTHER_JAVA_PROCESS = "State.Name.eq=java,Pid.Pid.ne=$$";
    private static final String[] OK_QUERIES = {JAVA_PROCESS, "Exe.Name.ew=java", "State.Name.eq=java,Exe.Cwd.eq=$user.dir", "State.Name.eq=java,Exe.Cwd.eq=$PWD", "State.Name.ne=java,Exe.Cwd.eq=$user.dir", "State.Name.sw=httpsd,State.Name.Pne=$1", "State.Name.ct=ssh", "State.Name.eq=java,Args.-1.ew=AgentDaemon", "Cred.Uid.eq=1003,State.Name.eq=java,Args.-1.ew=AgentDaemon", "Cred.Uid.gt=0,Cred.Uid.lt=1000", "Cred.Uid.eq=1003,Cred.Gid.eq=1003", "CredName.User.eq=dougm", "Time.Sys.gt=1000", "Fd.Total.gt=20", "Mem.Size.ge=10000000,Mem.Share.le=1000000", "State.Name.eq=sshd,Cred.Uid.eq=0", "State.Name.eq=crond,Cred.Uid.eq=0", "State.State.eq=R", "Args.0.eq=sendmail: accepting connections", "Args.0.sw=sendmail: Queue runner@", "Args.1000.eq=foo", "Args.*.eq=org.apache.tools.ant.Main", "Args.*.ct=java", "Args.*.ew=sigar.jar", "Modules.*.re=libc|kernel", "Port.tcp.eq=80,Cred.Uid.eq=0", "Port.udp.eq=161,Cred.Uid.eq=0", "Port.tcp.eq=8080,Cred.Uid.eq=1003", "Pid.PidFile.eq=pid.file", "Pid.Pid.eq=1", THIS_PROCESS, "Pid.Service.eq=Eventlog", "Service.Name.eq=NOSUCHSERVICE", "Service.Name.ct=Oracle", "Service.DisplayName.re=DHCP|DNS", "Service.Path.ct=svchost", "Service.Exe.Ieq=inetinfo.exe", OTHER_JAVA_PROCESS, "Cpu.Percent.ge=0.2", "State.Name.sw=java,Args.*.eq=org.jboss.Main", "State.Name.eq=java,Args.*.eq=com.ibm.ws.runtime.WsServer", "State.Name.eq=java,Args.-1.eq=weblogic.Server", "State.Name.eq=perl,Args.*.eq=v"};
    private static final String[] OK_RE_QUERIES = {"Args.-1.eq=weblogic.Server,Env.WEBLOGIC_CLASSPATH.re=.*weblogic.jar.*", "State.Name.re=https?d.*|[Aa]pache2?$,State.Name.Pne=$1", "State.Name.re=post(master|gres),State.Name.Pne=$1,Args.0.re=.*post(master|gres)$", "State.Name.re=cfmx7|java,State.Name.Pne=$1,Args.*.ct=jrun.jar"};
    private static final String[] MALFORMED_QUERIES = {"foo", "State.Name", "State.Name.eq", "State.Namex.eq=foo", "Statex.Name.eq=foo", "State.Name.eqx=foo", "State.Name.Xeq=foo", "State.Name.eq=foo,Args.*.eq=$3", "State.Name.eq=$1", "State.State.eq=read", "Args.x.eq=foo", "Time.Sys.gt=x", "Pid.Pid.eq=foo", "Cpu.Percent.ge=x", "Port.foo.eq=8080", "Port.tcp.gt=8080", "Port.tcp.eq=http", "Cpu.Sys.ew=lots", "Service.Invalid.ew=.exe", "", null};

    public TestPTQL(String name) {
        super(name);
    }

    @Override // junit.framework.TestCase
    protected void setUp() throws Exception {
        super.setUp();
        this.qf = new ProcessQueryFactory();
    }

    @Override // junit.framework.TestCase
    protected void tearDown() throws Exception {
        super.tearDown();
        this.qf.clear();
    }

    private int runQuery(Sigar sigar, String qs) throws SigarException {
        try {
            ProcessQuery query = this.qf.getQuery(qs);
            try {
                long[] pids = query.find(sigar);
                traceln(new StringBuffer().append(pids.length).append(" processes match: ").append(qs).toString());
                if (qs.indexOf(OTHER_PROCESS) != -1) {
                    long pid = sigar.getPid();
                    for (int i = 0; i < pids.length; i++) {
                        assertTrue(new StringBuffer().append(pid).append("!=").append(pids[i]).toString(), pid != pids[i]);
                    }
                }
                return pids.length;
            } catch (SigarNotImplementedException e) {
                return 0;
            } catch (SigarException e2) {
                traceln(new StringBuffer().append("Failed query: ").append(qs).toString());
                throw e2;
            }
        } catch (MalformedQueryException e3) {
            traceln(new StringBuffer().append("parse error: ").append(qs).toString());
            throw e3;
        }
    }

    public void testValidQueries() throws Exception {
        Sigar sigar = getSigar();
        assertTrue(THIS_PROCESS, runQuery(sigar, THIS_PROCESS) == 1);
        int numProcs = runQuery(sigar, JAVA_PROCESS);
        int numOtherProcs = runQuery(sigar, OTHER_JAVA_PROCESS);
        String msg = new StringBuffer().append("State.Name.eq=java [").append(numProcs).append("] vs. [").append(numOtherProcs).append("] ").append(OTHER_JAVA_PROCESS).toString();
        traceln(msg);
        for (int i = 0; i < OK_QUERIES.length; i++) {
            String qs = OK_QUERIES[i];
            assertTrue(qs, runQuery(sigar, qs) >= 0);
        }
    }

    public void testValidRegexQueries() throws Exception {
        for (int i = 0; i < OK_RE_QUERIES.length; i++) {
            String qs = OK_RE_QUERIES[i];
            assertTrue(qs, runQuery(getSigar(), qs) >= 0);
        }
    }

    public void testMalformedQueries() throws Exception {
        for (int i = 0; i < MALFORMED_QUERIES.length; i++) {
            String qs = MALFORMED_QUERIES[i];
            try {
                runQuery(getSigar(), qs);
                fail(new StringBuffer().append("'").append(qs).append("' did not throw MalformedQueryException").toString());
            } catch (MalformedQueryException e) {
                traceln(new StringBuffer().append(qs).append(": ").append(e.getMessage()).toString());
                assertTrue(new StringBuffer().append(qs).append(" Malformed").toString(), true);
            }
        }
    }

    public void testSelf() throws Exception {
        Sigar sigar = getSigar();
        ProcessQuery status = this.qf.getQuery("Cpu.Percent.ge=0.01");
        long pid = sigar.getPid();
        traceln(new StringBuffer().append("Cpu.Percent.ge=0.01").append(SymbolConstants.EQUAL_SYMBOL).append(status.match(sigar, pid)).toString());
    }
}
