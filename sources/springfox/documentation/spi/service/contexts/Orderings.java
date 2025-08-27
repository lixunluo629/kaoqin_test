package springfox.documentation.spi.service.contexts;

import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.service.DocumentationPlugin;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/Orderings.class */
public class Orderings {
    private Orderings() {
        throw new UnsupportedOperationException();
    }

    public static Comparator<Operation> nickNameComparator() {
        return new Comparator<Operation>() { // from class: springfox.documentation.spi.service.contexts.Orderings.1
            @Override // java.util.Comparator
            public int compare(Operation first, Operation second) {
                return Strings.nullToEmpty(first.getUniqueId()).compareTo(Strings.nullToEmpty(second.getUniqueId()));
            }
        };
    }

    public static Comparator<Operation> positionComparator() {
        return new Comparator<Operation>() { // from class: springfox.documentation.spi.service.contexts.Orderings.2
            @Override // java.util.Comparator
            public int compare(Operation first, Operation second) {
                return Ints.compare(first.getPosition(), second.getPosition());
            }
        };
    }

    public static Comparator<ApiListingReference> listingReferencePathComparator() {
        return new Comparator<ApiListingReference>() { // from class: springfox.documentation.spi.service.contexts.Orderings.3
            @Override // java.util.Comparator
            public int compare(ApiListingReference first, ApiListingReference second) {
                return first.getPath().compareTo(second.getPath());
            }
        };
    }

    public static Comparator<ApiListingReference> listingPositionComparator() {
        return new Comparator<ApiListingReference>() { // from class: springfox.documentation.spi.service.contexts.Orderings.4
            @Override // java.util.Comparator
            public int compare(ApiListingReference first, ApiListingReference second) {
                return Ints.compare(first.getPosition(), second.getPosition());
            }
        };
    }

    public static Comparator<ApiDescription> apiPathCompatator() {
        return new Comparator<ApiDescription>() { // from class: springfox.documentation.spi.service.contexts.Orderings.5
            @Override // java.util.Comparator
            public int compare(ApiDescription first, ApiDescription second) {
                return first.getPath().compareTo(second.getPath());
            }
        };
    }

    public static Comparator<ResourceGroup> resourceGroupComparator() {
        return new Comparator<ResourceGroup>() { // from class: springfox.documentation.spi.service.contexts.Orderings.6
            @Override // java.util.Comparator
            public int compare(ResourceGroup first, ResourceGroup second) {
                return first.getGroupName().compareTo(second.getGroupName());
            }
        };
    }

    public static Comparator<RequestMappingContext> methodComparator() {
        return new Comparator<RequestMappingContext>() { // from class: springfox.documentation.spi.service.contexts.Orderings.7
            @Override // java.util.Comparator
            public int compare(RequestMappingContext first, RequestMappingContext second) {
                return Orderings.qualifiedMethodName(first).compareTo(Orderings.qualifiedMethodName(second));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String qualifiedMethodName(RequestMappingContext context) {
        return String.format("%s.%s", context.getHandlerMethod().getBeanType().getName(), context.getHandlerMethod().getMethod().getName());
    }

    public static Ordering<RequestHandler> byPatternsCondition() {
        return Ordering.from(new Comparator<RequestHandler>() { // from class: springfox.documentation.spi.service.contexts.Orderings.8
            @Override // java.util.Comparator
            public int compare(RequestHandler first, RequestHandler second) {
                return first.getRequestMapping().getPatternsCondition().toString().compareTo(second.getRequestMapping().getPatternsCondition().toString());
            }
        });
    }

    public static Ordering<? super DocumentationPlugin> pluginOrdering() {
        return Ordering.from(byPluginType()).compound(byPluginName());
    }

    public static Comparator<? super DocumentationPlugin> byPluginType() {
        return new Comparator<DocumentationPlugin>() { // from class: springfox.documentation.spi.service.contexts.Orderings.9
            @Override // java.util.Comparator
            public int compare(DocumentationPlugin first, DocumentationPlugin second) {
                return Ints.compare(first.getDocumentationType().hashCode(), second.getDocumentationType().hashCode());
            }
        };
    }

    public static Comparator<? super DocumentationPlugin> byPluginName() {
        return new Comparator<DocumentationPlugin>() { // from class: springfox.documentation.spi.service.contexts.Orderings.10
            @Override // java.util.Comparator
            public int compare(DocumentationPlugin first, DocumentationPlugin second) {
                return first.getGroupName().compareTo(second.getGroupName());
            }
        };
    }
}
