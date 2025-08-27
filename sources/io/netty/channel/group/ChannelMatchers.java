package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/group/ChannelMatchers.class */
public final class ChannelMatchers {
    private static final ChannelMatcher ALL_MATCHER = new ChannelMatcher() { // from class: io.netty.channel.group.ChannelMatchers.1
        @Override // io.netty.channel.group.ChannelMatcher
        public boolean matches(Channel channel) {
            return true;
        }
    };
    private static final ChannelMatcher SERVER_CHANNEL_MATCHER = isInstanceOf(ServerChannel.class);
    private static final ChannelMatcher NON_SERVER_CHANNEL_MATCHER = isNotInstanceOf(ServerChannel.class);

    private ChannelMatchers() {
    }

    public static ChannelMatcher all() {
        return ALL_MATCHER;
    }

    public static ChannelMatcher isNot(Channel channel) {
        return invert(is(channel));
    }

    public static ChannelMatcher is(Channel channel) {
        return new InstanceMatcher(channel);
    }

    public static ChannelMatcher isInstanceOf(Class<? extends Channel> clazz) {
        return new ClassMatcher(clazz);
    }

    public static ChannelMatcher isNotInstanceOf(Class<? extends Channel> clazz) {
        return invert(isInstanceOf(clazz));
    }

    public static ChannelMatcher isServerChannel() {
        return SERVER_CHANNEL_MATCHER;
    }

    public static ChannelMatcher isNonServerChannel() {
        return NON_SERVER_CHANNEL_MATCHER;
    }

    public static ChannelMatcher invert(ChannelMatcher matcher) {
        return new InvertMatcher(matcher);
    }

    public static ChannelMatcher compose(ChannelMatcher... matchers) {
        if (matchers.length < 1) {
            throw new IllegalArgumentException("matchers must at least contain one element");
        }
        if (matchers.length == 1) {
            return matchers[0];
        }
        return new CompositeMatcher(matchers);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/group/ChannelMatchers$CompositeMatcher.class */
    private static final class CompositeMatcher implements ChannelMatcher {
        private final ChannelMatcher[] matchers;

        CompositeMatcher(ChannelMatcher... matchers) {
            this.matchers = matchers;
        }

        @Override // io.netty.channel.group.ChannelMatcher
        public boolean matches(Channel channel) {
            for (ChannelMatcher m : this.matchers) {
                if (!m.matches(channel)) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/group/ChannelMatchers$InvertMatcher.class */
    private static final class InvertMatcher implements ChannelMatcher {
        private final ChannelMatcher matcher;

        InvertMatcher(ChannelMatcher matcher) {
            this.matcher = matcher;
        }

        @Override // io.netty.channel.group.ChannelMatcher
        public boolean matches(Channel channel) {
            return !this.matcher.matches(channel);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/group/ChannelMatchers$InstanceMatcher.class */
    private static final class InstanceMatcher implements ChannelMatcher {
        private final Channel channel;

        InstanceMatcher(Channel channel) {
            this.channel = channel;
        }

        @Override // io.netty.channel.group.ChannelMatcher
        public boolean matches(Channel ch2) {
            return this.channel == ch2;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/group/ChannelMatchers$ClassMatcher.class */
    private static final class ClassMatcher implements ChannelMatcher {
        private final Class<? extends Channel> clazz;

        ClassMatcher(Class<? extends Channel> clazz) {
            this.clazz = clazz;
        }

        @Override // io.netty.channel.group.ChannelMatcher
        public boolean matches(Channel ch2) {
            return this.clazz.isInstance(ch2);
        }
    }
}
