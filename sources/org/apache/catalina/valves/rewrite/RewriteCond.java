package org.apache.catalina.valves.rewrite;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/rewrite/RewriteCond.class */
public class RewriteCond {
    protected String testString = null;
    protected String condPattern = null;
    protected String flagsString = null;
    protected boolean positive = true;
    protected Substitution test = null;
    protected ThreadLocal<Condition> condition = new ThreadLocal<>();
    public boolean nocase = false;
    public boolean ornext = false;

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/rewrite/RewriteCond$Condition.class */
    public static abstract class Condition {
        public abstract boolean evaluate(String str, Resolver resolver);
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/rewrite/RewriteCond$PatternCondition.class */
    public static class PatternCondition extends Condition {
        public Pattern pattern;
        public Matcher matcher = null;

        @Override // org.apache.catalina.valves.rewrite.RewriteCond.Condition
        public boolean evaluate(String value, Resolver resolver) {
            Matcher m = this.pattern.matcher(value);
            if (m.matches()) {
                this.matcher = m;
                return true;
            }
            return false;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/rewrite/RewriteCond$LexicalCondition.class */
    public static class LexicalCondition extends Condition {
        public int type = 0;
        public String condition;

        @Override // org.apache.catalina.valves.rewrite.RewriteCond.Condition
        public boolean evaluate(String value, Resolver resolver) {
            int result = value.compareTo(this.condition);
            switch (this.type) {
                case -1:
                    if (result < 0) {
                    }
                    break;
                case 0:
                    if (result == 0) {
                    }
                    break;
                case 1:
                    if (result > 0) {
                    }
                    break;
            }
            return false;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/rewrite/RewriteCond$ResourceCondition.class */
    public static class ResourceCondition extends Condition {
        public int type = 0;

        @Override // org.apache.catalina.valves.rewrite.RewriteCond.Condition
        public boolean evaluate(String value, Resolver resolver) {
            return resolver.resolveResource(this.type, value);
        }
    }

    public String getCondPattern() {
        return this.condPattern;
    }

    public void setCondPattern(String condPattern) {
        this.condPattern = condPattern;
    }

    public String getTestString() {
        return this.testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public final String getFlagsString() {
        return this.flagsString;
    }

    public final void setFlagsString(String flagsString) {
        this.flagsString = flagsString;
    }

    public void parse(Map<String, RewriteMap> maps) {
        this.test = new Substitution();
        this.test.setSub(this.testString);
        this.test.parse(maps);
        if (this.condPattern.startsWith("!")) {
            this.positive = false;
            this.condPattern = this.condPattern.substring(1);
        }
        if (this.condPattern.startsWith("<")) {
            LexicalCondition condition = new LexicalCondition();
            condition.type = -1;
            condition.condition = this.condPattern.substring(1);
            return;
        }
        if (this.condPattern.startsWith(">")) {
            LexicalCondition condition2 = new LexicalCondition();
            condition2.type = 1;
            condition2.condition = this.condPattern.substring(1);
            return;
        }
        if (this.condPattern.startsWith(SymbolConstants.EQUAL_SYMBOL)) {
            LexicalCondition condition3 = new LexicalCondition();
            condition3.type = 0;
            condition3.condition = this.condPattern.substring(1);
            return;
        }
        if (this.condPattern.equals("-d")) {
            ResourceCondition ncondition = new ResourceCondition();
            ncondition.type = 0;
            return;
        }
        if (this.condPattern.equals("-f")) {
            ResourceCondition ncondition2 = new ResourceCondition();
            ncondition2.type = 1;
        } else {
            if (this.condPattern.equals("-s")) {
                ResourceCondition ncondition3 = new ResourceCondition();
                ncondition3.type = 2;
                return;
            }
            PatternCondition condition4 = new PatternCondition();
            int flags = 0;
            if (isNocase()) {
                flags = 0 | 2;
            }
            condition4.pattern = Pattern.compile(this.condPattern, flags);
        }
    }

    public Matcher getMatcher() {
        Object condition = this.condition.get();
        if (condition instanceof PatternCondition) {
            return ((PatternCondition) condition).matcher;
        }
        return null;
    }

    public String toString() {
        return "RewriteCond " + this.testString + SymbolConstants.SPACE_SYMBOL + this.condPattern + (this.flagsString != null ? SymbolConstants.SPACE_SYMBOL + this.flagsString : "");
    }

    public boolean evaluate(Matcher rule, Matcher cond, Resolver resolver) {
        String value = this.test.evaluate(rule, cond, resolver);
        Condition condition = this.condition.get();
        if (condition == null) {
            if (this.condPattern.startsWith("<")) {
                LexicalCondition ncondition = new LexicalCondition();
                ncondition.type = -1;
                ncondition.condition = this.condPattern.substring(1);
                condition = ncondition;
            } else if (this.condPattern.startsWith(">")) {
                LexicalCondition ncondition2 = new LexicalCondition();
                ncondition2.type = 1;
                ncondition2.condition = this.condPattern.substring(1);
                condition = ncondition2;
            } else if (this.condPattern.startsWith(SymbolConstants.EQUAL_SYMBOL)) {
                LexicalCondition ncondition3 = new LexicalCondition();
                ncondition3.type = 0;
                ncondition3.condition = this.condPattern.substring(1);
                condition = ncondition3;
            } else if (this.condPattern.equals("-d")) {
                ResourceCondition ncondition4 = new ResourceCondition();
                ncondition4.type = 0;
                condition = ncondition4;
            } else if (this.condPattern.equals("-f")) {
                ResourceCondition ncondition5 = new ResourceCondition();
                ncondition5.type = 1;
                condition = ncondition5;
            } else if (this.condPattern.equals("-s")) {
                ResourceCondition ncondition6 = new ResourceCondition();
                ncondition6.type = 2;
                condition = ncondition6;
            } else {
                PatternCondition ncondition7 = new PatternCondition();
                int flags = 0;
                if (isNocase()) {
                    flags = 0 | 2;
                }
                ncondition7.pattern = Pattern.compile(this.condPattern, flags);
                condition = ncondition7;
            }
            this.condition.set(condition);
        }
        if (this.positive) {
            return condition.evaluate(value, resolver);
        }
        return !condition.evaluate(value, resolver);
    }

    public boolean isNocase() {
        return this.nocase;
    }

    public void setNocase(boolean nocase) {
        this.nocase = nocase;
    }

    public boolean isOrnext() {
        return this.ornext;
    }

    public void setOrnext(boolean ornext) {
        this.ornext = ornext;
    }

    public boolean isPositive() {
        return this.positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }
}
