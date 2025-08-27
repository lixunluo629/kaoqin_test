package org.apache.poi.hssf.extractor;

import ch.qos.logback.classic.net.SyslogAppender;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Locale;
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/extractor/ExcelExtractor.class */
public class ExcelExtractor extends POIOLE2TextExtractor implements org.apache.poi.ss.extractor.ExcelExtractor {
    private final HSSFWorkbook _wb;
    private final HSSFDataFormatter _formatter;
    private boolean _includeSheetNames;
    private boolean _shouldEvaluateFormulas;
    private boolean _includeCellComments;
    private boolean _includeBlankCells;
    private boolean _includeHeadersFooters;

    public ExcelExtractor(HSSFWorkbook wb) {
        super(wb);
        this._includeSheetNames = true;
        this._shouldEvaluateFormulas = true;
        this._includeCellComments = false;
        this._includeBlankCells = false;
        this._includeHeadersFooters = true;
        this._wb = wb;
        this._formatter = new HSSFDataFormatter();
    }

    public ExcelExtractor(POIFSFileSystem fs) throws IOException {
        this(fs.getRoot());
    }

    public ExcelExtractor(DirectoryNode dir) throws IOException {
        this(new HSSFWorkbook(dir, true));
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/extractor/ExcelExtractor$CommandParseException.class */
    private static final class CommandParseException extends Exception {
        public CommandParseException(String msg) {
            super(msg);
        }
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/extractor/ExcelExtractor$CommandArgs.class */
    private static final class CommandArgs {
        private final boolean _requestHelp;
        private final File _inputFile;
        private final boolean _showSheetNames;
        private final boolean _evaluateFormulas;
        private final boolean _showCellComments;
        private final boolean _showBlankCells;
        private final boolean _headersFooters;

        /* JADX WARN: Code restructure failed: missing block: B:44:0x0164, code lost:
        
            r5._requestHelp = r9;
            r5._inputFile = r8;
            r5._showSheetNames = r10;
            r5._evaluateFormulas = r11;
            r5._showCellComments = r12;
            r5._showBlankCells = r13;
            r5._headersFooters = r14;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x018d, code lost:
        
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public CommandArgs(java.lang.String[] r6) throws org.apache.poi.hssf.extractor.ExcelExtractor.CommandParseException {
            /*
                Method dump skipped, instructions count: 398
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.extractor.ExcelExtractor.CommandArgs.<init>(java.lang.String[]):void");
        }

        private static boolean parseBoolArg(String[] args, int i) throws CommandParseException {
            if (i >= args.length) {
                throw new CommandParseException("Expected value after '" + args[i - 1] + "'");
            }
            String value = args[i].toUpperCase(Locale.ROOT);
            if ("Y".equals(value) || "YES".equals(value) || "ON".equals(value) || "TRUE".equals(value)) {
                return true;
            }
            if ("N".equals(value) || "NO".equals(value) || "OFF".equals(value) || "FALSE".equals(value)) {
                return false;
            }
            throw new CommandParseException("Invalid value '" + args[i] + "' for '" + args[i - 1] + "'. Expected 'Y' or 'N'");
        }

        public boolean isRequestHelp() {
            return this._requestHelp;
        }

        public File getInputFile() {
            return this._inputFile;
        }

        public boolean shouldShowSheetNames() {
            return this._showSheetNames;
        }

        public boolean shouldEvaluateFormulas() {
            return this._evaluateFormulas;
        }

        public boolean shouldShowCellComments() {
            return this._showCellComments;
        }

        public boolean shouldShowBlankCells() {
            return this._showBlankCells;
        }

        public boolean shouldIncludeHeadersFooters() {
            return this._headersFooters;
        }
    }

    private static void printUsageMessage(PrintStream ps) {
        ps.println("Use:");
        ps.println("    " + ExcelExtractor.class.getName() + " [<flag> <value> [<flag> <value> [...]]] [-i <filename.xls>]");
        ps.println("       -i <filename.xls> specifies input file (default is to use stdin)");
        ps.println("       Flags can be set on or off by using the values 'Y' or 'N'.");
        ps.println("       Following are available flags and their default values:");
        ps.println("       --show-sheet-names  Y");
        ps.println("       --evaluate-formulas Y");
        ps.println("       --show-comments     N");
        ps.println("       --show-blanks       Y");
        ps.println("       --headers-footers   Y");
    }

    public static void main(String[] args) throws IOException {
        InputStream is;
        try {
            CommandArgs cmdArgs = new CommandArgs(args);
            if (cmdArgs.isRequestHelp()) {
                printUsageMessage(System.out);
                return;
            }
            if (cmdArgs.getInputFile() == null) {
                is = System.in;
            } else {
                is = new FileInputStream(cmdArgs.getInputFile());
            }
            HSSFWorkbook wb = new HSSFWorkbook(is);
            is.close();
            ExcelExtractor extractor = new ExcelExtractor(wb);
            extractor.setIncludeSheetNames(cmdArgs.shouldShowSheetNames());
            extractor.setFormulasNotResults(!cmdArgs.shouldEvaluateFormulas());
            extractor.setIncludeCellComments(cmdArgs.shouldShowCellComments());
            extractor.setIncludeBlankCells(cmdArgs.shouldShowBlankCells());
            extractor.setIncludeHeadersFooters(cmdArgs.shouldIncludeHeadersFooters());
            System.out.println(extractor.getText());
            extractor.close();
            wb.close();
        } catch (CommandParseException e) {
            System.err.println(e.getMessage());
            printUsageMessage(System.err);
            System.exit(1);
        }
    }

    @Override // org.apache.poi.ss.extractor.ExcelExtractor
    public void setIncludeSheetNames(boolean includeSheetNames) {
        this._includeSheetNames = includeSheetNames;
    }

    @Override // org.apache.poi.ss.extractor.ExcelExtractor
    public void setFormulasNotResults(boolean formulasNotResults) {
        this._shouldEvaluateFormulas = !formulasNotResults;
    }

    @Override // org.apache.poi.ss.extractor.ExcelExtractor
    public void setIncludeCellComments(boolean includeCellComments) {
        this._includeCellComments = includeCellComments;
    }

    public void setIncludeBlankCells(boolean includeBlankCells) {
        this._includeBlankCells = includeBlankCells;
    }

    @Override // org.apache.poi.ss.extractor.ExcelExtractor
    public void setIncludeHeadersFooters(boolean includeHeadersFooters) {
        this._includeHeadersFooters = includeHeadersFooters;
    }

    @Override // org.apache.poi.POITextExtractor
    public String getText() {
        String name;
        StringBuffer text = new StringBuffer();
        this._wb.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        for (int i = 0; i < this._wb.getNumberOfSheets(); i++) {
            HSSFSheet sheet = this._wb.getSheetAt(i);
            if (sheet != null) {
                if (this._includeSheetNames && (name = this._wb.getSheetName(i)) != null) {
                    text.append(name);
                    text.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
                if (this._includeHeadersFooters) {
                    text.append(_extractHeaderFooter(sheet.getHeader()));
                }
                int firstRow = sheet.getFirstRowNum();
                int lastRow = sheet.getLastRowNum();
                for (int j = firstRow; j <= lastRow; j++) {
                    HSSFRow row = sheet.getRow(j);
                    if (row != null) {
                        int firstCell = row.getFirstCellNum();
                        int lastCell = row.getLastCellNum();
                        if (this._includeBlankCells) {
                            firstCell = 0;
                        }
                        for (int k = firstCell; k < lastCell; k++) {
                            HSSFCell cell = row.getCell(k);
                            boolean outputContents = true;
                            if (cell == null) {
                                outputContents = this._includeBlankCells;
                            } else {
                                switch (cell.getCellTypeEnum()) {
                                    case STRING:
                                        text.append(cell.getRichStringCellValue().getString());
                                        break;
                                    case NUMERIC:
                                        text.append(this._formatter.formatCellValue(cell));
                                        break;
                                    case BOOLEAN:
                                        text.append(cell.getBooleanCellValue());
                                        break;
                                    case ERROR:
                                        text.append(ErrorEval.getText(cell.getErrorCellValue()));
                                        break;
                                    case FORMULA:
                                        if (!this._shouldEvaluateFormulas) {
                                            text.append(cell.getCellFormula());
                                            break;
                                        } else {
                                            switch (cell.getCachedFormulaResultTypeEnum()) {
                                                case STRING:
                                                    HSSFRichTextString str = cell.getRichStringCellValue();
                                                    if (str != null && str.length() > 0) {
                                                        text.append(str);
                                                        break;
                                                    }
                                                    break;
                                                case NUMERIC:
                                                    HSSFCellStyle style = cell.getCellStyle();
                                                    double nVal = cell.getNumericCellValue();
                                                    short df = style.getDataFormat();
                                                    String dfs = style.getDataFormatString();
                                                    text.append(this._formatter.formatRawCellContents(nVal, df, dfs));
                                                    break;
                                                case BOOLEAN:
                                                    text.append(cell.getBooleanCellValue());
                                                    break;
                                                case ERROR:
                                                    text.append(ErrorEval.getText(cell.getErrorCellValue()));
                                                    break;
                                                default:
                                                    throw new IllegalStateException("Unexpected cell cached formula result type: " + cell.getCachedFormulaResultTypeEnum());
                                            }
                                        }
                                    default:
                                        throw new RuntimeException("Unexpected cell type (" + cell.getCellTypeEnum() + ")");
                                }
                                HSSFComment comment = cell.getCellComment();
                                if (this._includeCellComments && comment != null) {
                                    String commentText = comment.getString().getString().replace('\n', ' ');
                                    text.append(" Comment by " + comment.getAuthor() + ": " + commentText);
                                }
                            }
                            if (outputContents && k < lastCell - 1) {
                                text.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
                            }
                        }
                        text.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                    }
                }
                if (this._includeHeadersFooters) {
                    text.append(_extractHeaderFooter(sheet.getFooter()));
                }
            }
        }
        return text.toString();
    }

    public static String _extractHeaderFooter(HeaderFooter hf) {
        StringBuffer text = new StringBuffer();
        if (hf.getLeft() != null) {
            text.append(hf.getLeft());
        }
        if (hf.getCenter() != null) {
            if (text.length() > 0) {
                text.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
            }
            text.append(hf.getCenter());
        }
        if (hf.getRight() != null) {
            if (text.length() > 0) {
                text.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
            }
            text.append(hf.getRight());
        }
        if (text.length() > 0) {
            text.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return text.toString();
    }
}
