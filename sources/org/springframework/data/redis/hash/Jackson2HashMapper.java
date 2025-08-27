package org.springframework.data.redis.hash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.PropertyAccessor;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/hash/Jackson2HashMapper.class */
public class Jackson2HashMapper implements HashMapper<Object, String, Object> {
    private final ObjectMapper typingMapper;
    private final ObjectMapper untypedMapper;
    private final boolean flatten;

    public Jackson2HashMapper(boolean flatten) {
        this(new ObjectMapper(), flatten);
        this.typingMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        this.typingMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        this.typingMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.typingMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Jackson2HashMapper(ObjectMapper mapper, boolean flatten) {
        Assert.notNull(mapper, "Mapper must not be null!");
        this.typingMapper = mapper;
        this.flatten = flatten;
        this.untypedMapper = new ObjectMapper();
        this.untypedMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        this.untypedMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public Map<String, Object> toHash(Object source) throws IllegalArgumentException {
        JsonNode tree = this.typingMapper.valueToTree(source);
        return this.flatten ? flattenMap(tree.fields()) : (Map) this.untypedMapper.convertValue(tree, Map.class);
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public Object fromHash(Map<String, Object> hash) {
        try {
            if (this.flatten) {
                return this.typingMapper.reader().forType(Object.class).readValue(this.untypedMapper.writeValueAsBytes(doUnflatten(hash)));
            }
            return this.typingMapper.treeToValue(this.untypedMapper.valueToTree(hash), Object.class);
        } catch (JsonParseException e) {
            throw new MappingException(e.getMessage(), e);
        } catch (JsonMappingException e2) {
            throw new MappingException(e2.getMessage(), e2);
        } catch (IOException e3) {
            throw new MappingException(e3.getMessage(), e3);
        }
    }

    private Map<String, Object> doUnflatten(Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        Set<String> treatSeperate = new LinkedHashSet<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            String[] args = key.split("\\.");
            if (args.length == 1 && !args[0].contains(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
                result.put(entry.getKey(), entry.getValue());
            } else if (args.length == 1 && args[0].contains(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
                String prunedKey = args[0].substring(0, args[0].indexOf(91));
                if (result.containsKey(prunedKey)) {
                    appendValueToTypedList(args[0], entry.getValue(), (List) result.get(prunedKey));
                } else {
                    result.put(prunedKey, createTypedListWithValue(entry.getValue()));
                }
            } else {
                treatSeperate.add(key.substring(0, key.indexOf(46)));
            }
        }
        for (String partial : treatSeperate) {
            Map<String, Object> newSource = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry2 : source.entrySet()) {
                if (entry2.getKey().startsWith(partial)) {
                    newSource.put(entry2.getKey().substring(partial.length() + 1), entry2.getValue());
                }
            }
            if (partial.endsWith("]")) {
                String prunedKey2 = partial.substring(0, partial.indexOf(91));
                if (result.containsKey(prunedKey2)) {
                    appendValueToTypedList(partial, doUnflatten(newSource), (List) result.get(prunedKey2));
                } else {
                    result.put(prunedKey2, createTypedListWithValue(doUnflatten(newSource)));
                }
            } else {
                result.put(partial, doUnflatten(newSource));
            }
        }
        return result;
    }

    private Map<String, Object> flattenMap(Iterator<Map.Entry<String, JsonNode>> source) {
        Map<String, Object> resultMap = new HashMap<>();
        doFlatten("", source, resultMap);
        return resultMap;
    }

    private void doFlatten(String propertyPrefix, Iterator<Map.Entry<String, JsonNode>> inputMap, Map<String, Object> resultMap) {
        if (StringUtils.hasText(propertyPrefix)) {
            propertyPrefix = propertyPrefix + ".";
        }
        while (inputMap.hasNext()) {
            Map.Entry<String, JsonNode> entry = inputMap.next();
            flattenElement(propertyPrefix + entry.getKey(), entry.getValue(), resultMap);
        }
    }

    private void flattenElement(String propertyPrefix, Object source, Map<String, Object> resultMap) {
        if (!(source instanceof JsonNode)) {
            resultMap.put(propertyPrefix, source);
            return;
        }
        JsonNode element = (JsonNode) source;
        if (!element.isArray()) {
            if (element.isContainerNode()) {
                doFlatten(propertyPrefix, element.fields(), resultMap);
                return;
            } else {
                resultMap.put(propertyPrefix, new DirectFieldAccessFallbackBeanWrapper(element).getPropertyValue("_value"));
                return;
            }
        }
        Iterator<JsonNode> nodes = element.elements();
        while (nodes.hasNext()) {
            JsonNode cur = nodes.next();
            if (cur.isArray()) {
                falttenCollection(propertyPrefix, cur.elements(), resultMap);
            }
        }
    }

    private void falttenCollection(String propertyPrefix, Iterator<JsonNode> list, Map<String, Object> resultMap) {
        int counter = 0;
        while (list.hasNext()) {
            JsonNode element = list.next();
            flattenElement(propertyPrefix + PropertyAccessor.PROPERTY_KEY_PREFIX + counter + "]", element, resultMap);
            counter++;
        }
    }

    private void appendValueToTypedList(String key, Object value, List<Object> destination) {
        int index = Integer.valueOf(key.substring(key.indexOf(91) + 1, key.length() - 1)).intValue();
        List<Object> resultList = (List) destination.get(1);
        if (resultList.size() < index) {
            resultList.add(value);
        } else {
            resultList.add(index, value);
        }
    }

    private List<Object> createTypedListWithValue(Object value) {
        List<Object> listWithTypeHint = new ArrayList<>();
        listWithTypeHint.add(ArrayList.class.getName());
        List<Object> values = new ArrayList<>();
        values.add(value);
        listWithTypeHint.add(values);
        return listWithTypeHint;
    }
}
