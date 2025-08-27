package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.core.mapper.ServereBackupsDataRecordMapper;
import com.moredian.onpremise.core.mapper.UpdateEigenvalueRecordMapper;
import com.moredian.onpremise.core.mapper.UpgradeScheduleMapper;
import com.moredian.onpremise.core.mapper.UpgradeServerScheduleMapper;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/DeleteCacheFileSchedule.class */
public class DeleteCacheFileSchedule extends AbstractCustomSchedule {

    @Value("${onpremise.save.log.path}")
    private String logPath;

    @Value("${onpremise.save.image.path}")
    private String imagePath;

    @Value("${onpremise.save.backups.path}")
    private String backupsPath;

    @Value("${onpremise.save.apk.path}")
    private String apkPath;

    @Value("${onpremise.save.server.path}")
    private String serverPath;

    @Autowired
    private UpdateEigenvalueRecordMapper recordMapper;

    @Autowired
    private UpgradeServerScheduleMapper serverScheduleMapper;

    @Autowired
    private UpgradeScheduleMapper deviceScheduleMapper;

    @Autowired
    private ServereBackupsDataRecordMapper backupsDataRecordMapper;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        super.saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        File logFile = new File(this.logPath);
        if (logFile.isDirectory()) {
            File[] files = logFile.listFiles();
            if (MyListUtils.arrayIsEmpty(files)) {
                int nowMonth = MyDateUtils.getMonth();
                if (nowMonth == 1) {
                    nowMonth += 12;
                }
                for (File file : files) {
                    int month = MyDateUtils.getMonth(MyDateUtils.getDate(file.lastModified()));
                    if (month < nowMonth) {
                        file.delete();
                    }
                }
            }
        }
        String queryTime = MyDateUtils.formatDate(MyDateUtils.getMonthStartTime(new Date()), "yyyy-MM-dd");
        List<String> urls = this.recordMapper.listOldUrl(queryTime);
        if (!CollectionUtils.isEmpty(urls)) {
            for (String url : urls) {
                MyFileUtils.deleteFile(this.imagePath + url);
            }
        }
        List<String> packageUrls = this.deviceScheduleMapper.listPackageUrl(queryTime);
        File apkFile = new File(this.apkPath);
        if (apkFile.isDirectory()) {
            File[] files2 = apkFile.listFiles();
            if (MyListUtils.arrayIsEmpty(files2)) {
                for (File file2 : files2) {
                    if (packageUrls.contains(this.apkPath + file2.getName())) {
                        file2.delete();
                    }
                }
            }
        }
        List<String> packageNames = this.serverScheduleMapper.listPackageName(queryTime);
        File serverFile = new File(this.serverPath);
        if (serverFile.isDirectory()) {
            File[] files3 = serverFile.listFiles();
            if (MyListUtils.arrayIsEmpty(files3)) {
                for (File file3 : files3) {
                    if (packageNames.contains(file3.getName())) {
                        file3.delete();
                    }
                }
            }
        }
    }
}
