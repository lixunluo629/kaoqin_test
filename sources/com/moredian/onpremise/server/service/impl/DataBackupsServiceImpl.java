package com.moredian.onpremise.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.server.DataBackupsService;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.enums.BackupsTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.OrganizationMapper;
import com.moredian.onpremise.core.mapper.ServereBackupsDataRecordMapper;
import com.moredian.onpremise.core.model.domain.Organization;
import com.moredian.onpremise.core.model.domain.ServereBackupsDataRecord;
import com.moredian.onpremise.core.model.info.UploadAndUnpackInfo;
import com.moredian.onpremise.core.model.request.BackupsDataRecordRequest;
import com.moredian.onpremise.core.model.request.DeleteBackupsRequest;
import com.moredian.onpremise.core.model.request.ListServerBackupsRecordRequest;
import com.moredian.onpremise.core.model.request.RestoreBackupsRequest;
import com.moredian.onpremise.core.model.request.UploadBackupsRequest;
import com.moredian.onpremise.core.model.response.ListServerBackupsRecordResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.core.utils.ShellUtils;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncGroupRequest;
import com.moredian.onpremise.model.SyncMemberRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-server-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/server/service/impl/DataBackupsServiceImpl.class */
public class DataBackupsServiceImpl implements DataBackupsService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) DataBackupsServiceImpl.class);

    @Autowired
    private ServereBackupsDataRecordMapper backupsDataRecordMapper;

    @Value("${onpremise.save.backups.path}")
    private String saveBackupsPath;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override // com.moredian.onpremise.api.server.DataBackupsService
    public PageList<ListServerBackupsRecordResponse> listRecord(ListServerBackupsRecordRequest request) {
        PageList<ListServerBackupsRecordResponse> result;
        AssertUtil.checkOrgId(request.getOrgId());
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            result = new PageList<>(this.backupsDataRecordMapper.listRecord(request));
        } else {
            List<ListServerBackupsRecordResponse> responses = this.backupsDataRecordMapper.listRecord(request);
            result = new PageList<>(Paginator.initPaginator(responses), responses);
        }
        return result;
    }

    @Override // com.moredian.onpremise.api.server.DataBackupsService
    public Boolean restoreBackups(RestoreBackupsRequest request) {
        checkRecordId(request.getBackupsDataId());
        ServereBackupsDataRecord record = this.backupsDataRecordMapper.getOneById(request.getOrgId(), request.getBackupsDataId());
        AssertUtil.isNullOrEmpty(record, OnpremiseErrorEnum.BACKUPS_DATA_RECORD_NOT_FIND);
        String result = ShellUtils.importDataSql(record.getRecordName());
        logger.info("backups data result :{}", result);
        AssertUtil.isTrue(Boolean.valueOf(result.indexOf(Constants.SUCCESS) >= 0), OnpremiseErrorEnum.SHELL_EXCUTE_FAIL);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(request.getOrgId());
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return true;
        }
        this.nettyMessageApi.sendMsg(new SyncMemberRequest(record.getOrgId(), 1), Integer.valueOf(SyncMemberRequest.MODEL_TYPE.type()), deviceSnList);
        this.nettyMessageApi.sendMsg(new SyncGroupRequest(record.getOrgId(), 1), Integer.valueOf(SyncGroupRequest.MODEL_TYPE.type()), deviceSnList);
        return true;
    }

    @Override // com.moredian.onpremise.api.server.DataBackupsService
    public Boolean deleteBackups(DeleteBackupsRequest request) {
        checkRecordId(request.getBackupsDataId());
        ServereBackupsDataRecord record = this.backupsDataRecordMapper.getOneById(request.getOrgId(), request.getBackupsDataId());
        AssertUtil.isNullOrEmpty(record, OnpremiseErrorEnum.BACKUPS_DATA_RECORD_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(this.backupsDataRecordMapper.delete(request.getOrgId(), request.getBackupsDataId()) > 0), OnpremiseErrorEnum.BACKUPS_DATA_RECORD_ID_MUST_NOT_NULL);
        return true;
    }

    @Override // com.moredian.onpremise.api.server.DataBackupsService
    public Boolean executeBackups(BackupsDataRecordRequest request) throws IOException, RuntimeException {
        Organization organization = this.organizationMapper.getOneById(request.getOrgId());
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        String fileNamePrefix = MyDateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String result = ShellUtils.exportDataSql(fileNamePrefix + ".sql");
        logger.info("backups data result :{}", result);
        AssertUtil.isTrue(Boolean.valueOf(result.indexOf(Constants.SUCCESS) >= 0), OnpremiseErrorEnum.SHELL_EXCUTE_FAIL);
        MyFileUtils.fileToZip(this.saveBackupsPath + fileNamePrefix + ".sql", this.saveBackupsPath + fileNamePrefix + ".zip");
        doInsert(new Date(), request.getBackupsType(), fileNamePrefix + ".zip", request.getOrgId());
        File file = new File(this.saveBackupsPath);
        List<String> expireZipNames = this.backupsDataRecordMapper.listExpireZipName(request.getOrgId(), 4);
        List<String> expireSqlNames = new ArrayList<>();
        for (String expireZipName : expireZipNames) {
            String[] strArr = expireZipName.split("\\.");
            expireSqlNames.add(strArr[0] + ".sql");
        }
        int deleteZip = 0;
        int deleteSql = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File var : files) {
                if (expireZipNames.contains(var.getName())) {
                    var.delete();
                    deleteZip++;
                }
                if (expireSqlNames.contains(var.getName())) {
                    var.delete();
                    deleteSql++;
                }
            }
        }
        this.backupsDataRecordMapper.deleteExpireBackupDataRecordByNames(request.getOrgId(), expireZipNames);
        logger.info("delete backups success,zip:{},sql:{}", Integer.valueOf(deleteZip), Integer.valueOf(deleteSql));
        return true;
    }

    @Override // com.moredian.onpremise.api.server.DataBackupsService
    public Boolean uploadBackups(UploadBackupsRequest request) throws IOException {
        Organization organization = this.organizationMapper.getOneById(request.getOrgId());
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        UploadAndUnpackInfo info = MyFileUtils.uploadAndUnpack(request.getFile(), this.saveBackupsPath, organization.getOrgName());
        boolean result = false;
        if (info != null) {
            doInsert(MyDateUtils.getDate(Long.valueOf(info.getBackupsTimeStamp()).longValue()), Integer.valueOf(info.getBackupsType()), info.getRecordName(), request.getOrgId());
            result = true;
        }
        return Boolean.valueOf(result);
    }

    private void checkRecordId(Long recordId) {
        AssertUtil.isTrue(Boolean.valueOf(recordId != null && recordId.longValue() > 0), OnpremiseErrorEnum.BACKUPS_DATA_RECORD_ID_MUST_NOT_NULL);
    }

    private void doInsert(Date backupsTime, Integer backupsType, String name, Long orgId) {
        ServereBackupsDataRecord record = new ServereBackupsDataRecord();
        record.setBackupsTime(backupsTime);
        record.setBackupsType(Integer.valueOf((backupsType == null || backupsType.intValue() == 0) ? BackupsTypeEnum.MANUAL_BACKUPS.getValue() : backupsType.intValue()));
        record.setRecordName(name);
        record.setOrgId(orgId);
        this.backupsDataRecordMapper.insert(record);
    }
}
