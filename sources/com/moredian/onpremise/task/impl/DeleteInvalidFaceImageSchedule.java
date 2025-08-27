package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/DeleteInvalidFaceImageSchedule.class */
public class DeleteInvalidFaceImageSchedule extends AbstractCustomSchedule {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) DeleteInvalidFaceImageSchedule.class);

    @Value("${onpremise.save.image.path}")
    private String imagePath;

    @Autowired
    private MemberService memberService;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        List<String> faceUrlList = this.memberService.listFaceUrl(1L);
        File imageFile = new File(this.imagePath);
        if (imageFile.isDirectory()) {
            File[] files = imageFile.listFiles();
            if (MyListUtils.arrayIsEmpty(files)) {
                for (File file : files) {
                    if (CollectionUtils.isEmpty(faceUrlList) || !faceUrlList.contains("/faceimg/" + file.getName())) {
                        file.delete();
                    }
                }
            }
        }
    }
}
