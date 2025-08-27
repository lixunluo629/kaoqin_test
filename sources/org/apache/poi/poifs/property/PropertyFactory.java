package org.apache.poi.poifs.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.poifs.storage.ListManagedBlock;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/property/PropertyFactory.class */
class PropertyFactory {
    private PropertyFactory() {
    }

    static List<Property> convertToProperties(ListManagedBlock[] blocks) throws IOException {
        List<Property> properties = new ArrayList<>();
        for (ListManagedBlock block : blocks) {
            byte[] data = block.getData();
            convertToProperties(data, properties);
        }
        return properties;
    }

    static void convertToProperties(byte[] data, List<Property> properties) throws IOException {
        int property_count = data.length / 128;
        int offset = 0;
        for (int k = 0; k < property_count; k++) {
            switch (data[offset + 66]) {
                case 1:
                    properties.add(new DirectoryProperty(properties.size(), data, offset));
                    break;
                case 2:
                    properties.add(new DocumentProperty(properties.size(), data, offset));
                    break;
                case 3:
                case 4:
                default:
                    properties.add(null);
                    break;
                case 5:
                    properties.add(new RootProperty(properties.size(), data, offset));
                    break;
            }
            offset += 128;
        }
    }
}
