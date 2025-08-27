package org.hyperic.sigar.shell;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hyperic.sigar.util.GetlineCompleter;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/CollectionCompleter.class */
public class CollectionCompleter implements GetlineCompleter {
    private ArrayList completions;
    private ShellBase shell;
    private PrintStream out;
    private Collection collection;

    public CollectionCompleter() {
        this.completions = new ArrayList();
        this.shell = null;
        this.out = System.out;
    }

    public CollectionCompleter(ShellBase shell) {
        this.completions = new ArrayList();
        this.shell = null;
        this.out = System.out;
        this.shell = shell;
        this.out = shell.getOutStream();
    }

    public CollectionCompleter(ShellBase shell, Collection collection) {
        this(shell);
        setCollection(collection);
    }

    public Iterator getIterator() {
        return getCollection().iterator();
    }

    public Collection getCollection() {
        return this.collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    private boolean startsWith(String substr, String[] possible) {
        for (String str : possible) {
            if (!str.startsWith(substr)) {
                return false;
            }
        }
        return true;
    }

    public String getPartialCompletion(String[] possible) {
        if (possible.length == 0) {
            return "";
        }
        String match = possible[0];
        StringBuffer lcd = new StringBuffer();
        for (int i = 0; i < match.length() && startsWith(match.substring(0, i + 1), possible); i++) {
            lcd.append(match.charAt(i));
        }
        return lcd.toString();
    }

    public String displayPossible(List possible) {
        return displayPossible((String[]) possible.toArray(new String[possible.size()]));
    }

    public String displayPossible(String[] possible) {
        String partial = getPartialCompletion(possible);
        for (String match : possible) {
            this.out.println();
            this.out.print(new StringBuffer().append(match).append(SymbolConstants.SPACE_SYMBOL).toString());
        }
        if (this.shell != null) {
            this.shell.getGetline().redraw();
        }
        if (partial.length() > 0) {
            return partial;
        }
        return null;
    }

    @Override // org.hyperic.sigar.util.GetlineCompleter
    public String complete(String line) {
        this.completions.clear();
        int len = line.length();
        Iterator it = getIterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            if (len == 0 || name.startsWith(line)) {
                this.completions.add(name);
            }
        }
        int size = this.completions.size();
        switch (size) {
            case 0:
                return line;
            case 1:
                return (String) this.completions.get(0);
            default:
                String partial = displayPossible(this.completions);
                if (partial != null) {
                    return partial;
                }
                return line;
        }
    }
}
