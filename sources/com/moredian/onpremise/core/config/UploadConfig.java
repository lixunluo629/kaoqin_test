package com.moredian.onpremise.core.config;

import com.moredian.onpremise.core.common.constants.DateFormatConstants;
import com.moredian.onpremise.core.utils.MyFileUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

@Configuration
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/config/UploadConfig.class */
public class UploadConfig {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) UploadConfig.class);

    @Value("${onpremise.save.image.path}")
    private String saveImagePath;

    @Value("${onpremise.save.snap.path}")
    private String snapImagePath;

    @Value("${onpremise.file.path}")
    private String filePath;

    @Value("${onpremise.save.visitConfig.path}")
    private String visitConfigPath;

    public void setSaveImagePath(String saveImagePath) {
        this.saveImagePath = saveImagePath;
    }

    public void setSnapImagePath(String snapImagePath) {
        this.snapImagePath = snapImagePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setVisitConfigPath(String visitConfigPath) {
        this.visitConfigPath = visitConfigPath;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UploadConfig)) {
            return false;
        }
        UploadConfig other = (UploadConfig) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$saveImagePath = getSaveImagePath();
        Object other$saveImagePath = other.getSaveImagePath();
        if (this$saveImagePath == null) {
            if (other$saveImagePath != null) {
                return false;
            }
        } else if (!this$saveImagePath.equals(other$saveImagePath)) {
            return false;
        }
        Object this$snapImagePath = getSnapImagePath();
        Object other$snapImagePath = other.getSnapImagePath();
        if (this$snapImagePath == null) {
            if (other$snapImagePath != null) {
                return false;
            }
        } else if (!this$snapImagePath.equals(other$snapImagePath)) {
            return false;
        }
        Object this$filePath = getFilePath();
        Object other$filePath = other.getFilePath();
        if (this$filePath == null) {
            if (other$filePath != null) {
                return false;
            }
        } else if (!this$filePath.equals(other$filePath)) {
            return false;
        }
        Object this$visitConfigPath = getVisitConfigPath();
        Object other$visitConfigPath = other.getVisitConfigPath();
        return this$visitConfigPath == null ? other$visitConfigPath == null : this$visitConfigPath.equals(other$visitConfigPath);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UploadConfig;
    }

    public int hashCode() {
        Object $saveImagePath = getSaveImagePath();
        int result = (1 * 59) + ($saveImagePath == null ? 43 : $saveImagePath.hashCode());
        Object $snapImagePath = getSnapImagePath();
        int result2 = (result * 59) + ($snapImagePath == null ? 43 : $snapImagePath.hashCode());
        Object $filePath = getFilePath();
        int result3 = (result2 * 59) + ($filePath == null ? 43 : $filePath.hashCode());
        Object $visitConfigPath = getVisitConfigPath();
        return (result3 * 59) + ($visitConfigPath == null ? 43 : $visitConfigPath.hashCode());
    }

    public String toString() {
        return "UploadConfig(saveImagePath=" + getSaveImagePath() + ", snapImagePath=" + getSnapImagePath() + ", filePath=" + getFilePath() + ", visitConfigPath=" + getVisitConfigPath() + ")";
    }

    public String getSaveImagePath() {
        return this.saveImagePath;
    }

    public String getSnapImagePath() {
        return this.snapImagePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getVisitConfigPath() {
        return this.visitConfigPath;
    }

    public String uploadImage(String base64) {
        return MyFileUtils.upload(base64, this.saveImagePath);
    }

    public String uploadImage(MultipartFile file) {
        return MyFileUtils.upload(file, this.saveImagePath);
    }

    public String uploadImageForReturnUrl(String base64) {
        String path = this.snapImagePath + DateFormatUtils.format(new Date(), DateFormatConstants.DATE_FOR_DIAGONAL) + "/";
        return getImageUrl(1, MyFileUtils.uploadImageForReturnPath(base64, path));
    }

    public List<String> uploadVisitConfigFileForReturnUrl(MultipartFile file) {
        String path = this.visitConfigPath + DateFormatUtils.format(new Date(), DateFormatConstants.DATE_FOR_DIAGONAL) + "/";
        String url = MyFileUtils.uploadFileForReturnPath(file, path);
        if (StringUtils.isEmpty(url)) {
            return new ArrayList();
        }
        if ("zip".equals(url.substring(url.indexOf(".") + 1, url.length()))) {
            return MyFileUtils.zipToFile(url);
        }
        List<String> urls = new ArrayList<>();
        urls.add(url.replace(this.filePath, "/"));
        return urls;
    }

    public String getRelativePath(String url) {
        return url.replace(this.filePath, "/");
    }

    public String getImageUrl(int type, String path) {
        if (type == 1) {
            return path.replace(this.filePath, "/");
        }
        return (this.saveImagePath + path).replace(this.filePath, "/");
    }

    public String getDeviceLogUrl(String path) {
        return path.replace(this.filePath, "/");
    }

    public String getImageUrlForAbsolutePath(String absolutePath) {
        return absolutePath.replace(this.filePath, "/");
    }

    public String restoreImageUrl(String path) {
        return this.filePath + path;
    }
}
