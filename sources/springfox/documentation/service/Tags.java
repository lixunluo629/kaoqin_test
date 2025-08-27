package springfox.documentation.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import springfox.documentation.builders.BuilderDefaults;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/Tags.class */
public class Tags {
    private Tags() {
        throw new UnsupportedOperationException();
    }

    public static Set<Tag> toTags(Multimap<String, ApiListing> apiListings) {
        Iterable<ApiListing> allListings = Iterables.concat(BuilderDefaults.nullToEmptyMultimap(apiListings).asMap().values());
        Collection<? extends Tag> list = FluentIterable.from(allListings).transformAndConcat(collectTags()).toList();
        TreeSet<Tag> tagSet = Sets.newTreeSet(byTagName());
        tagSet.addAll(list);
        return tagSet;
    }

    public static Comparator<Tag> byTagName() {
        return new Comparator<Tag>() { // from class: springfox.documentation.service.Tags.1
            @Override // java.util.Comparator
            public int compare(Tag first, Tag second) {
                return first.getName().compareTo(second.getName());
            }
        };
    }

    static Function<String, Tag> toTag(final ApiListing listing) {
        return new Function<String, Tag>() { // from class: springfox.documentation.service.Tags.2
            @Override // com.google.common.base.Function
            public Tag apply(String input) {
                return new Tag(input, listing.getDescription());
            }
        };
    }

    static Function<ApiListing, Iterable<Tag>> collectTags() {
        return new Function<ApiListing, Iterable<Tag>>() { // from class: springfox.documentation.service.Tags.3
            @Override // com.google.common.base.Function
            public Iterable<Tag> apply(ApiListing input) {
                return FluentIterable.from(input.getTags()).filter(Tags.emptyTags()).transform(Tags.toTag(input)).toSet();
            }
        };
    }

    public static Predicate<String> emptyTags() {
        return new Predicate<String>() { // from class: springfox.documentation.service.Tags.4
            @Override // com.google.common.base.Predicate
            public boolean apply(String input) {
                return !Strings.isNullOrEmpty(input);
            }
        };
    }
}
