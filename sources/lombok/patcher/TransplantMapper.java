package lombok.patcher;

/* loaded from: lombok-1.16.22.jar:lombok/patcher/TransplantMapper.SCL.lombok */
public interface TransplantMapper {
    public static final TransplantMapper IDENTITY_MAPPER = new TransplantMapper() { // from class: lombok.patcher.TransplantMapper.1
        @Override // lombok.patcher.TransplantMapper
        public String mapResourceName(int classFileFormatVersion, String resourceName) {
            return resourceName;
        }
    };

    String mapResourceName(int i, String str);
}
