package org.apache.xmlbeans.impl.tool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/SchemaImportResolver.class */
public abstract class SchemaImportResolver {

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/SchemaImportResolver$SchemaResource.class */
    public interface SchemaResource {
        SchemaDocument.Schema getSchema();

        String getNamespace();

        String getSchemaLocation();
    }

    public abstract SchemaResource lookupResource(String str, String str2);

    public abstract void reportActualNamespace(SchemaResource schemaResource, String str);

    protected final void resolveImports(SchemaResource[] resources) {
        SchemaResource nextResource;
        LinkedList queueOfResources = new LinkedList(Arrays.asList(resources));
        LinkedList queueOfLocators = new LinkedList();
        Set seenResources = new HashSet();
        while (true) {
            if (!queueOfResources.isEmpty()) {
                nextResource = (SchemaResource) queueOfResources.removeFirst();
            } else if (!queueOfLocators.isEmpty()) {
                SchemaLocator locator = (SchemaLocator) queueOfLocators.removeFirst();
                nextResource = lookupResource(locator.namespace, locator.schemaLocation);
                if (nextResource == null) {
                }
            } else {
                return;
            }
            if (!seenResources.contains(nextResource)) {
                seenResources.add(nextResource);
                SchemaDocument.Schema schema = nextResource.getSchema();
                if (schema != null) {
                    String actualTargetNamespace = schema.getTargetNamespace();
                    if (actualTargetNamespace == null) {
                        actualTargetNamespace = "";
                    }
                    String expectedTargetNamespace = nextResource.getNamespace();
                    if (expectedTargetNamespace == null || !actualTargetNamespace.equals(expectedTargetNamespace)) {
                        reportActualNamespace(nextResource, actualTargetNamespace);
                    }
                    ImportDocument.Import[] schemaImports = schema.getImportArray();
                    for (int i = 0; i < schemaImports.length; i++) {
                        queueOfLocators.add(new SchemaLocator(schemaImports[i].getNamespace() == null ? "" : schemaImports[i].getNamespace(), schemaImports[i].getSchemaLocation()));
                    }
                    IncludeDocument.Include[] schemaIncludes = schema.getIncludeArray();
                    for (IncludeDocument.Include include : schemaIncludes) {
                        queueOfLocators.add(new SchemaLocator(null, include.getSchemaLocation()));
                    }
                }
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/SchemaImportResolver$SchemaLocator.class */
    private static class SchemaLocator {
        public final String namespace;
        public final String schemaLocation;

        public SchemaLocator(String namespace, String schemaLocation) {
            this.namespace = namespace;
            this.schemaLocation = schemaLocation;
        }
    }
}
