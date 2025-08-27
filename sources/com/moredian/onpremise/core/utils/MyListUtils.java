package com.moredian.onpremise.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/MyListUtils.class */
public class MyListUtils<T> {
    public List<T> intersection(List<T> list1, List<T> list2) {
        List<T> newList = new ArrayList<>();
        newList.addAll(list1);
        newList.removeAll(list2);
        newList.addAll(list2);
        return newList;
    }

    public List<T> difference(List<T> list1, List<T> list2) {
        List<T> newList = new ArrayList<>();
        newList.addAll(list1);
        newList.removeAll(list2);
        return newList;
    }

    public List<T> union(List<T> list1, List<T> list2) {
        List<T> newList = new ArrayList<>();
        newList.addAll(list1);
        newList.retainAll(list2);
        return newList;
    }

    public List<T> removeDuplicate(List<T> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    public static boolean listIsEmpty(List list) {
        return list != null && list.size() > 0;
    }

    public static boolean arrayIsEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    public static String listToString(List list) {
        StringBuffer sb = new StringBuffer();
        if (listIsEmpty(list)) {
            for (Object var : list) {
                sb.append(String.valueOf(var));
                sb.append(",");
            }
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    public static List<Long> stringListToLong(List<String> list) {
        List<Long> result = new ArrayList<>();
        if (listIsEmpty(list)) {
            for (String var : list) {
                if (var != null && var.trim().length() > 0) {
                    result.add(Long.valueOf(var));
                }
            }
        }
        return result;
    }

    public static String getStringRandom(int lengths) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < lengths; i++) {
            String strOrNum = random.nextInt(2) % 2 == 0 ? "str" : "num";
            if ("str".equalsIgnoreCase(strOrNum)) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val = val + ((char) (random.nextInt(26) + temp));
            } else if ("num".equalsIgnoreCase(strOrNum)) {
                val = val + String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
