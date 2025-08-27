package org.yaml.snakeyaml.error;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.yaml.snakeyaml.scanner.Constant;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/error/Mark.class */
public final class Mark {
    private String name;
    private int index;
    private int line;
    private int column;
    private String buffer;
    private int pointer;

    public Mark(String name, int index, int line, int column, String buffer, int pointer) {
        this.name = name;
        this.index = index;
        this.line = line;
        this.column = column;
        this.buffer = buffer;
        this.pointer = pointer;
    }

    private boolean isLineBreak(char ch2) {
        return Constant.NULL_OR_LINEBR.has(ch2);
    }

    public String get_snippet(int indent, int max_length) {
        if (this.buffer == null) {
            return null;
        }
        float half = (max_length / 2) - 1;
        int start = this.pointer;
        String head = "";
        while (true) {
            if (start <= 0 || isLineBreak(this.buffer.charAt(start - 1))) {
                break;
            }
            start--;
            if (this.pointer - start > half) {
                head = " ... ";
                start += 5;
                break;
            }
        }
        String tail = "";
        int end = this.pointer;
        while (true) {
            if (end >= this.buffer.length() || isLineBreak(this.buffer.charAt(end))) {
                break;
            }
            end++;
            if (end - this.pointer > half) {
                tail = " ... ";
                end -= 5;
                break;
            }
        }
        String snippet = this.buffer.substring(start, end);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            result.append(SymbolConstants.SPACE_SYMBOL);
        }
        result.append(head);
        result.append(snippet);
        result.append(tail);
        result.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (int i2 = 0; i2 < ((indent + this.pointer) - start) + head.length(); i2++) {
            result.append(SymbolConstants.SPACE_SYMBOL);
        }
        result.append("^");
        return result.toString();
    }

    public String get_snippet() {
        return get_snippet(4, 75);
    }

    public String toString() {
        String snippet = get_snippet();
        StringBuilder where = new StringBuilder(" in ");
        where.append(this.name);
        where.append(", line ");
        where.append(this.line + 1);
        where.append(", column ");
        where.append(this.column + 1);
        if (snippet != null) {
            where.append(":\n");
            where.append(snippet);
        }
        return where.toString();
    }

    public String getName() {
        return this.name;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public int getIndex() {
        return this.index;
    }
}
