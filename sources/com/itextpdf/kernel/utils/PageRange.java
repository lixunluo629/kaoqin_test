package com.itextpdf.kernel.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PageRange.class */
public class PageRange {
    private static final Pattern SEQUENCE_PATTERN = Pattern.compile("(\\d+)-(\\d+)?");
    private static final Pattern SINGLE_PAGE_PATTERN = Pattern.compile("(\\d+)");
    private List<IPageRangePart> sequences = new ArrayList();

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PageRange$IPageRangePart.class */
    public interface IPageRangePart {
        List<Integer> getAllPagesInRange(int i);

        boolean isPageInRange(int i);
    }

    public PageRange() {
    }

    public PageRange(String pageRange) throws NumberFormatException {
        for (String pageRangePart : pageRange.replaceAll("\\s+", "").split(",")) {
            IPageRangePart cond = getRangeObject(pageRangePart);
            if (cond != null) {
                this.sequences.add(cond);
            }
        }
    }

    private static IPageRangePart getRangeObject(String rangeDef) throws NumberFormatException {
        if (rangeDef.contains("&")) {
            List<IPageRangePart> conditions = new ArrayList<>();
            for (String pageRangeCond : rangeDef.split("&")) {
                IPageRangePart cond = getRangeObject(pageRangeCond);
                if (cond != null) {
                    conditions.add(cond);
                }
            }
            if (conditions.size() > 0) {
                return new PageRangePartAnd((IPageRangePart[]) conditions.toArray(new IPageRangePart[0]));
            }
            return null;
        }
        Matcher matcher = SEQUENCE_PATTERN.matcher(rangeDef);
        if (matcher.matches()) {
            int start = Integer.parseInt(matcher.group(1));
            if (matcher.group(2) != null) {
                return new PageRangePartSequence(start, Integer.parseInt(matcher.group(2)));
            }
            return new PageRangePartAfter(start);
        }
        Matcher matcher2 = SINGLE_PAGE_PATTERN.matcher(rangeDef);
        if (matcher2.matches()) {
            return new PageRangePartSingle(Integer.parseInt(matcher2.group(1)));
        }
        if ("odd".equalsIgnoreCase(rangeDef)) {
            return PageRangePartOddEven.ODD;
        }
        if ("even".equalsIgnoreCase(rangeDef)) {
            return PageRangePartOddEven.EVEN;
        }
        return null;
    }

    public PageRange addPageRangePart(IPageRangePart part) {
        this.sequences.add(part);
        return this;
    }

    public PageRange addPageSequence(int startPageNumber, int endPageNumber) {
        return addPageRangePart(new PageRangePartSequence(startPageNumber, endPageNumber));
    }

    public PageRange addSinglePage(int pageNumber) {
        return addPageRangePart(new PageRangePartSingle(pageNumber));
    }

    public List<Integer> getQualifyingPageNums(int nbPages) {
        List<Integer> allPages = new ArrayList<>();
        for (IPageRangePart sequence : this.sequences) {
            allPages.addAll(sequence.getAllPagesInRange(nbPages));
        }
        return allPages;
    }

    public boolean isPageInRange(int pageNumber) {
        for (IPageRangePart sequence : this.sequences) {
            if (sequence.isPageInRange(pageNumber)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PageRange)) {
            return false;
        }
        PageRange other = (PageRange) obj;
        return this.sequences.equals(other.sequences);
    }

