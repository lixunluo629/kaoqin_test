package org.springframework.data.repository.query.parser;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/PartTree.class */
public class PartTree implements Iterable<OrPart> {
    private static final String KEYWORD_TEMPLATE = "(%s)(?=(\\p{Lu}|\\P{InBASIC_LATIN}))";
    private static final String QUERY_PATTERN = "find|read|get|query|stream";
    private static final String COUNT_PATTERN = "count";
    private static final String EXISTS_PATTERN = "exists";
    private static final String DELETE_PATTERN = "delete|remove";
    private static final Pattern PREFIX_TEMPLATE = Pattern.compile("^(find|read|get|query|stream|count|exists|delete|remove)((\\p{Lu}.*?))??By");
    private final Subject subject;
    private final Predicate predicate;

    public PartTree(String source, Class<?> domainClass) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(domainClass, "Domain class must not be null");
        Matcher matcher = PREFIX_TEMPLATE.matcher(source);
        if (!matcher.find()) {
            this.subject = new Subject(null);
            this.predicate = new Predicate(source, domainClass);
        } else {
            this.subject = new Subject(matcher.group(0));
            this.predicate = new Predicate(source.substring(matcher.group().length()), domainClass);
        }
    }

    @Override // java.lang.Iterable
    public Iterator<OrPart> iterator() {
        return this.predicate.iterator();
    }

    public Sort getSort() {
        OrderBySource orderBySource = this.predicate.getOrderBySource();
        if (orderBySource == null) {
            return null;
        }
        return orderBySource.toSort();
    }

    public boolean isDistinct() {
        return this.subject.isDistinct();
    }

    public Boolean isCountProjection() {
        return Boolean.valueOf(this.subject.isCountProjection());
    }

    public Boolean isExistsProjection() {
        return Boolean.valueOf(this.subject.isExistsProjection());
    }

    public Boolean isDelete() {
        return this.subject.isDelete();
    }

    public boolean isLimiting() {
        return getMaxResults() != null;
    }

    public Integer getMaxResults() {
        return this.subject.getMaxResults();
    }

    public Iterable<Part> getParts() {
        List<Part> result = new ArrayList<>();
        Iterator<OrPart> it = iterator();
        while (it.hasNext()) {
            OrPart orPart = it.next();
            Iterator<Part> it2 = orPart.iterator();
            while (it2.hasNext()) {
                Part part = it2.next();
                result.add(part);
            }
        }
        return result;
    }

    public Iterable<Part> getParts(Part.Type type) {
        List<Part> result = new ArrayList<>();
        for (Part part : getParts()) {
            if (part.getType().equals(type)) {
                result.add(part);
            }
        }
        return result;
    }

    public String toString() {
        OrderBySource orderBySource = this.predicate.getOrderBySource();
        Object[] objArr = new Object[2];
        objArr[0] = StringUtils.collectionToDelimitedString(this.predicate.nodes, " or ");
        objArr[1] = orderBySource == null ? "" : SymbolConstants.SPACE_SYMBOL + orderBySource;
        return String.format("%s%s", objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] split(String text, String keyword) {
        Pattern pattern = Pattern.compile(String.format(KEYWORD_TEMPLATE, keyword));
        return pattern.split(text);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/PartTree$OrPart.class */
    public static class OrPart implements Iterable<Part> {
        private final List<Part> children = new ArrayList();

        OrPart(String source, Class<?> domainClass, boolean alwaysIgnoreCase) {
            String[] split = PartTree.split(source, "And");
            for (String part : split) {
                if (StringUtils.hasText(part)) {
                    this.children.add(new Part(part, domainClass, alwaysIgnoreCase));
                }
            }
        }

        @Override // java.lang.Iterable
        public Iterator<Part> iterator() {
            return this.children.iterator();
        }

        public String toString() {
            return StringUtils.collectionToDelimitedString(this.children, " and ");
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/PartTree$Subject.class */
    private static class Subject {
        private static final String DISTINCT = "Distinct";
        private static final String LIMITING_QUERY_PATTERN = "(First|Top)(\\d*)?";
        private final boolean distinct;
        private final boolean count;
        private final boolean exists;
        private final boolean delete;
        private final Integer maxResults;
        private static final Pattern COUNT_BY_TEMPLATE = Pattern.compile("^count(\\p{Lu}.*?)??By");
        private static final Pattern EXISTS_BY_TEMPLATE = Pattern.compile("^(exists)(\\p{Lu}.*?)??By");
        private static final Pattern DELETE_BY_TEMPLATE = Pattern.compile("^(delete|remove)(\\p{Lu}.*?)??By");
        private static final Pattern LIMITED_QUERY_TEMPLATE = Pattern.compile("^(find|read|get|query|stream)(Distinct)?(First|Top)(\\d*)?(\\p{Lu}.*?)??By");

        public Subject(String subject) {
            this.distinct = subject == null ? false : subject.contains(DISTINCT);
            this.count = matches(subject, COUNT_BY_TEMPLATE);
            this.exists = matches(subject, EXISTS_BY_TEMPLATE);
            this.delete = matches(subject, DELETE_BY_TEMPLATE);
            this.maxResults = returnMaxResultsIfFirstKSubjectOrNull(subject);
        }

        private Integer returnMaxResultsIfFirstKSubjectOrNull(String subject) {
            if (subject == null) {
                return null;
            }
            Matcher grp = LIMITED_QUERY_TEMPLATE.matcher(subject);
            if (grp.find()) {
                return Integer.valueOf(StringUtils.hasText(grp.group(4)) ? Integer.valueOf(grp.group(4)).intValue() : 1);
            }
            return null;
        }

        public Boolean isDelete() {
            return Boolean.valueOf(this.delete);
        }

        public boolean isCountProjection() {
            return this.count;
        }

        public boolean isExistsProjection() {
            return this.exists;
        }

        public boolean isDistinct() {
            return this.distinct;
        }

        public Integer getMaxResults() {
            return this.maxResults;
        }

        private final boolean matches(String subject, Pattern pattern) {
            if (subject == null) {
                return false;
            }
            return pattern.matcher(subject).find();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/PartTree$Predicate.class */
    private static class Predicate {
        private static final Pattern ALL_IGNORE_CASE = Pattern.compile("AllIgnor(ing|e)Case");
        private static final String ORDER_BY = "OrderBy";
        private final List<OrPart> nodes = new ArrayList();
        private final OrderBySource orderBySource;
        private boolean alwaysIgnoreCase;

        public Predicate(String predicate, Class<?> domainClass) {
            String[] parts = PartTree.split(detectAndSetAllIgnoreCase(predicate), ORDER_BY);
            if (parts.length > 2) {
                throw new IllegalArgumentException("OrderBy must not be used more than once in a method name!");
            }
            buildTree(parts[0], domainClass);
            this.orderBySource = parts.length == 2 ? new OrderBySource(parts[1], domainClass) : null;
        }

        private String detectAndSetAllIgnoreCase(String predicate) {
            Matcher matcher = ALL_IGNORE_CASE.matcher(predicate);
            if (matcher.find()) {
                this.alwaysIgnoreCase = true;
                predicate = predicate.substring(0, matcher.start()) + predicate.substring(matcher.end(), predicate.length());
            }
            return predicate;
        }

        private void buildTree(String source, Class<?> domainClass) {
            String[] split = PartTree.split(source, "Or");
            for (String part : split) {
                this.nodes.add(new OrPart(part, domainClass, this.alwaysIgnoreCase));
            }
        }

        public Iterator<OrPart> iterator() {
            return this.nodes.iterator();
        }

        public OrderBySource getOrderBySource() {
            return this.orderBySource;
        }
    }
}
