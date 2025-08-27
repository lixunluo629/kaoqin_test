package com.drew.tools;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentData;
import com.drew.imaging.jpeg.JpegSegmentReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.Iterables;
import com.drew.lang.annotations.NotNull;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/tools/ExtractJpegSegmentTool.class */
public class ExtractJpegSegmentTool {
    public static void main(String[] args) throws IOException, JpegProcessingException {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }
        String filePath = args[0];
        if (!new File(filePath).exists()) {
            System.err.println("File does not exist");
            printUsage();
            System.exit(1);
        }
        Set<JpegSegmentType> segmentTypes = new HashSet<>();
        for (int i = 1; i < args.length; i++) {
            JpegSegmentType segmentType = JpegSegmentType.valueOf(args[i].toUpperCase());
            if (!segmentType.canContainMetadata) {
                System.err.printf("WARNING: Segment type %s cannot contain metadata so it may not be necessary to extract it%n", segmentType);
            }
            segmentTypes.add(segmentType);
        }
        if (segmentTypes.size() == 0) {
            segmentTypes.addAll(JpegSegmentType.canContainMetadataTypes);
        }
        System.out.println("Reading: " + filePath);
        JpegSegmentData segmentData = JpegSegmentReader.readSegments(new File(filePath), segmentTypes);
        saveSegmentFiles(filePath, segmentData);
    }

    public static void saveSegmentFiles(@NotNull String jpegFilePath, @NotNull JpegSegmentData segmentData) throws IOException {
        for (JpegSegmentType segmentType : segmentData.getSegmentTypes()) {
            List<byte[]> segments = Iterables.toList(segmentData.getSegments(segmentType));
            if (segments.size() != 0) {
                if (segments.size() > 1) {
                    for (int i = 0; i < segments.size(); i++) {
                        String outputFilePath = String.format("%s.%s.%d", jpegFilePath, segmentType.toString().toLowerCase(), Integer.valueOf(i));
                        System.out.println("Writing: " + outputFilePath);
                        FileUtil.saveBytes(new File(outputFilePath), segments.get(i));
                    }
                } else {
                    String outputFilePath2 = String.format("%s.%s", jpegFilePath, segmentType.toString().toLowerCase());
                    System.out.println("Writing: " + outputFilePath2);
                    FileUtil.saveBytes(new File(outputFilePath2), segments.get(0));
                }
            }
        }
    }

    private static void printUsage() {
        System.out.println("USAGE:\n");
        System.out.println("\tjava com.drew.tools.ExtractJpegSegmentTool <filename> [<segment> ...]\n");
        System.out.print("Where <segment> is zero or more of:");
        JpegSegmentType[] arr$ = (JpegSegmentType[]) JpegSegmentType.class.getEnumConstants();
        for (JpegSegmentType segmentType : arr$) {
            if (segmentType.canContainMetadata) {
                System.out.print(SymbolConstants.SPACE_SYMBOL + segmentType.toString());
            }
        }
        System.out.println();
    }
}
