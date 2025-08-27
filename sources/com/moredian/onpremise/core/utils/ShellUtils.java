package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/ShellUtils.class */
public class ShellUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ShellUtils.class);
    private static final String[] EXCLUDE_DBS = {"on_premise_terminal_sync_record", "on_premise_oper_log", "on_premise_warn_record", "on_premise_verify_record"};

    public static String exportDataSql(String fileName) {
        return doExec(getExportExec(fileName));
    }

    public static String importDataSql(String fileName) {
        return doExec(getImportExec(fileName.substring(0, fileName.indexOf(".")) + ".sql"));
    }

    public static void upgradeServer(String fileName) {
        StringBuffer result = new StringBuffer();
        Process process = null;
        try {
            try {
                logger.info("====start upgradeServer");
                String[] instruction = getUpgradeServer(fileName);
                logger.info("====start instruction :{}", JsonUtils.toJson(instruction));
                process = Runtime.getRuntime().exec(instruction);
                BufferedReader bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                BufferedReader bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
                while (true) {
                    String line = bufrIn.readLine();
                    if (line == null) {
                        break;
                    } else {
                        result.append(line).append('\n');
                    }
                }
                while (true) {
                    String line2 = bufrError.readLine();
                    if (line2 == null) {
                        break;
                    } else {
                        result.append(line2).append('\n');
                    }
                }
                logger.info("========result :{}", result.toString());
                Thread.sleep(5000L);
                logger.info("====end upgradeServer");
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                logger.error("error:{}", (Throwable) e);
                logger.info("====end upgradeServer");
                if (process != null) {
                    process.destroy();
                }
            }
        } catch (Throwable th) {
            logger.info("====end upgradeServer");
            if (process != null) {
                process.destroy();
            }
            throw th;
        }
    }

    private static String[] getExportExec(String fileName) {
        StringBuffer instruction = new StringBuffer();
        for (String db : EXCLUDE_DBS) {
            instruction.append("--ignore-table=on_premise.");
            instruction.append(db);
            instruction.append(SymbolConstants.SPACE_SYMBOL);
        }
        instruction.deleteCharAt(instruction.length() - 1);
        return new String[]{"/home/cmd/mysql_dump.sh", instruction.toString(), fileName};
    }

    private static String[] getImportExec(String fileName) {
        return new String[]{"/home/cmd/update_sql.sh", fileName};
    }

    private static String[] getUpgradeServer(String fileName) {
        return new String[]{"/home/cmd/run_deploy.sh", "deploy", fileName};
    }

    private static String doExec(String[] instruction) throws IOException {
        logger.info("===execute instruction :{}", JsonUtils.toJson(instruction));
        StringBuffer result = new StringBuffer();
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            try {
                Runtime run = Runtime.getRuntime();
                process = run.exec(instruction);
                process.waitFor();
                bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
                while (true) {
                    String line = bufrIn.readLine();
                    if (line == null) {
                        break;
                    }
                    result.append(line).append('\n');
                }
                while (true) {
                    String line2 = bufrError.readLine();
                    if (line2 == null) {
                        break;
                    }
                    result.append(line2).append('\n');
                }
                MyFileUtils.closeStream(bufrIn);
                MyFileUtils.closeStream(bufrError);
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                logger.error("error:{}", (Throwable) e);
                MyFileUtils.closeStream(bufrIn);
                MyFileUtils.closeStream(bufrError);
                if (process != null) {
                    process.destroy();
                }
            }
            return result.toString();
        } catch (Throwable th) {
            MyFileUtils.closeStream(bufrIn);
            MyFileUtils.closeStream(bufrError);
            if (process != null) {
                process.destroy();
            }
            throw th;
        }
    }
}