    public int hashCode() {
        return this.sequences.hashCode();
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PageRange$PageRangePartSingle.class */
    public static class PageRangePartSingle implements IPageRangePart {
        private final int page;

        public PageRangePartSingle(int page) {
            this.page = page;
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public List<Integer> getAllPagesInRange(int nbPages) {
            if (this.page <= nbPages) {
                return Collections.singletonList(Integer.valueOf(this.page));
            }
            return Collections.emptyList();
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public boolean isPageInRange(int pageNumber) {
            return this.page == pageNumber;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof PageRangePartSingle)) {
                return false;
            }
            PageRangePartSingle other = (PageRangePartSingle) obj;
            return this.page == other.page;
        }

        public int hashCode() {
            return this.page;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PageRange$PageRangePartSequence.class */
    public static class PageRangePartSequence implements IPageRangePart {
        private final int start;
        private final int end;

        public PageRangePartSequence(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public List<Integer> getAllPagesInRange(int nbPages) {
            List<Integer> allPages = new ArrayList<>();
            for (int pageInRange = this.start; pageInRange <= this.end && pageInRange <= nbPages; pageInRange++) {
                allPages.add(Integer.valueOf(pageInRange));
            }
            return allPages;
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public boolean isPageInRange(int pageNumber) {
            return this.start <= pageNumber && pageNumber <= this.end;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof PageRangePartSequence)) {
                return false;
            }
            PageRangePartSequence other = (PageRangePartSequence) obj;
            return this.start == other.start && this.end == other.end;
        }

        public int hashCode() {
            return (this.start * 31) + this.end;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PageRange$PageRangePartAfter.class */
    public static class PageRangePartAfter implements IPageRangePart {
        private final int start;

        public PageRangePartAfter(int start) {
            this.start = start;
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public List<Integer> getAllPagesInRange(int nbPages) {
            List<Integer> allPages = new ArrayList<>();
            for (int pageInRange = this.start; pageInRange <= nbPages; pageInRange++) {
                allPages.add(Integer.valueOf(pageInRange));
            }
            return allPages;
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public boolean isPageInRange(int pageNumber) {
            return this.start <= pageNumber;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof PageRangePartAfter)) {
                return false;
            }
            PageRangePartAfter other = (PageRangePartAfter) obj;
            return this.start == other.start;
        }

        public int hashCode() {
            return (this.start * 31) - 1;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PageRange$PageRangePartOddEven.class */
    public static class PageRangePartOddEven implements IPageRangePart {
        private final boolean isOdd;
        private final int mod;
        public static final PageRangePartOddEven ODD = new PageRangePartOddEven(true);
        public static final PageRangePartOddEven EVEN = new PageRangePartOddEven(false);

        private PageRangePartOddEven(boolean isOdd) {
            this.isOdd = isOdd;
            if (isOdd) {
                this.mod = 1;
            } else {
                this.mod = 0;
            }
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public List<Integer> getAllPagesInRange(int nbPages) {
            List<Integer> allPages = new ArrayList<>();
            for (int pageInRange = this.mod == 0 ? 2 : this.mod; pageInRange <= nbPages; pageInRange += 2) {
                allPages.add(Integer.valueOf(pageInRange));
            }
            return allPages;
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public boolean isPageInRange(int pageNumber) {
            return pageNumber % 2 == this.mod;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof PageRangePartOddEven)) {
                return false;
            }
            PageRangePartOddEven other = (PageRangePartOddEven) obj;
            return this.isOdd == other.isOdd;
        }

        public int hashCode() {
            if (this.isOdd) {
                return 127;
            }
            return 128;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/PageRange$PageRangePartAnd.class */
    public static class PageRangePartAnd implements IPageRangePart {
        private final List<IPageRangePart> conditions = new ArrayList();

        public PageRangePartAnd(IPageRangePart... conditions) {
            this.conditions.addAll(Arrays.asList(conditions));
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public List<Integer> getAllPagesInRange(int nbPages) {
            List<Integer> allPages = new ArrayList<>();
            if (!this.conditions.isEmpty()) {
                allPages.addAll(this.conditions.get(0).getAllPagesInRange(nbPages));
            }
            for (IPageRangePart cond : this.conditions) {
                allPages.retainAll(cond.getAllPagesInRange(nbPages));
            }
            return allPages;
        }

        @Override // com.itextpdf.kernel.utils.PageRange.IPageRangePart
        public boolean isPageInRange(int pageNumber) {
            for (IPageRangePart cond : this.conditions) {
                if (!cond.isPageInRange(pageNumber)) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof PageRangePartAnd)) {
                return false;
            }
            PageRangePartAnd other = (PageRangePartAnd) obj;
            return this.conditions.equals(other.conditions);
        }

        public int hashCode() {
            return this.conditions.hashCode();
        }
    }
}
