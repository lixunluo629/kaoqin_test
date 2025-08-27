package org.yaml.snakeyaml.error;

import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/error/MarkedYAMLException.class */
public class MarkedYAMLException extends YAMLException {
    private static final long serialVersionUID = -9119388488683035101L;
    private String context;
    private Mark contextMark;
    private String problem;
    private Mark problemMark;
    private String note;

    protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, String note) {
        this(context, contextMark, problem, problemMark, note, null);
    }

    protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, String note, Throwable cause) {
        super(context + "; " + problem + "; " + problemMark, cause);
        this.context = context;
        this.contextMark = contextMark;
        this.problem = problem;
        this.problemMark = problemMark;
        this.note = note;
    }

    protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark) {
        this(context, contextMark, problem, problemMark, null, null);
    }

    protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, Throwable cause) {
        this(context, contextMark, problem, problemMark, null, cause);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return toString();
    }

    @Override // java.lang.Throwable
    public String toString() {
        StringBuilder lines = new StringBuilder();
        if (this.context != null) {
            lines.append(this.context);
            lines.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.contextMark != null && (this.problem == null || this.problemMark == null || this.contextMark.getName().equals(this.problemMark.getName()) || this.contextMark.getLine() != this.problemMark.getLine() || this.contextMark.getColumn() != this.problemMark.getColumn())) {
            lines.append(this.contextMark.toString());
            lines.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.problem != null) {
            lines.append(this.problem);
            lines.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.problemMark != null) {
            lines.append(this.problemMark.toString());
            lines.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        if (this.note != null) {
            lines.append(this.note);
            lines.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return lines.toString();
    }

    public String getContext() {
        return this.context;
    }

    public Mark getContextMark() {
        return this.contextMark;
    }

    public String getProblem() {
        return this.problem;
    }

    public Mark getProblemMark() {
        return this.problemMark;
    }
}
