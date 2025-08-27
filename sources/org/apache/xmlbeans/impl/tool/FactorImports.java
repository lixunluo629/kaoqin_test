package org.apache.xmlbeans.impl.tool;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedAttributeGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelAttribute;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/FactorImports.class */
public class FactorImports {
    public static void printUsage() {
        System.out.println("Refactors a directory of XSD files to remove name conflicts.");
        System.out.println("Usage: sfactor [-import common.xsd] [-out outputdir] inputdir");
        System.out.println("    -import common.xsd - The XSD file to contain redundant ");
        System.out.println("                         definitions for importing.");
        System.out.println("    -out outputdir - The directory into which to place XSD ");
        System.out.println("                     files resulting from refactoring, ");
        System.out.println("                     plus a commonly imported common.xsd.");
        System.out.println("    inputdir - The directory containing the XSD files with");
        System.out.println("               redundant definitions.");
        System.out.println("    -license - Print license information.");
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("version");
        CommandLine cl = new CommandLine(args, flags, Arrays.asList(DefaultBeanDefinitionDocumentReader.IMPORT_ELEMENT, "out"));
        if (cl.getOpt("h") != null || cl.getOpt("help") != null || cl.getOpt("usage") != null || args.length < 1) {
            printUsage();
            System.exit(0);
            return;
        }
        String[] badopts = cl.getBadOpts();
        if (badopts.length > 0) {
            for (String str : badopts) {
                System.out.println("Unrecognized option: " + str);
            }
            printUsage();
            System.exit(0);
            return;
        }
        if (cl.getOpt("license") != null) {
            CommandLine.printLicense();
            System.exit(0);
            return;
        }
        if (cl.getOpt("version") != null) {
            CommandLine.printVersion();
            System.exit(0);
            return;
        }
        String[] args2 = cl.args();
        if (args2.length != 1) {
            System.exit(0);
            return;
        }
        String commonName = cl.getOpt(DefaultBeanDefinitionDocumentReader.IMPORT_ELEMENT);
        if (commonName == null) {
            commonName = "common.xsd";
        }
        String out = cl.getOpt("out");
        if (out == null) {
            System.out.println("Using output directory 'out'");
            out = "out";
        }
        File outdir = new File(out);
        File basedir = new File(args2[0]);
        File[] files = cl.getFiles();
        Map schemaDocs = new HashMap();
        Set elementNames = new HashSet();
        Set attributeNames = new HashSet();
        Set typeNames = new HashSet();
        Set modelGroupNames = new HashSet();
        Set attrGroupNames = new HashSet();
        Set dupeElementNames = new HashSet();
        Set dupeAttributeNames = new HashSet();
        Set dupeTypeNames = new HashSet();
        Set dupeModelGroupNames = new HashSet();
        Set dupeAttrGroupNames = new HashSet();
        Set<String> dupeNamespaces = new HashSet();
        for (int i = 0; i < files.length; i++) {
            try {
                SchemaDocument doc = SchemaDocument.Factory.parse(files[i]);
                schemaDocs.put(doc, files[i]);
                if (doc.getSchema().sizeOfImportArray() > 0 || doc.getSchema().sizeOfIncludeArray() > 0) {
                    System.out.println("warning: " + files[i] + " contains imports or includes that are being ignored.");
                }
                String targetNamespace = doc.getSchema().getTargetNamespace();
                if (targetNamespace == null) {
                    targetNamespace = "";
                }
                for (TopLevelComplexType topLevelComplexType : doc.getSchema().getComplexTypeArray()) {
                    noteName(topLevelComplexType.getName(), targetNamespace, typeNames, dupeTypeNames, dupeNamespaces);
                }
                for (TopLevelSimpleType topLevelSimpleType : doc.getSchema().getSimpleTypeArray()) {
                    noteName(topLevelSimpleType.getName(), targetNamespace, typeNames, dupeTypeNames, dupeNamespaces);
                }
                for (TopLevelElement topLevelElement : doc.getSchema().getElementArray()) {
                    noteName(topLevelElement.getName(), targetNamespace, elementNames, dupeElementNames, dupeNamespaces);
                }
                for (TopLevelAttribute topLevelAttribute : doc.getSchema().getAttributeArray()) {
                    noteName(topLevelAttribute.getName(), targetNamespace, attributeNames, dupeAttributeNames, dupeNamespaces);
                }
                for (NamedGroup namedGroup : doc.getSchema().getGroupArray()) {
                    noteName(namedGroup.getName(), targetNamespace, modelGroupNames, dupeModelGroupNames, dupeNamespaces);
                }
                for (NamedAttributeGroup namedAttributeGroup : doc.getSchema().getAttributeGroupArray()) {
                    noteName(namedAttributeGroup.getName(), targetNamespace, attrGroupNames, dupeAttrGroupNames, dupeNamespaces);
                }
            } catch (IOException e) {
                System.err.println("Unable to load " + files[i] + " - " + e.getMessage());
                System.exit(1);
                return;
            } catch (XmlException e2) {
                System.out.println("warning: " + files[i] + " is not a schema file - " + e2.getError().toString());
            }
        }
        if (schemaDocs.size() == 0) {
            System.out.println("No schema files found.");
            System.exit(0);
            return;
        }
        if (dupeTypeNames.size() + dupeElementNames.size() + dupeAttributeNames.size() + dupeModelGroupNames.size() + dupeAttrGroupNames.size() == 0) {
            System.out.println("No duplicate names found.");
            System.exit(0);
            return;
        }
        Map commonDocs = new HashMap();
        Map commonFiles = new HashMap();
        int count = dupeNamespaces.size() == 1 ? 0 : 1;
        for (String namespace : dupeNamespaces) {
            SchemaDocument commonDoc = SchemaDocument.Factory.parse("<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'/>");
            if (namespace.length() > 0) {
                commonDoc.getSchema().setTargetNamespace(namespace);
            }
            commonDoc.getSchema().setElementFormDefault(FormChoice.QUALIFIED);
            commonDocs.put(namespace, commonDoc);
            int i2 = count;
            count++;
            commonFiles.put(commonDoc, commonFileFor(commonName, namespace, i2, outdir));
        }
        for (SchemaDocument doc2 : schemaDocs.keySet()) {
            String targetNamespace2 = doc2.getSchema().getTargetNamespace();
            if (targetNamespace2 == null) {
                targetNamespace2 = "";
            }
            SchemaDocument commonDoc2 = (SchemaDocument) commonDocs.get(targetNamespace2);
            boolean needImport = false;
            TopLevelComplexType[] ct = doc2.getSchema().getComplexTypeArray();
            for (int j = ct.length - 1; j >= 0; j--) {
                if (isDuplicate(ct[j].getName(), targetNamespace2, dupeTypeNames)) {
                    if (isFirstDuplicate(ct[j].getName(), targetNamespace2, typeNames, dupeTypeNames)) {
                        commonDoc2.getSchema().addNewComplexType().set(ct[j]);
                    }
                    needImport = true;
                    doc2.getSchema().removeComplexType(j);
                }
            }
            TopLevelSimpleType[] st = doc2.getSchema().getSimpleTypeArray();
            for (int j2 = 0; j2 < st.length; j2++) {
                if (isDuplicate(st[j2].getName(), targetNamespace2, dupeTypeNames)) {
                    if (isFirstDuplicate(st[j2].getName(), targetNamespace2, typeNames, dupeTypeNames)) {
                        commonDoc2.getSchema().addNewSimpleType().set(st[j2]);
                    }
                    needImport = true;
                    doc2.getSchema().removeSimpleType(j2);
                }
            }
            TopLevelElement[] el = doc2.getSchema().getElementArray();
            for (int j3 = 0; j3 < el.length; j3++) {
                if (isDuplicate(el[j3].getName(), targetNamespace2, dupeElementNames)) {
                    if (isFirstDuplicate(el[j3].getName(), targetNamespace2, elementNames, dupeElementNames)) {
                        commonDoc2.getSchema().addNewElement().set(el[j3]);
                    }
                    needImport = true;
                    doc2.getSchema().removeElement(j3);
                }
            }
            TopLevelAttribute[] at = doc2.getSchema().getAttributeArray();
            for (int j4 = 0; j4 < at.length; j4++) {
                if (isDuplicate(at[j4].getName(), targetNamespace2, dupeAttributeNames)) {
                    if (isFirstDuplicate(at[j4].getName(), targetNamespace2, attributeNames, dupeAttributeNames)) {
                        commonDoc2.getSchema().addNewElement().set(at[j4]);
                    }
                    needImport = true;
                    doc2.getSchema().removeElement(j4);
                }
            }
            NamedGroup[] gr = doc2.getSchema().getGroupArray();
            for (int j5 = 0; j5 < gr.length; j5++) {
                if (isDuplicate(gr[j5].getName(), targetNamespace2, dupeModelGroupNames)) {
                    if (isFirstDuplicate(gr[j5].getName(), targetNamespace2, modelGroupNames, dupeModelGroupNames)) {
                        commonDoc2.getSchema().addNewElement().set(gr[j5]);
                    }
                    needImport = true;
                    doc2.getSchema().removeElement(j5);
                }
            }
            NamedAttributeGroup[] ag = doc2.getSchema().getAttributeGroupArray();
            for (int j6 = 0; j6 < ag.length; j6++) {
                if (isDuplicate(ag[j6].getName(), targetNamespace2, dupeAttrGroupNames)) {
                    if (isFirstDuplicate(ag[j6].getName(), targetNamespace2, attrGroupNames, dupeAttrGroupNames)) {
                        commonDoc2.getSchema().addNewElement().set(ag[j6]);
                    }
                    needImport = true;
                    doc2.getSchema().removeElement(j6);
                }
            }
            if (needImport) {
                IncludeDocument.Include newInclude = doc2.getSchema().addNewInclude();
                File outputFile = outputFileFor((File) schemaDocs.get(doc2), basedir, outdir);
                File commonFile = (File) commonFiles.get(commonDoc2);
                if (targetNamespace2 != null) {
                    newInclude.setSchemaLocation(relativeURIFor(outputFile, commonFile));
                }
            }
        }
        if (!outdir.isDirectory() && !outdir.mkdirs()) {
            System.err.println("Unable to makedir " + outdir);
            System.exit(1);
            return;
        }
        for (SchemaDocument doc3 : schemaDocs.keySet()) {
            File inputFile = (File) schemaDocs.get(doc3);
            File outputFile2 = outputFileFor(inputFile, basedir, outdir);
            if (outputFile2 == null) {
                System.out.println("Cannot copy " + inputFile);
            } else {
                doc3.save(outputFile2, new XmlOptions().setSavePrettyPrint().setSaveAggresiveNamespaces());
            }
        }
        for (SchemaDocument doc4 : commonFiles.keySet()) {
            doc4.save((File) commonFiles.get(doc4), new XmlOptions().setSavePrettyPrint().setSaveAggresiveNamespaces());
        }
    }

