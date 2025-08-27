package springfox.documentation.builders;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpMethod;
import springfox.documentation.OperationNameGenerator;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.VendorExtension;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/OperationBuilder.class */
public class OperationBuilder {
    private final OperationNameGenerator nameGenerator;
    private String summary;
    private String notes;
    private String uniqueId;
    private int position;
    private String deprecated;
    private boolean isHidden;
    private ModelRef responseModel;
    private HttpMethod method = HttpMethod.GET;
    private Set<String> produces = Sets.newHashSet();
    private Set<String> consumes = Sets.newHashSet();
    private Set<String> protocol = Sets.newHashSet();
    private List<SecurityReference> securityReferences = Lists.newArrayList();
    private List<Parameter> parameters = Lists.newArrayList();
    private Set<ResponseMessage> responseMessages = Sets.newHashSet();
    private Set<String> tags = Sets.newHashSet();
    private List<VendorExtension> vendorExtensions = Lists.newArrayList();

    public OperationBuilder(OperationNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public OperationBuilder method(HttpMethod method) {
        this.method = (HttpMethod) BuilderDefaults.defaultIfAbsent(method, this.method);
        return this;
    }

    public OperationBuilder summary(String summary) {
        this.summary = (String) BuilderDefaults.defaultIfAbsent(summary, this.summary);
        return this;
    }

    public OperationBuilder notes(String notes) {
        this.notes = (String) BuilderDefaults.defaultIfAbsent(notes, this.notes);
        return this;
    }

    public OperationBuilder uniqueId(String uniqueId) {
        this.uniqueId = (String) BuilderDefaults.defaultIfAbsent(uniqueId, this.uniqueId);
        return this;
    }

    public OperationBuilder position(int position) {
        this.position = position;
        return this;
    }

    public OperationBuilder produces(Set<String> mediaTypes) {
        this.produces.addAll(BuilderDefaults.nullToEmptySet(mediaTypes));
        return this;
    }

    public OperationBuilder consumes(Set<String> mediaTypes) {
        this.consumes.addAll(BuilderDefaults.nullToEmptySet(mediaTypes));
        return this;
    }

    public OperationBuilder protocols(Set<String> protocols) {
        this.protocol.addAll(BuilderDefaults.nullToEmptySet(protocols));
        return this;
    }

    public OperationBuilder authorizations(List<SecurityReference> securityReferences) {
        this.securityReferences.addAll(BuilderDefaults.nullToEmptyList(securityReferences));
        return this;
    }

    public OperationBuilder parameters(List<Parameter> parameters) {
        List<Parameter> source = BuilderDefaults.nullToEmptyList(parameters);
        List<Parameter> destination = Lists.newArrayList(this.parameters);
        ParameterMerger merger = new ParameterMerger(destination, source);
        this.parameters = Lists.newArrayList(merger.merged());
        return this;
    }

    public OperationBuilder responseMessages(Set<ResponseMessage> responseMessages) {
        this.responseMessages = Sets.newHashSet(mergeResponseMessages(responseMessages));
        return this;
    }

    public OperationBuilder deprecated(String deprecated) {
        this.deprecated = (String) BuilderDefaults.defaultIfAbsent(deprecated, this.deprecated);
        return this;
    }

    public OperationBuilder hidden(boolean isHidden) {
        this.isHidden = isHidden;
        return this;
    }

    public OperationBuilder responseModel(ModelRef responseType) {
        this.responseModel = (ModelRef) BuilderDefaults.defaultIfAbsent(responseType, this.responseModel);
        return this;
    }

    public OperationBuilder tags(Set<String> tags) {
        this.tags = BuilderDefaults.nullToEmptySet(tags);
        return this;
    }

    public OperationBuilder extensions(List<VendorExtension> extensions) {
        this.vendorExtensions.addAll(extensions);
        return this;
    }

    public Operation build() {
        String uniqueOperationId = this.nameGenerator.startingWith(String.format("%sUsing%s", this.uniqueId, this.method));
        return new Operation(this.method, this.summary, this.notes, this.responseModel, uniqueOperationId, this.position, this.tags, this.produces, this.consumes, this.protocol, this.securityReferences, this.parameters, this.responseMessages, this.deprecated, this.isHidden, this.vendorExtensions);
    }

    private Set<ResponseMessage> mergeResponseMessages(Set<ResponseMessage> responseMessages) {
        ImmutableMap<Integer, ResponseMessage> responsesByCode = Maps.uniqueIndex(this.responseMessages, byStatusCode());
        Set<ResponseMessage> merged = Sets.newHashSet(this.responseMessages);
        for (ResponseMessage each : responseMessages) {
            if (responsesByCode.containsKey(Integer.valueOf(each.getCode()))) {
                ResponseMessage responseMessage = responsesByCode.get(Integer.valueOf(each.getCode()));
                String message = (String) BuilderDefaults.defaultIfAbsent(Strings.emptyToNull(each.getMessage()), responseMessage.getMessage());
                ModelRef responseWithModel = (ModelRef) BuilderDefaults.defaultIfAbsent(each.getResponseModel(), responseMessage.getResponseModel());
                merged.remove(responseMessage);
                merged.add(new ResponseMessageBuilder().code(each.getCode()).message(message).responseModel(responseWithModel).build());
            } else {
                merged.add(each);
            }
        }
        return merged;
    }

    private Function<? super ResponseMessage, Integer> byStatusCode() {
        return new Function<ResponseMessage, Integer>() { // from class: springfox.documentation.builders.OperationBuilder.1
            @Override // com.google.common.base.Function
            public Integer apply(ResponseMessage input) {
                return Integer.valueOf(input.getCode());
            }
        };
    }
}
