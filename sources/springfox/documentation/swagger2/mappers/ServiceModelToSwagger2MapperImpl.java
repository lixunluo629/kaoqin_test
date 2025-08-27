package springfox.documentation.swagger2.mappers;

import io.swagger.models.Info;
import io.swagger.models.Operation;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.ResourceListing;

@Component
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/ServiceModelToSwagger2MapperImpl.class */
public class ServiceModelToSwagger2MapperImpl extends ServiceModelToSwagger2Mapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private SecurityMapper securityMapper;

    @Autowired
    private LicenseMapper licenseMapper;

    @Autowired
    private VendorExtensionsMapper vendorExtensionsMapper;

    @Override // springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper
    public Swagger mapDocumentation(Documentation from) {
        if (from == null) {
            return null;
        }
        Swagger swagger = new Swagger();
        swagger.setPaths(mapApiListings(from.getApiListings()));
        swagger.setSchemes(mapSchemes(from.getSchemes()));
        swagger.setDefinitions(this.modelMapper.modelsFromApiListings(from.getApiListings()));
        swagger.setSecurityDefinitions(this.securityMapper.toSecuritySchemeDefinitions(from.getResourceListing()));
        swagger.setInfo(mapApiInfo(fromResourceListingInfo(from)));
        swagger.setBasePath(from.getBasePath());
        swagger.setTags(tagSetToTagList(from.getTags()));
        if (from.getConsumes() != null) {
            swagger.setConsumes(new ArrayList(from.getConsumes()));
        }
        if (from.getProduces() != null) {
            swagger.setProduces(new ArrayList(from.getProduces()));
        }
        return swagger;
    }

    @Override // springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper
    protected Info mapApiInfo(ApiInfo from) {
        if (from == null) {
            return null;
        }
        Info info_ = new Info();
        info_.setLicense(this.licenseMapper.apiInfoToLicense(from));
        info_.setTermsOfService(from.getTermsOfServiceUrl());
        info_.setContact(mapContact(from.getContact()));
        info_.setDescription(from.getDescription());
        info_.setVersion(from.getVersion());
        info_.setTitle(from.getTitle());
        return info_;
    }

    @Override // springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper
    protected Operation mapOperation(springfox.documentation.service.Operation from) {
        Map<String, Object> targetMap;
        if (from == null) {
            return null;
        }
        Operation operation = new Operation();
        operation.setSecurity(mapAuthorizations(from.getSecurityReferences()));
        if (operation.getVendorExtensions() != null && (targetMap = this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions())) != null) {
            operation.getVendorExtensions().putAll(targetMap);
        }
        operation.setDescription(from.getNotes());
        operation.setOperationId(from.getUniqueId());
        operation.setResponses(mapResponseMessages(from.getResponseMessages()));
        operation.setSchemes(stringSetToSchemeList(from.getProtocol()));
        if (from.getTags() != null) {
            operation.setTags(new ArrayList(from.getTags()));
        }
        operation.setSummary(from.getSummary());
        if (from.getConsumes() != null) {
            operation.setConsumes(new ArrayList(from.getConsumes()));
        }
        if (from.getProduces() != null) {
            operation.setProduces(new ArrayList(from.getProduces()));
        }
        operation.setParameters(parameterListToParameterList(from.getParameters()));
        if (from.getDeprecated() != null) {
            operation.setDeprecated(Boolean.valueOf(Boolean.parseBoolean(from.getDeprecated())));
        }
        return operation;
    }

    @Override // springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper
    protected Tag mapTag(springfox.documentation.service.Tag from) {
        if (from == null) {
            return null;
        }
        Tag tag_ = new Tag();
        tag_.setName(from.getName());
        tag_.setDescription(from.getDescription());
        return tag_;
    }

    private ApiInfo fromResourceListingInfo(Documentation documentation) {
        ResourceListing resourceListing;
        ApiInfo info;
        if (documentation == null || (resourceListing = documentation.getResourceListing()) == null || (info = resourceListing.getInfo()) == null) {
            return null;
        }
        return info;
    }

    protected List<Tag> tagSetToTagList(Set<springfox.documentation.service.Tag> set) {
        if (set == null) {
            return null;
        }
        List<Tag> list = new ArrayList<>();
        for (springfox.documentation.service.Tag tag : set) {
            list.add(mapTag(tag));
        }
        return list;
    }

    protected List<Scheme> stringSetToSchemeList(Set<String> set) {
        if (set == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String string : set) {
            arrayList.add(Enum.valueOf(Scheme.class, string));
        }
        return arrayList;
    }

    protected List<Parameter> parameterListToParameterList(List<springfox.documentation.service.Parameter> list) {
        if (list == null) {
            return null;
        }
        List<Parameter> list_ = new ArrayList<>();
        for (springfox.documentation.service.Parameter parameter : list) {
            list_.add(this.parameterMapper.mapParameter(parameter));
        }
        return list_;
    }
}
