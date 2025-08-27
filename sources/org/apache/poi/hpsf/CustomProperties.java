package org.apache.poi.hpsf;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.poi.util.CodePageUtil;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/CustomProperties.class */
public class CustomProperties implements Map<String, Object> {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) CustomProperties.class);
    private final HashMap<Long, CustomProperty> props = new HashMap<>();
    private final TreeBidiMap<Long, String> dictionary = new TreeBidiMap<>();
    private boolean isPure = true;
    private int codepage = -1;

    public CustomProperty put(String name, CustomProperty cp) throws IllegalArgumentException {
        if (name == null) {
            this.isPure = false;
            return null;
        }
        if (!name.equals(cp.getName())) {
            throw new IllegalArgumentException("Parameter \"name\" (" + name + ") and custom property's name (" + cp.getName() + ") do not match.");
        }
        checkCodePage(name);
        this.props.remove(this.dictionary.getKey((Object) name));
        this.dictionary.put((TreeBidiMap<Long, String>) Long.valueOf(cp.getID()), (Long) name);
        return this.props.put(Long.valueOf(cp.getID()), cp);
    }

    @Override // java.util.Map
    public Object put(String key, Object value) {
        int variantType;
        if (value instanceof String) {
            variantType = 30;
        } else if (value instanceof Short) {
            variantType = 2;
        } else if (value instanceof Integer) {
            variantType = 3;
        } else if (value instanceof Long) {
            variantType = 20;
        } else if (value instanceof Float) {
            variantType = 4;
        } else if (value instanceof Double) {
            variantType = 5;
        } else if (value instanceof Boolean) {
            variantType = 11;
        } else if ((value instanceof BigInteger) && ((BigInteger) value).bitLength() <= 64 && ((BigInteger) value).compareTo(BigInteger.ZERO) >= 0) {
            variantType = 21;
        } else if (value instanceof java.util.Date) {
            variantType = 64;
        } else {
            throw new IllegalStateException("unsupported datatype - currently String,Short,Integer,Long,Float,Double,Boolean,BigInteger(unsigned long),Date can be processed.");
        }
        Property p = new MutableProperty(-1L, variantType, value);
        return put(new CustomProperty(p, key));
    }

    @Override // java.util.Map
    public Object get(Object key) {
        Long id = (Long) this.dictionary.getKey(key);
        CustomProperty cp = this.props.get(id);
        if (cp != null) {
            return cp.getValue();
        }
        return null;
    }

    @Override // java.util.Map
    /* renamed from: remove, reason: merged with bridge method [inline-methods] */
    public Object remove2(Object key) {
        Long id = (Long) this.dictionary.removeValue(key);
        return this.props.remove(id);
    }

    @Override // java.util.Map
    public int size() {
        return this.props.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.props.isEmpty();
    }

    @Override // java.util.Map
    public void clear() {
        this.props.clear();
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.props.hashCode();
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomProperties)) {
            return false;
        }
        return this.props.equals(((CustomProperties) obj).props);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends Object> m) {
        for (Map.Entry<? extends String, ? extends Object> me : m.entrySet()) {
            put(me.getKey(), me.getValue());
        }
    }

    public List<CustomProperty> properties() {
        List<CustomProperty> list = new ArrayList<>(this.props.size());
        for (K l : this.dictionary.keySet()) {
            list.add(this.props.get(l));
        }
        return Collections.unmodifiableList(list);
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        List<Object> list = new ArrayList<>(this.props.size());
        for (K l : this.dictionary.keySet()) {
            list.add(this.props.get(l).getValue());
        }
        return Collections.unmodifiableCollection(list);
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        LinkedHashMap linkedHashMap = new LinkedHashMap(this.props.size());
        Iterator i$ = this.dictionary.entrySet().iterator();
        while (i$.hasNext()) {
            Map.Entry<Long, String> se = (Map.Entry) i$.next();
            linkedHashMap.put(se.getValue(), this.props.get(se.getKey()).getValue());
        }
        return Collections.unmodifiableSet(linkedHashMap.entrySet());
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return Collections.unmodifiableSet(this.dictionary.values());
    }

    public Set<String> nameSet() {
        return Collections.unmodifiableSet(this.dictionary.values());
    }

    public Set<Long> idSet() {
        return Collections.unmodifiableSet(this.dictionary.keySet());
    }

    public void setCodepage(int codepage) {
        this.codepage = codepage;
    }

    public int getCodepage() {
        return this.codepage;
    }

    Map<Long, String> getDictionary() {
        return this.dictionary;
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return ((key instanceof Long) && this.dictionary.containsKey(key)) || this.dictionary.containsValue(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        if (value instanceof CustomProperty) {
            return this.props.containsValue(value);
        }
        for (CustomProperty cp : this.props.values()) {
            if (cp.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public boolean isPure() {
        return this.isPure;
    }

    public void setPure(boolean isPure) {
        this.isPure = isPure;
    }

    private Object put(CustomProperty customProperty) throws ClassCastException {
        String name = customProperty.getName();
        Long oldId = name == null ? null : (Long) this.dictionary.getKey((Object) name);
        if (oldId != null) {
            customProperty.setID(oldId.longValue());
        } else {
            long lastKey = this.dictionary.isEmpty() ? 0L : ((Long) this.dictionary.lastKey()).longValue();
            long nextKey = Math.max(lastKey, 31L) + 1;
            customProperty.setID(nextKey);
        }
        return put(name, customProperty);
    }

    private void checkCodePage(String value) {
        int cp = getCodepage();
        if (cp == -1) {
            cp = 1252;
        }
        if (cp == 1200) {
            return;
        }
        String cps = "";
        try {
            cps = CodePageUtil.codepageToEncoding(cp, false);
        } catch (UnsupportedEncodingException e) {
            LOG.log(7, "Codepage '" + cp + "' can't be found.");
        }
        if (!cps.isEmpty() && Charset.forName(cps).newEncoder().canEncode(value)) {
            return;
        }
        LOG.log(1, "Charset '" + cps + "' can't encode '" + value + "' - switching to unicode.");
        setCodepage(1200);
    }
}