    private static File outputFileFor(File file, File baseDir, File outdir) {
        URI base = baseDir.getAbsoluteFile().toURI();
        URI abs = file.getAbsoluteFile().toURI();
        URI rel = base.relativize(abs);
        if (rel.isAbsolute()) {
            System.out.println("Cannot relativize " + file);
            return null;
        }
        URI outbase = outdir.toURI();
        URI out = CodeGenUtil.resolve(outbase, rel);
        return new File(out);
    }

    private static URI commonAncestor(URI first, URI second) {
        String firstStr = first.toString();
        String secondStr = second.toString();
        int len = firstStr.length();
        if (secondStr.length() < len) {
            len = secondStr.length();
        }
        int i = 0;
        while (i < len && firstStr.charAt(i) == secondStr.charAt(i)) {
            i++;
        }
        int i2 = i - 1;
        if (i2 >= 0) {
            i2 = firstStr.lastIndexOf(47, i2);
        }
        if (i2 < 0) {
            return null;
        }
        try {
            return new URI(firstStr.substring(0, i2));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private static String relativeURIFor(File source, File target) {
        URI base = source.getAbsoluteFile().toURI();
        URI abs = target.getAbsoluteFile().toURI();
        URI commonBase = commonAncestor(base, abs);
        if (commonBase == null) {
            return abs.toString();
        }
        URI baserel = commonBase.relativize(base);
        URI targetrel = commonBase.relativize(abs);
        if (baserel.isAbsolute() || targetrel.isAbsolute()) {
            return abs.toString();
        }
        String prefix = "";
        String sourceRel = baserel.toString();
        int i = 0;
        while (i < sourceRel.length()) {
            int i2 = sourceRel.indexOf(47, i);
            if (i2 < 0) {
                break;
            }
            prefix = prefix + "../";
            i = i2 + 1;
        }
        return prefix + targetrel.toString();
    }

    private static File commonFileFor(String commonName, String namespace, int i, File outdir) {
        String name = commonName;
        if (i > 0) {
            int index = commonName.lastIndexOf(46);
            if (index < 0) {
                index = commonName.length();
            }
            name = commonName.substring(0, index) + i + commonName.substring(index);
        }
        return new File(outdir, name);
    }

    private static void noteName(String name, String targetNamespace, Set seen, Set dupes, Set dupeNamespaces) {
        if (name == null) {
            return;
        }
        QName qName = new QName(targetNamespace, name);
        if (seen.contains(qName)) {
            dupes.add(qName);
            dupeNamespaces.add(targetNamespace);
        } else {
            seen.add(qName);
        }
    }

    private static boolean isFirstDuplicate(String name, String targetNamespace, Set notseen, Set dupes) {
        if (name == null) {
            return false;
        }
        QName qName = new QName(targetNamespace, name);
        if (dupes.contains(qName) && notseen.contains(qName)) {
            notseen.remove(qName);
            return true;
        }
        return false;
    }

    private static boolean isDuplicate(String name, String targetNamespace, Set dupes) {
        if (name == null) {
            return false;
        }
        QName qName = new QName(targetNamespace, name);
        return dupes.contains(qName);
    }
}
