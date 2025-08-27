package org.aspectj.weaver.loadtime;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Collections;
import java.util.List;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.util.LangUtil;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/Options.class */
public class Options {
    private static final String OPTION_15 = "-1.5";
    private static final String OPTION_lazyTjp = "-XlazyTjp";
    private static final String OPTION_noWarn = "-nowarn";
    private static final String OPTION_noWarnNone = "-warn:none";
    private static final String OPTION_proceedOnError = "-proceedOnError";
    private static final String OPTION_verbose = "-verbose";
    private static final String OPTION_debug = "-debug";
    private static final String OPTION_reweavable = "-Xreweavable";
    private static final String OPTION_noinline = "-Xnoinline";
    private static final String OPTION_addSerialVersionUID = "-XaddSerialVersionUID";
    private static final String OPTION_hasMember = "-XhasMember";
    private static final String OPTION_pinpoint = "-Xdev:pinpoint";
    private static final String OPTION_showWeaveInfo = "-showWeaveInfo";
    private static final String OPTIONVALUED_messageHandler = "-XmessageHandlerClass:";
    private static final String OPTIONVALUED_Xlintfile = "-Xlintfile:";
    private static final String OPTIONVALUED_Xlint = "-Xlint:";
    private static final String OPTIONVALUED_joinpoints = "-Xjoinpoints:";
    private static final String OPTIONVALUED_Xset = "-Xset:";
    private static final String OPTION_timers = "-timers";
    private static final String OPTIONVALUED_loadersToSkip = "-loadersToSkip:";

    public static WeaverOption parse(String options, ClassLoader laoder, IMessageHandler imh) {
        WeaverOption weaverOption = new WeaverOption(imh);
        if (LangUtil.isEmpty(options)) {
            return weaverOption;
        }
        List<String> flags = LangUtil.anySplit(options, SymbolConstants.SPACE_SYMBOL);
        Collections.reverse(flags);
        for (String arg : flags) {
            if (arg.startsWith(OPTIONVALUED_messageHandler) && arg.length() > OPTIONVALUED_messageHandler.length()) {
                String handlerClass = arg.substring(OPTIONVALUED_messageHandler.length()).trim();
                try {
                    Class handler = Class.forName(handlerClass, false, laoder);
                    weaverOption.messageHandler = (IMessageHandler) handler.newInstance();
                } catch (Throwable t) {
                    weaverOption.messageHandler.handleMessage(new Message("Cannot instantiate message handler " + handlerClass, IMessage.ERROR, t, (ISourceLocation) null));
                }
            }
        }
        for (String arg2 : flags) {
            if (arg2.equals(OPTION_15)) {
                weaverOption.java5 = true;
            } else if (arg2.equalsIgnoreCase(OPTION_lazyTjp)) {
                weaverOption.lazyTjp = true;
            } else if (arg2.equalsIgnoreCase(OPTION_noinline)) {
                weaverOption.noInline = true;
            } else if (arg2.equalsIgnoreCase(OPTION_addSerialVersionUID)) {
                weaverOption.addSerialVersionUID = true;
            } else if (arg2.equalsIgnoreCase(OPTION_noWarn) || arg2.equalsIgnoreCase(OPTION_noWarnNone)) {
                weaverOption.noWarn = true;
            } else if (arg2.equalsIgnoreCase(OPTION_proceedOnError)) {
                weaverOption.proceedOnError = true;
            } else if (arg2.equalsIgnoreCase(OPTION_reweavable)) {
                weaverOption.notReWeavable = false;
            } else if (arg2.equalsIgnoreCase(OPTION_showWeaveInfo)) {
                weaverOption.showWeaveInfo = true;
            } else if (arg2.equalsIgnoreCase(OPTION_hasMember)) {
                weaverOption.hasMember = true;
            } else if (arg2.startsWith(OPTIONVALUED_joinpoints)) {
                if (arg2.length() > OPTIONVALUED_joinpoints.length()) {
                    weaverOption.optionalJoinpoints = arg2.substring(OPTIONVALUED_joinpoints.length()).trim();
                }
            } else if (arg2.equalsIgnoreCase(OPTION_verbose)) {
                weaverOption.verbose = true;
            } else if (arg2.equalsIgnoreCase(OPTION_debug)) {
                weaverOption.debug = true;
            } else if (arg2.equalsIgnoreCase(OPTION_pinpoint)) {
                weaverOption.pinpoint = true;
            } else if (!arg2.startsWith(OPTIONVALUED_messageHandler)) {
                if (arg2.startsWith(OPTIONVALUED_Xlintfile)) {
                    if (arg2.length() > OPTIONVALUED_Xlintfile.length()) {
                        weaverOption.lintFile = arg2.substring(OPTIONVALUED_Xlintfile.length()).trim();
                    }
                } else if (arg2.startsWith(OPTIONVALUED_Xlint)) {
                    if (arg2.length() > OPTIONVALUED_Xlint.length()) {
                        weaverOption.lint = arg2.substring(OPTIONVALUED_Xlint.length()).trim();
                    }
                } else if (arg2.startsWith(OPTIONVALUED_Xset)) {
                    if (arg2.length() > OPTIONVALUED_Xlint.length()) {
                        weaverOption.xSet = arg2.substring(OPTIONVALUED_Xset.length()).trim();
                    }
                } else if (arg2.equalsIgnoreCase(OPTION_timers)) {
                    weaverOption.timers = true;
                } else if (arg2.startsWith(OPTIONVALUED_loadersToSkip)) {
                    if (arg2.length() > OPTIONVALUED_loadersToSkip.length()) {
                        String value = arg2.substring(OPTIONVALUED_loadersToSkip.length()).trim();
                        weaverOption.loadersToSkip = value;
                    }
                } else {
                    weaverOption.messageHandler.handleMessage(new Message("Cannot configure weaver with option '" + arg2 + "': unknown option", IMessage.WARNING, (Throwable) null, (ISourceLocation) null));
                }
            }
        }
        if (weaverOption.noWarn) {
            weaverOption.messageHandler.ignore(IMessage.WARNING);
        }
        if (weaverOption.verbose) {
            weaverOption.messageHandler.dontIgnore(IMessage.INFO);
        }
        if (weaverOption.debug) {
            weaverOption.messageHandler.dontIgnore(IMessage.DEBUG);
        }
        if (weaverOption.showWeaveInfo) {
            weaverOption.messageHandler.dontIgnore(IMessage.WEAVEINFO);
        }
        return weaverOption;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/Options$WeaverOption.class */
    public static class WeaverOption {
        boolean java5;
        boolean lazyTjp;
        boolean hasMember;
        String optionalJoinpoints;
        boolean noWarn;
        boolean proceedOnError;
        boolean verbose;
        boolean debug;
        boolean noInline;
        boolean addSerialVersionUID;
        boolean showWeaveInfo;
        boolean pinpoint;
        IMessageHandler messageHandler;
        String lint;
        String lintFile;
        String xSet;
        String loadersToSkip;
        boolean timers = false;
        boolean notReWeavable = true;

        public WeaverOption(IMessageHandler imh) {
            this.messageHandler = imh;
        }
    }
}
