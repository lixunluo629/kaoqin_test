package com.itextpdf.layout.renderer;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import java.util.ArrayList;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TableBorderUtil.class */
final class TableBorderUtil {
    private TableBorderUtil() {
    }

    public static Border getCellSideBorder(Cell cellModel, int borderType) {
        Border cellModelSideBorder = (Border) cellModel.getProperty(borderType);
        if (null == cellModelSideBorder && !cellModel.hasProperty(borderType)) {
            cellModelSideBorder = (Border) cellModel.getProperty(9);
            if (null == cellModelSideBorder && !cellModel.hasProperty(9)) {
                cellModelSideBorder = (Border) cellModel.getDefaultProperty(9);
            }
        }
        return cellModelSideBorder;
    }

    public static Border getWidestBorder(List<Border> borderList) {
        Border theWidestBorder = null;
        if (0 != borderList.size()) {
            for (Border border : borderList) {
                if (null != border && (null == theWidestBorder || border.getWidth() > theWidestBorder.getWidth())) {
                    theWidestBorder = border;
                }
            }
        }
        return theWidestBorder;
    }

    public static Border getWidestBorder(List<Border> borderList, int start, int end) {
        Border theWidestBorder = null;
        if (0 != borderList.size()) {
            for (Border border : borderList.subList(start, end)) {
                if (null != border && (null == theWidestBorder || border.getWidth() > theWidestBorder.getWidth())) {
                    theWidestBorder = border;
                }
            }
        }
        return theWidestBorder;
    }

    public static List<Border> createAndFillBorderList(Border border, int size) {
        List<Border> borderList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            borderList.add(border);
        }
        return borderList;
    }

    public static List<Border> createAndFillBorderList(List<Border> originalList, Border borderToCollapse, int size) {
        List<Border> borderList = new ArrayList<>();
        if (null != originalList) {
            borderList.addAll(originalList);
        }
        while (borderList.size() < size) {
            borderList.add(borderToCollapse);
        }
        int end = null == originalList ? size : Math.min(originalList.size(), size);
        for (int i = 0; i < end; i++) {
            if (null == borderList.get(i) || (null != borderToCollapse && borderList.get(i).getWidth() <= borderToCollapse.getWidth())) {
                borderList.set(i, borderToCollapse);
            }
        }
        return borderList;
    }
}
