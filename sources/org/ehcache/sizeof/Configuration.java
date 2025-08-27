package org.ehcache.sizeof;

import java.util.ArrayList;
import java.util.Collections;
import org.ehcache.sizeof.filters.SizeOfFilter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/Configuration.class */
public final class Configuration {
    private final int maxDepth;
    private final boolean abort;
    private final boolean silent;
    private final SizeOfFilter[] filters;

    public Configuration(int maxDepth, boolean abort, boolean silent, SizeOfFilter... filters) {
        this.maxDepth = maxDepth;
        this.abort = abort;
        this.silent = silent;
        this.filters = filters;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }

    public boolean isAbort() {
        return this.abort;
    }

    public boolean isSilent() {
        return this.silent;
    }

    public SizeOfFilter[] getFilters() {
        return this.filters;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/Configuration$Builder.class */
    public static final class Builder {
        private int maxDepth;
        private boolean silent;
        private boolean abort;
        private ArrayList<SizeOfFilter> filters = new ArrayList<>();

        public Builder() {
        }

        public Builder(Configuration cfg) {
            maxDepth(cfg.maxDepth);
            silent(cfg.silent);
            abort(cfg.abort);
            Collections.addAll(this.filters, cfg.filters);
        }

        public Builder maxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
            return this;
        }

        public Builder silent(boolean silent) {
            this.silent = silent;
            return this;
        }

        public Builder abort(boolean abort) {
            this.abort = abort;
            return this;
        }

        public Builder addFilter(SizeOfFilter filter) {
            if (!this.filters.contains(filter)) {
                this.filters.add(filter);
            }
            return this;
        }

        public Builder addFilters(SizeOfFilter... filters) {
            for (SizeOfFilter filter : filters) {
                addFilter(filter);
            }
            return this;
        }

        public Builder removeFilter(SizeOfFilter filter) {
            this.filters.remove(filter);
            return this;
        }

        public Builder removeFilters(SizeOfFilter... filters) {
            for (SizeOfFilter filter : filters) {
                this.filters.remove(filter);
            }
            return this;
        }

        public Builder clearlFilters() {
            this.filters.clear();
            return this;
        }

        public Configuration build() {
            return new Configuration(this.maxDepth, this.abort, this.silent, (SizeOfFilter[]) this.filters.toArray(new SizeOfFilter[this.filters.size()]));
        }
    }
}
