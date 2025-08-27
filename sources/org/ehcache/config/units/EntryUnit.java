package org.ehcache.config.units;

import org.ehcache.config.ResourceUnit;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/units/EntryUnit.class */
public enum EntryUnit implements ResourceUnit {
    ENTRIES { // from class: org.ehcache.config.units.EntryUnit.1
        @Override // java.lang.Enum
        public String toString() {
            return "entries";
        }

        @Override // org.ehcache.config.ResourceUnit
        public int compareTo(long thisSize, long thatSize, ResourceUnit thatUnit) throws IllegalArgumentException {
            if (equals(thatUnit)) {
                return Long.signum(thisSize - thatSize);
            }
            throw new IllegalArgumentException();
        }
    }
}
