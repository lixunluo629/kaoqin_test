package com.alibaba.excel.util;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/PositionUtils.class */
public class PositionUtils {
    private PositionUtils() {
    }

    public static int getRowByRowTagt(String rowTagt) {
        int row = 0;
        if (rowTagt != null) {
            row = Integer.parseInt(rowTagt) - 1;
        }
        return row;
    }

    public static int getRowByRowTagt(String rowTagt, int before) {
        if (rowTagt != null) {
            int row = Integer.parseInt(rowTagt) - 1;
            return row;
        }
        return before + 1;
    }

    public static int getRow(String currentCellIndex) {
        int row = 0;
        if (currentCellIndex != null) {
            String rowStr = currentCellIndex.replaceAll("[A-Z]", "").replaceAll("[a-z]", "");
            row = Integer.parseInt(rowStr) - 1;
        }
        return row;
    }

    public static int getCol(String currentCellIndex) {
        int col = 0;
        if (currentCellIndex != null) {
            char[] currentIndex = currentCellIndex.replaceAll("[0-9]", "").toCharArray();
            for (int i = 0; i < currentIndex.length; i++) {
                col = (int) (col + ((currentIndex[i] - '@') * Math.pow(26.0d, (currentIndex.length - i) - 1)));
            }
        }
        return col - 1;
    }

    public static int getCol(String currentCellIndex, int before) {
        int col = 0;
        if (currentCellIndex != null) {
            char[] currentIndex = currentCellIndex.replaceAll("[0-9]", "").toCharArray();
            for (int i = 0; i < currentIndex.length; i++) {
                col = (int) (col + ((currentIndex[i] - '@') * Math.pow(26.0d, (currentIndex.length - i) - 1)));
            }
            return col - 1;
        }
        return before + 1;
    }
}
